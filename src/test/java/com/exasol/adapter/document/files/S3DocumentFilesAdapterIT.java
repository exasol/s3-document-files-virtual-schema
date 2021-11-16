package com.exasol.adapter.document.files;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.sql.*;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Tag;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.serializer.EdmlSerializer;
import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.exasol.ConnectionDefinition;
import com.exasol.dbbuilder.dialects.exasol.VirtualSchema;
import com.exasol.matcher.TypeMatchMode;
import com.exasol.performancetestrecorder.PerformanceTestRecorder;
import com.exasol.smalljsonfilesfixture.SmallJsonFilesTestSetup;

import software.amazon.awssdk.services.s3.model.*;

@Tag("integration")
@Testcontainers
class S3DocumentFilesAdapterIT extends AbstractDocumentFilesAdapterIT {
    private static final S3TestSetup AWS_S3_TEST_SETUP = new AwsS3TestSetup();
    private static final String CACHE_BUCKET_NAME = "persistent-s3-vs-test-file-cache";
    private static final String SMALL_JSON_FILES_FIXTURE_BUCKET = "persistent-small-json-files-test-fixture";
    private static String s3BucketName;
    private static IntegrationTestSetup SETUP;
    private static S3Cache s3Cache;

    @BeforeAll
    static void beforeAll() throws Exception {
        s3BucketName = "s3-virtual-schema-test-bucket-" + System.currentTimeMillis();
        AWS_S3_TEST_SETUP.getS3Client().createBucket(builder -> builder.bucket(s3BucketName));
        SETUP = new IntegrationTestSetup(AWS_S3_TEST_SETUP, s3BucketName);
        s3Cache = new S3Cache(SETUP.getS3Client(), s3BucketName, CACHE_BUCKET_NAME);
    }

    @AfterAll
    static void afterAll() throws Exception {
        AWS_S3_TEST_SETUP.getS3Client().deleteBucket(DeleteBucketRequest.builder().bucket(s3BucketName).build());
        SETUP.close();
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
        s3Cache.uploadFile(fileContent, fileName);
    }

    @Override
    protected void createVirtualSchema(final String schemaName, final String mapping) {
        SETUP.createVirtualSchema(schemaName, mapping);
    }

    @Test
    void testEmptyConnectionString() throws BucketAccessException, TimeoutException {
        SETUP.getBucket().uploadInputStream(() -> getClass().getClassLoader().getResourceAsStream("simpleMapping.json"),
                "mapping.json");
        final ConnectionDefinition connection = SETUP.getExasolObjectFactory().createConnectionDefinition(
                "EMPTY_S3_CONNECTION", "", SETUP.getS3TestSetup().getUsername(), SETUP.getS3TestSetup().getPassword());
        final VirtualSchema virtualSchema = SETUP.getPreconfiguredVirtualSchemaBuilder("EMPTY_CONNECTION_SCHEMA")
                .connectionDefinition(connection).properties(Map.of("MAPPING", "/bfsdefault/default/mapping.json"))
                .build();
        try {
            final SQLDataException exception = assertThrows(SQLDataException.class,
                    () -> SETUP.getStatement().executeQuery("SELECT * FROM EMPTY_CONNECTION_SCHEMA.MY_TABLE"));
            assertThat(exception.getMessage(), containsString(
                    "E-S3VS-1: The given S3 Bucket string 'testData-' has an invalid format. Expected format: http(s)://BUCKET.s3.REGION.amazonaws.com/KEY or http(s)://BUCKET.s3.REGION.CUSTOM_ENDPOINT/KEY. Note that the address from the CONNECTION and the source are concatenated. Change the address in your CONNECTION and the source in your mapping definition."));
        } finally {
            virtualSchema.drop();
        }
    }

    @Test
    @Tag("regression")
    void testManySmallJsonFiles(final TestInfo testInfo) throws Exception {
        final int numberOfJsonFiles = 1_000_000;
        createTestSetupWithSmallJsonFiles(numberOfJsonFiles);
        final String mapping = getMappingDefinitionForSmallJsonFiles();
        final String bucketAddress = "https://" + SMALL_JSON_FILES_FIXTURE_BUCKET + ".s3."
                + AWS_S3_TEST_SETUP.getRegion() + ".amazonaws.com/";
        final ConnectionDefinition connection = SETUP.getExasolObjectFactory().createConnectionDefinition(
                "SMALL_FILES_BUCKET", bucketAddress, AWS_S3_TEST_SETUP.getUsername(), AWS_S3_TEST_SETUP.getPassword());
        try {
            SETUP.createVirtualSchema("SMALL_JSON_FILES_VS", mapping, connection);
            for (int runCounter = 0; runCounter < 5; runCounter++) {
                PerformanceTestRecorder.getInstance().recordExecution(testInfo, () -> {
                    try (final ResultSet resultSet = getStatement()
                            .executeQuery("SELECT COUNT(*) FROM (SELECT * FROM SMALL_JSON_FILES_VS.TEST)")) {
                        assertThat(resultSet, table().row(numberOfJsonFiles).matches(TypeMatchMode.NO_JAVA_TYPE_CHECK));
                    }
                });
            }
        } finally {
            connection.drop();
        }
    }

    private String getMappingDefinitionForSmallJsonFiles() {
        final EdmlDefinition edmlDefinition = EdmlDefinition.builder()
                .schema("https://schemas.exasol.com/edml-1.3.0.json").source("test-data-*.json")
                .destinationTable("TEST")//
                .mapping(Fields.builder()//
                        .mapField("id", ToDecimalMapping.builder().build())//
                        .mapField("name", ToVarcharMapping.builder().varcharColumnSize(200).build()).build())
                .build();
        return new EdmlSerializer().serialize(edmlDefinition);
    }

    private void createTestSetupWithSmallJsonFiles(final int numberOfJsonFiles) throws IOException {
        createBucketIfNotExists(SMALL_JSON_FILES_FIXTURE_BUCKET);
        new SmallJsonFilesTestSetup().setup(
                Map.of("exa:project", "S3VS", "exa:owner", TestConfig.instance().getOwner()),
                SMALL_JSON_FILES_FIXTURE_BUCKET, TestConfig.instance().getAwsCredentialsProvider(), numberOfJsonFiles,
                20_000);
    }

    private void createBucketIfNotExists(final String bucketName) {
        try {
            AWS_S3_TEST_SETUP.getS3Client().createBucket(request -> request.bucket(bucketName));
        } catch (final BucketAlreadyOwnedByYouException | BucketAlreadyExistsException exception) {
            // ignore
        }
    }
}
