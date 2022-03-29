package com.exasol.adapter.document.files;

import java.nio.file.Path;

public interface S3UploadInterface {
    public void uploadFile(final Path file, final String key);

}
