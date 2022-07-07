package com.exasol.adapter.document.files.extension.process;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.List;

public class DelegatingStreamConsumer implements ProcessStreamConsumer {

    private final List<ProcessStreamConsumer> delegates;

    public DelegatingStreamConsumer(final ProcessStreamConsumer... delegates){
        this.delegates = asList(delegates);
    }

    @Override
    public void accept(final String line) {
        this.delegates.forEach(delegate -> delegate.accept(line));
    }

    @Override
    public void readFinished() {
        this.delegates.forEach(delegate -> delegate.readFinished());
    }

    @Override
    public void readFailed(final IOException exception) {
        this.delegates.forEach(delegate -> delegate.readFailed(exception));
    }
}