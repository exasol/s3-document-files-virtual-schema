package com.exasol.adapter.document.files;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.serializer.EdmlSerializer;
import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.classlistextractor.verifier.ClassListExtractor;
import com.exasol.classlistextractor.verifier.ClassListVerifier;
import com.exasol.dbbuilder.dialects.DatabaseObjectException;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.matcher.TypeMatchMode;
import com.exasol.performancetestrecorder.PerformanceTestRecorder;
import com.exasol.smalljsonfilesfixture.SmallJsonFilesTestSetup;

import jakarta.json.JsonObjectBuilder;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;

@Tag("integration")
@Testcontainers
class S3DocumentFilesAdapterIT extends AbstractDocumentFilesAdapterIT {
    private static final S3TestSetup AWS_S3_TEST_SETUP = new AwsS3TestSetup();
    private static final String SMALL_JSON_FILES_FIXTURE_BUCKET = "persistent-small-json-files-test-fixture";
    private static final List<Pattern> CLASS_LIST_IGNORES = List.of(Pattern.compile("java/util/concurrent/.*"),
            Pattern.compile("io/netty/util/concurrent/.*"));
    private static final Logger LOGGER = Logger.getLogger(S3DocumentFilesAdapterIT.class.getName());
    private static String s3BucketName;
    private static IntegrationTestSetup SETUP;
    private static S3UploadInterface s3Uploader;

    @BeforeAll
    static void beforeAll() throws Exception {
        s3BucketName = "my.s3.virtual-schema-test-bucket-" + System.currentTimeMillis();
        AWS_S3_TEST_SETUP.createBucket(s3BucketName);
        SETUP = new IntegrationTestSetup(AWS_S3_TEST_SETUP, s3BucketName);
        s3Uploader = getS3UploadClient();
    }

    private static S3UploadInterface getS3UploadClient() {
        final String s3CacheBucketName = TestConfig.instance().getS3CacheBucket();
        if ((s3CacheBucketName == null) || s3CacheBucketName.isBlank()) {
            LOGGER.warning("The " + TestConfig.FILE_NAME
                    + " does not set a s3CacheBucket. So we will upload all the test files again and again. If you run the tests more often, consider to create it and add it's name to the "
                    + TestConfig.FILE_NAME + ".");
            return new S3DirectUploader(SETUP.getS3Client(), s3BucketName);
        } else {
            return new S3Cache(SETUP.getS3Client(), s3BucketName, s3CacheBucketName);
        }
    }

    @AfterAll
    static void afterAll() throws Exception {
        AWS_S3_TEST_SETUP.deleteBucket(s3BucketName);
        if (SETUP != null) {
            SETUP.close();
        }
    }

    @AfterEach
    void after() {
        SETUP.emptyS3Bucket();
        SETUP.dropCreatedObjects();
    }

    @Override
    protected Statement getStatement() {
        return SETUP.getStatement();
    }

    @Override
    protected void uploadDataFile(final Supplier<InputStream> fileContent, final String fileName) {
        try (final InputStream inputStream = fileContent.get()) {
            final Path tempFile = Files.createTempFile("data-file", ".data");
            Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            uploadDataFile(tempFile, fileName);
            Files.delete(tempFile);
        } catch (final IOException exception) {
            throw new IllegalStateException("Filed to upload test file.", exception);
        }
    }

    @Override
    protected void uploadDataFile(final Path fileContent, final String fileName) {
        s3Uploader.uploadFile(fileContent, fileName);
    }

    @Override
    protected void createVirtualSchema(final String schemaName, final String mapping) {
        SETUP.createVirtualSchema(schemaName, mapping);
    }

    @Test
    void testInvalidConnection() throws BucketAccessException, TimeoutException {
        SETUP.getBucket().uploadInputStream(() -> getClass().getClassLoader().getResourceAsStream("simpleMapping.json"),
                "mapping.json");
        final ConnectionDefinition connection = SETUP.getExasolObjectFactory()
                .createConnectionDefinition("EMPTY_S3_CONNECTION", "", "", "{");
        final VirtualSchema.Builder virtualSchemaBuilder = SETUP
                .getPreconfiguredVirtualSchemaBuilder("EMPTY_CONNECTION_SCHEMA").connectionDefinition(connection)
                .properties(Map.of("MAPPING", "/bfsdefault/default/mapping.json"));
        final DatabaseObjectException exception = assertThrows(DatabaseObjectException.class,
                virtualSchemaBuilder::build);
        assertThat(exception.getCause().getMessage(), containsString(
                "E-VSD-94: Invalid connection. The connection definition has a invalid syntax. Please check the user-guide at: https://github.com/exasol/s3-document-files-virtual-schema/blob/main/doc/user_guide/user_guide.md."));
    }

    @Test
    void testPathStyleAccess() throws IOException, SQLException {
        final ConnectionDefinition originalConnection = SETUP.getConnectionDefinition();
        final JsonObjectBuilder connectionConfig = SETUP.getConnectionConfig();
        connectionConfig.add("s3PathStyleAccess", true);
        final ConnectionDefinition pathStyleConnection = SETUP.createConnectionDefinition(connectionConfig);
        SETUP.setConnectionDefinition(pathStyleConnection);
        try {
            this.createJsonVirtualSchema();
            try (final ResultSet result = this.getStatement()
                    .executeQuery("SELECT ID FROM TEST.BOOKS ORDER BY ID ASC;")) {
                assertThat(result, table().row("book-1").row("book-2").matches());
            }
        } finally {
            SETUP.setConnectionDefinition(originalConnection);
        }
    }

