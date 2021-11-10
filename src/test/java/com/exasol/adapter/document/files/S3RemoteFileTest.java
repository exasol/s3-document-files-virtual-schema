package com.exasol.adapter.document.files;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.*;

import com.exasol.adapter.document.files.s3testsetup.LocalStackS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

class S3RemoteFileTest {
    private static final String TEST_BUCKET = "test";
    private static final String TEST_DATA_VALUE = "test content";
    private static final String TEST_DATA_KEY = "TEST_DATA";
    private static S3TestSetup testSetup;
    private static S3Client s3Client;
    private static S3RemoteFile loadedFile;

    @BeforeAll
    static void beforeAll() {
        testSetup = new LocalStackS3TestSetup();
        s3Client = testSetup.getS3Client();
        s3Client.createBucket(CreateBucketRequest.builder().bucket(TEST_BUCKET).build());
        s3Client.putObject(b -> b.bucket(TEST_BUCKET).key(TEST_DATA_KEY), RequestBody.fromString(TEST_DATA_VALUE));
        loadedFile = new S3RemoteFile(s3Client, new S3ObjectDescription(
                new S3Uri(false, TEST_BUCKET, "eu-central-1", "", TEST_DATA_KEY), TEST_DATA_VALUE.length()));
    }

    @AfterAll
    static void afterAll() {
        testSetup.close();
    }

    @Test
    void testGetInputStream() throws IOException {
        assertThat(loadedFile.getInputStream().readAllBytes(),
                equalTo(TEST_DATA_VALUE.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void testGetRandomAccessInputStream() throws IOException {
        assertThat(loadedFile.getRandomAccessInputStream().readAllBytes(),
                equalTo(TEST_DATA_VALUE.getBytes(StandardCharsets.UTF_8)));
    }
}