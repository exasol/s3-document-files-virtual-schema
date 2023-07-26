package com.exasol.adapter.document.files.s3testsetup;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Optional;

import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.utility.DockerImageName;

import com.exasol.adapter.document.files.connection.S3ConnectionProperties;
import com.exasol.adapter.document.files.container.*;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * This setup currently supports LocalStack as well as MinIO containers.
 */
public class S3ContainerSetup implements S3TestSetup {

    static class DockerImage {
        static final String LOCALSTACK = "localstack/localstack:1.4.0";
        static final String MINIO = "minio/minio:RELEASE.2023-07-21T21-12-44Z.fips";
    }

    @SuppressWarnings("resource")
    public static S3ContainerSetup localStack() {
        final LocalStackContainer container = new LocalStackContainer( //
                DockerImageName.parse(DockerImage.LOCALSTACK)) //
                        .withServices(S3) //
                        .withReuse(true);
        return new S3ContainerSetup(LocalStackS3Container.of(container));
    }

    public static S3ContainerSetup minio() {
        final MinioContainer container = new MinioContainer(DockerImageName.parse(DockerImage.MINIO));
        return new S3ContainerSetup(container);
    }

    private final S3Container container;

    public S3ContainerSetup(final S3Container container) {
        this.container = container;
        this.container.start();
    }

    @Override
    public S3Client getS3Client() {
        return S3Client.builder() //
                .endpointOverride(getEndpointOverride()) //
                .credentialsProvider(getCredentialsProvider()) //
                .region(Region.of(getRegion())) //
                .build();
    }

    @Override
    public S3AsyncClient getS3AsyncClient() {
        return S3AsyncClient.builder() //
                .endpointOverride(getEndpointOverride()) //
                .credentialsProvider(getCredentialsProvider()) //
                .region(Region.of(getRegion())) //
                .build();
    }

    public URI getEndpointOverride() {
        return this.container.getEndpointOverride(S3);
    }

    private AwsCredentialsProvider getCredentialsProvider() {
        return StaticCredentialsProvider.create( //
                AwsBasicCredentials.create(getUsername(), getPassword()));
    }

    @Override
    public String getRegion() {
        return this.container.getRegion();
    }

    @Override
    public String getUsername() {
        return this.container.getAccessKey();
    }

    @Override
    public String getPassword() {
        return this.container.getSecretKey();
    }

    @Override
    public Optional<String> getMfaToken() {
        return Optional.empty();
    }

    @Override
    public Optional<InetSocketAddress> getEntrypoint() {
        return Optional.of(new InetSocketAddress("127.0.0.1", this.container.getMappedS3Port()));
    }

    @Override
    public void close() {
        this.container.stop();
    }

    public S3ConnectionProperties getConnectionProperties(final String bucket) {
        final String endpoint = getEndpointOverride().toString().replace("http://", "");
        return S3ConnectionProperties.builder() //
                .s3Bucket(bucket) //
                .awsRegion(getRegion()) //
                .awsEndpointOverride(endpoint) //
                .awsAccessKeyId(getUsername()) //
                .awsSecretAccessKey(getPassword()) //
                .useSsl(false) //
                .build();
    }
}
