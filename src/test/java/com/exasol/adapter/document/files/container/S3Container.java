package com.exasol.adapter.document.files.container;

import java.net.URI;

import org.testcontainers.containers.localstack.LocalStackContainer.Service;

public interface S3Container {

    String getAccessKey();

    String getSecretKey();

    String getRegion();

    URI getEndpointOverride(Service service);

    void start();

    void stop();

    Integer getMappedS3Port();
}
