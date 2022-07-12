package com.exasol.adapter.document.files.extension.installer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.adapter.document.files.extension.process.SimpleProcess;

class LocalProjectDirInstaller implements ExtensionManagerInstaller {
    private static final Logger LOGGER = Logger.getLogger(LocalProjectDirInstaller.class.getName());
    private final Path extensionManagerProjectDir;

    LocalProjectDirInstaller(final Path extensionManagerProjectDir) {
        this.extensionManagerProjectDir = extensionManagerProjectDir;
    }

    @Override
    public Path install() {
        final String executableName = "extension-manager";
        LOGGER.info(() -> "Building extension manager in " + extensionManagerProjectDir);
        SimpleProcess.start(extensionManagerProjectDir, List.of("go", "build", "-o", executableName, "main.go"),
                Duration.ofSeconds(30));
        final Path executable = extensionManagerProjectDir.resolve(executableName);
        if (!Files.exists(executable)) {
            throw new IllegalStateException("Extension manager executable " + executable + " not found after build");
        }
        return executable;
    }
}