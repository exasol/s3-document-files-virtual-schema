package com.exasol.adapter.document.files;

import com.exasol.adapter.document.files.s3testsetup.S3ContainerSetup;

public class RandomAccessInputStreamBuilder {

    private final S3ContainerSetup setup;
    private String key;
    private int dataSize;
    private String bucket;

    public RandomAccessInputStreamBuilder(final S3ContainerSetup setup) {
        this.setup = setup;
    }

    public RandomAccessInputStreamBuilder key(final String key) {
        this.key = key;
        return this;
    }

    public RandomAccessInputStreamBuilder dataSize(final int dataSize) {
        this.dataSize = dataSize;
        return this;
    }

    public RandomAccessInputStreamBuilder bucket(final String bucket) {
        this.bucket = bucket;
        return this;
    }

    public S3RandomAccessInputStream build() {
        return new S3RandomAccessInputStream( //
                this.setup.getS3Client(), //
                new S3ObjectDescription(this.key, this.dataSize), //
                this.bucket);
    }
}
