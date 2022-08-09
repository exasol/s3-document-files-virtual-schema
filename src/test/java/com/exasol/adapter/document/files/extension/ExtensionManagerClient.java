package com.exasol.adapter.document.files.extension;

import java.util.List;
import java.util.logging.Logger;

import com.exasol.exasoltestsetup.SqlConnectionInfo;
import com.exasol.extensionmanager.client.api.DefaultApi;
import com.exasol.extensionmanager.client.invoker.ApiClient;
import com.exasol.extensionmanager.client.model.*;

public class ExtensionManagerClient {
    private static final Logger LOGGER = Logger.getLogger(ExtensionManagerClient.class.getName());
    private final DefaultApi apiClient;
    private final SqlConnectionInfo dbConnectionInfo;

    private ExtensionManagerClient(final DefaultApi apiClient, final SqlConnectionInfo dbConnectionInfo) {
        this.apiClient = apiClient;
        this.dbConnectionInfo = dbConnectionInfo;
    }

    static ExtensionManagerClient create(final String serverBasePath, final SqlConnectionInfo connectionInfo) {
        final DefaultApi apiClient = new DefaultApi(new ApiClient().setBasePath(serverBasePath));
        return new ExtensionManagerClient(apiClient, connectionInfo);
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

    public void installExtension(final String version) {
        final RestAPIExtensionsResponseExtension extension = getSingleExtension();
        if (extension.getInstallableVersions().isEmpty()) {
            throw new IllegalStateException(
                    "Expected at least one installable version for extensions " + extension.getId());
        }
        LOGGER.fine(() -> "Installing extension " + extension.getId() + " in version " + version);
        install(extension.getId(), version);
    }

    public void installExtension() {
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

    public String createInstance(final List<RestAPIParameterValue> parameterValues) {
        final RestAPIExtensionsResponseExtension extension = getSingleExtension();
        if (extension.getInstallableVersions().isEmpty()) {
            throw new IllegalStateException(
                    "Expected at least one installable version for extensions " + extension.getId());
        }
        return createInstance(extension.getId(), extension.getInstallableVersions().get(0), parameterValues)
                .getInstanceName();
    }

    private RestAPICreateInstanceResponse createInstance(final String extensionId, final String extensionVersion,
            final List<RestAPIParameterValue> parameterValues) {
        return this.apiClient
                .createInstance(
                        new RestAPICreateInstanceRequest().extensionId(extensionId).extensionVersion(extensionVersion)
                                .parameterValues(parameterValues),
                        getDbHost(), getDbPort(), getDbUser(), getDbPassword());
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
