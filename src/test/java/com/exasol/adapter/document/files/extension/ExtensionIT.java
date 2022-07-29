package com.exasol.adapter.document.files.extension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import com.exasol.adapter.RequestDispatcher;
import com.exasol.adapter.document.GenericUdfCallHandler;
import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.adapter.document.files.IntegrationTestSetup;
import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.dbbuilder.dialects.exasol.AdapterScript.Language;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript.InputType;
import com.exasol.exasoltestsetup.ServiceAddress;
import com.exasol.extensionmanager.client.invoker.ApiException;
import com.exasol.extensionmanager.client.model.*;
import com.exasol.matcher.ResultSetStructureMatcher;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;

class ExtensionIT {

    private static final int EXPECTED_PARAMETER_COUNT = 9;
    private static ExtensionManagerSetup setup;
    private static S3TestSetup s3TestSetup;
    private static String s3BucketName;

    @TempDir
    static Path extensionFolder;

    @BeforeAll
    static void setup() {
        setup = ExtensionManagerSetup.create(extensionFolder);
        s3TestSetup = new AwsS3TestSetup();
        s3BucketName = "extension-test.s3.virtual-schema-test-bucket-" + System.currentTimeMillis();
        s3TestSetup.getS3Client().createBucket(builder -> builder.bucket(s3BucketName));
    }

    @AfterAll
    static void teardown() {
        s3TestSetup.getS3Client().deleteBucket(DeleteBucketRequest.builder().bucket(s3BucketName).build());
        if (setup != null) {
            setup.close();
        }
    }

    @AfterEach
    void cleanup() {
        setup.dropExtensionSchema();
    }

    @Test
    void listExtensions() {
        final List<RestAPIExtensionsResponseExtension> extensions = setup.client().getExtensions();
        final String projectVersion = MavenProjectVersionGetter.getCurrentProjectVersion();
        assertAll(() -> assertThat(extensions, hasSize(1)), //
                () -> assertThat(extensions.get(0).getName(), equalTo("S3 Virtual Schema")),
                () -> assertThat(extensions.get(0).getInstallableVersions(), contains(projectVersion)),
                () -> assertThat(extensions.get(0).getDescription(),
                        equalTo("Virtual Schema for document files on AWS S3")));
    }

    @Test
    void listInstallationsEmpty() {
        final List<RestAPIInstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertThat(installations, hasSize(0));
    }

    @Test
    void listInstallations_ignoresWrongScriptNames() {
        createAdapter("wrong_adapter_name", "wrong_import_script_name");
        final List<RestAPIInstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertThat(installations, hasSize(0));
    }

    @Test
    void listInstallations_findsMatchingScripts() {
        createAdapter("S3_FILES_ADAPTER", "IMPORT_FROM_S3_DOCUMENT_FILES");
        final List<RestAPIInstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertAll(() -> assertThat(installations, hasSize(1)), //
                () -> assertThat(installations.get(0).getName(),
                        equalTo(ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".S3_FILES_ADAPTER")),
                () -> assertThat(installations.get(0).getVersion(), equalTo(setup.getCurrentProjectVersion())),
                () -> assertThat(installations.get(0).getInstanceParameters(), hasSize(EXPECTED_PARAMETER_COUNT)));
    }

    @Test
    void listInstallations_findsOwnInstallation() {
        setup.client().installExtension();
        final List<RestAPIInstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertAll(() -> assertThat(installations, hasSize(1)), //
                () -> assertThat(installations.get(0).getName(),
                        equalTo(ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".S3_FILES_ADAPTER")),
                () -> assertThat(installations.get(0).getVersion(), equalTo(setup.getCurrentProjectVersion())),
                () -> assertThat(installations.get(0).getInstanceParameters(), hasSize(EXPECTED_PARAMETER_COUNT)));
    }

    @Test
    void install_createsScripts() {
        setup.client().installExtension();
        assertScriptsExist();
    }

    @Test
    void install_worksIfCalledTwice() {
        setup.client().installExtension();
        setup.client().installExtension();
        assertScriptsExist();
    }

