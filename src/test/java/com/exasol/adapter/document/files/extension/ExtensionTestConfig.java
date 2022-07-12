package com.exasol.adapter.document.files.extension;

import java.io.*;
import java.nio.file.*;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Logger;

public class ExtensionTestConfig {
    private static final Logger LOGGER = Logger.getLogger(ExtensionTestConfig.class.getName());
    private static final Path CONFIG_FILE = Paths.get("extension-test.properties");
    private final Properties properties;

    private ExtensionTestConfig(final Properties properties) {
        this.properties = properties;
    }

    static ExtensionTestConfig read() {
        final Path file = CONFIG_FILE.toAbsolutePath();
        if (!Files.exists(file)) {
            LOGGER.info(() -> "Extension test config file " + file + " not found. Using defaults.");
            return new ExtensionTestConfig(new Properties());
        }
        return new ExtensionTestConfig(loadProperties(file));
    }

    private static Properties loadProperties(final Path configFile) {
        LOGGER.info(() -> "Reading config file " + configFile);
        try (InputStream stream = Files.newInputStream(configFile)) {
            final Properties props = new Properties();
            props.load(stream);
            return props;
        } catch (final IOException e) {
            throw new UncheckedIOException("Error reading config file " + configFile, e);
        }
    }

    public Optional<Path> getLocalExtensionManagerProject() {
        return getOptionalValue("localExtensionManager") //
                .map(path -> Paths.get(path).toAbsolutePath()) //
                .map(path -> {
                    if (!Files.exists(path) || !Files.isDirectory(path)) {
                        throw new IllegalStateException("Path to extension manager '" + path + "' must be a directory");
                    }
                    return path;
                });
    }

    public String getExtensionManagerVersion() {
        return "latest";
    }

    private Optional<String> getOptionalValue(final String param) {
        return Optional.ofNullable(this.properties.getProperty(param));
    }
}
