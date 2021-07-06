package com.exasol.adapter.document.files.s3testsetup;

import software.amazon.awssdk.services.s3.S3Client;

public interface S3TestSetup {
    S3Client getS3Client();

    String getRegion();

    String getUsername();

    String getPassword();

    String getEntrypoint();

    void teardown();
}
