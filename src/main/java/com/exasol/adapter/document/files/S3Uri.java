package com.exasol.adapter.document.files;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;

import lombok.Builder;
import lombok.Data;

/**
 * This class represents S3 URIs.
 */
@Data
@Builder
public class S3Uri {
    private static final String AWS_ENDPOINT = "amazonaws.com";
    private static final Pattern OLD_URL_PATTERN = Pattern.compile("http(s)?://([^.]++).s3.([^.]++).([^/]++)/(.*+)");
    @Builder.Default
    private final boolean useSsl = true;
    private final String bucket;
    private final String region;
    @Builder.Default
    private final String endpointOverride = null;
    private final String key;
    @Builder.Default
    private final boolean usePathStyle = false;

    public static S3Uri fromString(final String endpointString) {
        if (endpointString.startsWith("http")) {
            return oldFromString(endpointString);
        } else {
            return newFromString(endpointString);
        }
    }

    private static S3Uri newFromString(final String endpointString) {
        final Map<String, String> keyValueInput = parseKeyValueString(endpointString);
        final S3UriBuilder s3UriBuilder = S3Uri.builder();
        for (final Map.Entry<String, String> entry : keyValueInput.entrySet()) {
            switch (entry.getKey()) {
            case "S3_BUCKET":
                s3UriBuilder.bucket(entry.getValue());
                break;
            case "S3_ENDPOINT_OVERRIDE":
                s3UriBuilder.endpointOverride(entry.getValue());
                break;
            case "S3_REGION":
                s3UriBuilder.region(entry.getValue());
                break;
            case "S3_PATH_STYLE_ACCESS":
                s3UriBuilder.usePathStyle(parseBoolean(endpointString, entry.getValue()));
                break;
            case "S3_SSL_ENABLE":
                s3UriBuilder.useSsl(parseBoolean(endpointString, entry.getValue()));
                break;
            default:
                throw getSyntaxException(endpointString, "Unsupported config key '" + entry.getKey() + "'.");
            }
        }
        return builder().build();
    }

    private static boolean parseBoolean(final String endpointString, final String value) {
        switch (value.toLowerCase()) {
        case "true":
            return true;
        case "false":
            return false;
        default:
            throw getSyntaxException(endpointString, "Invalid value '" + value + "'. Please use 'yes' or 'no'.");

        }
    }

    private static Map<String, String> parseKeyValueString(final String endpointString) {
        final String[] parts = endpointString.split(";");
        final Map<String, String> keyValueInput = new HashMap<>();
        for (final String part : parts) {
            final int indexOfSeparator = part.indexOf("=");
            if (indexOfSeparator <= 0) {
                throw getSyntaxException(endpointString, "The parameter group '" + part + "' does not contain a '='.");
            }
            final String key = part.substring(0, indexOfSeparator - 1);
            final String value = part.substring(indexOfSeparator);
            keyValueInput.put(key.toUpperCase(Locale.ROOT), value);
        }
        return keyValueInput;
    }

    private static IllegalArgumentException getSyntaxException(final String endpointString, final String cause) {
        return new IllegalArgumentException(ExaError.messageBuilder("E-S3VS-2")
                .message("Invalid configuration string {{config}}. {{cause|uq}}", endpointString, cause)
                .mitigation(
                        "Please check the syntax at https://github.com/exasol/s3-document-files-virtual-schema/blob/main/doc/user_guide/user_guide.md.")
                .toString());
    }

    private static S3Uri oldFromString(final String url) {
        final Matcher matcher = OLD_URL_PATTERN.matcher(url);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-S3VS-1").message(
                    "The given S3 Bucket string {{S3_URI}} has an invalid format. Expected format: http(s)://BUCKET.s3.REGION.amazonaws.com/KEY or http(s)://BUCKET.s3.REGION.CUSTOM_ENDPOINT/KEY. Note that the address from the CONNECTION and the source are concatenated.")
                    .parameter("S3_URI", url)
                    .mitigation("Change the address in your CONNECTION and the source in your mapping definition.")
                    .toString());
        }
        final boolean useSsl = matcher.group(1) != null;
        final String bucket = matcher.group(2);
        final String region = matcher.group(3);
        final String endpoint = matcher.group(4);
        final String key = matcher.group(5);
        return new S3Uri(useSsl, bucket, region, endpoint, key, false);
    }

    public boolean hasEndpointOverride() {
        return this.endpointOverride != null && !this.endpointOverride.equals(AWS_ENDPOINT);
    }
}
