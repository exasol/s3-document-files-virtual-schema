package com.exasol.adapter.document.files.s3testsetup;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class LocalStackS3TestSetup implements S3TestSetup {
    private final LocalStackContainer localStackContainer = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:0.12.2")).withServices(S3);

    public LocalStackS3TestSetup() {
        this.localStackContainer.start();
    }

    @Override
    public S3Client getS3Client() {
        return S3Client.builder().endpointOverride(this.localStackContainer.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(this.localStackContainer.getAccessKey(), this.localStackContainer.getSecretKey())))
                .region(Region.of(this.localStackContainer.getRegion())).build();
    }

    @Override
    public String getRegion() {
        return this.localStackContainer.getRegion();
    }

    @Override
    public String getUsername() {
        return this.localStackContainer.getAccessKey();
    }

    @Override
    public String getPassword() {
        return this.localStackContainer.getSecretKey();
    }

    @Override
    public String getEntrypoint() {
        return "127.0.0.1:" + this.localStackContainer.getFirstMappedPort();
    }

    @Override
    public void teardown() {
        this.localStackContainer.stop();
    }
}