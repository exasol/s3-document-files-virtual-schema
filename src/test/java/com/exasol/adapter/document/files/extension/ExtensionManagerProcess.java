package com.exasol.adapter.document.files.extension;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.exasol.adapter.document.files.extension.process.*;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;

public class ExtensionManagerProcess implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(ExtensionManagerProcess.class.getName());
    private static final Duration SERVER_START_TIMEOUT = Duration.ofSeconds(10);
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
        final ServerOutputConsumer stdOutConsumer = new ServerOutputConsumer(StreamType.STD_OUT);
        final ServerOutputConsumer stdErrorConsumer = new ServerOutputConsumer(StreamType.STD_ERR);
        final SimpleProcess process = SimpleProcess.start(command, stdOutConsumer, stdErrorConsumer);
        final Optional<Integer> port = stdErrorConsumer.getServerPort();
        if (port.isEmpty()) {
            process.stop();
            throw new IllegalStateException(
                    "Extension manager did not server log port. Check log output for error messages.");
        }
        return new ExtensionManagerProcess(process, port.get());
    }

    @Override
    public void close() {
        LOGGER.info(() -> "Stopping extension manager process");
        this.process.stop();
    }

    public String getServerBasePath() {
        return "http://localhost:" + this.serverPort;
    }

    private static class ServerOutputConsumer implements ProcessStreamConsumer {

        private static final Pattern PORT_LOG_LINE = Pattern.compile(".*Starting server on port (\\d+).*");
        private final CountDownLatch startupFinished = new CountDownLatch(1);
        private Integer serverPort = null;
        private final StreamType type;

        private ServerOutputConsumer(final StreamType type) {
            this.type = type;
        }

        @Override
        public void accept(final String line) {
            LOGGER.fine(() -> "Server " + this.type + "> " + line);
            final Optional<Integer> port = extractPort(line);
            if (port.isPresent()) {
                this.startupFinished.countDown();
                this.serverPort = port.get();
            }
        }

        Optional<Integer> getServerPort() {
            final boolean success = awaitStartupFinished();
            if (!success || (this.serverPort == null)) {
                return Optional.empty();
            }
            return Optional.of(this.serverPort);
        }

        private boolean awaitStartupFinished() {
            try {
                return this.startupFinished.await(SERVER_START_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
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