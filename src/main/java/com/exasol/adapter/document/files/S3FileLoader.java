package com.exasol.adapter.document.files;

import java.net.URI;
import java.util.Iterator;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.FlatMapIterator;
import com.exasol.adapter.document.documentfetcher.files.*;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

import software.amazon.awssdk.auth.credentials.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

/**
 * File loader for files on S3.
 */
public class S3FileLoader implements FileLoader {
    private final StringFilter filePattern;
    private final SegmentMatcher segmentMatcher;
    private final S3Client s3;

    private final S3Uri s3Uri;

    /**
     * Create a new instance of {@link S3FileLoader}.
     *
     * @param filePattern              files to load
     * @param segmentDescription       segment of this loader
     * @param exaConnectionInformation connection information
     */
    public S3FileLoader(final StringFilter filePattern, final SegmentDescription segmentDescription,
            final ExaConnectionInformation exaConnectionInformation) {
        this.filePattern = filePattern;
        this.segmentMatcher = new SegmentMatcher(segmentDescription);
        this.s3Uri = S3Uri.fromString(filePattern.getStaticPrefix());
        final S3ClientBuilder s3ClientBuilder = S3Client.builder();
        if (this.s3Uri.hasEndpointOverride()) {
            s3ClientBuilder.endpointOverride(
                    URI.create((this.s3Uri.isUseSsl() ? "https://" : "http://") + this.s3Uri.getEndpointOverride()));
        }
        this.s3 = s3ClientBuilder.region(Region.of(this.s3Uri.getRegion()))//
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
    public Iterator<LoadedFile> loadFiles() {
        final com.exasol.adapter.document.files.stringfilter.matcher.Matcher filePatternMatcher = this.filePattern
                .getDirectoryIgnoringMatcher();
        final Iterator<S3ObjectDescription> objectKeys = getQuickFilteredObjectKeys();
        final FilteringIterator<S3ObjectDescription> filteredObjectKeys = new FilteringIterator<>(objectKeys,
                s3Object -> filePatternMatcher.matches(s3Object.getUri().toString())
                        && this.segmentMatcher.matches(s3Object.getUri().getKey()));
        return new TransformingIterator<>(filteredObjectKeys, this::getS3Object);
    }

    /**
     * Get a list of object keys. This method only applies the filters that can be applied on S3. So you have to filter
     * the output once again with a more featured matcher.
     *
     * @return partially filtered list of object keys
     */
    private Iterator<S3ObjectDescription> getQuickFilteredObjectKeys() {
        final String globFreeKey = this.s3Uri.getKey();
        final ListObjectsV2Iterable listObjectsResponse = runS3Request(globFreeKey);
        final Iterator<S3Object> s3Objects = new FlatMapIterator<>(listObjectsResponse.iterator(),
                page -> page.contents().iterator());
        return new TransformingIterator<>(s3Objects,
                s3Object -> new S3ObjectDescription(new S3Uri(this.s3Uri.isUseSsl(), this.s3Uri.getBucket(),
                        this.s3Uri.getRegion(), this.s3Uri.getEndpoint(), s3Object.key()), s3Object.size()));
    }

    private ListObjectsV2Iterable runS3Request(final String globFreeKey) {
        return this.s3
                .listObjectsV2Paginator(builder -> builder.bucket(this.s3Uri.getBucket()).prefix(globFreeKey).build());
    }

    private LoadedFile getS3Object(final S3ObjectDescription objectUri) {
        return new S3LoadedFile(this.s3, objectUri);
    }
}
