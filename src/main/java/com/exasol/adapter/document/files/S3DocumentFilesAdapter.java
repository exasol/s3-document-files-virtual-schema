package com.exasol.adapter.document.files;

import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;

/**
 * This class is the entry point for the S3 files Virtual Schema.
 */
public class S3DocumentFilesAdapter extends DocumentFilesAdapter {
    /** Name of the s3-document-files-virtual-schema adapter */
    public static final String ADAPTER_NAME = "S3_DOCUMENT_FILES";

    @Override
    protected FileLoaderFactory getFileLoaderFactory() {
        return new S3FileLoaderFactory();
    }

    @Override
    protected String getAdapterName() {
        return ADAPTER_NAME;
    }
}
