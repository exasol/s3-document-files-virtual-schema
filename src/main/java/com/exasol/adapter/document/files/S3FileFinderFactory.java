package com.exasol.adapter.document.files;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.documentfetcher.files.FileFinderFactory;
import com.exasol.adapter.document.documentfetcher.files.RemoteFileFinder;
import com.exasol.adapter.document.files.connection.S3ConnectionPropertiesReader;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory for {@link S3FileFinder}s.
 */
public class S3FileFinderFactory implements FileFinderFactory {

    private static final long serialVersionUID = 6701264611866170553L;

    @Override
    public RemoteFileFinder getFinder(final StringFilter filePattern,
            final ConnectionPropertiesReader connectionInformation) {
        return new S3FileFinder(filePattern, new S3ConnectionPropertiesReader().read(connectionInformation));
    }

    @Override
    public String getUserGuideUrl() {
        return S3VsConstants.USER_GUIDE_URL;
    }
}
