package com.exasol.adapter.document.files.extension.installer;

import java.nio.file.*;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.adapter.document.files.extension.OsCheck;
import com.exasol.adapter.document.files.extension.process.SimpleProcess;

class GoInstaller implements ExtensionManagerInstaller {
    private static final Logger LOGGER = Logger.getLogger(GoInstaller.class.getName());
    private final String version;

    GoInstaller(final String version) {
        this.version = version;
    }

    @Override
    public Path install() {
        runGoInstall();
        return getExtensionManagerExecutable();
    }

    private void runGoInstall() {
        LOGGER.info(() -> "Installing extension manager version '" + version + "'...");
        SimpleProcess.start(List.of("go", "install", "github.com/exasol/extension-manager@" + version),
                Duration.ofSeconds(60));
    }

    private Path getExtensionManagerExecutable() {
        final String executableName = "extension-manager" + OsCheck.getExecutableSuffix();
        final Path executablePath = getGoPath().resolve("bin").resolve(executableName);
        if (!Files.exists(executablePath)) {
            throw new IllegalStateException("Executable was not installed at '" + executablePath + "'");
        }
        return executablePath;
    }

    private Path getGoPath() {
        final String rawPath = SimpleProcess.start(List.of("go", "env", "GOPATH"), Duration.ofSeconds(1));
        final Path goPath = Paths.get(rawPath.trim());
        if (!Files.exists(goPath)) {
            throw new IllegalStateException("GOPATH does not exist: '" + goPath + "'");
        }
        LOGGER.info(() -> "Got GOPATH '" + goPath + "'");
        return goPath;
    }
}