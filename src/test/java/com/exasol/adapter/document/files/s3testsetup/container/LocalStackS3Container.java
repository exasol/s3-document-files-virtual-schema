package com.exasol.adapter.document.files.s3testsetup.container;

import java.net.URI;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

public class LocalStackS3Container implements S3Container {

    public static LocalStackS3Container of(final LocalStackContainer localStackContainer) {
        return new LocalStackS3Container(localStackContainer);
    }

    private final LocalStackContainer container;

    LocalStackS3Container(final LocalStackContainer container) {
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
