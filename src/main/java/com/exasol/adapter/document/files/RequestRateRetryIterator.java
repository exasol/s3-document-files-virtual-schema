package com.exasol.adapter.document.files;

import java.util.Iterator;

import com.exasol.errorreporting.ExaError;

import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Exception;

/**
 * This iterators wraps an AWS S3 iterator and retries the next() method if it fails with a "Please reduce your request
 * rate".
 */
class RequestRateRetryIterator implements Iterator<ListObjectsV2Response> {
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
        while (true) {
            try {
                return this.source.next();
            } catch (final S3Exception exception) {
                if (exception.getMessage().contains("Please reduce your request rate.")) {
                    waitABit();
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
                    ExaError.messageBuilder("E-S3VS-6").message("Interrupted while waiting.").toString(), exception);
        }
    }
}
