import {
    ExaAllScripts,
    ExasolExtension,
    Installation,
    Instance, ParameterValues,
    registerExtension,
    SqlClient
} from "@exasol/extension-manager-interface";

export function createExtension(): ExasolExtension {
    const version = "2.2.0";
    const documentFilesVsVersion = "7.0.2";
    const bucketFsFilename = "document-files-virtual-schema-dist-" + documentFilesVsVersion + "-s3-" + version + ".jar";
    const fileSize = 123;
    const downloadUrl = "https://github.com/exasol/s3-document-files-virtual-schema/releases/download/" + version + "/" + bucketFsFilename;
    return {
        name: "S3 Document Files Virtual Schema",
        description: "Virtual Schema for document files on AWS S3",
        installableVersions: [version],
        bucketFsUploads: [{ bucketFsFilename, downloadUrl, fileSize, name: "S3 VS Jar file", licenseUrl: "", licenseAgreementRequired: false }],
        install(sqlClient) {
            sqlClient.runQuery("CREATE ADAPTER SCRIPT ...")
        },
        addInstance(_installation: Installation, _params: ParameterValues, _sql: SqlClient): Instance {
            return undefined;
        },
        findInstallations(_sqlClient: SqlClient, _exaAllScripts: ExaAllScripts): Installation[] {
            return [];
        },
        findInstances(_installation: Installation, _sql: SqlClient): Instance[] {
            return [];
        },
        uninstall(_installation: Installation, _sql: SqlClient): void {
            //empty on purpose
        },
        deleteInstance(_instance: Instance): void {
            //empty on purpose
        },
        readInstanceParameters(_installation: Installation, _instance: Instance, _sqlClient: SqlClient): ParameterValues {
            return undefined;
        }
    }
}

registerExtension(createExtension())
