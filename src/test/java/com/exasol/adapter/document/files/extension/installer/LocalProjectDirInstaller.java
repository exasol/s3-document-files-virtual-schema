package com.exasol.adapter.document.files.extension.installer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.adapter.document.files.extension.ExtensionTestConfig;
import com.exasol.adapter.document.files.extension.process.SimpleProcess;

class LocalProjectDirInstaller implements ExtensionManagerInstaller {
    private static final Logger LOGGER = Logger.getLogger(LocalProjectDirInstaller.class.getName());
    private static final String EXECUTABLE_NAME = "extension-manager";
    private final ExtensionTestConfig config;

    LocalProjectDirInstaller(final ExtensionTestConfig config) {
        this.config = config;
    }

    @Override
    public Path install() {
        final Path extensionManagerProjectDir = config.getLocalExtensionManagerProject().get();
        buildExtensionManager(extensionManagerProjectDir);
        final Path executable = extensionManagerProjectDir.resolve(EXECUTABLE_NAME);
        if (!Files.exists(executable)) {
            throw new IllegalStateException("Extension manager executable " + executable + " not found after build");
        }
        return executable;
    }

    private void buildExtensionManager(final Path extensionManagerProjectDir) {
        if (config.buildExtensionManager()) {
            LOGGER.info(() -> "Building extension manager in " + extensionManagerProjectDir);
            SimpleProcess.start(extensionManagerProjectDir, List.of("go", "build", "-o", EXECUTABLE_NAME, "main.go"),
                    Duration.ofSeconds(30));
        } else {
            LOGGER.warning("Skipping installation of extension manager");
        }
    }
}