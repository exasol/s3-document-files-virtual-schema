package com.exasol.adapter.document.files.connection;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class S3ConnectionProperties {
    @Builder.Default
    private final boolean useSsl = true;
    private final String awsAccessKeyId;
    private final String awsSecretAccessKey;
    private final String awsSessionToken;
    private final String awsRegion;
    private final String awsEndpointOverride;
    private final String s3Bucket;
    @Builder.Default
    private final boolean s3PathStyleAccess = false;

    /**
     * Get if this connection properties contain an AWS session token.
     * 
     * @return {@code true} if it does
     */
    public boolean hasAwsSessionToken() {
        return this.awsSessionToken != null && !this.awsSessionToken.isBlank();
    }

    /**
     * Get if this connection contains an AWS endpoint override.
     * 
     * @return AWS endpoint override
     */
    public boolean hasEndpointOverride() {
        return this.awsEndpointOverride != null && !this.awsEndpointOverride.isBlank()
                && !this.awsEndpointOverride.equals("amazonaws.com");
    }
}
