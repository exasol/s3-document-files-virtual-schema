package com.exasol.adapter.document.files.extension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.logging.Logger;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.function.Executable;

import com.exasol.exasoltestsetup.SqlConnectionInfo;
import com.exasol.extensionmanager.client.api.ExtensionApi;
import com.exasol.extensionmanager.client.api.InstanceApi;
import com.exasol.extensionmanager.client.invoker.ApiClient;
import com.exasol.extensionmanager.client.invoker.ApiException;
import com.exasol.extensionmanager.client.model.*;

import jakarta.json.JsonObject;
import jakarta.json.bind.JsonbBuilder;

public class ExtensionManagerClient {
    private static final Logger LOGGER = Logger.getLogger(ExtensionManagerClient.class.getName());
    private final ExtensionApi extensionClient;
    private final InstanceApi instanceClient;
    private final SqlConnectionInfo dbConnectionInfo;

    private ExtensionManagerClient(final ExtensionApi extensionClient, final InstanceApi instanceClient,
            final SqlConnectionInfo dbConnectionInfo) {
        this.extensionClient = extensionClient;
        this.instanceClient = instanceClient;
        this.dbConnectionInfo = dbConnectionInfo;
    }

    static ExtensionManagerClient create(final String serverBasePath, final SqlConnectionInfo connectionInfo) {
        final ApiClient apiClient = createApiClient(serverBasePath, connectionInfo);
        final ExtensionApi extensionClient = new ExtensionApi(apiClient);
        final InstanceApi instanceClient = new InstanceApi(apiClient);
        return new ExtensionManagerClient(extensionClient, instanceClient, connectionInfo);
    }

    private static ApiClient createApiClient(final String serverBasePath, final SqlConnectionInfo connectionInfo) {
        final ApiClient apiClient = new ApiClient().setBasePath(serverBasePath);
        apiClient.setUsername(connectionInfo.getUser());
        apiClient.setPassword(connectionInfo.getPassword());
        return apiClient;
    }

    public List<ExtensionsResponseExtension> getExtensions() {
        return this.extensionClient.listAvailableExtensions(getDbHost(), getDbPort()).getExtensions();
    }

    public List<InstallationsResponseInstallation> getInstallations() {
        return this.extensionClient.listInstalledExtensions(getDbHost(), getDbPort()).getInstallations();
    }

    public void installExtension(final String version) {
        final Extension extension = getExtension();
        LOGGER.fine(() -> "Installing extension " + extension.getId() + " in version " + version);
        install(extension.getId(), version);
    }

    public void installExtension() {
        installExtension(getExtension().getCurrentVersion());
    }

    public void install(final String extensionId, final String extensionVersion) {
        final InstallExtensionRequest request = new InstallExtensionRequest().extensionId(extensionId)
                .extensionVersion(extensionVersion);
        this.extensionClient.installExtension(request, getDbHost(), getDbPort());
    }

    public void uninstallExtension() {
        final Extension extension = getExtension();
        this.uninstall(extension.getId(), extension.getCurrentVersion());
    }

    public void uninstallExtension(final String extensionVersion) {
        this.uninstall(getExtension().getId(), extensionVersion);
    }

    private void uninstall(final String extensionId, final String extensionVersion) {
        this.extensionClient.uninstallExtension(extensionId, extensionVersion, getDbHost(), getDbPort());
    }

    public String createInstance(final List<ParameterValue> parameterValues) {
        final Extension extension = getExtension();
        return createInstance(extension.getId(), extension.getCurrentVersion(), parameterValues).getInstanceName();
    }

    public List<Instance> listInstances() {
        final Extension extension = getExtension();
        return listInstances(extension.getCurrentVersion());
    }

    public List<Instance> listInstances(final String version) {
        final Extension extension = getExtension();
        return listInstances(extension.getId(), version).getInstances();
    }

    private ListInstancesResponse listInstances(final String extensionId, final String extensionVersion) {
        return this.instanceClient.listInstances(extensionId, extensionVersion, getDbHost(), getDbPort());
    }

    public void deleteInstance(final String instanceId) {
        deleteInstance(getExtension().getId(), instanceId);
    }

    private void deleteInstance(final String extensionId, final String instanceId) {
        this.instanceClient.deleteInstance(extensionId, instanceId, getDbHost(), getDbPort());
    }

    public void assertRequestFails(final Executable executable, final Matcher<String> messageMatcher,
            final Matcher<Integer> statusMatcher) {
        final ApiException exception = assertThrows(ApiException.class, executable);
        final JsonObject error = JsonbBuilder.create().fromJson(exception.getMessage(), JsonObject.class);
        assertAll(() -> assertThat(error.getJsonString("message").getString(), messageMatcher),
                () -> assertThat(error.getJsonNumber("code").intValue(), statusMatcher));
    }

    private CreateInstanceResponse createInstance(final String extensionId, final String extensionVersion,
            final List<ParameterValue> parameterValues) {
        final CreateInstanceRequest request = new CreateInstanceRequest().extensionId(extensionId)
                .extensionVersion(extensionVersion).parameterValues(parameterValues);
        return this.instanceClient.createInstance(request, getDbHost(), getDbPort());
    }

    private ExtensionsResponseExtension getSingleExtension() {
        final List<ExtensionsResponseExtension> extensions = this.getExtensions();
        if (extensions.size() != 1) {
            throw new IllegalStateException(
                    "Expected exactly one extension but found " + extensions.size() + ": " + extensions);
        }
        return extensions.get(0);
    }

    private Extension getExtension() {
        final ExtensionsResponseExtension extension = getSingleExtension();
        if (extension.getInstallableVersions().size() != 1) {
            throw new IllegalStateException("Expected at exactly one installable version for extensions "
                    + extension.getId() + " but got " + extension.getInstallableVersions());
        }
        return new Extension(extension.getId(), extension.getInstallableVersions().get(0));
    }

    private static class Extension {
        private final String id;
        private final String currentVersion;

        private Extension(final String id, final String currentVersion) {
            this.id = id;
            this.currentVersion = currentVersion;
        }

        public String getId() {
            return id;
        }

        public String getCurrentVersion() {
            return currentVersion;
        }
    }

    private String getDbHost() {
        return this.dbConnectionInfo.getHost();
    }

    private int getDbPort() {
        return this.dbConnectionInfo.getPort();
    }
}
