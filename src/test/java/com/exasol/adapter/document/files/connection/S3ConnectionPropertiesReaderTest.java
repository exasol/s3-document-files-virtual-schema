package com.exasol.adapter.document.files.connection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Test;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;

class S3ConnectionPropertiesReaderTest {
    @Test
    void testReadOnlyRequired() {
        final S3ConnectionProperties properties = runReader(
                "{\"awsAccessKeyId\": \"myKey\", \"awsSecretAccessKey\": \"mySecretAccessKey\", "
                        + "\"awsRegion\": \"eu-central-1\", \"s3Bucket\": \"my-bucket\" }");
        assertAll(() -> assertThat(properties.getAwsAccessKeyId(), equalTo("myKey")),
                () -> assertThat(properties.getAwsSecretAccessKey(), equalTo("mySecretAccessKey")),
                () -> assertThat(properties.getAwsRegion(), equalTo("eu-central-1")),
                () -> assertThat(properties.getS3Bucket(), equalTo("my-bucket")),
                () -> assertThat(properties.getAwsEndpointOverride(), equalTo(null)),
                () -> assertThat(properties.getAwsSessionToken(), equalTo(null)),
                () -> assertThat(properties.isS3PathStyleAccess(), equalTo(false)),
                () -> assertThat(properties.isUseSsl(), equalTo(true)));
    }

    private S3ConnectionProperties runReader(final String json) {
        return new S3ConnectionPropertiesReader().read(new ConnectionPropertiesReader(json, ""));
    }

    @Test
    void testReadAll() {
        final S3ConnectionProperties properties = runReader(
                "{\"awsAccessKeyId\": \"myKey\", \"awsSecretAccessKey\": \"mySecretAccessKey\", "
                        + "\"awsRegion\": \"eu-central-1\", \"s3Bucket\": \"my-bucket\", "
                        + "\"useSsl\": false, \"s3PathStyleAccess\": true, \"awsSessionToken\": \"myToken\", "
                        + "\"awsEndpointOverride\": \"s3.my-company.com\" }");
        assertAll(() -> assertThat(properties.getAwsAccessKeyId(), equalTo("myKey")),
                () -> assertThat(properties.getAwsSecretAccessKey(), equalTo("mySecretAccessKey")),
                () -> assertThat(properties.getAwsRegion(), equalTo("eu-central-1")),
                () -> assertThat(properties.getS3Bucket(), equalTo("my-bucket")),
                () -> assertThat(properties.getAwsEndpointOverride(), equalTo("s3.my-company.com")),
                () -> assertThat(properties.getAwsSessionToken(), equalTo("myToken")),
                () -> assertThat(properties.isS3PathStyleAccess(), equalTo(true)),
                () -> assertThat(properties.isUseSsl(), equalTo(false)));
    }
}