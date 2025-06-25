package com.exasol.adapter.document.files;

import static com.exasol.utils.LogHelper.logFine;

import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.RemoteFileFinder;
import com.exasol.adapter.document.files.connection.S3ConnectionProperties;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.iterators.*;

import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

/**
 * File loader for files on S3.
 */
public class S3FileFinder implements RemoteFileFinder {
    private final StringFilter filePattern;
    private final S3ConnectionProperties connectionProperties;
    private final S3Client s3;
    private final S3AsyncClient s3Async;

    private final Logger logger;

    /**
     * Constructs a new instance of {@link S3FileFinder} with the given file pattern, S3 connection properties,
     * and a custom {@link Logger} instance.
     * <p>
     * This constructor is primarily intended for testing purposes, where a mock logger can be injected
     * to verify logging behavior.
     * </p>
     *
     * @param filePattern          the filter used to match file keys
     * @param connectionProperties the connection properties for accessing the S3 bucket
     * @param logger               the logger to use for internal logging
     */
    S3FileFinder(final StringFilter filePattern, final S3ConnectionProperties connectionProperties, Logger logger) {
        this.filePattern = filePattern;
        this.connectionProperties = connectionProperties;
        final S3ClientFactory s3ClientFactory = new S3ClientFactory(connectionProperties);
        this.s3 = s3ClientFactory.buildSyncClient();
        this.s3Async = s3ClientFactory.buildAsyncClient();
        this.logger = logger;
    }

    /**
     * Create a new instance of {@link S3FileFinder}.
     *
     * @param filePattern          files to load
     * @param connectionProperties connection information
     */
    public S3FileFinder(final StringFilter filePattern, final S3ConnectionProperties connectionProperties) {
        this(filePattern, connectionProperties, Logger.getLogger(S3FileFinder.class.getName()));
    }

    /**
     * Loads the list of {@link RemoteFile}s from the S3 storage using the given pattern.
     * <p>
     * The process includes:
     * <ul>
     *   <li>Creating a directory-ignoring matcher based on the file pattern</li>
     *   <li>Obtaining a list of S3 object keys after initial quick filtering</li>
     *   <li>Filtering those keys using the matcher</li>
     *   <li>Transforming each matching object into a {@link RemoteFile}</li>
     * </ul>
     *
     * Logging at {@link Level#FINE} is used throughout the process for tracing and debugging.
     *
     * @return a {@link CloseableIterator} over the matching {@link RemoteFile}s
     */
    @Override
    public CloseableIterator<RemoteFile> loadFiles() {
        logFine(logger, "Starting to load files using S3 file pattern matcher with static prefix: '%s' | s3 bucket: '%s' | awsAccessKeyId: '***%s'.", filePattern.getStaticPrefix(), this.connectionProperties.getS3Bucket(), getAwsAccessKeyIdLast3Digits());

        final com.exasol.adapter.document.files.stringfilter.matcher.Matcher filePatternMatcher =
                this.filePattern.getDirectoryIgnoringMatcher();
        final CloseableIterator<S3ObjectDescription> objectKeys = getQuickFilteredObjectKeys();

        final FilteringIterator<S3ObjectDescription> filteredObjectKeys = new FilteringIterator<>(
                objectKeys,
                s3Object -> {
                    boolean matches = filePatternMatcher.matches(s3Object.getKey());
                    logFine(logger, "Checking if key matches pattern: %s => %b", s3Object.getKey(), matches);
                    return matches;
                });

        return new TransformingIterator<>(filteredObjectKeys, this::getS3Object);
    }

    /**
     * Returns the last 3 characters of the AWS access key ID if it is non-null and has at least 3 characters.
     * <p>
     * If the access key ID is {@code null} or shorter than 3 characters, the string {@code "null"} is returned.
     * This method is typically used for safe logging or debugging without exposing the full key.
     * </p>
     *
     * @return the last 3 characters of the access key ID, or {@code "null"} if not available
     */
    private String getAwsAccessKeyIdLast3Digits() {
        String accessKeyId = this.connectionProperties.getAwsAccessKeyId();
        return (accessKeyId != null && accessKeyId.length() >= 3)
                ? accessKeyId.substring(accessKeyId.length() - 3)
                : "null";
    }

    /**
     * Get a list of object keys. This method only applies the filters that can be applied on S3. So you have to filter
     * the output once again with a more featured matcher.
     *
     * @return partially filtered list of object keys
     */
    CloseableIterator<S3ObjectDescription> getQuickFilteredObjectKeys() {
        final String globFreeKey = this.filePattern.getStaticPrefix();
        final Iterator<ListObjectsV2Response> s3Responses = runS3Request(globFreeKey).iterator();
        final Iterator<ListObjectsV2Response> s3ResponsesWithRetry = new RequestRateRetryIterator(s3Responses);
        final CloseableIterator<S3Object> s3Objects = new FlatMapIterator<>(
                new CloseableIteratorWrapper<>(s3ResponsesWithRetry),
                page -> new CloseableIteratorWrapper<>(page.contents().iterator()));
        return new TransformingIterator<>(s3Objects,
                s3Object -> new S3ObjectDescription(s3Object.key(), s3Object.size()));
    }

    private ListObjectsV2Iterable runS3Request(final String globFreeKey) {
        return this.s3.listObjectsV2Paginator(
                builder -> builder.bucket(this.connectionProperties.getS3Bucket()).prefix(globFreeKey).build());
    }

    RemoteFile getS3Object(final S3ObjectDescription objectUri) {
        return new RemoteFile(objectUri.getKey(), objectUri.getSize(),
                new S3RemoteFileContent(this.s3, this.s3Async, objectUri, this.connectionProperties.getS3Bucket()));
    }
}
