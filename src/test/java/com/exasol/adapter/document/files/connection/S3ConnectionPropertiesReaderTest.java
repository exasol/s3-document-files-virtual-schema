package com.exasol.adapter.document.files.connection;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    void testMissingRequired() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> runReader("{\"awsAccessKeyId\": \"myKey\", \"awsSecretAccessKey\": \"mySecretAccessKey\", "
                        + "\"awsRegion\": \"eu-central-1\" }"));
        assertThat(exception.getMessage(), startsWith("E-VSD-93: Invalid connection."
                + " The connection definition does not specify the required property 's3Bucket'."
                + " Please check the user-guide at:"));
    }

    @Test
    void testAwsEndpointOverrideWithProtocol() {
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> runReader("{\"awsAccessKeyId\": \"myKey\", "
                        + "\"awsSecretAccessKey\": \"mySecretAccessKey\", "
                        + "\"awsRegion\": \"eu-central-1\", "
                        + "\"awsEndpointOverride\": \"https://aws.com\" }"));
        assertThat(exception.getMessage(), startsWith("E-S3VS-8: "
                + "Property 'awsEndpointOverride' has invalid value 'https://aws.com'."
                + " Value must not contain a protocol."
                + " Please remove prefix 'https://'."));
    }

    private S3ConnectionProperties runReader(final String json) {
        return new S3ConnectionPropertiesReader().read(new ConnectionPropertiesReader(json, ""));
    }
}