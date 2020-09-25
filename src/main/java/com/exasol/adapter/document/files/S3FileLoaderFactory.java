package com.exasol.adapter.document.files;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.documentfetcher.files.FileLoader;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.SegmentDescription;

/**
 * Factory for {@link S3FileLoader}s.
 */
public class S3FileLoaderFactory implements FileLoaderFactory {
    @Override
    public FileLoader getLoader(final String fileName, final SegmentDescription segmentDescription,
            final ExaConnectionInformation exaConnectionInformation) {
        return new S3FileLoader(fileName, segmentDescription, exaConnectionInformation);
    }
}
