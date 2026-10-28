package com.exasol.adapter.document.files.itest.filefinder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.AfterParameterizedClassInvocation;
import org.junit.jupiter.params.BeforeParameterizedClassInvocation;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.document.files.S3FileFinder;
import com.exasol.adapter.document.files.connection.S3ConnectionProperties;
import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestBackend;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

@ParameterizedClass
@MethodSource("backends")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
class FileFinderIT {

    private static final String TEST_BUCKET = "test-bucket";
    private static final String CONTENT_1 = "content-1";
    private static final String CONTENT_2 = "content-2";
    private static final String CONTENT_OTHER = "other";
    @Parameter(0)
    private S3TestBackend backend;
    private S3ContainerSetup setup;
    private S3Client s3;
    private S3ConnectionProperties connectionInformation;

    static Stream<S3TestBackend> backends() {
        return Stream.of(S3TestBackend.values());
    }

    @BeforeParameterizedClassInvocation
    void beforeAll(final S3TestBackend backend) {
        this.backend = backend;
        this.setup = this.backend.createSetup();
        this.s3 = this.setup.getS3Client();
        this.s3.createBucket(b -> b.bucket(TEST_BUCKET));
        this.s3.putObject(b -> b.bucket(TEST_BUCKET).key("file-1.json"), RequestBody.fromBytes(CONTENT_1.getBytes()));
        this.s3.putObject(b -> b.bucket(TEST_BUCKET).key("file-2.json"), RequestBody.fromBytes(CONTENT_2.getBytes()));
        this.s3.putObject(b -> b.bucket(TEST_BUCKET).key("other.json"), RequestBody.fromBytes(CONTENT_OTHER.getBytes()));
        this.connectionInformation = this.setup.getConnectionProperties(TEST_BUCKET);
    }

    @AfterParameterizedClassInvocation
    void afterAll() {
        this.setup.close();
    }

    @Test
    void testReadFile() {
        final S3FileFinder s3FileFinder = new S3FileFinder(WildcardExpression.forNonWildcardString("file-1.json"),
                this.connectionInformation);
        assertThat(runAndGetFirstLines(s3FileFinder), containsInAnyOrder(CONTENT_1));
    }

    private List<String> runAndGetFirstLines(final S3FileFinder s3FileFinder) {
        final List<String> result = new ArrayList<>();
        s3FileFinder.loadFiles().forEachRemaining(file -> {
            try (InputStream inputStream = file.getContent().getInputStream()) {
                result.add(readFirstLine(inputStream));
            } catch (final IOException exception) {
                throw new UncheckedIOException("Failed to read file " + file, exception);
            }
        });
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
        final S3FileFinder s3FileFinder = new S3FileFinder(filePattern, this.connectionInformation);
        assertThat(runAndGetFirstLines(s3FileFinder), containsInAnyOrder(CONTENT_1, CONTENT_2));
    }

    @Test
    void testReadAllFiles() {
        final WildcardExpression filePattern = WildcardExpression.fromGlob("*");
        final S3FileFinder s3FileFinder = new S3FileFinder(filePattern, this.connectionInformation);
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
