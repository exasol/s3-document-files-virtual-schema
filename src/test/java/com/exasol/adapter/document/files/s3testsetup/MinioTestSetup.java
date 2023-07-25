package com.exasol.adapter.document.files.s3testsetup;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import java.net.InetSocketAddress;
import java.util.Optional;

import com.exasol.adapter.document.files.container.MinioContainer;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

public class MinioTestSetup implements S3TestSetup {
    public static final String DOCKER_IMAGE_NAME = "minio/minio:RELEASE.2023-07-21T21-12-44Z.fips";

    // https://stackoverflow.com/questions/55402610/configuring-minio-server-for-use-with-testcontainers
    private final MinioContainer minioContainer = new MinioContainer(DOCKER_IMAGE_NAME);

    public MinioTestSetup() {
        this.minioContainer.start();
    }

    @Override
    public S3Client getS3Client() {
        return S3Client.builder().endpointOverride(this.minioContainer.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(this.minioContainer.getAccessKey(), this.minioContainer.getSecretKey())))
                .region(Region.of(this.minioContainer.getRegion())).build();
    }

    @Override
    public S3AsyncClient getS3AsyncClient() {
        return S3AsyncClient.builder().endpointOverride(this.minioContainer.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(this.minioContainer.getAccessKey(), this.minioContainer.getSecretKey())))
                .region(Region.of(this.minioContainer.getRegion())).build();
    }

    @Override
    public String getRegion() {
        return this.minioContainer.getRegion();
    }

    @Override
    public String getUsername() {
        return this.minioContainer.getAccessKey();
    }

    @Override
    public String getPassword() {
        return this.minioContainer.getSecretKey();
    }

    @Override
    public Optional<String> getMfaToken() {
        return Optional.empty();
    }

    @Override
    public Optional<InetSocketAddress> getEntrypoint() {
        return Optional.of(new InetSocketAddress("127.0.0.1", this.minioContainer.getFirstMappedPort()));
    }

    @Override
    public void close() {
        this.minioContainer.stop();
    }
}
