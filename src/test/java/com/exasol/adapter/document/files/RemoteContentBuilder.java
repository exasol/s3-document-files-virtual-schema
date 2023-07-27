package com.exasol.adapter.document.files;

import com.exasol.adapter.document.documentfetcher.files.RemoteFileContent;
import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;

public class RemoteContentBuilder {

    private final S3ContainerSetup setup;
    private String bucket;
    private String key;
    private String value;

    public RemoteContentBuilder(final S3ContainerSetup setup) {
        this.setup = setup;
    }

    public RemoteContentBuilder bucket(final String bucket) {
        this.bucket = bucket;
        return this;
    }

    public RemoteContentBuilder key(final String key) {
        this.key = key;
        return this;
    }

    public RemoteContentBuilder value(final String value) {
        this.value = value;
        return this;
    }

    public RemoteFileContent build() {
        final S3Client s3Client = this.setup.getS3Client();
        final S3AsyncClient s3AsyncClient = this.setup.getS3AsyncClient();
        s3Client.createBucket(CreateBucketRequest.builder().bucket(this.bucket).build());
        s3Client.putObject(b -> b.bucket(this.bucket).key(this.key), RequestBody.fromString(this.value));
        return new S3RemoteFileContent(s3Client, s3AsyncClient, new S3ObjectDescription(this.key, this.value.length()),
                this.bucket);
    }
}
