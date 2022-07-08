package com.exasol.adapter.document.files.extension;

import java.nio.file.*;
import java.time.Duration;
import java.util.List;
import java.util.logging.Logger;

import com.exasol.adapter.document.files.extension.process.SimpleProcess;

interface ExtensionManagerInstaller {

    static ExtensionManagerInstaller forConfig(final ExtensionTestConfig config) {
        if (config.getLocalExtensionManagerProject().isPresent()) {
            return new LocalProjectDirInstaller(config.getLocalExtensionManagerProject().get());
        }
        return new GoInstaller(config.getExtensionManagerVersion());
    }

    /**
     * Install the extension manager in the given version.
     * 
     * @return the path of the executable.
     */
    Path install();

    static class LocalProjectDirInstaller implements ExtensionManagerInstaller {
        private static final Logger LOGGER = Logger.getLogger(LocalProjectDirInstaller.class.getName());
        private final Path extensionManagerProjectDir;

        private LocalProjectDirInstaller(final Path extensionManagerProjectDir) {
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
                throw new IllegalStateException(
                        "Extension manager executable " + executable + " not found after build");
            }
            return executable;
        }
    }

    static class GoInstaller implements ExtensionManagerInstaller {
        private static final Logger LOGGER = Logger.getLogger(GoInstaller.class.getName());
        private final String version;

        private GoInstaller(final String version) {
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
}
