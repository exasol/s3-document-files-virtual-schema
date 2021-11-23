package com.exasol.adapter.document.files;

import java.io.InputStream;

import com.exasol.adapter.document.documentfetcher.files.RemoteFile;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStreamCache;

import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

class S3RemoteFile extends RemoteFile {
    private static final int SIZE_1_MB = 1000000;
    private final S3Client s3;
    private final S3ObjectDescription s3ObjectToRead;

    /**
     * Create a new instance of {@link S3RemoteFile}.
     * 
     * @param s3             s3 client
     * @param s3ObjectToRead s3 uri
     */
    public S3RemoteFile(final S3Client s3, final S3ObjectDescription s3ObjectToRead) {
        super(s3ObjectToRead.getUri().toString());
        this.s3 = s3;
        this.s3ObjectToRead = s3ObjectToRead;
    }

    @Override
    public InputStream getInputStream() {
        return this.s3.getObject(GetObjectRequest.builder().bucket(this.s3ObjectToRead.getUri().getBucket())
                .key(this.s3ObjectToRead.getUri().getKey()).build(), ResponseTransformer.toInputStream());
    }

    @Override
    public RandomAccessInputStream getRandomAccessInputStream() {
        return new RandomAccessInputStreamCache(new S3RandomAccessInputStream(this.s3, this.s3ObjectToRead), SIZE_1_MB);
    }
}
