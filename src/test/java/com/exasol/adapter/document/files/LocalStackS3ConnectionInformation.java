package com.exasol.adapter.document.files;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import org.testcontainers.containers.localstack.LocalStackContainer;

import com.exasol.ExaConnectionInformation;

public class LocalStackS3ConnectionInformation implements ExaConnectionInformation {
    private final LocalStackContainer localStackContainer;
    private final String bucket;
    private final String key;

    public LocalStackS3ConnectionInformation(final LocalStackContainer localStackContainer, final String bucket,
            final String key) {
        this.localStackContainer = localStackContainer;
        this.bucket = bucket;
        this.key = key;
    }

    @Override
    public ConnectionType getType() {
        return ConnectionType.PASSWORD;
    }

    @Override
    public String getAddress() {
        return "http://" + this.bucket + ".s3." + this.localStackContainer.getRegion() + "."
                + this.localStackContainer.getEndpointOverride(S3).toString().replace("http://", "") + "/" + this.key;
    }

    @Override
    public String getUser() {
        return this.localStackContainer.getAccessKey();
    }

    @Override
    public String getPassword() {
        return this.localStackContainer.getSecretKey();
    }
}
