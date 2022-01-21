package com.exasol.adapter.document.files.connection;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;

public class S3ConnectionPropertiesReader {

    public S3ConnectionProperties read(final ConnectionPropertiesReader reader) {
        return S3ConnectionProperties.builder()//
                .useSsl(reader.readBooleanWithDefault("useSsl", true))//
                .awsAccessKeyId(reader.readRequiredString("awsAccessKeyId"))
                .awsSecretAccessKey(reader.readRequiredString("awsSecretAccessKey"))
                .awsRegion(reader.readRequiredString("awsRegion"))
                .awsEndpointOverride(reader.readString("awsEndpointOverride").orElse(null))
                .awsSessionToken(reader.readString("awsSessionToken").orElse(null))
                .s3PathStyleAccess(reader.readBooleanWithDefault("s3PathStyleAccess", false))
                .s3Bucket(reader.readRequiredString("s3Bucket"))//
                .build();
    }
}
