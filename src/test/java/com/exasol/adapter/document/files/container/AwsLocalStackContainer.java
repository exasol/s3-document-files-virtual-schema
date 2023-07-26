package com.exasol.adapter.document.files.container;

import java.net.URI;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

public class AwsLocalStackContainer implements S3Container {

    public static AwsLocalStackContainer of(final LocalStackContainer localStackContainer) {
        return new AwsLocalStackContainer(localStackContainer);
    }

    private final LocalStackContainer container;

    AwsLocalStackContainer(final LocalStackContainer container) {
        this.container = container;
    }

    @Override
    public String getAccessKey() {
        return this.container.getAccessKey();
    }

    @Override
    public String getSecretKey() {
        return this.container.getSecretKey();
    }

    @Override
    public String getRegion() {
        this.container.close();
        return this.container.getRegion();
    }

    @Override
    public URI getEndpointOverride(final Service service) {
        return this.container.getEndpointOverride(service);
    }

    @Override
    public void start() {
        this.container.start();
    }

    @Override
    public void stop() {
        this.container.stop();
    }

    @Override
    public Integer getMappedS3Port() {
        return this.container.getFirstMappedPort();
    }
}
