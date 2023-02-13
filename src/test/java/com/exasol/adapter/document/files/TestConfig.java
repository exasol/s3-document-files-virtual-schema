package com.exasol.adapter.document.files;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.logging.Logger;

import software.amazon.awssdk.auth.credentials.*;

public final class TestConfig {
    private static final Logger LOGGER = Logger.getLogger(S3Cache.class.getName());
    public static final String FILE_NAME = "test_config.properties";
    private static final TestConfig CONFIG = new Reader().readTestConfig();
    private final String awsProfile;
    /** E-mail address of the contact-person for this project. Will be used in exa:owner tag for AWS resources. */
    private final String owner;
    private final String s3CacheBucket;

    public TestConfig(final Properties properties) {
        this.awsProfile = properties.getProperty("awsProfile");
        this.owner = properties.getProperty("owner");
        this.s3CacheBucket = properties.getProperty("s3CacheBucket");
    }

    public static TestConfig instance() {
        return CONFIG;
    }

    public AwsCredentialsProvider getAwsCredentialsProvider() {
        if ((this.awsProfile != null) && !this.awsProfile.isBlank()) {
            return ProfileCredentialsProvider.create(this.getAwsProfile());
        } else {
            return DefaultCredentialsProvider.builder().build();
        }
    }

    public String getAwsProfile() {
        return this.awsProfile;
    }

    public String getOwner() {
        return this.owner;
    }

    public String getS3CacheBucket() {
        return this.s3CacheBucket;
    }

    private static class Reader {
        public TestConfig readTestConfig() {
            LOGGER.finer(() -> "Reading test configuration from " + new File(FILE_NAME).getAbsoluteFile().toString());
            final Path path = Path.of(FILE_NAME).toAbsolutePath();
            final Properties properties = loadProperties(path);
            return new TestConfig(properties);
        }

        private Properties loadProperties(final Path path) {
            final Properties properties = new Properties();
            try (final InputStream stream = Files.newInputStream(path)) {
                properties.load(stream);
                return properties;
            } catch (final NoSuchFileException exception) {
                throw new IllegalArgumentException("Could not find " + path
                        + ". Please create that file in the root of this project or migrate from old config file 'test_config.yml'");
            } catch (final IOException exception) {
                throw new UncheckedIOException("Failed to load " + path, exception);
            }
        }
    }
}
