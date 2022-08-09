package com.exasol.adapter.document.files.s3testsetup;

import java.util.Optional;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;

public interface S3TestSetup extends AutoCloseable {
    S3Client getS3Client();

    S3AsyncClient getS3AsyncClient();

    String getRegion();

    String getUsername();

    String getPassword();

    Optional<String> getMfaToken();

    String getEntrypoint();

    @Override
    void close();

    default void emptyS3Bucket(final String bucketName) {
        final S3Client s3 = getS3Client();
        final ListObjectsV2Iterable pages = s3.listObjectsV2Paginator(request -> request.bucket(bucketName));
        for (final ListObjectsV2Response page : pages) {
            page.contents()
                    .forEach(s3Object -> s3.deleteObject(builder -> builder.bucket(bucketName).key(s3Object.key())));
        }
    }

    default void createBucket(final String bucketName) {
        getS3Client().createBucket(builder -> builder.bucket(bucketName));
    }

    default void deleteBucket(final String bucketName) {
        getS3Client().deleteBucket(builder -> builder.bucket(bucketName));
    }

    default void upload(final String bucketName, final String key, final RequestBody content) {
        getS3Client().putObject(builder -> builder.bucket(bucketName).key(key).build(), content);
    }
}
