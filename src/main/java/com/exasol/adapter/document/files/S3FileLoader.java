package com.exasol.adapter.document.files;

import java.io.InputStream;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.files.*;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

/**
 * File loader for files on S3.
 */
public class S3FileLoader implements FileLoader {
    public static final Pattern NON_GLOB_KEY_PATTERN = Pattern.compile("([^\\*\\?]*).*");
    private final SegmentMatcher segmentMatcher;
    private final S3Client s3;
    private final String uriString;
    private final S3Uri s3Uri;

    /**
     * Create a new instance of {@link S3FileLoader}.
     * 
     * @param filePattern              files to load
     * @param segmentDescription       segment of this loader
     * @param exaConnectionInformation connection information
     */
    public S3FileLoader(final String filePattern, final SegmentDescription segmentDescription,
            final ExaConnectionInformation exaConnectionInformation) {
        this.segmentMatcher = new SegmentMatcher(segmentDescription);
        this.uriString = URI.create(exaConnectionInformation.getAddress()).resolve(filePattern).toString();
        this.s3Uri = S3Uri.fromString(this.uriString);
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
        final Pattern globPattern = GlobToRegexConverter.convert(this.s3Uri.getKey());
        return getObjectKeysOnlyQuickFiltered().filter(key -> globPattern.matcher(key).matches())
                .filter(this.segmentMatcher::matches).map(this::getS3Object);
    }

    /**
     * Get a list of object keys. This method only applies the filters that can be applied on S3. So you have to filter
     * the output once again with a more featured matcher.
     * 
     * @return partially filtered list of object keys
     */
    private Stream<String> getObjectKeysOnlyQuickFiltered() {
        final Matcher matcher = NON_GLOB_KEY_PATTERN.matcher(this.s3Uri.getKey());
        if (matcher.matches()) {
            final String globFreeKey = matcher.group(1);
            final ListObjectsV2Iterable listObjectsResponse = this.s3.listObjectsV2Paginator(
                    builder -> builder.bucket(this.s3Uri.getBucket()).prefix(globFreeKey).build());
            return listObjectsResponse.stream().flatMap(page -> page.contents().stream()).map(S3Object::key);
        } else { // no glob is used
            return Stream.of(this.s3Uri.getKey());
        }
    }

    private InputStreamWithResourceName getS3Object(final String key) {
        final InputStream inputStream = this.s3.getObject(
                GetObjectRequest.builder().bucket(this.s3Uri.getBucket()).key(key).build(),
                ResponseTransformer.toInputStream());
        return new InputStreamWithResourceName(inputStream, key);
    }

    @Override
    public String getFilePattern() {
        return this.uriString;
    }
}
