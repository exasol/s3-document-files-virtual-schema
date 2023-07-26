import { Context, NotFoundError, UpgradeResult } from "@exasol/extension-manager-interface";
import { ADAPTER_SCRIPT_NAME, IMPORT_SCRIPT_NAME } from "./common";

export function upgrade(context: Context): UpgradeResult {
    const scripts = getAdapterScripts(context)


    return { previousVersion: "y", newVersion: "x" };
}

function getAdapterScripts(context: Context) {
    const missingScripts = []
    const adapterScript = context.metadata.getScriptByName(ADAPTER_SCRIPT_NAME)
    const importScript = context.metadata.getScriptByName(IMPORT_SCRIPT_NAME)
    if (!adapterScript) {
        missingScripts.push(ADAPTER_SCRIPT_NAME)
    }
    if (!importScript) {
        missingScripts.push(IMPORT_SCRIPT_NAME)
    }
    if (missingScripts.length > 0) {
        throw new NotFoundError(`extension is not installed, the following scripts are missing: ${missingScripts.join(', ')}`)
    }
    return { adapterScript, importScript }
}