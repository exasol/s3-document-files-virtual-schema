package com.exasol.adapter.document.files.extension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.extensionmanager.client.model.RestAPIExtensionsResponseExtension;
import com.exasol.extensionmanager.client.model.RestAPIInstallationsResponseInstallation;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

class ExtensionIT {

    private static ExtensionManagerSetup extensionManagerSetup;

    @TempDir
    static Path extensionFolder;

    @BeforeAll
    static void setup() {
        extensionManagerSetup = ExtensionManagerSetup.create(extensionFolder);
    }

    @AfterAll
    static void teardown() {
        extensionManagerSetup.close();
    }

    @Test
    void listExtensions() {
        final List<RestAPIExtensionsResponseExtension> extensions = extensionManagerSetup.client().getExtensions();
        final String projectVersion = MavenProjectVersionGetter.getCurrentProjectVersion();
        assertAll(() -> assertThat(extensions, hasSize(1)), //
                () -> assertThat(extensions.get(0).getName(), equalTo("S3 Virtual Schema")),
                () -> assertThat(extensions.get(0).getInstallableVersions(), contains(projectVersion)),
                () -> assertThat(extensions.get(0).getDescription(),
                        equalTo("Virtual Schema for document files on AWS S3")));
    }

    @Test
    void listInstallationsEmpty() {
        final List<RestAPIInstallationsResponseInstallation> installations = extensionManagerSetup.client()
                .getInstallations();
        assertThat(installations, hasSize(0));
    }
}
