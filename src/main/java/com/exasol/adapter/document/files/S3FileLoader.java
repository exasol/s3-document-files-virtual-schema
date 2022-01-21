package com.exasol.adapter.document.files;

import java.net.URI;
import java.util.Iterator;

import com.exasol.adapter.document.documentfetcher.files.FileLoader;
import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.files.connection.S3ConnectionProperties;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.iterators.*;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.*;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

/**
 * File loader for files on S3.
 */
public class S3FileLoader implements FileLoader {
    private final StringFilter filePattern;
    private final S3ConnectionProperties connectionProperties;
    private final S3Client s3;
    private final S3AsyncClient s3Async;

    /**
     * Create a new instance of {@link S3FileLoader}.
     *
     * @param filePattern          files to load
     * @param connectionProperties connection information
     */
    public S3FileLoader(final StringFilter filePattern, final S3ConnectionProperties connectionProperties) {
        this.filePattern = filePattern;
        this.connectionProperties = connectionProperties;
        final S3ClientBuilder s3ClientBuilder = S3Client.builder();
        final S3AsyncClientBuilder s3AsyncClientBuilder = S3AsyncClient.builder();
        if (connectionProperties.isS3PathStyleAccess()) {
            final S3Configuration s3Configuration = S3Configuration.builder().pathStyleAccessEnabled(true).build();
            s3ClientBuilder.serviceConfiguration(s3Configuration);
            s3AsyncClientBuilder.serviceConfiguration(s3Configuration);
        }
        if (connectionProperties.hasEndpointOverride()) {
            final URI endpointOverride = URI.create((connectionProperties.isUseSsl() ? "https://" : "http://")
                    + connectionProperties.getAwsEndpointOverride());
            s3ClientBuilder.endpointOverride(endpointOverride);
            s3AsyncClientBuilder.endpointOverride(endpointOverride);
        }
        this.s3 = s3ClientBuilder.region(Region.of(connectionProperties.getAwsRegion()))//
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials(connectionProperties)))//
                .build();
        this.s3Async = s3AsyncClientBuilder.region(Region.of(connectionProperties.getAwsRegion()))//
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials(connectionProperties)))//
                .build();
    }

    private AwsCredentials getCredentials(final S3ConnectionProperties properties) {
        if (properties.hasAwsSessionToken()) {
            return AwsSessionCredentials.create(properties.getAwsAccessKeyId(), properties.getAwsSecretAccessKey(),
                    properties.getAwsSessionToken());
        } else {
            return AwsBasicCredentials.create(properties.getAwsAccessKeyId(), properties.getAwsSecretAccessKey());
        }
    }

    @Override
    public CloseableIterator<RemoteFile> loadFiles() {
        final com.exasol.adapter.document.files.stringfilter.matcher.Matcher filePatternMatcher = this.filePattern
                .getDirectoryIgnoringMatcher();
        final CloseableIterator<S3ObjectDescription> objectKeys = getQuickFilteredObjectKeys();
        final FilteringIterator<S3ObjectDescription> filteredObjectKeys = new FilteringIterator<>(objectKeys,
                s3Object -> filePatternMatcher.matches(s3Object.getKey()));
        return new TransformingIterator<>(filteredObjectKeys, this::getS3Object);
    }

    /**
     * Get a list of object keys. This method only applies the filters that can be applied on S3. So you have to filter
     * the output once again with a more featured matcher.
     *
     * @return partially filtered list of object keys
     */
    private CloseableIterator<S3ObjectDescription> getQuickFilteredObjectKeys() {
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

    private RemoteFile getS3Object(final S3ObjectDescription objectUri) {
        return new RemoteFile(objectUri.getKey(), objectUri.getSize(),
                new S3RemoteFileContent(this.s3, this.s3Async, objectUri, this.connectionProperties.getS3Bucket()));
    }
}
