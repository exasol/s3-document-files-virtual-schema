package com.exasol.adapter.document.files.extension.process;

public enum StreamType {
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