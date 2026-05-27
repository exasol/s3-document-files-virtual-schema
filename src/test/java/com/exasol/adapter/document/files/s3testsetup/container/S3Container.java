package com.exasol.adapter.document.files.s3testsetup.container;

import java.net.URI;

public interface S3Container {

    String getAccessKey();

    String getSecretKey();

    String getRegion();

    URI getEndpointOverride();

    void start();

    void stop();

    Integer getMappedS3Port();
}
