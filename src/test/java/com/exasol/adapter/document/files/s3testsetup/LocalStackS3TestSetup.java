package com.exasol.adapter.document.files.s3testsetup;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import java.util.Optional;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

public class LocalStackS3TestSetup implements S3TestSetup {
    private final LocalStackContainer localStackContainer = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:1.0.3")).withServices(S3);

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
    public S3AsyncClient getS3AsyncClient() {
        return S3AsyncClient.builder().endpointOverride(this.localStackContainer.getEndpointOverride(S3))
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
    public Optional<String> getMfaToken() {
        return Optional.empty();
    }

    @Override
    public String getEntrypoint() {
        return "127.0.0.1:" + this.localStackContainer.getFirstMappedPort();
    }

    @Override
    public void close() {
        this.localStackContainer.stop();
    }
}
