package com.exasol.adapter.document.files.container;

import java.net.URI;

import org.testcontainers.containers.localstack.LocalStackContainer.Service;

public interface S3InterfaceContainer {

    public abstract String getAccessKey();

    public abstract String getSecretKey();

    public abstract String getRegion();

    public abstract URI getEndpointOverride(Service service);
}
