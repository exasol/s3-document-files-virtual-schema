package com.exasol.adapter.document.files.extension.process;

import java.io.IOException;

class CollectingStreamConsumer implements ProcessStreamConsumer {

    private final StringBuilder stringBuilder;

    CollectingStreamConsumer(final StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void accept(final String line) {
        this.stringBuilder.append(line).append('\n');
    }

    @Override
    public void readFinished() {
        // nothing to do
    }

    @Override
    public void readFailed(final IOException ioException) {
        // nothing to do
    }
}
