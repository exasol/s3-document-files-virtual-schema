package com.exasol.adapter.document.files;

import java.nio.file.Path;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

public class S3DirectUploader implements S3UploadInterface {
    private final S3Client s3Client;
    private final String testBucket;

    public S3DirectUploader(final S3Client s3Client, final String testBucket) {
        this.s3Client = s3Client;
        this.testBucket = testBucket;
    }

    @Override
    public void uploadFile(final Path file, final String key) {
        this.s3Client.putObject(PutObjectRequest.builder().bucket(this.testBucket).key(key).build(), file);
    }
}
