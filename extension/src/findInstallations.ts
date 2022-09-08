import { Installation } from "@exasol/extension-manager-interface";
import { ExaAllScriptsRow } from "@exasol/extension-manager-interface/dist/exasolSchema";
import { createInstanceParameters } from "./addInstance";
import { ADAPTER_SCRIPT_NAME, IMPORT_SCRIPT_NAME } from "./common";

function findScriptByName(scripts: ExaAllScriptsRow[], name: string): ExaAllScriptsRow | undefined {
    return scripts.find(script => script.name === name);
}

export function findInstallations(scripts: ExaAllScriptsRow[]): Installation[] {
    const importScript = findScriptByName(scripts, IMPORT_SCRIPT_NAME);
    const adapterScript = findScriptByName(scripts, ADAPTER_SCRIPT_NAME);
    if (!importScript && !adapterScript) {
        return [];
    }
    if (!importScript || !adapterScript) {
        console.log(`Either import script or adapter script not found`);
        return [];
    }
    if (!isValidImportScript(importScript) || !isValidAdapterScript(adapterScript)) {
        return [];
    }
    return [{
        name: `${adapterScript.schema}.${adapterScript.name}`,
        version: extractVersion(adapterScript.text),
        instanceParameters: createInstanceParameters()
    }];
}

const unknownVersion = "(unknown)"
const adapterScriptFileNamePattern = /.*%jar\s+[\w-/]+\/([^/]+.jar)\s*;.*/
const jarNameVersionPattern = /document-files-virtual-schema-dist-[\d.]+-s3-(\d+\.\d+\.\d+).jar/

function extractVersion(adapterScriptText: string): string {
    const jarNameMatch = adapterScriptFileNamePattern.exec(adapterScriptText)
    if (!jarNameMatch) {
        console.log(`WARN: Could not find jar filename in adapter script "${adapterScriptText}"`)
        return unknownVersion
    }
    const jarFileName = jarNameMatch[1];
    const versionMatch = jarNameVersionPattern.exec(jarFileName)
    if (!versionMatch) {
        console.log(`WARN: Could not find version in jar file name "${jarFileName}"`)
        return unknownVersion
    }
    return versionMatch[1]
}

function isValidAdapterScript(script: ExaAllScriptsRow): script is ExaAllScriptsRow {
    if (script.type !== "ADAPTER") {
        console.log(`Invalid type for adapter script: ${script.type}`)
        return false;
    }
    return true;
}

function isValidImportScript(script: ExaAllScriptsRow): script is ExaAllScriptsRow {
    if (script.type !== "UDF") {
        console.log(`Invalid type for importer script: ${script.type}`)
        return false;
    }
    if (script.inputType !== "SET") {
        console.log(`Invalid input type for importer script: ${script.inputType ? script.inputType : '<undefined>'}`)
        return false;
    }
    if (script.resultType !== "EMITS") {
        console.log(`Invalid result type for importer script: ${script.resultType ? script.resultType : '<undefined>'}`)
        return false;
    }
    return true;
}

