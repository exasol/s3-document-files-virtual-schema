package com.exasol.adapter.document.files;

import java.io.InputStream;
import java.util.concurrent.*;

import com.exasol.adapter.document.documentfetcher.files.RemoteFileContent;
import com.exasol.adapter.document.documentfetcher.files.TooManyRequestsException;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStream;
import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.RandomAccessInputStreamCache;

import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

class S3RemoteFileContent implements RemoteFileContent {
    /** Exception message used by S3 if there were too many API requests. */
    static final String REDUCE_REQUEST_RATE_MESSAGE = "Please reduce your request rate.";
    private static final int SIZE_1_MB = 1000000;
    private final S3Client s3;
    private final S3AsyncClient s3AsyncClient;
    private final S3ObjectDescription s3ObjectToRead;

    /**
     * Create a new instance of {@link S3RemoteFileContent}.
     * 
     * @param s3             s3 client
     * @param s3ObjectToRead s3 uri
     */
    public S3RemoteFileContent(final S3Client s3, final S3AsyncClient s3AsyncClient,
            final S3ObjectDescription s3ObjectToRead) {
        this.s3 = s3;
        this.s3AsyncClient = s3AsyncClient;
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

    @Override
    public Future<byte[]> loadAsync() {
        final Future<ResponseBytes<GetObjectResponse>> responseFuture = this.s3AsyncClient
                .getObject(
                        request -> request.bucket(this.s3ObjectToRead.getUri().getBucket())
                                .key(this.s3ObjectToRead.getUri().getKey()).build(),
                        AsyncResponseTransformer.toBytes());
        return new ContentFuture(responseFuture);
    }

    private static class ContentFuture implements Future<byte[]> {
        private final Future<ResponseBytes<GetObjectResponse>> responseFuture;

        private ContentFuture(final Future<ResponseBytes<GetObjectResponse>> responseFuture) {
            this.responseFuture = responseFuture;
        }

        @Override
        public boolean cancel(final boolean mayInterruptIfRunning) {
            return this.responseFuture.cancel(mayInterruptIfRunning);
        }

        @Override
        public boolean isCancelled() {
            return this.responseFuture.isCancelled();
        }

        @Override
        public boolean isDone() {
            return this.responseFuture.isDone();
        }

        @Override
        public byte[] get() throws InterruptedException, ExecutionException {
            try {
                return this.responseFuture.get().asByteArray();
            } catch (final ExecutionException exception) {
                throw wrapTooManyRequestsException(exception);
            }
        }

        @Override
        public byte[] get(final long timeout, final TimeUnit unit)
                throws InterruptedException, ExecutionException, TimeoutException {
            try {
                return this.responseFuture.get(timeout, unit).asByteArray();
            } catch (final ExecutionException exception) {
                throw wrapTooManyRequestsException(exception);
            }
        }

        private ExecutionException wrapTooManyRequestsException(final ExecutionException exception) {
            if (exception.getCause().getMessage().contains(REDUCE_REQUEST_RATE_MESSAGE)) {
                throw new TooManyRequestsException();
            } else {
                return exception;
            }
        }
    }
}
