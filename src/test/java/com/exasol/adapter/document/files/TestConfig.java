package com.exasol.adapter.document.files;

import java.io.*;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import lombok.Data;
import software.amazon.awssdk.auth.credentials.*;

@Data
public class TestConfig {
    private static final TestConfig CONFIG = new Reader().readTestConfig();
    private String awsProfile;
    /** E-mail address of the contact-person for this project. Will be used in exa:owner tag for AWS resources. */
    private String owner;

    public static TestConfig instance() {
        return CONFIG;
    }

    public AwsCredentialsProvider getAwsCredentialsProvider() {
        if (this.awsProfile != null && !this.awsProfile.isBlank()) {
            return ProfileCredentialsProvider.create(TestConfig.instance().getAwsProfile());
        } else {
            return DefaultCredentialsProvider.builder().build();
        }
    }

    private static class Reader {
        public TestConfig readTestConfig() {
            final Yaml yaml = new Yaml(new Constructor(TestConfig.class));
            try (final FileReader fileReader = new FileReader("test_config.yml")) {
                return yaml.load(fileReader);
            } catch (final FileNotFoundException exception) {
                throw new IllegalArgumentException(
                        "Could not find test_config.yml. Please create that file in the root of this project.");
            } catch (final IOException exception) {
                throw new UncheckedIOException("Failed to load test_config.yml", exception);
            }
        }
    }
}
