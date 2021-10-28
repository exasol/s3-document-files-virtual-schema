package com.exasol.adapter.document.files.s3testsetup;

import com.amazonaws.regions.DefaultAwsRegionProviderChain;
import com.exasol.adapter.document.files.TestConfig;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

public class AwsS3TestSetup implements S3TestSetup {
    private final AwsCredentials awsCredentials;
    private final String region;
    private final ProfileCredentialsProvider credentialsProvider;

    public AwsS3TestSetup() {
        this.region = new DefaultAwsRegionProviderChain().getRegion();
        this.credentialsProvider = ProfileCredentialsProvider.create(TestConfig.instance().getAwsProfile());
        this.awsCredentials = this.credentialsProvider.resolveCredentials();
    }

    @Override
    public S3Client getS3Client() {
        return S3Client.builder().region(Region.of(this.region)).credentialsProvider(this.credentialsProvider).build();
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
        if (this.awsCredentials instanceof AwsSessionCredentials) {
            final AwsSessionCredentials sessionCredentials = (AwsSessionCredentials) this.awsCredentials;
            return sessionCredentials.secretAccessKey() + "##TOKEN##" + sessionCredentials.sessionToken();
        } else {
            return this.awsCredentials.secretAccessKey();
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
