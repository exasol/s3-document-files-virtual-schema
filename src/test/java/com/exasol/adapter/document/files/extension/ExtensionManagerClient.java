package com.exasol.adapter.document.files.extension;

import java.util.List;
import java.util.logging.Logger;

import com.exasol.exasoltestsetup.SqlConnectionInfo;
import com.exasol.extensionmanager.client.api.DefaultApi;
import com.exasol.extensionmanager.client.model.RestAPIExtensionsResponseExtension;
import com.exasol.extensionmanager.client.model.RestAPIInstallationsResponseInstallation;

public class ExtensionManagerClient {
    private static final Logger LOGGER = Logger.getLogger(ExtensionManagerClient.class.getName());
    private final DefaultApi apiClient;
    private final SqlConnectionInfo dbConnectionInfo;

    ExtensionManagerClient(final DefaultApi apiClient, final SqlConnectionInfo dbConnectionInfo) {
        this.apiClient = apiClient;
        this.dbConnectionInfo = dbConnectionInfo;
    }

    public List<RestAPIExtensionsResponseExtension> getExtensions() {
        return this.apiClient.getExtensions(getDbHost(), getDbPort(), getDbUser(), getDbPassword()).getExtensions();
    }

    public RestAPIExtensionsResponseExtension getSingleExtension() {
        final List<RestAPIExtensionsResponseExtension> extensions = this.getExtensions();
        if (extensions.size() != 1) {
            throw new IllegalStateException(
                    "Expected exactly one extension but found " + extensions.size() + ": " + extensions);
        }
        return extensions.get(0);
    }

    public List<RestAPIInstallationsResponseInstallation> getInstallations() {
        return this.apiClient.getInstallations(getDbHost(), getDbPort(), getDbUser(), getDbPassword())
                .getInstallations();
    }

    public void installCurrentExtension() {
        final RestAPIExtensionsResponseExtension extension = getSingleExtension();
        if (extension.getInstallableVersions().isEmpty()) {
            throw new IllegalStateException(
                    "Expected at least one installable version for extensions " + extension.getId());
        }
        final String version = extension.getInstallableVersions().get(0);
        LOGGER.fine(() -> "Installing extension " + extension.getId() + " in version " + version);
        install(extension.getId(), version);
    }

    public void install(final String extensionId, final String extensionVersion) {
        this.apiClient.installExtension(getDbHost(), getDbPort(), getDbUser(), getDbPassword(), extensionId,
                extensionVersion, "dummyBody");
    }

    private String getDbHost() {
        return this.dbConnectionInfo.getHost();
    }

    private int getDbPort() {
        return this.dbConnectionInfo.getPort();
    }

    private String getDbUser() {
        return this.dbConnectionInfo.getUser();
    }

    private String getDbPassword() {
        return this.dbConnectionInfo.getPassword();
    }
}