    @Test
    void install_failsForUnsupportedVersion() {
        final ExtensionManagerClient client = setup.client();
        final ApiException exception = assertThrows(ApiException.class, () -> client.installExtension("unsupported"));
        assertThat(exception.getMessage(),
                allOf(containsString("Request failed: error installing extension: failed to install extension"),
                        containsString("Error: Installing version 'unsupported' not supported, try '"
                                + setup.getCurrentProjectVersion() + "'")));
        assertScriptsDoNotExist();
    }

    @Test
    void createInstanceFailsWithoutRequiredParameters() {
        final ExtensionManagerClient client = setup.client();
        client.installExtension();
        final List<RestAPIParameterValue> emptyParameters = List.of();
        final ApiException exception = assertThrows(ApiException.class, () -> client.createInstance(emptyParameters));
        assertThat(exception.getMessage(), equalTo(
                "Request failed: error installing extension: invalid parameters: Failed to validate parameter \"Name of the new virtual schema\": This is a required field., Failed to validate parameter \"AWS Access Key Id\": This is a required field., Failed to validate parameter \"AWS Secret AccessKey\": This is a required field., Failed to validate parameter \"AWS Region\": This is a required field., Failed to validate parameter \"S3 Bucket\": This is a required field."));
    }

    @Test
    void createInstance() {
        final ExtensionManagerClient client = setup.client();
        client.installExtension();
        final String instanceName = client.createInstance(createValidParameters("my_s3_vs"));
        assertThat(instanceName, equalTo("my_s3_vs"));
        setup.exasolMetadata().assertConnection(ResultSetStructureMatcher.table()
                .row("MY_S3_VS_CONNECTION", "Created by extension manager for S3 virtual schema my_s3_vs").matches());
    }

    private List<RestAPIParameterValue> createValidParameters(final String virtualSchemaName) {
        return List.of(param("virtualSchemaName", virtualSchemaName),
                param("awsEndpointOverride", getInDatabaseS3Address()), param("awsRegion", s3TestSetup.getRegion()),
                param("s3Bucket", "s3BucketName"), param("awsAccessKeyId", s3TestSetup.getUsername()),
                param("awsSecretAccessKey", s3TestSetup.getPassword()));
    }

    private String getInDatabaseS3Address() {
        final String s3Entrypoint = s3TestSetup.getEntrypoint();
        if (s3Entrypoint.contains(":")) {
            return setup.makeTcpServiceAccessibleFromDatabase(ServiceAddress.parse(s3Entrypoint)).toString();
        } else {
            return s3Entrypoint;
        }
    }

    private RestAPIParameterValue param(final String name, final String value) {
        return new RestAPIParameterValue().name(name).value(value);
    }

    private void assertScriptsExist() {
        final String jarDirective = "%jar /buckets/bfsdefault/default/" + IntegrationTestSetup.ADAPTER_JAR + ";";
        setup.exasolMetadata()
                .assertScript(ResultSetStructureMatcher.table()
                        .row("IMPORT_FROM_S3_DOCUMENT_FILES", "UDF", "SET", "EMITS",
                                allOf(containsString("%scriptclass " + UdfEntryPoint.class.getName() + ";"), //
                                        containsString(jarDirective))) //
                        .row("S3_FILES_ADAPTER", "ADAPTER", null, null,
                                allOf(containsString("%scriptclass " + RequestDispatcher.class.getName() + ";"), //
                                        containsString(jarDirective))) //
                        .matches());
    }

    private void assertScriptsDoNotExist() {
        setup.exasolMetadata().assertScript(
                ResultSetStructureMatcher.table("VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR").matches());
    }

    private void createAdapter(final String adapterScriptName, final String importScriptName) {
        final ExasolSchema schema = setup.createExtensionSchema();
        schema.createAdapterScriptBuilder(adapterScriptName)
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", setup.getAdapterJarInBucketFs())
                .language(Language.JAVA).build();
        schema.createUdfBuilder(importScriptName).language(UdfScript.Language.JAVA).inputType(InputType.SET)
                .parameter(GenericUdfCallHandler.PARAMETER_DOCUMENT_FETCHER, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), setup.getAdapterJarInBucketFs()).build();
    }
}
