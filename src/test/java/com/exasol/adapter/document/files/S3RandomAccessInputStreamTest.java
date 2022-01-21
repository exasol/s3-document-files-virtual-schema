package com.exasol.adapter.document.files;

import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStreamTestBase;
import com.exasol.adapter.document.files.s3testsetup.LocalStackS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

class S3RandomAccessInputStreamTest extends RandomAccessInputStreamTestBase {
    private static final String TEST_BUCKET = "test";
    private static final String TEST_DATA_KEY = "TEST_DATA";
    private S3TestSetup testSetup;
    private S3Client s3Client;
    private int dataSize;

    @Override
    protected void prepareTestSetup(final byte[] bytes) {
        this.testSetup = new LocalStackS3TestSetup();
        this.s3Client = this.testSetup.getS3Client();
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
        return new S3RandomAccessInputStream(this.s3Client, new S3ObjectDescription(TEST_DATA_KEY, this.dataSize),
                TEST_BUCKET);
    }
}