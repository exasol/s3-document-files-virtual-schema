package com.exasol.adapter.document.files;

import java.net.URI;

import com.exasol.adapter.document.files.connection.S3ConnectionProperties;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.core.client.builder.SdkClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;

/**
 * Factory for AWS S3 clients.
 */
public class S3ClientFactory {
    private final S3ConnectionProperties connectionProperties;

    /**
     * Create a new instance of {@link S3ClientFactory}.
     * 
     * @param connectionProperties connection properties
     */
    public S3ClientFactory(final S3ConnectionProperties connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    /**
     * Build a synchronous AWS S3 client.
     * 
     * @return client
     */
    public S3Client buildSyncClient() {
        final S3ClientBuilder s3ClientBuilder = S3Client.builder();
        setPathStyleAccess(s3ClientBuilder);
        setEndpointOverride(s3ClientBuilder);
        return s3ClientBuilder.region(Region.of(this.connectionProperties.getAwsRegion()))//
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials(this.connectionProperties)))//
                .build();
    }

    /**
     * Build an asynchronous AWS S3 client.
     *
     * @return client
     */
    public S3AsyncClient buildAsyncClient() {
        final S3AsyncClientBuilder s3AsyncClientBuilder = S3AsyncClient.builder();
        setPathStyleAccess(s3AsyncClientBuilder);
        setEndpointOverride(s3AsyncClientBuilder);
        return s3AsyncClientBuilder.region(Region.of(this.connectionProperties.getAwsRegion()))//
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials(this.connectionProperties)))//
                .build();
    }

    private AwsCredentials getCredentials(final S3ConnectionProperties properties) {
        if (properties.hasAwsSessionToken()) {
            return AwsSessionCredentials.create(properties.getAwsAccessKeyId(), properties.getAwsSecretAccessKey(),
                    properties.getAwsSessionToken());
        } else {
            return AwsBasicCredentials.create(properties.getAwsAccessKeyId(), properties.getAwsSecretAccessKey());
        }
    }

    private void setPathStyleAccess(final S3BaseClientBuilder<?, ?> s3ClientBuilder) {
        if (this.connectionProperties.isS3PathStyleAccess()) {
            final S3Configuration s3Configuration = S3Configuration.builder().pathStyleAccessEnabled(true).build();
            s3ClientBuilder.serviceConfiguration(s3Configuration);
        }
    }

    private void setEndpointOverride(final SdkClientBuilder<?, ?> s3AsyncClientBuilder) {
        if (this.connectionProperties.hasEndpointOverride()) {
            final URI endpointOverride = URI.create((this.connectionProperties.isUseSsl() ? "https://" : "http://")
                    + this.connectionProperties.getAwsEndpointOverride());
            s3AsyncClientBuilder.endpointOverride(endpointOverride);
        }
    }
}
