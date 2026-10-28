package com.exasol.adapter.document.files.itest.remotefilecontent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.AfterParameterizedClassInvocation;
import org.junit.jupiter.params.BeforeParameterizedClassInvocation;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.document.documentfetcher.files.RemoteFileContent;
import com.exasol.adapter.document.files.RemoteContentBuilder;
import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestBackend;

@ParameterizedClass
@MethodSource("backends")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integration")
class RemoteFileContentIT {
    private static final String TEST_BUCKET = "test";
    private static final String TEST_DATA_VALUE = "test content";
    private static final String TEST_DATA_KEY = "TEST_DATA";
    @Parameter(0)
    private S3TestBackend backend;
    private S3ContainerSetup setup;
    private RemoteFileContent remoteFileContent;

    static Stream<S3TestBackend> backends() {
        return Stream.of(S3TestBackend.values());
    }

    @BeforeParameterizedClassInvocation
    void beforeAll(final S3TestBackend backend) {
        this.backend = backend;
        this.setup = this.backend.createSetup();
        this.remoteFileContent = new RemoteContentBuilder(this.setup) //
                .bucket(TEST_BUCKET) //
                .key(TEST_DATA_KEY) //
                .value(TEST_DATA_VALUE) //
                .build();
    }

    @AfterParameterizedClassInvocation
    void afterAll() {
        this.setup.close();
    }

    @Test
    void testGetInputStream() throws IOException {
        assertThat(this.remoteFileContent.getInputStream().readAllBytes(),
                equalTo(TEST_DATA_VALUE.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testGetRandomAccessInputStream() throws IOException {
        assertThat(this.remoteFileContent.getRandomAccessInputStream().readAllBytes(),
                equalTo(TEST_DATA_VALUE.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testLoadAsync() throws ExecutionException, InterruptedException {
        final byte[] byteArray = this.remoteFileContent.loadAsync().get();
        assertThat(byteArray, equalTo(TEST_DATA_VALUE.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testLoadAsyncWithTimeout() throws ExecutionException, InterruptedException, TimeoutException {
        final byte[] byteArray = this.remoteFileContent.loadAsync().get(10, TimeUnit.SECONDS);
        assertThat(byteArray, equalTo(TEST_DATA_VALUE.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testCancelAsyncLoading() {
        final Future<byte[]> future = this.remoteFileContent.loadAsync();
        future.cancel(true);
        assertTrue(future.isCancelled());
    }
}
