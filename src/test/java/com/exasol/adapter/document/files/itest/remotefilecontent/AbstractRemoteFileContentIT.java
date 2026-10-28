package com.exasol.adapter.document.files.itest.remotefilecontent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.documentfetcher.files.RemoteFileContent;
import com.exasol.adapter.document.files.RemoteContentBuilder;
import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;

abstract class AbstractRemoteFileContentIT {
    private static final String TEST_BUCKET = "test";
    private static final String TEST_DATA_VALUE = "test content";
    private static final String TEST_DATA_KEY = "TEST_DATA";
    private static RemoteFileContent remoteFileContent;

    static void beforeAll(final S3ContainerSetup setup) {
        remoteFileContent = new RemoteContentBuilder(setup) //
                .bucket(TEST_BUCKET) //
                .key(TEST_DATA_KEY) //
                .value(TEST_DATA_VALUE) //
                .build();
    }

    static void afterAll(final S3ContainerSetup setup) {
        setup.close();
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