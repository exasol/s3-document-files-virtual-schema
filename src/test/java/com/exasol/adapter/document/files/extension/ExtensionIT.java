package com.exasol.adapter.document.files.extension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.dbbuilder.dialects.exasol.AdapterScript.Language;
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
        if (extensionManagerSetup != null) {
            extensionManagerSetup.close();
        }
    }

    @AfterEach
    void cleanup() {
        extensionManagerSetup.dropCreatedObjects();
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

    @Test
    void listInstallations_findsOtherInstallation() {
        final String currentProjectVersion = MavenProjectVersionGetter.getCurrentProjectVersion();
        createAdapter("script_name");
        final List<RestAPIInstallationsResponseInstallation> installations = extensionManagerSetup.client()
                .getInstallations();
        assertAll(() -> assertThat(installations, hasSize(1)), //
                () -> assertThat(installations.get(0).getName(),
                        equalTo(ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".script_name")),
                () -> assertThat(installations.get(0).getVersion(), equalTo(currentProjectVersion)),
                () -> assertThat(installations.get(0).getInstanceParameters(), hasSize(0)));
    }

    private void createAdapter(final String scriptName) {
        extensionManagerSetup.createExtensionSchema() //
                .createAdapterScriptBuilder(scriptName).bucketFsContent("com.exasol.adapter.RequestDispatcher",
                        extensionManagerSetup.getAdapterJarInBucketFs())
                .language(Language.JAVA).build();
    }
}
