package com.exasol.adapter.document.files;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStreamTestBase;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

@Testcontainers
class S3LoadedFileTest extends RandomAccessInputStreamTestBase {
    private static final String TEST_BUCKET = "test";

    @Container
    private static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:0.12.2")).withServices(S3);
    private static final String TEST_DATA_KEY = "TEST_DATA";
    private S3Client s3Client;
    private int dataSize;

    @Override
    protected void prepareTestSetup(final byte[] bytes) {
        this.s3Client = S3Client.builder().endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(LOCAL_STACK_CONTAINER.getAccessKey(), LOCAL_STACK_CONTAINER.getSecretKey())))
                .region(Region.of(LOCAL_STACK_CONTAINER.getRegion())).build();
        this.s3Client.createBucket(CreateBucketRequest.builder().bucket(TEST_BUCKET).build());
        this.dataSize = bytes.length;
        this.s3Client.putObject(b -> b.bucket(TEST_BUCKET).key(TEST_DATA_KEY), RequestBody.fromBytes(bytes));
    }

    @Override
    protected RandomAccessInputStream getSeekableInputStream() {
        final S3LoadedFile loadedFile = new S3LoadedFile(this.s3Client, new S3ObjectDescription(
                new S3Uri(false, TEST_BUCKET, "eu-central-1", "", TEST_DATA_KEY), this.dataSize));
        return loadedFile.getRandomAccessInputStream();
    }
}