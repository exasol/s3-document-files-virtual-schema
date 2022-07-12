package com.exasol.adapter.document.files.extension;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.adapter.document.files.extension.process.*;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;

public class ExtensionManagerProcess implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(ExtensionManagerProcess.class.getName());
    private static final Duration SERVER_STARTUP_TIMEOUT = Duration.ofSeconds(5);
    private final SimpleProcess process;
    private final int serverPort;

    private ExtensionManagerProcess(final SimpleProcess process, final int serverPort) {
        this.process = process;
        this.serverPort = serverPort;
    }

    public static ExtensionManagerProcess start(final Path extensionManagerBinary, final Path extensionFolder) {
        LOGGER.info(() -> "Starting extension manager " + extensionManagerBinary + " with extension folder "
                + extensionFolder + "...");
        final List<String> command = List.of(extensionManagerBinary.toString(), "-pathToExtensionFolder",
                extensionFolder.toString());

        final ServerPortConsumer serverPortConsumer = new ServerPortConsumer();
        final SimpleProcess process = SimpleProcess.start(command,
                new DelegatingStreamConsumer(new LoggingStreamConsumer("server stdout>", Level.FINE)),
                new DelegatingStreamConsumer(new LoggingStreamConsumer("server stderr>", Level.FINE),
                        serverPortConsumer));
        final Optional<Integer> port = serverPortConsumer.getServerPort(SERVER_STARTUP_TIMEOUT);
        if (port.isEmpty()) {
            process.stop();
            throw new IllegalStateException("Extension manager did not log server port after " + SERVER_STARTUP_TIMEOUT
                    + ". Check log output for error messages.");
        }
        return new ExtensionManagerProcess(process, port.get());
    }

    @Override
    public void close() {
        LOGGER.fine("Stopping extension manager process");
        this.process.stop();
    }

    public String getServerBasePath() {
        return "http://localhost:" + this.serverPort;
    }

    private static class ServerPortConsumer implements ProcessStreamConsumer {
        private static final Pattern PORT_LOG_LINE = Pattern.compile(".*Starting server on port (\\d+).*");
        private final CountDownLatch startupFinished = new CountDownLatch(1);
        private final AtomicInteger serverPort = new AtomicInteger(-1);

        @Override
        public void accept(final String line) {
            final Optional<Integer> port = extractPort(line);
            if (port.isPresent()) {
                this.serverPort.set(port.get());
                this.startupFinished.countDown();
                LOGGER.info(() -> "Found server port " + this.serverPort + " in line '" + line + "', countdown latch = "
                        + this.startupFinished.getCount());
            }
        }

        Optional<Integer> getServerPort(final Duration timeout) {
            final boolean success = awaitStartupFinished(timeout);
            if (this.serverPort.get() < 0) {
                LOGGER.warning("Server port not found in log output after " + timeout + ". Success flag: " + success);
                return Optional.empty();
            }
            return Optional.of(this.serverPort.get());
        }

        private boolean awaitStartupFinished(final Duration timeout) {
            try {
                return this.startupFinished.await(timeout.toMillis(), TimeUnit.MILLISECONDS);
            } catch (final InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for startup to finish", exception);
            }
        }

        private Optional<Integer> extractPort(final String line) {
            final Matcher matcher = PORT_LOG_LINE.matcher(line);
            if (matcher.matches()) {
                return Optional.of(Integer.parseInt(matcher.group(1)));
            }
            return Optional.empty();
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