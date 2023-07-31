import { ExaScriptsRow, Installation } from "@exasol/extension-manager-interface";
import { AdapterScript } from "./adapterScript";
import { ADAPTER_SCRIPT_NAME, IMPORT_SCRIPT_NAME } from "./common";



function findScriptByName(scripts: ExaScriptsRow[], name: string): AdapterScript | undefined {
    return scripts.map(script => new AdapterScript(script)).find(script => script.name === name);
}

export function findInstallations(scripts: ExaScriptsRow[]): Installation[] {
    const importScript = findScriptByName(scripts, IMPORT_SCRIPT_NAME);
    const adapterScript = findScriptByName(scripts, ADAPTER_SCRIPT_NAME);
    if (!importScript && !adapterScript) {
        return [];
    }
    if (!importScript || !adapterScript) {
        console.log(`Either import script or adapter script not found`);
        return [];
    }
    if (!importScript.isValidImportScript() || !adapterScript.isValidAdapterScript()) {
        return [];
    }

    return [{
        name: `${adapterScript.schema}.${adapterScript.name}`,
        version: adapterScript.getVersion() ?? "(unknown)"
    }];
}

