package com.exasol.adapter.document.files.extension;

import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

import org.junit.jupiter.api.BeforeAll;

import com.exasol.adapter.RequestDispatcher;
import com.exasol.adapter.document.GenericUdfCallHandler;
import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.adapter.document.files.IntegrationTestSetup;
import com.exasol.adapter.document.files.s3testsetup.AwsS3TestSetup;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.exasol.AdapterScript.Language;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript.InputType;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.ExasolTestSetupFactory;
import com.exasol.extensionmanager.client.model.ParameterValue;
import com.exasol.extensionmanager.itest.ExasolVersionCheck;
import com.exasol.extensionmanager.itest.ExtensionManagerSetup;
import com.exasol.extensionmanager.itest.base.AbstractExtensionIT;
import com.exasol.extensionmanager.itest.base.ExtensionITConfig;
import com.exasol.extensionmanager.itest.builder.ExtensionBuilder;
import com.exasol.mavenprojectversiongetter.MavenProjectVersionGetter;

class NewExtensionIT extends AbstractExtensionIT {
    private static final Path EXTENSION_SOURCE_DIR = Paths.get("extension").toAbsolutePath();
    private static final String PROJECT_VERSION = MavenProjectVersionGetter.getCurrentProjectVersion();

    private static final String EXTENSION_ID = "s3-vs-extension.js";
    private static ExasolTestSetup exasolTestSetup;
    private static ExtensionManagerSetup setup;
    private static AwsS3TestSetup s3TestSetup;
    private static String s3BucketName;

    @Override
    protected ExtensionITConfig createConfig() {
        return ExtensionITConfig.builder().projectName("s3-document-files-virtual-schema") //
                .extensionId(EXTENSION_ID) //
                .currentVersion(PROJECT_VERSION) //
                .expectedParameterCount(13) //
                .extensionName("S3 Virtual Schema") //
                .extensionDescription("Virtual Schema for document files on AWS S3") //
                .previousVersion("2.8.2") //
                .previousVersionJarFile("document-files-virtual-schema-dist-7.3.6-s3-2.8.2.jar").build();
    }

    @BeforeAll
    static void setup() throws FileNotFoundException, BucketAccessException, TimeoutException {
        if (System.getProperty("com.exasol.dockerdb.image") == null) {
            System.setProperty("com.exasol.dockerdb.image", "8.23.0");
        }
        exasolTestSetup = new ExasolTestSetupFactory(IntegrationTestSetup.CLOUD_SETUP_CONFIG).getTestSetup();
        ExasolVersionCheck.assumeExasolVersion8(exasolTestSetup);
        setup = ExtensionManagerSetup.create(exasolTestSetup, ExtensionBuilder.createDefaultNpmBuilder(
                EXTENSION_SOURCE_DIR, EXTENSION_SOURCE_DIR.resolve("dist").resolve(EXTENSION_ID)));
        s3TestSetup = new AwsS3TestSetup();
        s3BucketName = "extension-test.s3.virtual-schema-test-bucket-" + System.currentTimeMillis();
        s3TestSetup.createBucket(s3BucketName);
        exasolTestSetup.getDefaultBucket().uploadFile(IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH,
                IntegrationTestSetup.ADAPTER_JAR);
    }

    @Override
    protected ExtensionManagerSetup getSetup() {
        return setup;
    }

    @Override
    protected void createScripts() {
        final String adapterJarBucketFsPath = "/buckets/bfsdefault/default/"
                + IntegrationTestSetup.ADAPTER_JAR_LOCAL_PATH.getFileName().toString();
        final ExasolSchema schema = setup.createExtensionSchema();
        schema.createAdapterScriptBuilder("S3_FILES_ADAPTER")
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", adapterJarBucketFsPath).language(Language.JAVA)
                .build();
        schema.createUdfBuilder("IMPORT_FROM_S3_DOCUMENT_FILES").language(UdfScript.Language.JAVA)
                .inputType(InputType.SET)
                .parameter(GenericUdfCallHandler.PARAMETER_DOCUMENT_FETCHER, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(GenericUdfCallHandler.PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), adapterJarBucketFsPath).build();
    }

    @Override
    protected void assertScriptsExist() {
        final String jarDirective = "%jar /buckets/bfsdefault/default/" + IntegrationTestSetup.ADAPTER_JAR + ";";
        final String comment = "Created by Extension Manager for S3 Virtual Schema " + PROJECT_VERSION;
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

    @Override
    protected void prepareInstance() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'prepareInstance'");
    }

    @Override
    protected void verifyVirtualTableContainsData(final String virtualSchemaName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyVirtualTableContainsData'");
    }

    @Override
    protected Collection<ParameterValue> createValidParameterValues() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createValidParameterValues'");
    }
}
