package com.exasol.adapter.document.files;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.SQLDataException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.exasol.ConnectionDefinition;
import com.exasol.dbbuilder.dialects.exasol.VirtualSchema;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;

@Tag("integration")
@Testcontainers
class S3DocumentFilesAdapterIT extends AbstractDocumentFilesAdapterIT {
    private static final S3TestSetup AWS_S3_TEST_SETUP = new AwsS3TestSetup();
    private static String s3BucketName;
    private static IntegrationTestSetup SETUP;

    @BeforeAll
    static void beforeAll() throws Exception {
        s3BucketName = "s3-virtual-schema-test-bucket-" + System.currentTimeMillis();
        AWS_S3_TEST_SETUP.getS3Client().createBucket(builder -> builder.bucket(s3BucketName));
        SETUP = new IntegrationTestSetup(AWS_S3_TEST_SETUP, s3BucketName);
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
            SETUP.getS3Client().putObject(builder -> builder.bucket(s3BucketName).key(fileName),
                    RequestBody.fromBytes(inputStream.readAllBytes()));
        } catch (final IOException exception) {
            throw new IllegalStateException("Filed to upload test file.", exception);
        }
    }

    @Override
    protected void uploadDataFile(final Path fileContent, final String fileName) {
        SETUP.getS3Client().putObject(builder -> builder.bucket(s3BucketName).key(fileName),
                RequestBody.fromFile(fileContent));
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
}
