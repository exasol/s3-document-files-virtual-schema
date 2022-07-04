package com.exasol.adapter.document.files.extension.process;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class LoggingStreamConsumer implements ProcessStreamConsumer {
    private static final Logger LOGGER = Logger.getLogger(LoggingStreamConsumer.class.getName());
    private final String prefix;
    private final Level logLevel;

    LoggingStreamConsumer(final String prefix, final Level logLevel) {
        this.prefix = prefix;
        this.logLevel = logLevel;
    }

    @Override
    public void accept(final String line) {
        LOGGER.log(logLevel, prefix + line);
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