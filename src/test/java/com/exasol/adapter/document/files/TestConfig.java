package com.exasol.adapter.document.files;

import java.io.*;
import java.util.Objects;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import software.amazon.awssdk.auth.credentials.*;

public final class TestConfig {
    public static final String FILE_NAME = "test_config.yml";
    private static final TestConfig CONFIG = new Reader().readTestConfig();
    private String awsProfile;
    /** E-mail address of the contact-person for this project. Will be used in exa:owner tag for AWS resources. */
    private String owner;
    private String s3CacheBucket;

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

    public void setAwsProfile(final String awsProfile) {
        this.awsProfile = awsProfile;
    }

    public void setOwner(final String owner) {
        this.owner = owner;
    }

    public void setS3CacheBucket(final String s3CacheBucket) {
        this.s3CacheBucket = s3CacheBucket;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.awsProfile, this.owner, this.s3CacheBucket);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TestConfig other = (TestConfig) obj;
        return Objects.equals(this.awsProfile, other.awsProfile) && Objects.equals(this.owner, other.owner)
                && Objects.equals(this.s3CacheBucket, other.s3CacheBucket);
    }

    private static class Reader {
        public TestConfig readTestConfig() {
            final Yaml yaml = new Yaml(new Constructor(TestConfig.class));
            try (final FileReader fileReader = new FileReader(FILE_NAME)) {
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
