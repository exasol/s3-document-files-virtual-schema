package com.exasol.adapter.document.files.extension.process;

import java.io.IOException;

/**
 * Callback interface used by {@link AsyncStreamReader}.
 */
public interface ProcessStreamConsumer {
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
     * @param exception the exception.
     */
    void readFailed(IOException exception);
}