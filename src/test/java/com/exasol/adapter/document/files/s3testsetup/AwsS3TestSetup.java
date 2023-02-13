package com.exasol.adapter.document.files.s3testsetup;

import java.net.InetSocketAddress;
import java.util.Optional;

import com.amazonaws.regions.DefaultAwsRegionProviderChain;
import com.exasol.adapter.document.files.TestConfig;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

public class AwsS3TestSetup implements S3TestSetup {
    private final AwsCredentials awsCredentials;
    private final String region;
    private final AwsCredentialsProvider credentialsProvider;
    private S3Client client;
    private S3AsyncClient asyncClient;

    public AwsS3TestSetup() {
        this.region = new DefaultAwsRegionProviderChain().getRegion();
        this.credentialsProvider = TestConfig.instance().getAwsCredentialsProvider();
        this.awsCredentials = this.credentialsProvider.resolveCredentials();
    }

    @Override
    public S3Client getS3Client() {
        if (this.client == null) {
            this.client = S3Client.builder().region(Region.of(this.region))
                    .credentialsProvider(this.credentialsProvider).build();
        }
        return this.client;
    }

    @Override
    public S3AsyncClient getS3AsyncClient() {
        if (this.asyncClient == null) {
            this.asyncClient = S3AsyncClient.builder().region(Region.of(this.region))
                    .credentialsProvider(this.credentialsProvider).build();
        }
        return this.asyncClient;
    }

    @Override
    public String getRegion() {
        return this.region;
    }

    @Override
    public String getUsername() {
        return this.awsCredentials.accessKeyId();
    }

    @Override
    public String getPassword() {
        return this.awsCredentials.secretAccessKey();
    }

    @Override
    public Optional<String> getMfaToken() {
        if (this.awsCredentials instanceof AwsSessionCredentials) {
            final AwsSessionCredentials sessionCredentials = (AwsSessionCredentials) this.awsCredentials;
            return Optional.of(sessionCredentials.sessionToken());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public InetSocketAddress getEntrypoint() {
        return new InetSocketAddress("amazonaws.com", 443);
    }

    @Override
    public void close() {
        // nothing to do
    }
}
