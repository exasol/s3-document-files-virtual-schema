package com.exasol.adapter.document.files;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.exasol.errorreporting.ExaError;

/**
 * This class represents S3 URIs.
 */
public class S3Uri {
    public static final String AWS_ENDPOINT = "amazonaws.com";
    private static final Pattern URL_PATTERN = Pattern.compile("http(s)?://([^.]++).s3.([^.]++).([^/]++)/(.*+)");
    private final boolean useSsl;
    private final String bucket;
    private final String region;
    private final String endpoint;
    private final String key;

    /**
     * Create a new instance {@link S3Uri}.
     *
     * @param useSsl   {@code true} if the connection uses ssl encryption (https)
     * @param bucket   the name of the bucket
     * @param region   the AWS region
     * @param endpoint the AWS endpoint
     * @param key      the key to fech
     */
    public S3Uri(final boolean useSsl, final String bucket, final String region, final String endpoint,
            final String key) {
        this.useSsl = useSsl;
        this.bucket = bucket;
        this.region = region;
        this.endpoint = endpoint;
        this.key = key;
    }

    /**
     * Read the URI from an S3 resource string.
     * <p>
     * The following formats are supported:
     * <ul>
     * <li>http(s)://BUCKET.s3.REGION.amazonaws.com/KEY</li>
     * <li>http(s)://BUCKET.s3.REGION.CUSTOM_ENDPOINT/KEY</li>
     * </ul>
     * </p>
     *
     * @param url URI to parse.
     * @return built {@link S3Uri}
     */
    public static S3Uri fromString(final String url) {
        final Matcher matcher = URL_PATTERN.matcher(url);
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
        return new S3Uri(useSsl, bucket, region, endpoint, key);
    }

    @Override
    public String toString() {
        return (this.useSsl ? "https://" : "http://") + this.bucket + ".s3." + this.region + "." + this.endpoint + "/"
                + this.key;
    }

    /**
     * Get if the URI uses SSL.
     *
     * @return if the URI uses SSL
     */
    public boolean isUseSsl() {
        return this.useSsl;
    }

    /**
     * Get the AWS region.
     *
     * @return AWS region
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * Get the name of the bucket.
     *
     * @return name of the bucket
     */
    public String getBucket() {
        return this.bucket;
    }

    /**
     * Get if the S3Uri has an endpoint different from the default AWS one.
     *
     * @return true if non-default endpoint
     */
    public boolean hasEndpointOverride() {
        return !this.endpoint.equals(AWS_ENDPOINT);
    }

    /**
     * Get the endpoint override.
     * <p>
     * In case the endpoint is set to the default AWS endpoint {@code null} is returned.
     * </p>
     *
     * @return endpoint override or {@code null}
     */
    public String getEndpointOverride() {
        if (hasEndpointOverride()) {
            return this.endpoint;
        } else {
            return null;
        }
    }

    /**
     * Get the endpoint.
     *
     * @return AWS endpoint
     */
    public String getEndpoint() {
        return this.endpoint;
    }

    /**
     * Get the key.
     *
     * @return key
     */
    public String getKey() {
        return this.key;
    }
}
