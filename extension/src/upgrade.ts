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
    verifyVersionOutdated(extensionInfo, scripts)
    const previousVersion = scripts.adapterScript.getVersion()
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

function verifyVersionOutdated(extensionInfo: ExtensionInfo, scripts: Scripts) {
    if (scripts.adapterScript.getVersion() !== scripts.importScript.getVersion()) {
        throw new PreconditionFailedError(`Scripts have inconsistent versions. ${scripts.adapterScript.name}: ${scripts.adapterScript.getVersion()}, ${scripts.importScript.name}: ${scripts.importScript.getVersion()}`)
    } else if (scripts.adapterScript.getVersion() === extensionInfo.version) {
        throw new PreconditionFailedError(`Extension is already installed in latest version ${extensionInfo.version}`)
    }
}
