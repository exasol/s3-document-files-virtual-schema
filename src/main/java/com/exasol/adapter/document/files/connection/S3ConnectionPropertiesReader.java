package com.exasol.adapter.document.files.connection;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.errorreporting.ExaError;

public class S3ConnectionPropertiesReader {

    private static final Pattern PROTOCOLS = Pattern.compile("^https?://");
    public static final String AWS_ENDPOINT_OVERRIDE = "awsEndpointOverride";

    private Optional<String> readAwsEndpointOverride(final ConnectionPropertiesReader reader) {
        return reader.readString(AWS_ENDPOINT_OVERRIDE).map(this::ensureNoProtocol);
    }

    private String ensureNoProtocol(final String input) {
        final Matcher matcher = PROTOCOLS.matcher(input);
        if (matcher.find()) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSS3-8")
                    .message("Property {{property}} has invalid value {{value}}."
                            + " Value must not contain a protocol.", AWS_ENDPOINT_OVERRIDE, input)
                    .mitigation("Please remove prefix {{prefix}}.", matcher.group(0)).toString());
        } else {
            return input;
        }
    }

    public S3ConnectionProperties read(final ConnectionPropertiesReader reader) {
        return S3ConnectionProperties.builder()//
                .useSsl(reader.readBooleanWithDefault("useSsl", true))//
                .awsAccessKeyId(reader.readRequiredString("awsAccessKeyId"))
                .awsSecretAccessKey(reader.readRequiredString("awsSecretAccessKey"))
                .awsRegion(reader.readRequiredString("awsRegion")) //
                .awsEndpointOverride(readAwsEndpointOverride(reader).orElse(null))
                .awsSessionToken(reader.readString("awsSessionToken").orElse(null))
                .s3PathStyleAccess(reader.readBooleanWithDefault("s3PathStyleAccess", false))
                .s3Bucket(reader.readRequiredString("s3Bucket"))//
                .build();
    }
}
