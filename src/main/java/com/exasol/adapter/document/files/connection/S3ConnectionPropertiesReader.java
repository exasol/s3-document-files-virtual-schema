package com.exasol.adapter.document.files.connection;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.errorreporting.ExaError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class S3ConnectionPropertiesReader {

    private static final Pattern PROTOCOLS = Pattern.compile("^https?://");
    public static final String AWS_ENDPOINT_OVERRIDE = "awsEndpointOverride";

    private String readAwsEndpointOverride(final ConnectionPropertiesReader reader) {
        final String raw = reader.readString(AWS_ENDPOINT_OVERRIDE).orElse(null);
        if (raw == null) {
            return null;
        }
        final Matcher matcher = PROTOCOLS.matcher(raw);
        if (!matcher.find()) {
            return raw;
        }
        throw new IllegalArgumentException(ExaError
                .messageBuilder("E-S3VS-8")
                .message("Property {{property}} has invalid value {{value}}."
                                + " Value must not contain a protocol.",
                        AWS_ENDPOINT_OVERRIDE, raw)
                .mitigation("Please remove prefix {{prefix}}.", matcher.group(0))
                .toString());
    }

    public S3ConnectionProperties read(final ConnectionPropertiesReader reader) {
        return S3ConnectionProperties.builder()//
                .useSsl(reader.readBooleanWithDefault("useSsl", true))//
                .awsAccessKeyId(reader.readRequiredString("awsAccessKeyId"))
                .awsSecretAccessKey(reader.readRequiredString("awsSecretAccessKey"))
                .awsRegion(reader.readRequiredString("awsRegion"))
                .awsEndpointOverride(readAwsEndpointOverride(reader))
                .awsSessionToken(reader.readString("awsSessionToken").orElse(null))
                .s3PathStyleAccess(reader.readBooleanWithDefault("s3PathStyleAccess", false))
                .s3Bucket(reader.readRequiredString("s3Bucket"))//
                .build();
    }
}
