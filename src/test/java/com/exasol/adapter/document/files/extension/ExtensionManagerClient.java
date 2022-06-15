package com.exasol.adapter.document.files.extension;

import java.util.List;

import com.exasol.exasoltestsetup.SqlConnectionInfo;
import com.exasol.extensionmanager.client.api.DefaultApi;
import com.exasol.extensionmanager.client.model.RestAPIExtensionsResponseExtension;
import com.exasol.extensionmanager.client.model.RestAPIInstallationsResponseInstallation;

public class ExtensionManagerClient {

    private final DefaultApi apiClient;
    private final SqlConnectionInfo dbConnectionInfo;

    ExtensionManagerClient(final DefaultApi apiClient, final SqlConnectionInfo dbConnectionInfo) {
        this.apiClient = apiClient;
        this.dbConnectionInfo = dbConnectionInfo;
    }

    public List<RestAPIExtensionsResponseExtension> getExtensions() {
        return this.apiClient.extensionsGet(getDbHost(), getDbPort(), getDbUser(), getDbPassword()).getExtensions();
    }

    public List<RestAPIInstallationsResponseInstallation> getInstallations() {
        return this.apiClient.installationsGet(getDbHost(), getDbPort(), getDbUser(), getDbPassword())
                .getInstallations();
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
