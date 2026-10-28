package com.exasol.adapter.document.files.s3testsetup.container;

import java.net.URI;

import org.testcontainers.utility.DockerImageName;

import io.floci.testcontainers.FlociContainer;

public class FlociS3Container implements S3Container {

    private final FlociContainer container;

    @SuppressWarnings("resource") // Container will be closed in stop()
    public FlociS3Container(final DockerImageName image) {
        this(new FlociContainer(image).withReuse(true));
    }

    private FlociS3Container(final FlociContainer container) {
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
    public URI getEndpointOverride() {
        return URI.create(this.container.getEndpoint());
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
        return getEndpointOverride().getPort();
    }
}
