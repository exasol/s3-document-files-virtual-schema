package com.exasol.adapter.document.files.s3testsetup;

import java.util.function.Supplier;

public enum S3TestBackend {
    FLOCI("Floci", S3ContainerSetup::floci),
    MINIO("MinIO", S3ContainerSetup::minio);

    private final String displayName;
    private final Supplier<S3ContainerSetup> setupFactory;

    S3TestBackend(final String displayName, final Supplier<S3ContainerSetup> setupFactory) {
        this.displayName = displayName;
        this.setupFactory = setupFactory;
    }

    public S3ContainerSetup createSetup() {
        return this.setupFactory.get();
    }

    @Override
    public String toString() {
        return this.displayName;
    }
}
