package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.files.S3RemoteFileContent.REDUCE_REQUEST_RATE_MESSAGE;

import java.util.Iterator;

import com.exasol.errorreporting.ExaError;

import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * This iterator wraps an AWS S3 iterator and retries the {@code next()} method if it fails with a "Please reduce your
 * request rate".
 */
class RequestRateRetryIterator implements Iterator<ListObjectsV2Response> {
    private static final int MAX_RETRIES = 100;
    private final Iterator<ListObjectsV2Response> source;

    /**
     * Create a new instance of {@link RequestRateRetryIterator}.
     *
     * @param source aws s3 iterator
     */
    RequestRateRetryIterator(final Iterator<ListObjectsV2Response> source) {
        this.source = source;
    }

    @Override
    public boolean hasNext() {
        return this.source.hasNext();
    }

    @Override
    public ListObjectsV2Response next() {
        int retryCounter = 0;
        while (true) {
            try {
                return this.source.next();
            } catch (final S3Exception exception) {
                if (exception.getMessage().contains(REDUCE_REQUEST_RATE_MESSAGE)) {
                    waitABit();
                    if (retryCounter >= MAX_RETRIES) {
                        throw new IllegalStateException(ExaError.messageBuilder("E-VSS3-7")
                                .message("Failed to get next response from AWS after {{num retries}} retries.",
                                        retryCounter)
                                .toString());
                    }
                    retryCounter++;
                } else {
                    throw exception;
                }
            }
        }
    }

    private void waitABit() {
        try {
            Thread.sleep(10);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-VSS3-6").message("Interrupted while waiting.").toString(), exception);
        }
    }
}
