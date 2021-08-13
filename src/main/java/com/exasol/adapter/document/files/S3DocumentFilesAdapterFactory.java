package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.files.S3DocumentFilesAdapter.ADAPTER_NAME;

import com.exasol.adapter.AdapterFactory;
import com.exasol.adapter.VirtualSchemaAdapter;
import com.exasol.logging.VersionCollector;

/**
 * Factory for {@link S3DocumentFilesAdapter}.
 * 
 * <p>
 * This factory is loaded via a service loader (resources/services/com.exasol.adapter.AdapterFactory).
 * </p>
 */
public class S3DocumentFilesAdapterFactory implements AdapterFactory {
    @Override
    public VirtualSchemaAdapter createAdapter() {
        return new S3DocumentFilesAdapter();
    }

    @Override
    public String getAdapterVersion() {
        final VersionCollector versionCollector = new VersionCollector(
                "META-INF/maven/com.exasol/s3-document-files-virtual-schema/pom.properties");
        return versionCollector.getVersionNumber();
    }

    @Override
    public String getAdapterName() {
        return ADAPTER_NAME;
    }
}
