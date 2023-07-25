package com.exasol.adapter.document.files;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

import com.exasol.adapter.document.files.connection.S3ConnectionProperties;
import com.exasol.adapter.document.files.container.S3InterfaceContainer;

public class LocalStackS3ConnectionInformationFactory {

//    public S3ConnectionProperties getConnectionInfo(final LocalStackContainer localStackContainer,
//            final String bucket) {
//        final String endpoint = localStackContainer.getEndpointOverride(S3).toString().replace("http://", "");
//        return S3ConnectionProperties.builder().s3Bucket(bucket).awsRegion(localStackContainer.getRegion())
//                .awsEndpointOverride(endpoint).awsAccessKeyId(localStackContainer.getAccessKey())
//                .awsSecretAccessKey(localStackContainer.getSecretKey()).useSsl(false).build();
//    }

    public S3ConnectionProperties getConnectionInfo(final S3InterfaceContainer s3Container, final String bucket) {
        final String endpoint = s3Container.getEndpointOverride(S3).toString().replace("http://", "");
        return S3ConnectionProperties.builder().s3Bucket(bucket).awsRegion(s3Container.getRegion())
                .awsEndpointOverride(endpoint).awsAccessKeyId(s3Container.getAccessKey())
                .awsSecretAccessKey(s3Container.getSecretKey()).useSsl(false).build();
    }
}
