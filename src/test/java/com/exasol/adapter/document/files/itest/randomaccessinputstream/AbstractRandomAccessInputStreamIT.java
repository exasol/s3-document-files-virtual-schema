package com.exasol.adapter.document.files.itest.randomaccessinputstream;

import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStreamTestBase;
import com.exasol.adapter.document.files.RandomAccessInputStreamBuilder;
import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

abstract class AbstractRandomAccessInputStreamIT extends RandomAccessInputStreamTestBase {
    private static final String TEST_BUCKET = "test";
    private static final String TEST_DATA_KEY = "TEST_DATA";

    private S3ContainerSetup testSetup;
    private S3Client s3Client;
    private int dataSize;

    protected void prepareTestSetup(final S3ContainerSetup testSetup, final byte[] bytes) {
        this.testSetup = testSetup;
        this.s3Client = testSetup.getS3Client();
        this.s3Client.createBucket(CreateBucketRequest.builder().bucket(TEST_BUCKET).build());
        this.dataSize = bytes.length;
        this.s3Client.putObject(b -> b.bucket(TEST_BUCKET).key(TEST_DATA_KEY), RequestBody.fromBytes(bytes));
    }

    @Override
    protected void cleanupTestSetup() {
        this.testSetup.close();
    }

    @Override
    protected RandomAccessInputStream getSeekableInputStream() {
        return new RandomAccessInputStreamBuilder(this.testSetup) //
                .bucket(TEST_BUCKET) //
                .key(TEST_DATA_KEY) //
                .dataSize(this.dataSize) //
                .build();
    }
}