package com.exasol.adapter.document.files;

import java.net.URI;
import java.util.Iterator;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.files.FileLoader;
import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
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
    private final S3Client s3;
    private final S3AsyncClient s3Async;

    private final S3Uri s3Uri;

    /**
     * Create a new instance of {@link S3FileLoader}.
     *
     * @param filePattern              files to load
     * @param exaConnectionInformation connection information
     */
    public S3FileLoader(final StringFilter filePattern, final ExaConnectionInformation exaConnectionInformation) {
        this.filePattern = filePattern;
        this.s3Uri = S3Uri.fromString(filePattern.getStaticPrefix());
        final S3ClientBuilder s3ClientBuilder = S3Client.builder();
        final S3AsyncClientBuilder s3AsyncClientBuilder = S3AsyncClient.builder();
        if (this.s3Uri.hasEndpointOverride()) {
            final URI endpointOverride = URI
                    .create((this.s3Uri.isUseSsl() ? "https://" : "http://") + this.s3Uri.getEndpointOverride());
            s3ClientBuilder.endpointOverride(endpointOverride);
            s3AsyncClientBuilder.endpointOverride(endpointOverride);
        }
        this.s3 = s3ClientBuilder.region(Region.of(this.s3Uri.getRegion()))//
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials(exaConnectionInformation)))//
                .build();
        this.s3Async = s3AsyncClientBuilder.region(Region.of(this.s3Uri.getRegion()))//
                .credentialsProvider(StaticCredentialsProvider.create(getCredentials(exaConnectionInformation)))//
                .build();
    }

    private AwsCredentials getCredentials(final ExaConnectionInformation exaConnectionInformation) {
        if (exaConnectionInformation.getPassword().contains("##TOKEN##")) {
            final String[] parts = exaConnectionInformation.getPassword().split("##TOKEN##");
            return AwsSessionCredentials.create(exaConnectionInformation.getUser(), parts[0], parts[1]);
        } else {
            return AwsBasicCredentials.create(exaConnectionInformation.getUser(),
                    exaConnectionInformation.getPassword());
        }
    }

    @Override
    public CloseableIterator<RemoteFile> loadFiles() {
        final com.exasol.adapter.document.files.stringfilter.matcher.Matcher filePatternMatcher = this.filePattern
                .getDirectoryIgnoringMatcher();
        final CloseableIterator<S3ObjectDescription> objectKeys = getQuickFilteredObjectKeys();
        final FilteringIterator<S3ObjectDescription> filteredObjectKeys = new FilteringIterator<>(objectKeys,
                s3Object -> filePatternMatcher.matches(s3Object.getUri().toString()));
        return new TransformingIterator<>(filteredObjectKeys, this::getS3Object);
    }

    /**
     * Get a list of object keys. This method only applies the filters that can be applied on S3. So you have to filter
     * the output once again with a more featured matcher.
     *
     * @return partially filtered list of object keys
     */
    private CloseableIterator<S3ObjectDescription> getQuickFilteredObjectKeys() {
        final String globFreeKey = this.s3Uri.getKey();
        final Iterator<ListObjectsV2Response> s3Responses = runS3Request(globFreeKey).iterator();
        final Iterator<ListObjectsV2Response> s3ResponsesWithRetry = new RequestRateRetryIterator(s3Responses);
        final CloseableIterator<S3Object> s3Objects = new FlatMapIterator<>(
                new CloseableIteratorWrapper<>(s3ResponsesWithRetry),
                page -> new CloseableIteratorWrapper<>(page.contents().iterator()));
        return new TransformingIterator<>(s3Objects,
                s3Object -> new S3ObjectDescription(new S3Uri(this.s3Uri.isUseSsl(), this.s3Uri.getBucket(),
                        this.s3Uri.getRegion(), this.s3Uri.getEndpoint(), s3Object.key()), s3Object.size()));
    }

    private ListObjectsV2Iterable runS3Request(final String globFreeKey) {
        return this.s3
                .listObjectsV2Paginator(builder -> builder.bucket(this.s3Uri.getBucket()).prefix(globFreeKey).build());
    }

    private RemoteFile getS3Object(final S3ObjectDescription objectUri) {
        return new RemoteFile(objectUri.getUri().toString(), objectUri.getSize(),
                new S3RemoteFileContent(this.s3, this.s3Async, objectUri));
    }
}
