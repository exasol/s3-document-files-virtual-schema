package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.files.S3DocumentFilesAdapter.ADAPTER_NAME;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.exasol.AdapterScript;
import com.exasol.dbbuilder.dialects.exasol.ConnectionDefinition;
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.github.dockerjava.api.model.ContainerNetwork;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Tag("integration")
@Testcontainers
public class S3DocumentFilesAdapterIT {
    public static final String TEST_BUCKET = "test-bucket";
    private static final String TEST_SCHEMA = "TEST_SCHEMA";
    private static final String ADAPTER_JAR = "document-files-virtual-schema-dist-0.2.0-SNAPSHOT-s3-0.1.0.jar";
    private static final Logger LOGGER = LoggerFactory.getLogger(S3DocumentFilesAdapterIT.class);
    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> EXASOL_CONTAINER = new ExasolContainer<>()
            .withLogConsumer(new Slf4jLogConsumer(LOGGER)).withReuse(true);
    @Container
    private static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer().withServices(S3)
            .withReuse(true);
    @TempDir
    static File tempDir;
    private static Connection connection;
    private static Statement statement;
    private static ExasolObjectFactory exasolObjectFactory;
    private static AdapterScript adapterScript;
    private static ConnectionDefinition connectionDefinition;

    @BeforeAll
    static void beforeAll() throws Exception {
        connection = EXASOL_CONTAINER.createConnectionForUser(EXASOL_CONTAINER.getUsername(),
                EXASOL_CONTAINER.getPassword());
        statement = connection.createStatement();
        createS3TestSetup();
        exasolObjectFactory = new ExasolObjectFactory(EXASOL_CONTAINER.createConnection());
        adapterScript = createAdapterScript(exasolObjectFactory);
        connectionDefinition = getConnectionDefinition();
        createUdf();
        final Path mappingFile = saveResourceToFile("mapJsonFile.json");
        createVirtualSchema(mappingFile);
    }

    private static void createS3TestSetup() {
        final S3Client s3 = S3Client.builder().endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(LOCAL_STACK_CONTAINER.getAccessKey(), LOCAL_STACK_CONTAINER.getSecretKey())))
                .region(Region.of(LOCAL_STACK_CONTAINER.getRegion())).build();

        s3.createBucket(builder -> builder.bucket(TEST_BUCKET));
        s3.putObject(builder -> builder.bucket(TEST_BUCKET).key("testData-1.json"),
                RequestBody.fromBytes("{\"id\": \"book-1\"}".getBytes()));
        s3.putObject(builder -> builder.bucket(TEST_BUCKET).key("testData-2.json"),
                RequestBody.fromBytes("{\"id\": \"book-2\"}".getBytes()));
        s3.putObject(builder -> builder.bucket(TEST_BUCKET).key("otherData.json"),
                RequestBody.fromBytes("{\"id\": \"other\"}".getBytes()));
    }

    private static ConnectionDefinition getConnectionDefinition() {
        final String address = "http://" + TEST_BUCKET + ".s3." + LOCAL_STACK_CONTAINER.getRegion() + "."
                + getTestHostIp() + ":" + LOCAL_STACK_CONTAINER.getMappedPort(S3.getPort()) + "/";
        return exasolObjectFactory.createConnectionDefinition("S3_CONNECTION", address,
                LOCAL_STACK_CONTAINER.getAccessKey(), LOCAL_STACK_CONTAINER.getSecretKey());
    }

    @AfterAll
    static void afterAll() throws SQLException {
        statement.close();
        connection.close();
    }

    private static Path saveResourceToFile(final String resource) throws IOException {
        try (final InputStream inputStream = S3DocumentFilesAdapterIT.class.getClassLoader()
                .getResourceAsStream(resource)) {
            final Path tempFile = File.createTempFile("resource", "", tempDir).toPath();
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            return tempFile;
        }
    }

    private static AdapterScript createAdapterScript(final ExasolObjectFactory exasolObjectFactory)
            throws InterruptedException, BucketAccessException, TimeoutException {
        EXASOL_CONTAINER.getDefaultBucket().uploadFile(Path.of("target", ADAPTER_JAR), ADAPTER_JAR);
        final ExasolSchema adapterSchema = exasolObjectFactory.createSchema("ADAPTER");
        return adapterSchema.createAdapterScriptBuilder().name("FILES_ADAPTER")
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", "/buckets/bfsdefault/default/" + ADAPTER_JAR)
                .language(AdapterScript.Language.JAVA).debuggerConnection(getTestHostIp() + ":8000").build();
    }

    private static String getTestHostIp() {
        final Map<String, ContainerNetwork> networks = EXASOL_CONTAINER.getContainerInfo().getNetworkSettings()
                .getNetworks();
        return networks.size() == 0 ? null : networks.values().iterator().next().getGateway();
    }

    private static void createUdf() throws SQLException {
        final StringBuilder statementBuilder = new StringBuilder(
                "CREATE OR REPLACE JAVA SET SCRIPT ADAPTER.IMPORT_FROM_S3_DOCUMENT_FILES(DATA_LOADER VARCHAR(2000000), REMOTE_TABLE_QUERY VARCHAR(2000000), CONNECTION_NAME VARCHAR(500)) EMITS(...) AS\n");
        statementBuilder.append("    %scriptclass " + UdfEntryPoint.class.getName() + ";\n");
        statementBuilder.append("    %jar /buckets/bfsdefault/default/" + ADAPTER_JAR + ";\n");
        statementBuilder.append("/");
        final String sql = statementBuilder.toString();
        statement.execute(sql);
    }

    private static void createVirtualSchema(final Path mapping)
            throws InterruptedException, BucketAccessException, TimeoutException {
        final String mappingInBucketfs = "mapping.json";
        EXASOL_CONTAINER.getDefaultBucket().uploadFile(mapping, mappingInBucketfs);
        exasolObjectFactory.createVirtualSchemaBuilder(TEST_SCHEMA).connectionDefinition(connectionDefinition)
                .adapterScript(adapterScript).dialectName(ADAPTER_NAME)
                .properties(Map.of("MAPPING", "/bfsdefault/default/" + mappingInBucketfs)).build();
    }

    @Test
    void testReadJson()
            throws SQLException, InterruptedException, BucketAccessException, TimeoutException, IOException {
        final ResultSet resultSet = statement.executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        final List<String> result = new ArrayList<>();
        while (resultSet.next()) {
            result.add(resultSet.getString("ID"));
        }
        assertThat(result, containsInAnyOrder("book-1", "book-2"));
    }
}
