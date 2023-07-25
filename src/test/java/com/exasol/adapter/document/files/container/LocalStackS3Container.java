package com.exasol.adapter.document.files.container;

import java.net.URI;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.localstack.LocalStackContainer.Service;

public class LocalStackS3Container implements S3InterfaceContainer {

    public static LocalStackS3Container of(final LocalStackContainer localStackContainer) {
        return new LocalStackS3Container(localStackContainer);
    }

    private final LocalStackContainer localStackContainer;

    public LocalStackS3Container(final LocalStackContainer localStackContainer) {
        this.localStackContainer = localStackContainer;
    }

    @Override
    public String getAccessKey() {
        return this.localStackContainer.getAccessKey();
    }

    @Override
    public String getSecretKey() {
        return this.localStackContainer.getSecretKey();
    }

    @Override
    public String getRegion() {
        this.localStackContainer.close();
        return this.localStackContainer.getRegion();
    }

    @Override
    public URI getEndpointOverride(final Service service) {
        return this.localStackContainer.getEndpointOverride(service);
    }
}
