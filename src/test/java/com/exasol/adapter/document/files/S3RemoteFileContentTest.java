package com.exasol.adapter.document.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

import org.junit.jupiter.api.*;

import com.exasol.adapter.document.documentfetcher.files.RemoteFileContent;
import com.exasol.adapter.document.files.s3testsetup.LocalStackS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

class S3RemoteFileContentTest {
    private static final String TEST_BUCKET = "test";
    private static final String TEST_DATA_VALUE = "test content";
    private static final String TEST_DATA_KEY = "TEST_DATA";
    private static S3TestSetup testSetup;
    private static RemoteFileContent remoteFileContent;

    @BeforeAll
    static void beforeAll() {
        testSetup = new LocalStackS3TestSetup();
        final S3Client s3Client = testSetup.getS3Client();
        final S3AsyncClient s3AsyncClient = testSetup.getS3AsyncClient();
        s3Client.createBucket(CreateBucketRequest.builder().bucket(TEST_BUCKET).build());
        s3Client.putObject(b -> b.bucket(TEST_BUCKET).key(TEST_DATA_KEY), RequestBody.fromString(TEST_DATA_VALUE));
        remoteFileContent = new S3RemoteFileContent(s3Client, s3AsyncClient,
                new S3ObjectDescription(TEST_DATA_KEY, TEST_DATA_VALUE.length()), TEST_BUCKET);
    }

    @AfterAll
    static void afterAll() {
        testSetup.close();
    }

    @Test
    void testGetInputStream() throws IOException {
        assertThat(remoteFileContent.getInputStream().readAllBytes(),
                equalTo(TEST_DATA_VALUE.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testGetRandomAccessInputStream() throws IOException {
        assertThat(remoteFileContent.getRandomAccessInputStream().readAllBytes(),
                equalTo(TEST_DATA_VALUE.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testLoadAsync() throws ExecutionException, InterruptedException {
        final byte[] byteArray = remoteFileContent.loadAsync().get();
        assertThat(byteArray, equalTo(TEST_DATA_VALUE.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testLoadAsyncWithTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        final byte[] byteArray = remoteFileContent.loadAsync().get(10, TimeUnit.SECONDS);
        assertThat(byteArray, equalTo(TEST_DATA_VALUE.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testCancelAsyncLoading() {
        final Future<byte[]> future = remoteFileContent.loadAsync();
        future.cancel(true);
        assertTrue(future.isCancelled());
    }
}