package com.exasol.adapter.document.files.extension;

import java.nio.file.*;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.adapter.document.files.extension.OsCheck.OSType;
import com.exasol.adapter.document.files.extension.process.SimpleProcess;

class ExtensionManagerInstaller {
    private static final Logger LOGGER = Logger.getLogger(ExtensionManagerInstaller.class.getName());

    private final String version;

    private ExtensionManagerInstaller(final String version) {
        this.version = version;
    }

    static ExtensionManagerInstaller forVersion(final String version) {
        return new ExtensionManagerInstaller(version);
    }

    /**
     * Install the extension manager in the given version.
     * 
     * @return the path of the executable.
     */
    Path install() {
        runGoInstall();
        return getExtensionManagerExecutable();
    }

    private void runGoInstall() {
        LOGGER.info(() -> "Installing extension manager version '" + version + "'...");
        SimpleProcess.start(List.of("go", "install", "github.com/exasol/extension-manager@" + version),
                Duration.ofSeconds(60));
    }

    private Path getExtensionManagerExecutable() {
        final OSType osType = new OsCheck().getOperatingSystemType();
        final String suffix = osType == OSType.WINDOWS ? ".exe" : "";
        final Path executable = getGoPath().resolve("bin").resolve("extension-manager" + suffix);
        if (!Files.exists(executable)) {
            throw new IllegalStateException("Executable was not installed at '" + executable + "'");
        }
        return executable;
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
