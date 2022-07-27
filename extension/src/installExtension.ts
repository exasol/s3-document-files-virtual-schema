import { ADAPTER_SCRIPT_NAME, CombinedContext, IMPORT_SCRIPT_NAME } from "./common";

export function installExtension(context: CombinedContext, versionToInstall: string): void {
    if (context.local.version !== versionToInstall) {
        throw new Error(`Installing version '${versionToInstall}' not supported, try '${context.local.version}'.`)
    }
    const jarPath = context.global.bucketFs.resolvePath(context.local.fileName)

    context.global.sqlClient.runQuery(`
        CREATE OR REPLACE JAVA ADAPTER SCRIPT "${context.global.extensionSchemaName}"."${ADAPTER_SCRIPT_NAME}" AS
            %scriptclass com.exasol.adapter.RequestDispatcher;
            %jar ${jarPath};`)

    context.global.sqlClient.runQuery(`
        CREATE OR REPLACE JAVA SET SCRIPT "${context.global.extensionSchemaName}"."${IMPORT_SCRIPT_NAME}"
                (DATA_LOADER VARCHAR(2000000), SCHEMA_MAPPING_REQUEST VARCHAR(2000000), CONNECTION_NAME VARCHAR(500))
        EMITS(...) AS
            %scriptclass com.exasol.adapter.document.UdfEntryPoint;
            %jar ${jarPath};`)
    context.global.sqlClient.runQuery("COMMIT")
}