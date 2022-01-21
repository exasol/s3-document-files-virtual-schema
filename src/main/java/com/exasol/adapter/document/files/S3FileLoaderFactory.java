package com.exasol.adapter.document.files;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.files.FileLoader;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;
import com.exasol.adapter.document.files.connection.S3ConnectionPropertiesReader;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory for {@link S3FileLoader}s.
 */
public class S3FileLoaderFactory implements FileLoaderFactory {

    @Override
    public FileLoader getLoader(final StringFilter filePattern,
            final ConnectionPropertiesReader connectionInformation) {
        return new S3FileLoader(filePattern, new S3ConnectionPropertiesReader().read(connectionInformation));
    }

    @Override
    public String getUserGuideUrl() {
        return S3VsConstants.USER_GUIDE_URL;
    }
}
