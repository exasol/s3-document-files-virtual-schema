package com.exasol.adapter.document.files.extension;

import java.io.*;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import com.exasol.adapter.document.files.IntegrationTestSetup;
import com.exasol.adapter.document.files.extension.installer.ExtensionManagerInstaller;
import com.exasol.adapter.document.files.extension.process.SimpleProcess;
import com.exasol.bucketfs.Bucket;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.extensionmanager.client.api.DefaultApi;
import com.exasol.extensionmanager.client.invoker.ApiClient;
import com.exasol.udfdebugging.UdfTestSetup;

public class ExtensionManagerSetup implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(ExtensionManagerSetup.class.getName());
    public static final String EXTENSION_SCHEMA_NAME = "EXA_EXTENSIONS";
    private final ExtensionManagerProcess extensionManager;
    private final ExasolTestSetup exasolTestSetup;
    private final ExasolObjectFactory exasolObjectFactory;
    private final Connection connection;
    private final UdfTestSetup udfTestSetup;
    private final ExtensionTestConfig config;

    private ExtensionManagerSetup(final ExtensionManagerProcess extensionManager, final ExasolTestSetup exasolTestSetup,
            final ExtensionTestConfig config) {
        this.extensionManager = extensionManager;
        this.exasolTestSetup = exasolTestSetup;
        this.config = config;
        try {
            this.connection = this.exasolTestSetup.createConnection();
        } catch (final SQLException exception) {
            throw new AssertionError("Failed to create db connection", exception);
        }
        this.udfTestSetup = new UdfTestSetup(this.exasolTestSetup, this.connection);
        this.exasolObjectFactory = new ExasolObjectFactory(this.connection,
                ExasolObjectConfiguration.builder().withJvmOptions(this.udfTestSetup.getJvmOptions()).build());
    }

    public static ExtensionManagerSetup create(final Path extensionFolder) {
        final ExtensionTestConfig config = ExtensionTestConfig.read();
        prepareExtension(config, extensionFolder);
        final ExasolTestSetup exasolTestSetup = new ExasolTestSetupFactory(
                Path.of("cloudSetup/generated/testConfig.json")).getTestSetup();
        final ExtensionManagerInstaller installer = ExtensionManagerInstaller.forConfig(config);
        final Path extensionManagerExecutable = installer.install();
        final ExtensionManagerProcess extensionManager = ExtensionManagerProcess.start(extensionManagerExecutable,
                extensionFolder);
        uploadToBucketFs(exasolTestSetup.getDefaultBucket());
        return new ExtensionManagerSetup(extensionManager, exasolTestSetup, config);
    }

    private static void prepareExtension(final ExtensionTestConfig config, final Path extensionFolder) {
        if (config.buildExtension()) {
            SimpleProcess.start(Paths.get("extension"), List.of("npm", "run", "build"), Duration.ofSeconds(30));
        } else {
            LOGGER.warning("Skip building extension");
        }
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

    public ExasolMetadata exasolMetadata() {
        return new ExasolMetadata(connection, EXTENSION_SCHEMA_NAME);
    }

    public String getAdapterJarInBucketFs() {
        return "/buckets/bfsdefault/default/" + IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH.getFileName().toString();
    }

    public ExasolSchema createExtensionSchema() {
        return exasolObjectFactory.createSchema(EXTENSION_SCHEMA_NAME);
    }

    public void dropExtensionSchema() {
        try {
            connection.createStatement().execute("DROP SCHEMA IF EXISTS \"" + EXTENSION_SCHEMA_NAME + "\" CASCADE");
        } catch (final SQLException exception) {
            throw new IllegalStateException("Failed to delete extension schema " + EXTENSION_SCHEMA_NAME, exception);
        }
    }

    @Override
    public void close() {
        dropExtensionSchema();
        this.extensionManager.close();
        try {
            this.exasolTestSetup.close();
        } catch (final Exception exception) {
            throw new IllegalStateException("Error closing exasol test setup", exception);
        }
    }
}
