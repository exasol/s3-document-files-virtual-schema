import {
    ExaMetadata,
    ExasolExtension,
    Installation,
    Instance, ParameterValues,
    registerExtension,
    SqlClient
} from "@exasol/extension-manager-interface";
import { ExaAllScriptsRow } from "@exasol/extension-manager-interface/dist/exasolSchema";
import { CONFIG } from "./extension-config";


export function createExtension(): ExasolExtension {
    const version = CONFIG.version;
    const filename = CONFIG.fileName;
    const fileSize = CONFIG.fileSizeBytes;
    const repoBaseUrl = "https://github.com/exasol/s3-document-files-virtual-schema"
    const downloadUrl = `${repoBaseUrl}/releases/download/${version}/${filename}`;
    function scriptMatches(_script: ExaAllScriptsRow): boolean {
        return true;
    }
    return {
        name: "S3 Virtual Schema",
        description: "Virtual Schema for document files on AWS S3",
        installableVersions: [version],
        bucketFsUploads: [{ bucketFsFilename: filename, downloadUrl, fileSize, name: "S3 VS Jar file", licenseUrl: `${repoBaseUrl}/blob/main/LICENSE`, licenseAgreementRequired: false }],
        install(sqlClient: SqlClient) {
            sqlClient.runQuery("CREATE ADAPTER SCRIPT ...")
        },
        addInstance(_installation: Installation, _params: ParameterValues, _sql: SqlClient): Instance {
            return undefined;
        },
        findInstallations(_sqlClient: SqlClient, metadata: ExaMetadata): Installation[] {
            const scripts = metadata.allScripts.rows.filter(scriptMatches)
            return scripts.map(script => { return { name: `${script.schema}.${script.name}`, version: version, instanceParameters: [] } })
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
