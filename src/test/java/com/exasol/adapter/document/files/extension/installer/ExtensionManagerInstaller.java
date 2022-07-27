package com.exasol.adapter.document.files.extension.installer;

import java.nio.file.Path;

import com.exasol.adapter.document.files.extension.ExtensionTestConfig;

public interface ExtensionManagerInstaller {

    public static ExtensionManagerInstaller forConfig(final ExtensionTestConfig config) {
        if (config.getLocalExtensionManagerProject().isPresent()) {
            return new LocalProjectDirInstaller(config);
        }
        return new GoInstaller(config);
    }

    /**
     * Install the extension manager in the given version.
     * 
     * @return the path of the executable.
     */
    Path install();
}
