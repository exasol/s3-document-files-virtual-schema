package com.exasol.adapter.document.files;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.files.FileLoader;
import com.exasol.adapter.document.documentfetcher.files.InputStreamWithResourceName;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;
import com.exasol.adapter.document.documentfetcher.files.SegmentMatcher;
import com.exasol.adapter.document.files.stringfilter.StringFilter;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

import java.io.InputStream;
import java.net.URI;
import java.util.stream.Stream;

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
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials
                        .create(exaConnectionInformation.getUser(), exaConnectionInformation.getPassword())))//
                .build();
    }

    @Override
    public Stream<InputStreamWithResourceName> loadFiles() {
        final com.exasol.adapter.document.files.stringfilter.matcher.Matcher filePatternMatcher = this.filePattern
                .getDirectoryIgnoringMatcher();
        return getObjectKeysOnlyQuickFiltered().filter(uri -> filePatternMatcher.matches(uri.toString()))
                .filter(uri -> this.segmentMatcher.matches(uri.getKey())).map(this::getS3Object);
    }

    /**
     * Get a list of object keys. This method only applies the filters that can be applied on S3. So you have to filter
     * the output once again with a more featured matcher.
     *
     * @return partially filtered list of object keys
     */
    private Stream<S3Uri> getObjectKeysOnlyQuickFiltered() {
        final String globFreeKey = this.s3Uri.getKey();
        final ListObjectsV2Iterable listObjectsResponse = this.s3
                .listObjectsV2Paginator(builder -> builder.bucket(this.s3Uri.getBucket()).prefix(globFreeKey).build());
        return listObjectsResponse.stream().flatMap(page -> page.contents().stream())
                .map(s3Object -> new S3Uri(this.s3Uri.isUseSsl(), this.s3Uri.getBucket(), this.s3Uri.getRegion(),
                        this.s3Uri.getEndpoint(), s3Object.key()));
    }

    private InputStreamWithResourceName getS3Object(final S3Uri objectUri) {
        final InputStream inputStream = this.s3.getObject(
                GetObjectRequest.builder().bucket(objectUri.getBucket()).key(objectUri.getKey()).build(),
                ResponseTransformer.toInputStream());
        return new InputStreamWithResourceName(inputStream, objectUri.toString());
    }
}
