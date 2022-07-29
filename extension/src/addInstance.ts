import { Context, Instance, ParameterValues } from "@exasol/extension-manager-interface";
import { Parameter } from "@exasol/extension-manager-interface/dist/parameters";
import { ExtensionInfo } from "./common";

const allParams = {
    awsAccessKeyId: <Parameter>{ id: "awsAccessKeyId", name: "AWS Access Key Id", type: "string", required: true },
    awsSecretAccessKey: <Parameter>{ id: "awsSecretAccessKey", name: "AWS Secret AccessKey", type: "string", required: true, secret: true },
    awsRegion: <Parameter>{ id: "awsRegion", name: "AWS Region", type: "string", required: true },
    s3Bucket: <Parameter>{ id: "s3Bucket", name: "S3 Bucket", type: "string", required: true },
    awsSessionToken: <Parameter>{ id: "awsSessionToken", name: "AWS Session Token", type: "string", required: false, secret: true },
    awsEndpointOverride: <Parameter>{ id: "awsEndpointOverride", name: "AWS Endpoint Override", type: "string", required: false },
    s3PathStyleAccess: <Parameter>{ id: "s3PathStyleAccess", name: "S3 Path Style Access", type: "boolean", required: false },
    useSsl: <Parameter>{ id: "useSsl", name: "Use SSL", type: "boolean", required: false },
};
export function createInstanceParameters(): Parameter[] {
    return [
        allParams.awsAccessKeyId,
        allParams.awsSecretAccessKey,
        allParams.awsRegion,
        allParams.s3Bucket,
        allParams.awsSessionToken,
        allParams.awsEndpointOverride,
        allParams.s3PathStyleAccess,
        allParams.useSsl
    ];
}

export function addInstance(context: Context, extensionInfo: ExtensionInfo, versionToInstall: string, paramValues: ParameterValues): Instance {
    if (extensionInfo.version !== versionToInstall) {
        throw new Error(`Version '${versionToInstall}' not supported, can only use ${extensionInfo.version}.`)
    }
    const connectionName = "mys3conn"
    context.sqlClient.runQuery(createConnectionStatement(context.extensionSchemaName, connectionName, paramValues));
    return { name: "NEW_S3_VS" }
}

function createConnectionStatement(schemaName: string, connectionName: string, paramValues: ParameterValues): string {
    let jsonArgs = JSON.stringify(convertParamValues(paramValues))
    jsonArgs = escapeSingleQuotes(jsonArgs)
    return `CREATE CONNECTION "${schemaName}"."${connectionName}" TO '' USER '' IDENTIFIED BY '${jsonArgs}'`;
}

function escapeSingleQuotes(value: string): string {
    return value.replace(/'/g, "''")
}

function findParam(id: string): Parameter | undefined {
    return (<any>allParams)[id]
}

function convertParamValues(paramValues: ParameterValues): any {
    const result: any = {}
    for (const paramValue of paramValues.values) {
        const param = findParam(paramValue.name);
        if (param) {
            result[param.id] = convertParamValue(paramValue.value, param)
        } else {
            console.log(`WARN: Ignoring unexpected parameter "${paramValue.name}" with value "${paramValue.value}"`)
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
