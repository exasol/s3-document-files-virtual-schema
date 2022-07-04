package com.exasol.adapter.document.files.extension.process;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class starts a new {@link Thread} that reads from an {@link InputStream} and forwards the input line by line to
 * a given {@link Consumer}.
 */
class AsyncStreamReader {
    private static final Logger LOGGER = Logger.getLogger(AsyncStreamReader.class.getName());
    private final Executor executor;

    /**
     * Creates a new {@link AsyncStreamReader} that starts a new thread for reading the stream.
     */
    public AsyncStreamReader() {
        this(createThreadExecutor());
    }

    AsyncStreamReader(final Executor executor) {
        this.executor = executor;
    }

    private static Executor createThreadExecutor() {
        return runnable -> {
            final Thread thread = new Thread(runnable);
            thread.setName(AsyncStreamReader.class.getSimpleName());
            thread.setUncaughtExceptionHandler(new LoggingExceptionHandler());
            thread.start();
        };
    }

    /**
     * Start a new {@link Thread} that reads from the given {@link InputStream} and forwards the input to a
     * {@link ProcessStreamConsumer}.
     *
     * @param stream   the input stream to read
     * @param consumer the consumer.
     */
    void startCollectingConsumer(final InputStream stream, final ProcessStreamConsumer consumer) {
        this.executor.execute(() -> readStream(stream, consumer));
    }

    private void readStream(final InputStream stream, final ProcessStreamConsumer consumer) {
        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                consumer.accept(line);
            }
            consumer.readFinished();
        } catch (final IOException exception) {
            LOGGER.log(Level.WARNING, "Failed to read input stream", exception);
            consumer.readFailed(exception);
        }
    }

    private static class LoggingExceptionHandler implements UncaughtExceptionHandler {
        @Override
        public void uncaughtException(final Thread thread, final Throwable throwable) {
            LOGGER.log(Level.SEVERE, "Failed to read input stream", throwable);
        }
    }
}