    @Test
    @Tag("regression")
    void testManySmallJsonFiles(final TestInfo testInfo) throws Exception {
        final int numberOfJsonFiles = 1_000_000;
        createTestSetupWithSmallJsonFiles(numberOfJsonFiles);
        final String mapping = getMappingDefinitionForSmallJsonFiles();
        final JsonObjectBuilder connectionConfig = SETUP.getConnectionConfig();
        connectionConfig.add("s3Bucket", SMALL_JSON_FILES_FIXTURE_BUCKET);
        try (final ConnectionDefinition connection = SETUP.createConnectionDefinition(connectionConfig)) {
            final VirtualSchema virtualSchema = SETUP.createVirtualSchema("SMALL_JSON_FILES_VS", mapping, connection);
            final String sql = "SELECT COUNT(*) FROM (SELECT * FROM " + virtualSchema.getFullyQualifiedName()
                    + ".TEST)";
            for (int runCounter = 0; runCounter < 5; runCounter++) {
                PerformanceTestRecorder.getInstance().recordExecution(testInfo, () -> {
                    try (final ResultSet resultSet = getStatement().executeQuery(sql)) {
                        assertThat(resultSet, table().row(numberOfJsonFiles).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
                    }
                });
            }
        }
    }

    protected void uploadDataFile(final String content, final String fileName) {
        uploadDataFile(() -> new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), fileName);
    }

    @Test
    void testClassList() throws BucketAccessException, FileNotFoundException, TimeoutException, SQLException {
        final List<String> classList = getClassListFromVirtualSchema();
        new ClassListVerifier(CLASS_LIST_IGNORES).verifyClassListFile(classList,
                IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH);
    }

    @SuppressWarnings("try") // auto-closeable resource udf is never referenced in body of corresponding try statement
    private List<String> getClassListFromVirtualSchema()
            throws BucketAccessException, TimeoutException, FileNotFoundException {
        final ClassListExtractor classListExtractor = new ClassListExtractor(SETUP.getBucket(),
                SETUP::makeLocalServiceAvailableInExasol);
        final ExasolObjectFactory objectFactory = new ExasolObjectFactory(SETUP.getConnection(),
                ExasolObjectConfiguration.builder().withJvmOptions(classListExtractor.getJvmOptions()).build());
        AWS_S3_TEST_SETUP.upload(s3BucketName, "test-data-1.json",
                RequestBody.fromString("{ \"id\": 1, \"name\": \"tom\" }"));
        final String mapping = getMappingDefinitionForSmallJsonFiles();
        try (final ExasolSchema schema = objectFactory.createSchema("ADAPTER_FOR_LIST");
                final AdapterScript adapterScript = SETUP.createAdapterScript(schema);
                final UdfScript udf = IntegrationTestSetup.createUdf(schema);
                final VirtualSchema virtualSchema = SETUP
                        .getPreconfiguredVirtualSchemaBuilder("VS_FOR_CLASS_LIST_EXTRACTION")
                        .adapterScript(adapterScript).properties(Map.of("MAPPING", mapping)).build()) {
            return readClassList(classListExtractor, virtualSchema);
        }
    }

    private List<String> readClassList(final ClassListExtractor classListExtractor, final VirtualSchema virtualSchema) {
        final String query = "SELECT * FROM " + virtualSchema.getFullyQualifiedName() + ".TEST";
        return classListExtractor.capture(() -> {
            try (final ResultSet resultSet = getStatement().executeQuery(query)) {
                resultSet.next();
                assertThat(resultSet.getInt("ID"), equalTo(1));
            }
        });
    }

    private String getMappingDefinitionForSmallJsonFiles() {
        final EdmlDefinition edmlDefinition = EdmlDefinition.builder().source("test-data-*.json")
                .destinationTable("TEST")//
                .mapping(Fields.builder()//
                        .mapField("id", ToDecimalMapping.builder().build())//
                        .mapField("name", ToVarcharMapping.builder().varcharColumnSize(200).build()).build())
                .build();
        return new EdmlSerializer().serialize(edmlDefinition);
    }

    private void createTestSetupWithSmallJsonFiles(final int numberOfJsonFiles) throws IOException {
        createBucketIfNotExists(SMALL_JSON_FILES_FIXTURE_BUCKET);
        final AwsCredentialsProvider awsCredentialsProvider = TestConfig.instance().getAwsCredentialsProvider();
        final Map<String, String> tags = Map.of("exa:project", "VSS3", "exa:owner", TestConfig.instance().getOwner());
        new SmallJsonFilesTestSetup(awsCredentialsProvider, tags, SMALL_JSON_FILES_FIXTURE_BUCKET)
                .setup(numberOfJsonFiles);
    }

    private void createBucketIfNotExists(final String bucketName) {
        try {
            AWS_S3_TEST_SETUP.getS3Client().createBucket(request -> request.bucket(bucketName));
        } catch (final BucketAlreadyOwnedByYouException | BucketAlreadyExistsException exception) {
            // ignore
        }
    }
}
