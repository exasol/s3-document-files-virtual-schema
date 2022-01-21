package com.exasol.adapter.document.files;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import org.testcontainers.containers.localstack.LocalStackContainer;

import com.exasol.adapter.document.files.connection.S3ConnectionProperties;

public class LocalStackS3ConnectionInformationFactory {

    public S3ConnectionProperties getConnectionInfo(final LocalStackContainer localStackContainer,
            final String bucket) {
        final String endpoint = localStackContainer.getEndpointOverride(S3).toString().replace("http://", "");
        return S3ConnectionProperties.builder().s3Bucket(bucket).awsRegion(localStackContainer.getRegion())
                .awsEndpointOverride(endpoint).awsAccessKeyId(localStackContainer.getAccessKey())
                .awsSecretAccessKey(localStackContainer.getSecretKey()).useSsl(false).build();
    }
}
