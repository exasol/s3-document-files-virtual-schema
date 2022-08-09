import { Context, Instance, ParameterValues } from "@exasol/extension-manager-interface";
import { Parameter } from "@exasol/extension-manager-interface/dist/parameters";
import { ADAPTER_SCRIPT_NAME, ExtensionInfo } from "./common";

type ScopedParameter = Parameter & { scope: "general" | "connection" | "vs" }

const allParams: { [key: string]: ScopedParameter } = {
    virtualSchemaName: { scope: "general", id: "virtualSchemaName", name: "Name of the new virtual schema", type: "string", required: true },

    // Connection parameters
    awsAccessKeyId: { scope: "connection", id: "awsAccessKeyId", name: "AWS Access Key Id", type: "string", required: true },
    awsSecretAccessKey: { scope: "connection", id: "awsSecretAccessKey", name: "AWS Secret AccessKey", type: "string", required: true, secret: true },
    awsRegion: { scope: "connection", id: "awsRegion", name: "AWS Region", type: "string", required: true },
    s3Bucket: { scope: "connection", id: "s3Bucket", name: "S3 Bucket", type: "string", required: true },
    awsSessionToken: { scope: "connection", id: "awsSessionToken", name: "AWS Session Token", type: "string", required: false, secret: true },
    awsEndpointOverride: { scope: "connection", id: "awsEndpointOverride", name: "AWS Endpoint Override", type: "string", required: false },
    s3PathStyleAccess: { scope: "connection", id: "s3PathStyleAccess", name: "S3 Path Style Access", type: "boolean", required: false },
    useSsl: { scope: "connection", id: "useSsl", name: "Use SSL", type: "boolean", required: false },

    // Virtual Schema parameters
    mapping: { scope: "vs", id: "mapping", name: "EDML Mapping", type: "string", required: true, multiline: true },
};
export function createInstanceParameters(): Parameter[] {
    return [
        allParams.virtualSchemaName,
        allParams.awsAccessKeyId,
        allParams.awsSecretAccessKey,
        allParams.awsRegion,
        allParams.s3Bucket,
        allParams.awsSessionToken,
        allParams.awsEndpointOverride,
        allParams.s3PathStyleAccess,
        allParams.useSsl,
        allParams.mapping
    ];
}

export function addInstance(context: Context, extensionInfo: ExtensionInfo, versionToInstall: string, paramValues: ParameterValues): Instance {
    if (extensionInfo.version !== versionToInstall) {
        throw new Error(`Version '${versionToInstall}' not supported, can only use ${extensionInfo.version}.`)
    }

    const virtualSchemaName = getParameterValue(paramValues, allParams.virtualSchemaName)
    const mapping = getParameterValue(paramValues, allParams.mapping);
    const connectionName = `${virtualSchemaName}_CONNECTION`
    context.sqlClient.runQuery(createConnectionStatement(connectionName, paramValues));
    context.sqlClient.runQuery(createVirtualSchemaStatement(virtualSchemaName, context.extensionSchemaName, connectionName, mapping));

    const comment = `Created by extension manager for S3 virtual schema ${escapeSingleQuotes(virtualSchemaName)}`;
    context.sqlClient.runQuery(`COMMENT ON CONNECTION "${connectionName}" IS '${comment}'`);
    context.sqlClient.runQuery(`COMMENT ON SCHEMA "${virtualSchemaName}" IS '${comment}'`);
    context.sqlClient.runQuery("COMMIT");
    return { name: virtualSchemaName }
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
    return allParams[id]
}

function convertConnectionParameters(paramValues: ParameterValues): any {
    const result: any = {}
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
            throw Error(`Unsupported parameter type ${definition}`);
    }
}
