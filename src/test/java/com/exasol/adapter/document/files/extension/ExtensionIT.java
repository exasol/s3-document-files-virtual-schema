package com.exasol.adapter.document.files.extension;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.net.*;
import java.net.http.*;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

import org.junit.jupiter.api.*;

import com.exasol.adapter.RequestDispatcher;
import com.exasol.adapter.document.GenericUdfCallHandler;
import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.adapter.document.edml.*;
import com.exasol.adapter.document.edml.serializer.EdmlSerializer;
import com.exasol.adapter.document.files.IntegrationTestSetup;
import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.adapter.document.files.s3testsetup.S3TestSetup;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.exasol.AdapterScript.Language;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript.InputType;
import com.exasol.errorreporting.ExaError;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.extensionmanager.client.model.*;
import com.exasol.extensionmanager.itest.ExtensionManagerClient;
import com.exasol.extensionmanager.itest.ExtensionManagerSetup;
import com.exasol.extensionmanager.itest.builder.ExtensionBuilder;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

import software.amazon.awssdk.core.sync.RequestBody;

class ExtensionIT {
    private static final Logger LOG = Logger.getLogger(ExtensionIT.class.getName());
    private static final String PREVIOUS_VERSION = "2.6.2";
    private static final String PREVIOUS_VERSION_JAR_FILE = "document-files-virtual-schema-dist-7.3.3-s3-"
            + PREVIOUS_VERSION + ".jar";
    private static final Path EXTENSION_SOURCE_DIR = Paths.get("extension").toAbsolutePath();
    private static final String EXTENSION_ID = "s3-vs-extension.js";
    private static final int EXPECTED_PARAMETER_COUNT = 10;
    private static ExasolTestSetup exasolTestSetup;
    private static ExtensionManagerSetup setup;
    private static S3TestSetup s3TestSetup;
    private static String s3BucketName;
    private static String projectVersion;

    @BeforeAll
    static void setup() throws FileNotFoundException, BucketAccessException, TimeoutException {
        exasolTestSetup = new ExasolTestSetupFactory(IntegrationTestSetup.CLOUD_SETUP_CONFIG).getTestSetup();
        setup = ExtensionManagerSetup.create(exasolTestSetup, ExtensionBuilder.createDefaultNpmBuilder(
                EXTENSION_SOURCE_DIR, EXTENSION_SOURCE_DIR.resolve("dist").resolve(EXTENSION_ID)));
        s3TestSetup = new AwsS3TestSetup();
        s3BucketName = "extension-test.s3.virtual-schema-test-bucket-" + System.currentTimeMillis();
        s3TestSetup.createBucket(s3BucketName);
        projectVersion = MavenProjectVersionGetter.getCurrentProjectVersion();
        exasolTestSetup.getDefaultBucket().uploadFile(IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH,
                IntegrationTestSetup.ADAPTER_JAR);
    }

    @AfterAll
    static void teardown() throws Exception {
        if (s3TestSetup != null) {
            s3TestSetup.emptyS3Bucket(s3BucketName);
            s3TestSetup.deleteBucket(s3BucketName);
        }
        if (setup != null) {
            setup.close();
        }
        // do not delete ADAPTER_JAR as it is required by other integration tests, too
        // and upload immediately after delete fails.
        exasolTestSetup.close();
    }

    @AfterEach
    void cleanup() {
        setup.cleanup();
    }

