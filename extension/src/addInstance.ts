import { Context, Instance, ParameterValues } from "@exasol/extension-manager-interface";
import { Parameter } from "@exasol/extension-manager-interface/dist/parameters";
import { ADAPTER_SCRIPT_NAME, EXTENSION_NAME, ExtensionInfo, convertSchemaNameToInstanceId, getConnectionName } from "./common";
import { ScopedParameter, getAllParameterDefinitions } from "./parameterDefinitions";


export function addInstance(context: Context, extensionInfo: ExtensionInfo, versionToInstall: string, paramValues: ParameterValues): Instance {
    if (extensionInfo.version !== versionToInstall) {
        throw new Error(`Version '${versionToInstall}' not supported, can only use ${extensionInfo.version}.`)
    }

    const allParams = getAllParameterDefinitions();
    const virtualSchemaName = getParameterValue(paramValues, allParams.virtualSchemaName);
    const mapping = getParameterValue(paramValues, allParams.mapping);
    const connectionName = getConnectionName(virtualSchemaName);
    context.sqlClient.execute(createConnectionStatement(connectionName, paramValues));
    context.sqlClient.execute(createVirtualSchemaStatement(virtualSchemaName, context.extensionSchemaName, connectionName, mapping));

    const comment = `Created by Extension Manager for ${EXTENSION_NAME} ${escapeSingleQuotes(virtualSchemaName)}`;
    context.sqlClient.execute(`COMMENT ON CONNECTION "${connectionName}" IS '${comment}'`);
    context.sqlClient.execute(`COMMENT ON SCHEMA "${virtualSchemaName}" IS '${comment}'`);
    return { id: convertSchemaNameToInstanceId(virtualSchemaName), name: virtualSchemaName }
}

function getParameterValue(paramValues: ParameterValues, definition: Parameter): string {
    for (const value of paramValues.values) {
        if (value.name === definition.id) {
            return value.value
        }
    }
    throw new Error(`Missing parameter "${definition.id}"`)
}

function createConnectionStatement(connectionName: string, paramValues: ParameterValues): string {
    let jsonArgs = JSON.stringify(convertConnectionParameters(paramValues))
    jsonArgs = escapeSingleQuotes(jsonArgs)
    return `CREATE OR REPLACE CONNECTION "${connectionName}" TO '' USER '' IDENTIFIED BY '${jsonArgs}'`;
}

function createVirtualSchemaStatement(name: string, adapterSchema: string, connectionName: string, mapping: string): string {
    const escapedMapping = escapeSingleQuotes(mapping)
    const escapedConnectionName = escapeSingleQuotes(connectionName)
    return `CREATE VIRTUAL SCHEMA "${name}" USING "${adapterSchema}"."${ADAPTER_SCRIPT_NAME}" WITH CONNECTION_NAME = '${escapedConnectionName}' MAPPING = '${escapedMapping}';`
}

function escapeSingleQuotes(value: string): string {
    return value.replace(/'/g, "''")
}

function findParam(id: string): ScopedParameter | undefined {
    return getAllParameterDefinitions()[id]
}

function convertConnectionParameters(paramValues: ParameterValues) {
    const result: { [key: string]: string | boolean } = {}
    for (const paramValue of paramValues.values) {
        const param = findParam(paramValue.name);
        if (param && param.scope == "connection") {
            result[param.id] = convertParamValue(paramValue.value, param)
        }
    }
    return result
}

function convertParamValue(value: string, definition: Parameter): string | boolean {
    switch (definition.type) {
        case "string":
        case "select":
            return value;
        case "boolean":
            return value === 'true';
        default:
            throw Error("Unsupported parameter type");
    }
}
