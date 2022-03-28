package com.exasol.adapter.document.files;

import java.nio.file.Path;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@RequiredArgsConstructor
public class S3DirectUploader implements S3UploadInterface {
    private final S3Client s3Client;
    private final String testBucket;

    @Override
    public void uploadFile(final Path file, final String key) {
        this.s3Client.putObject(PutObjectRequest.builder().bucket(this.testBucket).key(key).build(), file);
    }
}
