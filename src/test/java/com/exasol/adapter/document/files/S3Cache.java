package com.exasol.adapter.document.files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Object;

/**
 * This class speeds up write requests to an S3 bucket using an additional cache bucket. If the file exists in the cache
 * bucket this class copies the file from that bucket which is a lot faster than uploading from a test PC (depending on
 * your bandwidth).
 */
public class S3Cache implements S3UploadInterface {
    private static final Logger LOGGER = Logger.getLogger(S3Cache.class.getName());
    private final S3Interface s3Interface;
    private final String testBucket;
    private final String cacheBucket;

    public S3Cache(final S3Client s3, final String testBucket, final String cacheBucket) {
        this(new S3Interface(s3), testBucket, cacheBucket);
    }

    S3Cache(final S3Interface s3Interface, final String testBucket, final String cacheBucket) {
        this.s3Interface = s3Interface;
        this.testBucket = testBucket;
        this.cacheBucket = cacheBucket;
    }

    @Override
    public void uploadFile(final Path file, final String key) {
        final String checksum = localSha512Checksum(file);
        if (isUploadRequired(checksum, getFileSize(file), file.toString())) {
            LOGGER.log(Level.INFO, "Could not find {0} in cache. Uploading to {1}...", file, this.cacheBucket);
            this.s3Interface.uploadFile(this.cacheBucket, checksum, file);
        } else {
            LOGGER.log(Level.INFO, "Using cached version of {0}", file);
        }
        this.s3Interface.copyObject(this.cacheBucket, checksum, this.testBucket, key);
    }

    private long getFileSize(final Path file) {
        try {
            return Files.size(file);
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-VSS3-5")
                    .message("Failed to determine size of {{file}}.", file).toString(), exception);
        }
    }

    private boolean isUploadRequired(final String checksum, final long fileSize, final String fileName) {
        final Optional<S3Object> cachedFile = this.s3Interface.listObjects(this.cacheBucket, checksum).contents()
                .stream().filter(s3object -> s3object.key().equals(checksum)).findAny();
        if (cachedFile.isPresent()) {
            if (cachedFile.get().size() != fileSize) {
                throw new IllegalStateException(ExaError.messageBuilder("F-VSS3-4")
                        .message("Found a different sized version of file {{file name}} in cache with same checksum.",
                                fileName)
                        .message("This can be caused by a very unlikely checksum collision.")
                        .mitigation("Try to empty the cache.").toString());
            }
            return false;
        } else {
            return true;
        }
    }

    private String localSha512Checksum(final Path localPath) {
        try {
            final MessageDigest checksumBuilder = MessageDigest.getInstance("SHA-512");
            try (final InputStream inputStream = Files.newInputStream(localPath);
                    final DigestInputStream checksumBuildingStream = new DigestInputStream(inputStream,
                            checksumBuilder)) {
                final byte[] buffer = new byte[1000];
                while (checksumBuildingStream.read(buffer) != -1) {
                    // nothing to do. Just read to run through the stream.
                }
            }
            return toHex(checksumBuilder.digest());
        } catch (final NoSuchAlgorithmException | IOException exception) {
            throw new IllegalStateException(
                    ExaError.messageBuilder("F-VSS3-3")
                            .message("Failed to calculate checksum of file {{file}}.", localPath).toString(),
                    exception);
        }
    }

    /**
     * Converts a byte array into a hex representation.
     *
     * @param bytes bytes to convert
     * @return hex representation as string
     */
    private String toHex(final byte[] bytes) {
        final StringBuilder result = new StringBuilder();
        for (final byte eachByte : bytes) {
            result.append(String.format("%02x", eachByte));
        }
        return result.toString();
    }

    /**
     * This class is a wrapper around the s3 API to facilitate testing. (The AWS API uses final classes, so we can't spy
     * on it with mockito.)
     */
    static class S3Interface {
        private final S3Client s3;

        S3Interface(final S3Client s3) {
            this.s3 = s3;
        }

        void uploadFile(final String bucketName, final String key, final Path file) {
            this.s3.putObject(request -> request.bucket(bucketName).key(key), file);
        }

        void copyObject(final String sourceBucket, final String sourceKey, final String destinationBucket,
                final String destinationKey) {
            this.s3.copyObject(request -> request.sourceKey(sourceKey).destinationKey(destinationKey)
                    .sourceBucket(sourceBucket).destinationBucket(destinationBucket));
        }

        ListObjectsResponse listObjects(final String bucket, final String prefix) {
            return this.s3.listObjects(request -> request.bucket(bucket).prefix(prefix));
        }
    }
}
