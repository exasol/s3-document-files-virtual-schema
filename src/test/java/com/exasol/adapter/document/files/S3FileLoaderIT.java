package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Tag("integration")
@Testcontainers
class S3FileLoaderIT {

    @Container
    public static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer().withServices(S3)
            .withReuse(true);
    public static final String TEST_BUCKET = "test-bucket";
    public static final String CONTENT_1 = "content-1";
    public static final String CONTENT_2 = "content-2";

    @BeforeAll
    static void beforeAll() {
        final S3Client s3 = S3Client.builder().endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(LOCAL_STACK_CONTAINER.getAccessKey(), LOCAL_STACK_CONTAINER.getSecretKey())))
                .region(Region.of(LOCAL_STACK_CONTAINER.getRegion())).build();

        s3.createBucket(b -> b.bucket(TEST_BUCKET));
        s3.putObject(b -> b.bucket(TEST_BUCKET).key("file-1.json"), RequestBody.fromBytes(CONTENT_1.getBytes()));
        s3.putObject(b -> b.bucket(TEST_BUCKET).key("file-2.json"), RequestBody.fromBytes(CONTENT_2.getBytes()));
    }

    @Test
    void testReadFile() throws IOException {
        final ExaConnectionInformation connectionInformation = new LocalStackS3ConnectionInformation(
                LOCAL_STACK_CONTAINER, TEST_BUCKET, "");
        final S3FileLoader s3FileLoader = new S3FileLoader("file-1.json", SegmentDescription.NO_SEGMENTATION,
                connectionInformation);
        assertThat(s3FileLoader.loadFiles().map(this::readFirstLine).collect(Collectors.toList()),
                containsInAnyOrder(CONTENT_1));
    }

    @CsvSource({ //
            "file-*.json", //
            "file*.json", //
            "file-?.json", //
            "*" //
    })
    @ParameterizedTest
    void testReadFiles(final String filePattern) throws IOException {
        final ExaConnectionInformation connectionInformation = new LocalStackS3ConnectionInformation(
                LOCAL_STACK_CONTAINER, TEST_BUCKET, "");
        final S3FileLoader s3FileLoader = new S3FileLoader(filePattern, SegmentDescription.NO_SEGMENTATION,
                connectionInformation);
        assertThat(s3FileLoader.loadFiles().map(this::readFirstLine).collect(Collectors.toList()),
                containsInAnyOrder(CONTENT_1, CONTENT_2));

    }

    private String readFirstLine(final InputStream stream) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return reader.readLine();
        } catch (final IOException exception) {
            throw new IllegalArgumentException("", exception);
        }
    }
}