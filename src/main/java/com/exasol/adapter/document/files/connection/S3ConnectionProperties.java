package com.exasol.adapter.document.files.connection;

import java.util.Objects;

/**
 * Properties for a connection to AWS S3.
 */
public final class S3ConnectionProperties {
    private final boolean useSsl;
    private final String awsAccessKeyId;
    private final String awsSecretAccessKey;
    private final String awsSessionToken;
    private final String awsRegion;
    private final String awsEndpointOverride;
    private final String s3Bucket;
    private final boolean s3PathStyleAccess;

    private S3ConnectionProperties(final Builder builder) {
        this.useSsl = builder.useSsl;
        this.awsAccessKeyId = builder.awsAccessKeyId;
        this.awsSecretAccessKey = builder.awsSecretAccessKey;
        this.awsSessionToken = builder.awsSessionToken;
        this.awsRegion = builder.awsRegion;
        this.awsEndpointOverride = builder.awsEndpointOverride;
        this.s3Bucket = builder.s3Bucket;
        this.s3PathStyleAccess = builder.s3PathStyleAccess;
    }

    /**
     * @return {@code true} if SSL should be used
     */
    public boolean isUseSsl() {
        return this.useSsl;
    }

    /**
     * @return AWS AccessKeyId
     */
    public String getAwsAccessKeyId() {
        return this.awsAccessKeyId;
    }

    /**
     * @return AWS SecretAccessKey
     */
    public String getAwsSecretAccessKey() {
        return this.awsSecretAccessKey;
    }

    /**
     * @return AWS SessionToken
     */
    public String getAwsSessionToken() {
        return this.awsSessionToken;
    }

    /**
     * @return AWS region
     */
    public String getAwsRegion() {
        return this.awsRegion;
    }

    /**
     * @return AWS endpoint override
     */
    public String getAwsEndpointOverride() {
        return this.awsEndpointOverride;
    }

    /**
     * @return AWS S3 bucket
     */
    public String getS3Bucket() {
        return this.s3Bucket;
    }

    /**
     * @return {@code true} if S3 path style access should be used
     */
    public boolean isS3PathStyleAccess() {
        return this.s3PathStyleAccess;
    }

    /**
     * Check if this connection properties contain an AWS session token.
     *
     * @return {@code true} if it does
     */
    public boolean hasAwsSessionToken() {
        return (this.awsSessionToken != null) && !this.awsSessionToken.isBlank();
    }

    /**
     * Check if this connection contains an AWS endpoint override.
     *
     * @return AWS endpoint override
     */
    public boolean hasEndpointOverride() {
        return (this.awsEndpointOverride != null) && !this.awsEndpointOverride.isBlank()
                && !this.awsEndpointOverride.equals("amazonaws.com");
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.awsAccessKeyId, this.awsEndpointOverride, this.awsRegion, this.awsSecretAccessKey,
                this.awsSessionToken, this.s3Bucket, this.s3PathStyleAccess, this.useSsl);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final S3ConnectionProperties other = (S3ConnectionProperties) obj;
        return Objects.equals(this.awsAccessKeyId, other.awsAccessKeyId)
                && Objects.equals(this.awsEndpointOverride, other.awsEndpointOverride)
                && Objects.equals(this.awsRegion, other.awsRegion)
                && Objects.equals(this.awsSecretAccessKey, other.awsSecretAccessKey)
                && Objects.equals(this.awsSessionToken, other.awsSessionToken)
                && Objects.equals(this.s3Bucket, other.s3Bucket) && (this.s3PathStyleAccess == other.s3PathStyleAccess)
                && (this.useSsl == other.useSsl);
    }

    /**
     * Creates builder to build {@link S3ConnectionProperties}.
     *
     * @return created builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder to build {@link S3ConnectionProperties}.
     */
    public static final class Builder {
        private boolean useSsl = true;
        private String awsAccessKeyId;
        private String awsSecretAccessKey;
        private String awsSessionToken;
        private String awsRegion;
        private String awsEndpointOverride;
        private String s3Bucket;
        private boolean s3PathStyleAccess = false;

        private Builder() {
        }

        /**
         * Builder method for useSsl parameter.
         *
         * @param useSsl field to set
         * @return builder
         */
        public Builder useSsl(final boolean useSsl) {
            this.useSsl = useSsl;
            return this;
        }

        /**
         * Builder method for awsAccessKeyId parameter.
         *
         * @param awsAccessKeyId field to set
         * @return builder
         */
        public Builder awsAccessKeyId(final String awsAccessKeyId) {
            this.awsAccessKeyId = awsAccessKeyId;
            return this;
        }

        /**
         * Builder method for awsSecretAccessKey parameter.
         *
         * @param awsSecretAccessKey field to set
         * @return builder
         */
        public Builder awsSecretAccessKey(final String awsSecretAccessKey) {
            this.awsSecretAccessKey = awsSecretAccessKey;
            return this;
        }

        /**
         * Builder method for awsSessionToken parameter.
         *
         * @param awsSessionToken field to set
         * @return builder
         */
        public Builder awsSessionToken(final String awsSessionToken) {
            this.awsSessionToken = awsSessionToken;
            return this;
        }

        /**
         * Builder method for awsRegion parameter.
         *
         * @param awsRegion field to set
         * @return builder
         */
        public Builder awsRegion(final String awsRegion) {
            this.awsRegion = awsRegion;
            return this;
        }

        /**
         * Builder method for awsEndpointOverride parameter.
         *
         * @param awsEndpointOverride field to set
         * @return builder
         */
        public Builder awsEndpointOverride(final String awsEndpointOverride) {
            this.awsEndpointOverride = awsEndpointOverride;
            return this;
        }

        /**
         * Builder method for s3Bucket parameter.
         *
         * @param s3Bucket field to set
         * @return builder
         */
        public Builder s3Bucket(final String s3Bucket) {
            this.s3Bucket = s3Bucket;
            return this;
        }

        /**
         * Builder method for s3PathStyleAccess parameter.
         *
         * @param s3PathStyleAccess field to set
         * @return builder
         */
        public Builder s3PathStyleAccess(final boolean s3PathStyleAccess) {
            this.s3PathStyleAccess = s3PathStyleAccess;
            return this;
        }

        /**
         * Builder method of the builder.
         *
         * @return built class
         */
        public S3ConnectionProperties build() {
            return new S3ConnectionProperties(this);
        }
    }
}
