import { BadRequestError, Context } from "@exasol/extension-manager-interface";
import { ADAPTER_SCRIPT_NAME, ExtensionInfo, IMPORT_SCRIPT_NAME } from "./common";

export function uninstall(context: Context, extension: ExtensionInfo, versionToUninstall: string): void {
    if (extension.version !== versionToUninstall) {
        throw new BadRequestError(`Uninstalling version '${versionToUninstall}' not supported, try '${extension.version}'.`)
    }

    function extensionSchemaExists(): boolean {
        const result = context.sqlClient.query("SELECT 1 FROM SYS.EXA_ALL_SCHEMAS WHERE SCHEMA_NAME=?", context.extensionSchemaName)
        return result.rows.length > 0
    }

    if (extensionSchemaExists()) { // Drop commands fail when schema does not exist.
        context.sqlClient.execute(`DROP ADAPTER SCRIPT "${context.extensionSchemaName}"."${ADAPTER_SCRIPT_NAME}"`)
        context.sqlClient.execute(`DROP SCRIPT "${context.extensionSchemaName}"."${IMPORT_SCRIPT_NAME}"`)
    }
}
