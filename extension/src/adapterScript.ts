import { ExaScriptsRow } from "@exasol/extension-manager-interface";

export class AdapterScript {
    #script: ExaScriptsRow
    constructor(script: ExaScriptsRow) {
        this.#script = script
    }
    isValidAdapterScript() {
        return isValidAdapterScript(this.#script)
    }
    isValidImportScript() {
        return isValidImportScript(this.#script)
    }
    getVersion() {
        return extractVersion(this.#script.text)
    }
    get name() {
        return this.#script.name
    }
    get qualifiedName() {
        return `${this.#script.schema}.${this.#script.name}`
    }
    get schema() {
        return this.#script.schema
    }
    get text() {
        return this.#script.text
    }
}


const adapterScriptFileNamePattern = /.*%jar\s+[\w-/]+\/([^/]+.jar)\s*;.*/
const jarNameVersionPattern = /document-files-virtual-schema-dist-[\d.]+-s3-(\d+\.\d+\.\d+).jar/

function extractVersion(adapterScriptText: string): string | undefined {
    const jarNameMatch = adapterScriptFileNamePattern.exec(adapterScriptText)
    if (!jarNameMatch) {
        console.warn(`WARN: Could not find jar filename in adapter script "${adapterScriptText}"`)
        return undefined
    }
    const jarFileName = jarNameMatch[1];
    const versionMatch = jarNameVersionPattern.exec(jarFileName)
    if (!versionMatch) {
        console.warn(`WARN: Could not find version in jar file name "${jarFileName}"`)
        return undefined
    }
    return versionMatch[1]
}

function isValidAdapterScript(script: ExaScriptsRow): script is ExaScriptsRow {
    if (script.type !== "ADAPTER") {
        console.warn(`Invalid type for adapter script: ${script.type}`)
        return false;
    }
    return true;
}

function isValidImportScript(script: ExaScriptsRow): script is ExaScriptsRow {
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
