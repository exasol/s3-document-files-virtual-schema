package com.exasol.adapter.document.files.extension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.adapter.document.GenericUdfCallHandler;
import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.dbbuilder.dialects.exasol.AdapterScript.Language;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript.InputType;
import com.exasol.extensionmanager.client.model.RestAPIExtensionsResponseExtension;
import com.exasol.extensionmanager.client.model.RestAPIInstallationsResponseInstallation;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

class ExtensionIT {
    private static ExtensionManagerSetup extensionManagerSetup;

    @TempDir
    static Path extensionFolder;

    private ExasolSchema schema;

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

    @BeforeEach
    void createSchema() {
        schema = extensionManagerSetup.createExtensionSchema();
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
    void listInstallations_ignoresWrongScriptNames() {
        createAdapter("wrong_adapter_name", "wrong_import_script_name");
        final List<RestAPIInstallationsResponseInstallation> installations = extensionManagerSetup.client()
                .getInstallations();
        assertThat(installations, hasSize(0));
    }

    @Test
    void listInstallations_findsMatchingScripts() {
        createAdapter("S3_FILES_ADAPTER", "IMPORT_FROM_S3_DOCUMENT_FILES");
        final List<RestAPIInstallationsResponseInstallation> installations = extensionManagerSetup.client()
                .getInstallations();
        assertAll(() -> assertThat(installations, hasSize(1)), //
                () -> assertThat(installations.get(0).getName(),
                        equalTo(ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".S3_FILES_ADAPTER")),
                () -> assertThat(installations.get(0).getVersion(), equalTo("(unknown)")),
                () -> assertThat(installations.get(0).getInstanceParameters(), hasSize(0)));
    }

    private void createAdapter(final String adapterScriptName, final String importScriptName) {
        schema.createAdapterScriptBuilder(adapterScriptName).bucketFsContent("com.exasol.adapter.RequestDispatcher",
                extensionManagerSetup.getAdapterJarInBucketFs()).language(Language.JAVA).build();
        schema.createUdfBuilder(importScriptName).language(UdfScript.Language.JAVA).inputType(InputType.SET)
                .parameter(GenericUdfCallHandler.PARAMETER_DOCUMENT_FETCHER, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), extensionManagerSetup.getAdapterJarInBucketFs())
                .build();
    }
}
