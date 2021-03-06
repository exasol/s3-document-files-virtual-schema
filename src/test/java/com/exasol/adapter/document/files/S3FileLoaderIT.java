package com.exasol.adapter.document.files;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.files.InputStreamWithResourceName;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Tag("integration")
@Testcontainers
class S3FileLoaderIT {

    @Container
    private static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.12.2")).withServices(S3);
    private static final String TEST_BUCKET = "test-bucket";
    private static final String CONTENT_1 = "content-1";
    private static final String CONTENT_2 = "content-2";
    private static final String CONTENT_OTHER = "other";

    @BeforeAll
    static void beforeAll() {
        final S3Client s3 = S3Client.builder().endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(LOCAL_STACK_CONTAINER.getAccessKey(), LOCAL_STACK_CONTAINER.getSecretKey())))
                .region(Region.of(LOCAL_STACK_CONTAINER.getRegion())).build();

        s3.createBucket(b -> b.bucket(TEST_BUCKET));
        s3.putObject(b -> b.bucket(TEST_BUCKET).key("file-1.json"), RequestBody.fromBytes(CONTENT_1.getBytes()));
        s3.putObject(b -> b.bucket(TEST_BUCKET).key("file-2.json"), RequestBody.fromBytes(CONTENT_2.getBytes()));
        s3.putObject(b -> b.bucket(TEST_BUCKET).key("other.json"), RequestBody.fromBytes(CONTENT_OTHER.getBytes()));
    }

    @Test
    void testReadFile() throws IOException {
        final ExaConnectionInformation connectionInformation = new LocalStackS3ConnectionInformation(
                LOCAL_STACK_CONTAINER, TEST_BUCKET, "file-1.json");
        final S3FileLoader s3FileLoader = new S3FileLoader(
                WildcardExpression.forNonWildcardString(connectionInformation.getAddress()),
                SegmentDescription.NO_SEGMENTATION, connectionInformation);
        assertThat(s3FileLoader.loadFiles().map(InputStreamWithResourceName::getInputStream).map(this::readFirstLine)
                .collect(Collectors.toList()), containsInAnyOrder(CONTENT_1));
    }

    @CsvSource({ //
            "file-*.json", //
            "file*.json", //
            "file-?.json" //
    })
    @ParameterizedTest
    void testReadFilesWithWildcard(final String fileGlob) {
        final ExaConnectionInformation connectionInformation = new LocalStackS3ConnectionInformation(
                LOCAL_STACK_CONTAINER, TEST_BUCKET, fileGlob);
        final WildcardExpression filePattern = WildcardExpression.fromGlob(connectionInformation.getAddress());
        final S3FileLoader s3FileLoader = new S3FileLoader(filePattern, SegmentDescription.NO_SEGMENTATION,
                connectionInformation);
        assertThat(s3FileLoader.loadFiles().map(InputStreamWithResourceName::getInputStream).map(this::readFirstLine)
                .collect(Collectors.toList()), containsInAnyOrder(CONTENT_1, CONTENT_2));
    }

    @Test
    void testReadAllFiles() {
        final ExaConnectionInformation connectionInformation = new LocalStackS3ConnectionInformation(
                LOCAL_STACK_CONTAINER, TEST_BUCKET, "*");
        final WildcardExpression filePattern = WildcardExpression.fromGlob(connectionInformation.getAddress());
        final S3FileLoader s3FileLoader = new S3FileLoader(filePattern, SegmentDescription.NO_SEGMENTATION,
                connectionInformation);
        assertThat(s3FileLoader.loadFiles().map(InputStreamWithResourceName::getInputStream).map(this::readFirstLine)
                .collect(Collectors.toList()), containsInAnyOrder(CONTENT_1, CONTENT_2, CONTENT_OTHER));
    }

    private String readFirstLine(final InputStream stream) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return reader.readLine();
        } catch (final IOException exception) {
            throw new IllegalArgumentException("", exception);
        }
    }
}