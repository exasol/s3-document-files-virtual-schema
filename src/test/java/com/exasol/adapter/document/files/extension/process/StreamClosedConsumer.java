package com.exasol.adapter.document.files.extension.process;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class StreamClosedConsumer implements ProcessStreamConsumer {

    private final CountDownLatch latch = new CountDownLatch(1);

    void waitUntilStreamClosed(Duration timeout) {
        if (!await(timeout)) {
            throw new IllegalStateException("Stream was not closed within timeout of " + timeout);
        }
    }

    private boolean await(Duration timeout) {
        try {
            return latch.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Interrupted while waiting for stream to be closed");
        }
    }

    @Override
    public void accept(final String line) {
        // ignore
    }

    @Override
    public void readFinished() {
        latch.countDown();
    }

    @Override
    public void readFailed(final IOException ioException) {
        latch.countDown();
    }
}