    @Test
    void listExtensions() {
        final List<ExtensionsResponseExtension> extensions = setup.client().getExtensions();
        assertAll(() -> assertThat(extensions, hasSize(1)), //
                () -> assertThat(extensions.get(0).getName(), equalTo("S3 Virtual Schema")),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).getName(), equalTo(projectVersion)),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).isLatest(), is(true)),
                () -> assertThat(extensions.get(0).getInstallableVersions().get(0).isDeprecated(), is(false)),
                () -> assertThat(extensions.get(0).getDescription(),
                        equalTo("Virtual Schema for document files on AWS S3")));
    }

    @Test
    void listInstallationsEmpty() {
        final List<InstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertThat(installations, hasSize(0));
    }

    @Test
    void listInstallations_ignoresWrongScriptNames() {
        createAdapter("wrong_adapter_name", "wrong_import_script_name");
        final List<InstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertThat(installations, hasSize(0));
    }

    @Test
    void listInstallations_findsMatchingScripts() {
        createAdapter("S3_FILES_ADAPTER", "IMPORT_FROM_S3_DOCUMENT_FILES");
        final List<InstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertAll(() -> assertThat(installations, hasSize(1)), //
                () -> assertThat(installations.get(0).getName(),
                        equalTo(ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".S3_FILES_ADAPTER")),
                () -> assertThat(installations.get(0).getVersion(), equalTo(projectVersion)));
    }

    @Test
    void listInstallations_findsOwnInstallation() {
        setup.client().install();
        final List<InstallationsResponseInstallation> installations = setup.client().getInstallations();
        assertAll(() -> assertThat(installations, hasSize(1)), //
                () -> assertThat(installations.get(0).getName(),
                        equalTo(ExtensionManagerSetup.EXTENSION_SCHEMA_NAME + ".S3_FILES_ADAPTER")),
                () -> assertThat(installations.get(0).getVersion(), equalTo(projectVersion)));
    }

    @Test
    void getExtensionDetailsFailsForUnknownVersion() {
        setup.client().assertRequestFails(() -> setup.client().getExtensionDetails("unknownVersion"),
                equalTo("Version 'unknownVersion' not supported, can only use '" + projectVersion + "'."),
                equalTo(404));
    }

    @Test
    void getExtensionDetailsSuccess() {
        final ExtensionDetailsResponse extensionDetails = setup.client().getExtensionDetails(projectVersion);
        final List<ParamDefinition> parameters = extensionDetails.getParameterDefinitions();
        final ParamDefinition param1 = new ParamDefinition().id("virtualSchemaName")
                .name("Name of the new virtual schema").definition(Map.of("id", "virtualSchemaName", "name",
                        "Name of the new virtual schema", "required", true, "scope", "general", "type", "string"));
        assertAll(() -> assertThat(extensionDetails.getId(), equalTo("s3-vs-extension.js")),
                () -> assertThat(extensionDetails.getVersion(), equalTo(projectVersion)),
                () -> assertThat(parameters, hasSize(EXPECTED_PARAMETER_COUNT)),
                () -> assertThat(parameters.get(0), equalTo(param1)));
    }

    @Test
    void install_createsScripts() {
        setup.client().install();
        assertScriptsExist();
    }

    @Test
    void install_worksIfCalledTwice() {
        setup.client().install();
        setup.client().install();
        assertScriptsExist();
    }

    @Test
    void install_failsForUnsupportedVersion() {
        final ExtensionManagerClient client = setup.client();
        client.assertRequestFails(() -> client.install("unsupported"),
                equalTo("Installing version 'unsupported' not supported, try '" + projectVersion + "'."), equalTo(400));
        setup.exasolMetadata().assertNoScripts();
    }

    @Test
    void createInstanceFailsWithoutRequiredParameters() {
        final ExtensionManagerClient client = setup.client();
        client.install();
        client.assertRequestFails(() -> client.createInstance(List.of()), startsWith(
                "invalid parameters: Failed to validate parameter 'Name of the new virtual schema': This is a required parameter."),
                equalTo(400));
    }

    @Test
    void uninstall_failsForUnknownVersion() {
        setup.client().assertRequestFails(() -> setup.client().uninstall("unknownVersion"),
                equalTo("Uninstalling version 'unknownVersion' not supported, try '" + projectVersion + "'."),
                equalTo(400));
    }

    @Test
    void uninstall_succeedsForNonExistingInstallation() {
        assertDoesNotThrow(() -> setup.client().uninstall());
    }

    @Test
    void uninstall_removesAdapters() {
        setup.client().install();
        assertAll(() -> assertScriptsExist(), //
                () -> assertThat(setup.client().getInstallations(), hasSize(1)));
        setup.client().uninstall(projectVersion);
        assertAll(() -> assertThat(setup.client().getInstallations(), is(empty())),
                () -> setup.exasolMetadata().assertNoScripts());
    }

    @Test
    void upgradeFailsWhenNotInstalled() {
        setup.client().assertRequestFails(() -> setup.client().upgrade(),
                "extension is not installed, the following scripts are missing: S3_FILES_ADAPTER, IMPORT_FROM_S3_DOCUMENT_FILES",
                404);
    }

    @Test
    void upgradeFailsWhenAlreadyUpToDate() {
        setup.client().install();
        setup.client().assertRequestFails(() -> setup.client().upgrade(),
                "Extension is already installed in latest version " + projectVersion, 412);
    }

    @Test
    void upgradeFromPreviousVersion() throws InterruptedException, BucketAccessException, TimeoutException,
            FileNotFoundException, URISyntaxException {
        final String previousVersionExtensionId = setup.fetchExtension(getDownloadUrl(PREVIOUS_VERSION, EXTENSION_ID));
        preparePreviousVersionAdapter();
        setup.client().install(previousVersionExtensionId, PREVIOUS_VERSION);
        final String virtualTable = createVirtualSchema(previousVersionExtensionId, PREVIOUS_VERSION);
        verifyVirtualTableContainsData(virtualTable);
        assertInstalledVersion("EXA_EXTENSIONS.S3_FILES_ADAPTER", PREVIOUS_VERSION);
        upgrade();
        assertInstalledVersion("EXA_EXTENSIONS.S3_FILES_ADAPTER", projectVersion);
        verifyVirtualTableContainsData(virtualTable);
    }

    private URI getDownloadUrl(final String version, final String fileName) {
        final String project = "s3-document-files-virtual-schema";
        return URI.create(
                "https://extensions-internal.exasol.com/com.exasol/" + project + "/" + version + "/" + fileName);
    }

    private void upgrade() {
        final UpgradeExtensionResponse upgradeResult = setup.client().upgrade(EXTENSION_ID);
        assertEquals(new UpgradeExtensionResponse().previousVersion(PREVIOUS_VERSION).newVersion(projectVersion),
                upgradeResult);
    }

    private void preparePreviousVersionAdapter()
            throws URISyntaxException, FileNotFoundException, BucketAccessException, TimeoutException {
        final Path file = downloadToTemp(getDownloadUrl(PREVIOUS_VERSION, PREVIOUS_VERSION_JAR_FILE));
        exasolTestSetup.getDefaultBucket().uploadFile(file, PREVIOUS_VERSION_JAR_FILE);
    }

    private Path downloadToTemp(final URI url) {
        final HttpClient httpClient = HttpClient.newBuilder().followRedirects(Redirect.NORMAL).build();
        final HttpRequest request = HttpRequest.newBuilder(url).GET().build();
        try {
            final Path tempFile = Files.createTempFile("s3-adapter-", ".jar");
            final HttpResponse<Path> response = httpClient.send(request, BodyHandlers.ofFile(tempFile));
            final long fileSize = Files.size(tempFile);
            LOG.fine("Downloaded " + url + " with response status " + response.statusCode() + " to " + tempFile
                    + " with file size " + fileSize + " bytes");
            return tempFile;
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-EMIT-31")
                    .message("Failed to download {{url}} to temp file", url).toString(), exception);
        } catch (final InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    ExaError.messageBuilder("E-EMIT-32").message("Download of {{url}} was interrupted", url).toString(),
                    exception);
        }
    }

    private void assertInstalledVersion(final String expectedName, final String expectedVersion) {
        final List<InstallationsResponseInstallation> installations = setup.client().getInstallations();
        final InstallationsResponseInstallation expectedInstallation = new InstallationsResponseInstallation()
                .name(expectedName).version(expectedVersion);
        // The extension is installed twice (previous and current version), so each one returns the same installation.
        assertAll(() -> assertThat(installations, hasSize(2)),
                () -> assertThat(installations.get(0), equalTo(expectedInstallation)),
                () -> assertThat(installations.get(1), equalTo(expectedInstallation)));
    }

    @Test
    void virtualSchemaWorks() throws SQLException {
        setup.client().install();
        final String virtualTable = createVirtualSchema();
        verifyVirtualTableContainsData(virtualTable);
    }

    private String createVirtualSchema() {
        return createVirtualSchema(EXTENSION_ID, projectVersion);
    }

    private String createVirtualSchema(final String extensionId, final String extensionVersion) {
        final String prefix = "vs-works-test-" + System.currentTimeMillis() + "/";
        upload(prefix + "test-data-1.json", "{\"id\": 1, \"name\": \"abc\" }");
        upload(prefix + "test-data-2.json", "{\"id\": 2, \"name\": \"xyz\" }");
        final String virtualSchemaName = "MY_VS";
        createInstance(extensionId, extensionVersion, virtualSchemaName,
                getMappingDefinition(prefix + "test-data-*.json"));
        return virtualSchemaName + ".TEST";
    }

    private void verifyVirtualTableContainsData(final String virtualTable) {
        try (final ResultSet result = exasolTestSetup.createConnection().createStatement()
                .executeQuery("SELECT ID, NAME FROM " + virtualTable + " ORDER BY ID ASC")) {
            assertThat(result, table().row(1L, "abc").row(2L, "xyz").matches());
        } catch (final SQLException exception) {
            throw new AssertionError("Assertion query failed", exception);
        }
    }

    @Test
    void listingInstancesNoVSExists() throws SQLException {
        assertThat(setup.client().listInstances(), hasSize(0));
    }

    private void upload(final String key, final String content) {
        s3TestSetup.upload(s3BucketName, key, RequestBody.fromString(content));
    }

    @Test
    void listingInstancesIgnoresVersion() {
        setup.client().install();
        final String name = "my_virtual_SCHEMA";
        createInstance(name);
        assertThat(setup.client().listInstances("unknownVersion"),
                allOf(hasSize(1), equalTo(List.of(new Instance().id(name).name(name)))));
    }

    @Test
    void createInstanceCreatesDbObjects() {
        setup.client().install();
        final String name = "my_virtual_SCHEMA";
        createInstance(name);

        setup.exasolMetadata().assertConnection(table().row("MY_VIRTUAL_SCHEMA_CONNECTION",
                "Created by extension manager for S3 virtual schema my_virtual_SCHEMA").matches());
        setup.exasolMetadata()
                .assertVirtualSchema(table()
                        .row("my_virtual_SCHEMA", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString()))
                        .matches());
        assertThat(setup.client().listInstances(),
                allOf(hasSize(1), equalTo(List.of(new Instance().id(name).name(name)))));
    }

    @Test
    void createTwoInstances() {
        setup.client().install();
        createInstance("vs1");
        createInstance("vs2");

        setup.exasolMetadata()
                .assertConnection(table()
                        .row("VS1_CONNECTION", "Created by extension manager for S3 virtual schema vs1")
                        .row("VS2_CONNECTION", "Created by extension manager for S3 virtual schema vs2").matches());
        setup.exasolMetadata()
                .assertVirtualSchema(table()
                        .row("vs1", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString()))
                        .row("vs2", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString())).matches());

        assertThat(setup.client().listInstances(), allOf(hasSize(2),
                equalTo(List.of(new Instance().id("vs1").name("vs1"), new Instance().id("vs2").name("vs2")))));
    }

    @Test
    void createInstanceWithSingleQuote() {
        setup.client().install();
        createInstance("Quoted'schema");
        setup.exasolMetadata().assertConnection(table()
                .row("QUOTED'SCHEMA_CONNECTION", "Created by extension manager for S3 virtual schema Quoted'schema")
                .matches());
        setup.exasolMetadata().assertVirtualSchema(table()
                .row("Quoted'schema", "SYS", "EXA_EXTENSIONS.S3_FILES_ADAPTER", not(emptyOrNullString())).matches());
    }

    @Test
    void deleteNonExistingInstance() {
        assertDoesNotThrow(() -> setup.client().deleteInstance("no-such-instance"));
    }

    @Test
    void deleteFailsForUnknownVersion() {
        setup.client().assertRequestFails(() -> setup.client().deleteInstance("unknownVersion", "no-such-instance"),
                equalTo("Version 'unknownVersion' not supported, can only use '" + projectVersion + "'."),
                equalTo(404));
    }

    @Test
    void deleteExistingInstance() {
        setup.client().install();
        createInstance("vs1");
        final List<Instance> instances = setup.client().listInstances();
        assertThat(instances, hasSize(1));
        setup.client().deleteInstance(instances.get(0).getId());
        assertThat(setup.client().listInstances(), is(empty()));
        assertAll(() -> setup.exasolMetadata().assertNoConnections(),
                () -> setup.exasolMetadata().assertNoVirtualSchema());
    }

    private void createInstance(final String virtualSchemaName) {
        createInstance(virtualSchemaName, getMappingDefinition("test-data-*.json"));
    }

    private void createInstance(final String virtualSchemaName, final EdmlDefinition mapping) {
        createInstance(EXTENSION_ID, projectVersion, virtualSchemaName, mapping);
    }

    private void createInstance(final String extensionId, final String extensionVersion, final String virtualSchemaName,
            final EdmlDefinition mapping) {
        setup.addVirtualSchemaToCleanupQueue(virtualSchemaName);
        setup.addConnectionToCleanupQueue(virtualSchemaName.toUpperCase() + "_CONNECTION");
        final String instanceName = setup.client().createInstance(extensionId, extensionVersion,
                createValidParameters(virtualSchemaName, mapping));
        assertThat(instanceName, equalTo(virtualSchemaName));
    }

    private List<ParameterValue> createValidParameters(final String virtualSchemaName, final EdmlDefinition mapping) {
        final List<ParameterValue> parameters = new ArrayList<>(
                List.of(param("virtualSchemaName", virtualSchemaName), param("awsRegion", s3TestSetup.getRegion()), //
                        param("s3Bucket", s3BucketName), //
                        param("awsAccessKeyId", s3TestSetup.getUsername()), //
                        param("awsSecretAccessKey", s3TestSetup.getPassword()), //
                        param("mapping", new EdmlSerializer().serialize(mapping))));
        getInDatabaseS3Address().map(address -> param("awsEndpointOverride", address)) //
                .ifPresent(parameters::add);
        if (s3TestSetup.getMfaToken().isPresent()) {
            parameters.add(param("awsSessionToken", s3TestSetup.getMfaToken().get()));
        }
        return parameters;
    }

    private EdmlDefinition getMappingDefinition(final String source) {
        return EdmlDefinition.builder().source(source).destinationTable("TEST") //
                .mapping(Fields.builder() //
                        .mapField("id", ToDecimalMapping.builder().build()) //
                        .mapField("name", ToVarcharMapping.builder().varcharColumnSize(200).build()) //
                        .mapField("fieldWith'Quote", ToVarcharMapping.builder().varcharColumnSize(200).build()) //
                        .build())
                .build();
    }

    private Optional<String> getInDatabaseS3Address() {
        return s3TestSetup.getEntrypoint()
                .map(endpoint -> exasolTestSetup.makeTcpServiceAccessibleFromDatabase(endpoint))
                .map(InetSocketAddress::toString);
    }

    private ParameterValue param(final String name, final String value) {
        return new ParameterValue().name(name).value(value);
    }

    private void assertScriptsExist() {
        final String jarDirective = "%jar /buckets/bfsdefault/default/" + IntegrationTestSetup.ADAPTER_JAR + ";";
        final String comment = "Created by extension manager for S3 virtual schema extension " + projectVersion;
        setup.exasolMetadata()
                .assertScript(table()
                        .row("IMPORT_FROM_S3_DOCUMENT_FILES", "UDF", "SET", "EMITS",
                                allOf(containsString("%scriptclass " + UdfEntryPoint.class.getName() + ";"), //
                                        containsString(jarDirective)),
                                comment) //
                        .row("S3_FILES_ADAPTER", "ADAPTER", null, null,
                                allOf(containsString("%scriptclass " + RequestDispatcher.class.getName() + ";"), //
                                        containsString(jarDirective)),
                                comment) //
                        .matches());
    }

    private void createAdapter(final String adapterScriptName, final String importScriptName) {
        final String adapterJarBucketFsPath = "/buckets/bfsdefault/default/"
                + IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH.getFileName().toString();
        final ExasolSchema schema = setup.createExtensionSchema();
        schema.createAdapterScriptBuilder(adapterScriptName)
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", adapterJarBucketFsPath).language(Language.JAVA)
                .build();
        schema.createUdfBuilder(importScriptName).language(UdfScript.Language.JAVA).inputType(InputType.SET)
                .parameter(GenericUdfCallHandler.PARAMETER_DOCUMENT_FETCHER, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), adapterJarBucketFsPath).build();
    }
}
