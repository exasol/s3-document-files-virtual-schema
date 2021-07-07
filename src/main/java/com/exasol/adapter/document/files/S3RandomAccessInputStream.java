package com.exasol.adapter.document.files;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

class S3RandomAccessInputStream extends RandomAccessInputStream {
    private static final Logger LOGGER = Logger.getLogger(S3RandomAccessInputStream.class.getName());
    private final S3Client s3;
    private final S3ObjectDescription s3ObjectToRead;
    long position = 0;

    S3RandomAccessInputStream(final S3Client s3, final S3ObjectDescription s3ObjectToRead) {
        this.s3 = s3;
        this.s3ObjectToRead = s3ObjectToRead;
    }

    @Override
    public void seek(final long position) {
        LOGGER.info("seek");
        this.position = position;
    }

    @Override
    public long getPos() {
        return this.position;
    }

    @Override
    public long getLength() {
        return this.s3ObjectToRead.getSize();
    }

    @Override
    public int read() {
        LOGGER.info("singleRead");
        if (this.position < getLength()) {
            final String range = getRange(this.position, this.position + 1);
            final GetObjectRequest request = GetObjectRequest.builder()//
                    .bucket(this.s3ObjectToRead.getUri().getBucket())//
                    .key(this.s3ObjectToRead.getUri().getKey())//
                    .range(range)//
                    .build();
            final ResponseBytes<GetObjectResponse> result = this.s3.getObject(request, ResponseTransformer.toBytes());
            final byte[] bytes = result.asByteArray();
            this.position++;
            return bytes[0] & 0xFF;
        } else {
            return -1;
        }
    }

    private String getRange(final long start, final long end) {
        return "bytes=" + start + "-" + end;
    }

    @Override
    public int read(final byte[] targetBuffer, final int offset, final int length) {
        LOGGER.log(Level.INFO, "read length: {}", length);
        if (length == 0) {
            return 0;
        }
        final long remainingBytesInFile = getLength() - this.position;
        final int actualReadLength = (int) Math.min(length, remainingBytesInFile);
        if (actualReadLength > 0) {
            final String range = getRange(this.position, this.position + actualReadLength);
            final GetObjectRequest request = GetObjectRequest.builder()//
                    .bucket(this.s3ObjectToRead.getUri().getBucket())//
                    .key(this.s3ObjectToRead.getUri().getKey())//
                    .range(range)//
                    .build();
            final ResponseBytes<GetObjectResponse> result = this.s3.getObject(request, ResponseTransformer.toBytes());
            final byte[] bytes = result.asByteArray();
            this.position += actualReadLength;
            System.arraycopy(bytes, 0, targetBuffer, offset, actualReadLength);
        }
        return actualReadLength == 0 ? -1 : actualReadLength;
    }
}
