package com.exasol.adapter.document.files.s3testsetup;

import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Optional;

public interface S3TestSetup extends AutoCloseable {
    S3Client getS3Client();

    S3AsyncClient getS3AsyncClient();

    String getRegion();

    String getUsername();

    String getPassword();

    Optional<String> getMfaToken();

    String getEntrypoint();

    @Override
    void close();
}
