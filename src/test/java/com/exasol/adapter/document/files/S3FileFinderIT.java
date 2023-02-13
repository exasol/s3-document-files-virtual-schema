package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.exasol.adapter.document.files.connection.S3ConnectionProperties;
import com.exasol.adapter.document.files.s3testsetup.LocalStackS3TestSetup;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Tag("integration")
@Testcontainers
class S3FileFinderIT {
    @Container
    private static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer(
            DockerImageName.parse(LocalStackS3TestSetup.LOCALSTACK_CONTAINER)).withServices(S3);
    private static final String TEST_BUCKET = "test-bucket";
    private static final String CONTENT_1 = "content-1";
    private static final String CONTENT_2 = "content-2";
    private static final String CONTENT_OTHER = "other";
    private static S3Client s3;
    private static S3ConnectionProperties connectionInformation;

    @BeforeAll
    static void beforeAll() {
        s3 = S3Client.builder().endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(LOCAL_STACK_CONTAINER.getAccessKey(), LOCAL_STACK_CONTAINER.getSecretKey())))
                .region(Region.of(LOCAL_STACK_CONTAINER.getRegion())).build();

        s3.createBucket(b -> b.bucket(TEST_BUCKET));
        s3.putObject(b -> b.bucket(TEST_BUCKET).key("file-1.json"), RequestBody.fromBytes(CONTENT_1.getBytes()));
        s3.putObject(b -> b.bucket(TEST_BUCKET).key("file-2.json"), RequestBody.fromBytes(CONTENT_2.getBytes()));
        s3.putObject(b -> b.bucket(TEST_BUCKET).key("other.json"), RequestBody.fromBytes(CONTENT_OTHER.getBytes()));
        connectionInformation = new LocalStackS3ConnectionInformationFactory().getConnectionInfo(LOCAL_STACK_CONTAINER,
                TEST_BUCKET);
    }

    @Test
    void testReadFile() {
        final S3FileFinder s3FileFinder = new S3FileFinder(WildcardExpression.forNonWildcardString("file-1.json"),
                connectionInformation);
        assertThat(runAndGetFirstLines(s3FileFinder), containsInAnyOrder(CONTENT_1));
    }

    private List<String> runAndGetFirstLines(final S3FileFinder s3FileFinder) {
        final List<String> result = new ArrayList<>();
        s3FileFinder.loadFiles()
                .forEachRemaining(file -> result.add(readFirstLine(file.getContent().getInputStream())));
        return result;
    }

    @CsvSource({ //
            "file-*.json", //
            "file*.json", //
            "file-?.json" //
    })
    @ParameterizedTest
    void testReadFilesWithWildcard(final String fileGlob) {
        final WildcardExpression filePattern = WildcardExpression.fromGlob(fileGlob);
        final S3FileFinder s3FileFinder = new S3FileFinder(filePattern, connectionInformation);
        assertThat(runAndGetFirstLines(s3FileFinder), containsInAnyOrder(CONTENT_1, CONTENT_2));
    }

    @Test
    void testReadAllFiles() {
        final WildcardExpression filePattern = WildcardExpression.fromGlob("*");
        final S3FileFinder s3FileFinder = new S3FileFinder(filePattern, connectionInformation);
        assertThat(runAndGetFirstLines(s3FileFinder), containsInAnyOrder(CONTENT_1, CONTENT_2, CONTENT_OTHER));
    }

    private String readFirstLine(final InputStream stream) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return reader.readLine();
        } catch (final IOException exception) {
            throw new IllegalArgumentException("", exception);
        }
    }
}