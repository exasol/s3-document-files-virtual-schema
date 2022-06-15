package com.exasol.adapter.document.files.extension;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.errorreporting.ExaError;

/**
 * This is a convenient wrapper for {@link ProcessBuilder} and {@link Process} that simplifies starting a process,
 * waiting for it to finish and getting its stdout.
 */
public class ServerProcess {
    private static final Logger LOGGER = Logger.getLogger(ServerProcess.class.getName());

    private final Process process;
    private final List<String> command;
    private final Instant startTime;

    private ServerProcess(final Process process, final List<String> command, final Instant startTime) {
        this.process = process;
        this.command = command;
        this.startTime = startTime;
    }

    /**
     * Starts a new process using the working directory of the current Java process.
     *
     * @param command the command to execute
     * @return a new {@link ServerProcess} you can use to wait for the process to finish and retrieve its output
     */
    public static ServerProcess start(final List<String> command, final StreamConsumer outputStreamConsumer,
            final StreamConsumer errorStreamConsumer) {
        return start(null, command, outputStreamConsumer, errorStreamConsumer);
    }

    /**
     * Starts a new process.
     *
     * @param workingDirectory the directory in which to start the process. Use the working directory of the current
     *                         Java process if {@code null}.
     * @param command          the command to execute
     * @return a new {@link ServerProcess} you can use to wait for the process to finish and retrieve its output
     */
    public static ServerProcess start(final Path workingDirectory, final List<String> command,
            final StreamConsumer outputStreamConsumer, final StreamConsumer errorStreamConsumer) {
        LOGGER.fine(() -> "Executing command '" + formatCommand(command) + "' in working dir " + workingDirectory);
        try {
            final Process process = new ProcessBuilder(command)
                    .directory(workingDirectory == null ? null : workingDirectory.toFile()) //
                    .redirectErrorStream(false) //
                    .start();
            final Instant startTime = Instant.now();
            new AsyncStreamReader().startCollectingConsumer(process.getInputStream(), outputStreamConsumer);
            new AsyncStreamReader().startCollectingConsumer(process.getErrorStream(), errorStreamConsumer);
            return new ServerProcess(process, command, startTime);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("E-PK-CORE-125")
                    .message("Error executing command {{command}}.", String.join(" ", command))
                    .mitigation("Verify that the {{executable name}} executable is on the PATH.", command.get(0))
                    .toString(), exception);
        }
    }

    /**
     * Wait for the process to finish.
     *
     * @param executionTimeout the maximum time to wait until the process finishes
     * @throws IllegalStateException if the process did not finish within the given timeout
     */
    public void waitUntilFinished(final Duration executionTimeout) {
        waitForExecutionFinished(executionTimeout);
        final Duration duration = Duration.between(this.startTime, Instant.now());
        final int exitCode = this.process.exitValue();
        if (exitCode != 0) {
            throw new IllegalStateException(
                    "Failed to run command " + formatCommand() + ", exit code " + exitCode + " after " + duration);
        }
        LOGGER.fine(() -> "Command '" + formatCommand() + "' finished successfully after " + duration);
    }

    public boolean isAlive() {
        return this.process.isAlive();
    }

    public void stop() {
        this.process.destroy();
    }

    private void waitForExecutionFinished(final Duration executionTimeout) {
        try {
            if (!this.process.waitFor(executionTimeout.toMillis(), TimeUnit.MILLISECONDS)) {
                throw new IllegalStateException(
                        "Timeout while waiting " + executionTimeout + " for command " + formatCommand());
            }
        } catch (final InterruptedException exception) {
            throw handleInterruptedException(exception);
        }
    }

    private RuntimeException handleInterruptedException(final InterruptedException exception) {
        Thread.currentThread().interrupt();
        return new IllegalStateException("Interrupted while waiting for command " + formatCommand(), exception);
    }

    private static String formatCommand(final List<String> command) {
        return String.join(" ", command);
    }

    private String formatCommand() {
        return formatCommand(this.command);
    }

    /**
     * Callback interface used by {@link AsyncStreamReader}.
     */
    public interface StreamConsumer {
        /**
         * Called when a new line was read from the input stream.
         *
         * @param line the read line
         */
        void accept(String line);

        /**
         * Called when reading the input stream finished.
         */
        void readFinished();

        /**
         * Called when reading the input stream failed.
         *
         * @param ioException the exception.
         */
        void readFailed(IOException ioException);
    }

    /**
     * This class starts a new {@link Thread} that reads from an {@link InputStream} and forwards the input line by line
     * to a given {@link Consumer}.
     */
    private static class AsyncStreamReader {
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
                thread.setUncaughtExceptionHandler(new LoggingExceptionHandler());
                thread.start();
            };
        }

        /**
         * Start a new {@link Thread} that reads from the given {@link InputStream} and forwards the input to a
         * {@link StreamConsumer}.
         *
         * @param stream   the input stream to read
         * @param consumer the consumer.
         */
        private void startCollectingConsumer(final InputStream stream, final StreamConsumer consumer) {
            this.executor.execute(() -> readStream(stream, consumer));
        }

        private void readStream(final InputStream stream, final StreamConsumer consumer) {
            try (final BufferedReader reader = new BufferedReader(
                    new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    consumer.accept(line);
                }
            } catch (final IOException exception) {
                LOGGER.log(Level.WARNING, "Failed to read input stream", exception);
            }
        }

        private static class LoggingExceptionHandler implements UncaughtExceptionHandler {
            @Override
            public void uncaughtException(final Thread thread, final Throwable throwable) {
                LOGGER.log(Level.SEVERE, "Failed to read input stream", throwable);
            }
        }
    }
}
