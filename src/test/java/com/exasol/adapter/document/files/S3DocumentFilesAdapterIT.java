package com.exasol.adapter.document.files;

import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.DatabaseObject;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.udfdebugging.UdfTestSetup;
import com.github.dockerjava.api.model.ContainerNetwork;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import static com.exasol.adapter.document.UdfEntryPoint.*;
import static com.exasol.adapter.document.files.S3DocumentFilesAdapter.ADAPTER_NAME;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Tag("integration")
@Testcontainers
class S3DocumentFilesAdapterIT extends AbstractDocumentFilesAdapterIT {
    public static final String TEST_BUCKET = "test-bucket";
    private static final String ADAPTER_JAR = "document-files-virtual-schema-dist-0.3.0-SNAPSHOT-s3-0.1.0.jar";
    private static final Logger LOGGER = LoggerFactory.getLogger(S3DocumentFilesAdapterIT.class);
    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> EXASOL_CONTAINER = new ExasolContainer<>()
            .withLogConsumer(new Slf4jLogConsumer(LOGGER)).withReuse(true);
    @Container
    private static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer().withServices(S3);
    /*
     * new LocalStackContainer( DockerImageName.parse("localstack/localstack:0.12.2")).withServices(S3);
     */

    private static Connection connection;
    private static Statement statement;
    private static ExasolObjectFactory exasolObjectFactory;
    private static AdapterScript adapterScript;
    private static ConnectionDefinition connectionDefinition;
    private static S3Client s3;

    private final List<DatabaseObject> createdObjects = new LinkedList<>();

    @BeforeAll
    static void beforeAll() throws Exception {
        connection = EXASOL_CONTAINER.createConnectionForUser(EXASOL_CONTAINER.getUsername(),
                EXASOL_CONTAINER.getPassword());
        statement = connection.createStatement();
        final UdfTestSetup udfTestSetup = new UdfTestSetup(getTestHostIp());
        exasolObjectFactory = new ExasolObjectFactory(EXASOL_CONTAINER.createConnection(),
                ExasolObjectConfiguration.builder().withJvmOptions(udfTestSetup.getJvmOptions()).build());
        final ExasolSchema adapterSchema = exasolObjectFactory.createSchema("ADAPTER");
        adapterScript = createAdapterScript(adapterSchema);
        createUdf(adapterSchema);
        connectionDefinition = getConnectionDefinition();
        s3 = S3Client.builder().endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(LOCAL_STACK_CONTAINER.getAccessKey(), LOCAL_STACK_CONTAINER.getSecretKey())))
                .region(Region.of(LOCAL_STACK_CONTAINER.getRegion())).build();
        s3.createBucket(builder -> builder.bucket(TEST_BUCKET));
    }

    private static ConnectionDefinition getConnectionDefinition() {
        final String address = "http://" + TEST_BUCKET + ".s3." + LOCAL_STACK_CONTAINER.getRegion() + "."
                + getTestHostIp() + ":" + LOCAL_STACK_CONTAINER.getFirstMappedPort() + "/";
        return exasolObjectFactory.createConnectionDefinition("S3_CONNECTION", address,
                LOCAL_STACK_CONTAINER.getAccessKey(), LOCAL_STACK_CONTAINER.getSecretKey());
    }

    @AfterAll
    static void afterAll() throws SQLException {
        statement.close();
        connection.close();
    }

    private static AdapterScript createAdapterScript(final ExasolSchema adapterSchema)
            throws InterruptedException, BucketAccessException, TimeoutException {
        EXASOL_CONTAINER.getDefaultBucket().uploadFile(Path.of("target", ADAPTER_JAR), ADAPTER_JAR);
        return adapterSchema.createAdapterScriptBuilder("FILES_ADAPTER")
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", "/buckets/bfsdefault/default/" + ADAPTER_JAR)
                .language(AdapterScript.Language.JAVA).build();
    }

    private static void createUdf(final ExasolSchema adapterSchema) {
        adapterSchema.createUdfBuilder("IMPORT_FROM_S3_DOCUMENT_FILES").language(UdfScript.Language.JAVA)
                .inputType(UdfScript.InputType.SET).parameter(PARAMETER_DATA_LOADER, "VARCHAR(2000000)")
                .parameter(PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), "/buckets/bfsdefault/default/" + ADAPTER_JAR).build();
    }

    @AfterEach
    void after() {
        emptyTestBucket();
        for (final DatabaseObject createdObject : this.createdObjects) {
            createdObject.drop();
        }
    }

    private void emptyTestBucket() {
        final ListObjectsResponse listObjectsResponse = s3.listObjects(builder -> builder.bucket(TEST_BUCKET));
        listObjectsResponse.contents()
                .forEach(s3Object -> s3.deleteObject(builder -> builder.bucket(TEST_BUCKET).key(s3Object.key())));
    }

    private static String getTestHostIp() {
        final Map<String, ContainerNetwork> networks = EXASOL_CONTAINER.getContainerInfo().getNetworkSettings()
                .getNetworks();
        return networks.size() == 0 ? null : networks.values().iterator().next().getGateway();
    }

    @Override
    protected Statement getStatement() {
        return statement;
    }

    @Override
    protected void uploadDataFile(final Supplier<InputStream> fileContent, final String fileName) {
        try {
            s3.putObject(builder -> builder.bucket(TEST_BUCKET).key(fileName),
                    RequestBody.fromBytes(fileContent.get().readAllBytes()));
        } catch (final IOException exception) {
            throw new IllegalStateException("Filed to upload test file.", exception);
        }
    }

    @Override
    protected void createVirtualSchema(final String schemaName, final Supplier<InputStream> mapping) {
        try {
            EXASOL_CONTAINER.getDefaultBucket().uploadInputStream(mapping, "mapping.json");
            this.createdObjects.add(exasolObjectFactory.createVirtualSchemaBuilder(schemaName)
                    .connectionDefinition(connectionDefinition).adapterScript(adapterScript).dialectName(ADAPTER_NAME)
                    .properties(Map.of("MAPPING", "/bfsdefault/default/mapping.json")).build());
        } catch (final InterruptedException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to create Virtual Schema.", exception);
        }
    }
}
