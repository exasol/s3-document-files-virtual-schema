package com.exasol.adapter.document.files.extension;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.exasol.adapter.document.files.extension.ServerProcess.StreamConsumer;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;

public class ExtensionManagerProcess implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(ExtensionManagerProcess.class.getName());
    private static final Duration SERVER_START_TIMEOUT = Duration.ofSeconds(10);
    private final ServerProcess process;
    private final int serverPort;

    private ExtensionManagerProcess(final ServerProcess process, final int serverPort) {
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
        final ServerProcess process = ServerProcess.start(command, stdOutConsumer, stdErrorConsumer);
        final int port = stdErrorConsumer.waitUntilStartupFinished();
        return new ExtensionManagerProcess(process, port);
    }

    @Override
    public void close() {
        LOGGER.info(() -> "Stopping extension manager process");
        this.process.stop();
    }

    public String getServerBasePath() {
        return "http://localhost:" + this.serverPort;
    }

    private enum StreamType {
        STD_OUT("out"), STD_ERR("err");

        private String typeName;

        private StreamType(final String name) {
            this.typeName = name;
        }

        @Override
        public String toString() {
            return this.typeName;
        }
    }

    private static class ServerOutputConsumer implements StreamConsumer {

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
                readingFinished();
                this.serverPort = port.get();
            }
        }

        public int waitUntilStartupFinished() {
            try {
                this.startupFinished.await(SERVER_START_TIMEOUT.toMillis(), TimeUnit.MILLISECONDS);
            } catch (final InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for startup to finish", exception);
            }
            return Objects.requireNonNull(this.serverPort, "server port");
        }

        private Optional<Integer> extractPort(final String line) {
            final Matcher matcher = Pattern.compile(".*Starting server on port (\\d+).*").matcher(line);
            if (matcher.matches()) {
                return Optional.of(Integer.parseInt(matcher.group(1)));
            }
            return Optional.empty();
        }

        @Override
        public void readFinished() {
            readingFinished();
        }

        @Override
        public void readFailed(final IOException exception) {
            LOGGER.log(Level.WARNING, "Failed to read from stream", exception);
            readingFinished();
        }

        private void readingFinished() {
            this.startupFinished.countDown();
        }
    }
}