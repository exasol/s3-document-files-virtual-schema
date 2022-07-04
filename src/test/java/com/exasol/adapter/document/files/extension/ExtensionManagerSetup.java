package com.exasol.adapter.document.files.extension;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.TimeoutException;

import com.exasol.adapter.document.files.IntegrationTestSetup;
import com.exasol.bucketfs.Bucket;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.extensionmanager.client.api.DefaultApi;
import com.exasol.extensionmanager.client.invoker.ApiClient;

public class ExtensionManagerSetup implements AutoCloseable {

    private final ExtensionManagerProcess extensionManager;
    private final ExasolTestSetup exasolTestSetup;

    private ExtensionManagerSetup(final ExtensionManagerProcess extensionManager,
            final ExasolTestSetup exasolTestSetup) {
        this.extensionManager = extensionManager;
        this.exasolTestSetup = exasolTestSetup;
    }

    public static ExtensionManagerSetup create(final Path extensionFolder) {
        prepareExtension(extensionFolder);
        final ExasolTestSetup exasolTestSetup = new ExasolTestSetupFactory(
                Path.of("cloudSetup/generated/testConfig.json")).getTestSetup();
        final ExtensionManagerProcess extensionManager = ExtensionManagerProcess
                .start(Paths.get("../extension-manager/extension-manager").toAbsolutePath(), extensionFolder);
        uploadToBucketFs(exasolTestSetup.getDefaultBucket());
        return new ExtensionManagerSetup(extensionManager, exasolTestSetup);
    }

    private static void prepareExtension(final Path extensionFolder) {
        final Path extension = Paths.get("extension/dist/s3-vs-extension.js").toAbsolutePath();
        if (!Files.exists(extension)) {
            throw new IllegalStateException("Extension file " + extension + " not found. Build it by executing: cd "
                    + extension.getParent().getParent() + " && npm install && npm run build");
        }
        copy(extension, extensionFolder.resolve(extension.getFileName()));
    }

    private static void copy(final Path sourceFile, final Path targetFile) {
        try {
            Files.copy(sourceFile, targetFile);
        } catch (final IOException exception) {
            throw new UncheckedIOException("Error copying extension " + sourceFile + " to " + targetFile, exception);
        }
    }

    private static void uploadToBucketFs(final Bucket bucket) {
        final Path localPath = IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH;
        try {
            bucket.uploadFile(localPath, localPath.getFileName().toString());
        } catch (FileNotFoundException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to upload file " + localPath + " to bucketfs", exception);
        }
    }

    public ExtensionManagerClient client() {
        final DefaultApi apiClient = new DefaultApi(
                new ApiClient().setBasePath(this.extensionManager.getServerBasePath()));
        return new ExtensionManagerClient(apiClient, this.exasolTestSetup.getConnectionInfo());
    }

    @Override
    public void close() {
        this.extensionManager.close();
        try {
            this.exasolTestSetup.close();
        } catch (final Exception exception) {
            throw new IllegalStateException("Error closing exasol test setup", exception);
        }
    }
}
