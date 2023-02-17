package com.exasol.adapter.document.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.adapter.document.files.s3testsetup.LocalStackS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;

import software.amazon.awssdk.services.s3.S3Client;

class S3CacheIT {
    private static final S3TestSetup S3_TEST_SETUP = new LocalStackS3TestSetup();
    private static final String TEST_BUCKET_NAME = "test-bucket";
    private static final String CACHE_BUCKET_NAME = "cache-bucket";
    private static S3Client s3Client;
    private S3Cache.S3Interface s3Interface;
    private S3Cache s3Cache;

    @BeforeAll
    static void beforeAll() {
        s3Client = S3_TEST_SETUP.getS3Client();
        s3Client.createBucket(request -> request.bucket(TEST_BUCKET_NAME));
        s3Client.createBucket(request -> request.bucket(CACHE_BUCKET_NAME));
    }

    @AfterAll
    static void afterAll() {
        S3_TEST_SETUP.close();
    }

    @BeforeEach
    void beforeEach() {
        this.s3Interface = spy(new S3Cache.S3Interface(s3Client));
        this.s3Cache = new S3Cache(this.s3Interface, TEST_BUCKET_NAME, CACHE_BUCKET_NAME);
    }

    @Test
    void testFileIsUploadedOnlyOnce(@TempDir final Path tempDir) throws IOException {
        final Path file = createFileWithRandomContent(tempDir, 1001, "file.txt");
        final String fileName = "aNewFile.txt";
        this.s3Cache.uploadFile(file, fileName);
        this.s3Cache.uploadFile(file, fileName);
        verify(this.s3Interface, times(1)).uploadFile(eq(CACHE_BUCKET_NAME), any(), eq(file));
        assertThat(s3Client.getObject(request -> request.bucket(TEST_BUCKET_NAME).key(fileName)).readAllBytes(),
                equalTo(Files.readAllBytes(file)));
    }

    @Test
    void testFileIsUploadedTwiceWhenContentChanged(@TempDir final Path tempDir) throws IOException {
        final Path file = createFileWithRandomContent(tempDir, 1001, "file.txt");
        final Path otherFile = createFileWithRandomContent(tempDir, 1001, "otherFile.txt");
        final String fileName = "aFile.txt";
        this.s3Cache.uploadFile(file, fileName);
        this.s3Cache.uploadFile(otherFile, fileName);
        verify(this.s3Interface, times(1)).uploadFile(eq(CACHE_BUCKET_NAME), any(), eq(file));
        verify(this.s3Interface, times(1)).uploadFile(eq(CACHE_BUCKET_NAME), any(), eq(otherFile));
        assertThat(s3Client.getObject(request -> request.bucket(TEST_BUCKET_NAME).key(fileName)).readAllBytes(),
                equalTo(Files.readAllBytes(otherFile)));
    }

    private Path createFileWithRandomContent(final Path tempDir, final int contentLength, final String fileName)
            throws IOException {
        final Path file = tempDir.resolve(fileName);
        final Random random = new Random();
        final byte[] randomBytes = new byte[contentLength];
        random.nextBytes(randomBytes);
        Files.write(file, randomBytes);
        return file;
    }
}
