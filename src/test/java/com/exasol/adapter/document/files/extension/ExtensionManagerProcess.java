package com.exasol.adapter.document.files.extension;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.adapter.document.files.extension.process.*;
import com.google.re2j.Pattern;

public class ExtensionManagerProcess implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(ExtensionManagerProcess.class.getName());
    private static final Duration SERVER_STARTUP_TIMEOUT = Duration.ofSeconds(5);
    private final SimpleProcess process;
    private final int port;

    private ExtensionManagerProcess(final SimpleProcess process, final int port) {
        this.process = process;
        this.port = port;
    }

    public static ExtensionManagerProcess start(final Path extensionManagerBinary, final Path extensionFolder) {
        final int port = findOpenPort();
        LOGGER.info(() -> "Starting extension manager " + extensionManagerBinary + " on port " + port
                + " with extension folder " + extensionFolder + "...");
        final List<String> command = List.of(extensionManagerBinary.toString(), "-pathToExtensionFolder",
                extensionFolder.toString(), "-serverAddress", "localhost:" + port);

        final ServerPortConsumer serverPortConsumer = new ServerPortConsumer();
        final SimpleProcess process = SimpleProcess.start(command,
                new DelegatingStreamConsumer(new LoggingStreamConsumer("server stdout>", Level.FINE)),
                new DelegatingStreamConsumer(new LoggingStreamConsumer("server stderr>", Level.FINE),
                        serverPortConsumer));
        if (!serverPortConsumer.isStartupFinished(SERVER_STARTUP_TIMEOUT)) {
            process.stop();
            throw new IllegalStateException("Extension manager did not log server port after " + SERVER_STARTUP_TIMEOUT
                    + ". Check log output for error messages.");
        }
        return new ExtensionManagerProcess(process, port);
    }

    private static int findOpenPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (final IOException exception) {
            throw new UncheckedIOException("Failed to find an open port", exception);
        }
    }

    @Override
    public void close() {
        LOGGER.fine("Stopping extension manager process");
        this.process.stop();
    }

    public String getServerBasePath() {
        return "http://localhost:" + this.port;
    }

    private static class ServerPortConsumer implements ProcessStreamConsumer {
        private static final Pattern STARTUP_FINISHED = Pattern.compile(".*Starting server on localhost:\\d+.*");
        private final CountDownLatch startupFinishedLatch = new CountDownLatch(1);

        @Override
        public void accept(final String line) {
            if (STARTUP_FINISHED.matches(line)) {
                this.startupFinishedLatch.countDown();
                LOGGER.info(() -> "Found server startup message in line '" + line + "'");
            }
        }

        boolean isStartupFinished(final Duration timeout) {
            return awaitStartupFinished(timeout);
        }

        private boolean awaitStartupFinished(final Duration timeout) {
            try {
                return this.startupFinishedLatch.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (final InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for startup to finish", exception);
            }
        }

        @Override
        public void readFinished() {
            // Ignore
        }

        @Override
        public void readFailed(final IOException exception) {
            // Ignore
        }
    }
}