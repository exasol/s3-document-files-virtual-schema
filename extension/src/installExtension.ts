import { BadRequestError, Context } from "@exasol/extension-manager-interface";
import { ADAPTER_SCRIPT_NAME, ExtensionInfo, IMPORT_SCRIPT_NAME } from "./common";

export function installExtension(context: Context, extension: ExtensionInfo, versionToInstall: string): void {
    if (extension.version !== versionToInstall) {
        throw new BadRequestError(`Installing version '${versionToInstall}' not supported, try '${extension.version}'.`)
    }
    const jarPath = context.bucketFs.resolvePath(extension.fileName)

    context.sqlClient.execute(`
        CREATE OR REPLACE JAVA ADAPTER SCRIPT "${context.extensionSchemaName}"."${ADAPTER_SCRIPT_NAME}" AS
            %scriptclass com.exasol.adapter.RequestDispatcher;
            %jar ${jarPath};`)

    context.sqlClient.execute(`
        CREATE OR REPLACE JAVA SET SCRIPT "${context.extensionSchemaName}"."${IMPORT_SCRIPT_NAME}"
                (DATA_LOADER VARCHAR(2000000), SCHEMA_MAPPING_REQUEST VARCHAR(2000000), CONNECTION_NAME VARCHAR(500))
        EMITS(...) AS
            %scriptclass com.exasol.adapter.document.UdfEntryPoint;
            %jar ${jarPath};`)
    context.sqlClient.execute(`COMMENT ON SCRIPT "${context.extensionSchemaName}"."${IMPORT_SCRIPT_NAME}" IS 'Created by extension manager for S3 virtual schema extension ${extension.version}'`);
    context.sqlClient.execute(`COMMENT ON SCRIPT "${context.extensionSchemaName}"."${ADAPTER_SCRIPT_NAME}" IS 'Created by extension manager for S3 virtual schema extension ${extension.version}'`);
}