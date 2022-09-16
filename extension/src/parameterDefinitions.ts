import { Parameter } from "@exasol/extension-manager-interface";

export type ScopedParameter = Parameter & { scope: "general" | "connection" | "vs" }
export type ScopedParameters = { [key: string]: ScopedParameter }

const allParams: ScopedParameters = {
    virtualSchemaName: { scope: "general", id: "virtualSchemaName", name: "Name of the new virtual schema", type: "string", required: true },

    // Connection parameters
    awsAccessKeyId: { scope: "connection", id: "awsAccessKeyId", name: "AWS Access Key Id", type: "string", required: true, regex: ".*" },
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

export function getAllParameterDefinitions(): ScopedParameters {
    return allParams;
}

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