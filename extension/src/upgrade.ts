import { Context, NotFoundError, PreconditionFailedError, UpgradeResult } from "@exasol/extension-manager-interface";
import { AdapterScript } from "./adapterScript";
import { ADAPTER_SCRIPT_NAME, ExtensionInfo, IMPORT_SCRIPT_NAME } from "./common";
import { installExtension } from "./installExtension";

interface Scripts {
    adapterScript: AdapterScript
    importScript: AdapterScript
}

export function upgrade(context: Context, extensionInfo: ExtensionInfo): UpgradeResult {
    const scripts = getAdapterScripts(context)
    const previousVersion = getAdapterVersion(extensionInfo, scripts)
    const newVersion = extensionInfo.version
    installExtension(context, extensionInfo, newVersion)
    return { previousVersion, newVersion };
}

function getAdapterScripts(context: Context): Scripts {
    const adapterScript = context.metadata.getScriptByName(ADAPTER_SCRIPT_NAME)
    const importScript = context.metadata.getScriptByName(IMPORT_SCRIPT_NAME)
    if (!adapterScript || !importScript) {
        const missingScripts: string[] = []
        if (!adapterScript) {
            missingScripts.push(ADAPTER_SCRIPT_NAME)
        }
        if (!importScript) {
            missingScripts.push(IMPORT_SCRIPT_NAME)
        }
        throw new NotFoundError(`extension is not installed, the following scripts are missing: ${missingScripts.join(', ')}`)
    }
    return {
        adapterScript: new AdapterScript(adapterScript),
        importScript: new AdapterScript(importScript)
    }
}

function getAdapterVersion(extensionInfo: ExtensionInfo, scripts: Scripts): string {
    const { adapterScript, importScript } = scripts;
    const adapterVersion = adapterScript.getVersion();
    const importScriptVersion = importScript.getVersion();
    if (!adapterVersion) {
        throw new PreconditionFailedError(`Failed to extract version from adapter script ${adapterScript.qualifiedName}, script text: '${adapterScript.text}'`)
    } else if (!importScriptVersion) {
        throw new PreconditionFailedError(`Failed to extract version from import script ${importScript.qualifiedName}, script text: '${importScript.text}'`)
    } else if (adapterVersion !== importScriptVersion) {
        throw new PreconditionFailedError(`Scripts have inconsistent versions. ${adapterScript.qualifiedName}: ${adapterVersion}, ${importScript.qualifiedName}: ${importScriptVersion}`)
    } else if (adapterVersion === extensionInfo.version) {
        throw new PreconditionFailedError(`Extension is already installed in latest version ${extensionInfo.version}`)
    }
    return adapterVersion
}
