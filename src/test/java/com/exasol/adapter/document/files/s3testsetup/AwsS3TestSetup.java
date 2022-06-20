package com.exasol.adapter.document.files.s3testsetup;

import com.amazonaws.regions.DefaultAwsRegionProviderChain;
import com.exasol.adapter.document.files.TestConfig;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Optional;

public class AwsS3TestSetup implements S3TestSetup {
    private final AwsCredentials awsCredentials;
    private final String region;
    private final AwsCredentialsProvider credentialsProvider;

    public AwsS3TestSetup() {
        this.region = new DefaultAwsRegionProviderChain().getRegion();
        this.credentialsProvider = TestConfig.instance().getAwsCredentialsProvider();
        this.awsCredentials = this.credentialsProvider.resolveCredentials();
    }

    @Override
    public S3Client getS3Client() {
        return S3Client.builder().region(Region.of(this.region)).credentialsProvider(this.credentialsProvider).build();
    }

    @Override
    public S3AsyncClient getS3AsyncClient() {
        return S3AsyncClient.builder().region(Region.of(this.region)).credentialsProvider(this.credentialsProvider)
                .build();
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
    public String getEntrypoint() {
        return "amazonaws.com";
    }

    @Override
    public void close() {
        // nothing to do
    }
}
