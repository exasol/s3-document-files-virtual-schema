package com.exasol.adapter.document.files.itest.randomaccessinputstream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.AfterParameterizedClassInvocation;
import org.junit.jupiter.params.BeforeParameterizedClassInvocation;
import org.junit.jupiter.params.Parameter;
import org.junit.jupiter.params.ParameterizedClass;
import org.junit.jupiter.params.provider.MethodSource;

import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;
import com.exasol.adapter.document.files.RandomAccessInputStreamBuilder;
import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestBackend;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@ParameterizedClass
@MethodSource("backends")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RandomAccessInputStreamIT {
    private static final String TEST_BUCKET = "test";
    private static final String TEST_DATA_KEY = "TEST_DATA";
    private static final byte[] TEST_DATA = generateTestData(50_000);

    @Parameter(0)
    private S3TestBackend backend;
    private S3ContainerSetup testSetup;
    private RandomAccessInputStream stream;

    static Stream<S3TestBackend> backends() {
        return Stream.of(S3TestBackend.values());
    }

    @BeforeParameterizedClassInvocation
    void beforeBackend(final S3TestBackend backend) {
        this.backend = backend;
        this.testSetup = this.backend.createSetup();
        final S3Client s3Client = this.testSetup.getS3Client();
        s3Client.createBucket(CreateBucketRequest.builder().bucket(TEST_BUCKET).build());
        s3Client.putObject(builder -> builder.bucket(TEST_BUCKET).key(TEST_DATA_KEY),
                RequestBody.fromBytes(TEST_DATA));
    }

    @AfterParameterizedClassInvocation
    void afterBackend() {
        this.testSetup.close();
    }

    @BeforeEach
    void beforeEach() {
        this.stream = new RandomAccessInputStreamBuilder(this.testSetup) //
                .bucket(TEST_BUCKET) //
                .key(TEST_DATA_KEY) //
                .dataSize(TEST_DATA.length) //
                .build();
    }

    @AfterEach
    void afterEach() throws IOException {
        this.stream.close();
    }

    @Test
    void testRead() throws IOException {
        assertThat(this.stream.read(), equalTo((int) TEST_DATA[0]));
    }

    @Test
    void testReadAtEof() throws IOException {
        this.stream.skip(TEST_DATA.length);
        assertThat(this.stream.read(), equalTo(-1));
    }

    @Test
    void testMark() throws IOException {
        this.stream.mark(10);
        this.stream.read();
        this.stream.reset();
        assertThat(this.stream.read(), equalTo((int) TEST_DATA[0]));
    }

    @Test
    void testMarkSupported() {
        assertTrue(this.stream.markSupported());
    }

    @Test
    void testReadToArray() throws IOException {
        final byte[] actual = new byte[TEST_DATA.length];
        this.stream.read(actual);
        assertThat(actual, equalTo(TEST_DATA));
    }

    @Test
    void testReadToArrayEof() throws IOException {
        final byte[] actual = new byte[TEST_DATA.length + 1];
        assertThat(this.stream.read(actual), equalTo(TEST_DATA.length));
    }

    @Test
    void testNormalRead() throws IOException {
        assertThat(this.stream.readAllBytes(), equalTo(TEST_DATA));
    }

    @Test
    void testSeekAndRead() throws IOException {
        this.stream.seek(2);
        assertThat((byte) this.stream.read(), equalTo(TEST_DATA[2]));
    }

    @Test
    void testSkipAndRead() throws IOException {
        long remaining = 2;
        while (remaining > 0) {
            remaining -= this.stream.skip(remaining);
        }
        assertThat((byte) this.stream.read(), equalTo(TEST_DATA[2]));
    }

    @Test
    void testBackwardSeek() throws IOException {
        this.stream.readAllBytes();
        this.stream.seek(0);
        assertThat("data was different", Arrays.equals(this.stream.readAllBytes(), TEST_DATA));
    }

    @Test
    void testSeekOutOfRange() {
        assertDoesNotThrow(() -> this.stream.seek(TEST_DATA.length + 1L));
    }

    @Test
    void testArrayReadAfterSeek() throws IOException {
        final byte[] actual = new byte[3];
        this.stream.seek(2);
        this.stream.read(actual, 0, actual.length);
        assertThat(actual, equalTo(Arrays.copyOfRange(TEST_DATA, 2, 5)));
    }

    @Test
    void testGetPosAfterInit() throws IOException {
        assertThat(this.stream.getPos(), equalTo(0L));
    }

    @Test
    void testGetPosAfterRead() throws IOException {
        this.stream.read();
        assertThat(this.stream.getPos(), equalTo(1L));
    }

    @Test
    void testGetPosAfterSeek() throws IOException {
        this.stream.seek(2);
        assertThat(this.stream.getPos(), equalTo(2L));
    }

    @Test
    void testGetLength() throws IOException {
        assertThat(this.stream.getLength(), equalTo((long) TEST_DATA.length));
    }

    private static byte[] generateTestData(final int size) {
        final byte[] data = new byte[size];
        for (int i = 0; i < size; i++) {
            data[i] = (byte) ((i * 23) % 200);
        }
        return data;
    }
}
