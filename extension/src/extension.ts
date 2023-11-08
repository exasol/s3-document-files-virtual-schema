/* eslint-disable @typescript-eslint/no-unused-vars */
import {
    ExasolExtension,
    Parameter,
    SelectOption,
    registerExtension
} from "@exasol/extension-manager-interface";
import { ScriptDefinition, jarFileVersionExtractor } from "@exasol/extension-manager-interface/dist/base";
import { convertVirtualSchemaBaseExtension, createJsonConnectionDefinition, createVirtualSchemaBuilder } from "@exasol/extension-manager-interface/dist/base-vs";
import { ADAPTER_SCRIPT_NAME, EXTENSION_NAME, IMPORT_SCRIPT_NAME } from "./common";
import { CONFIG } from "./extension-config";

export function createExtension(): ExasolExtension {
    return convertVirtualSchemaBaseExtension({
        name: EXTENSION_NAME,
        description: "Virtual Schema for document files on AWS S3",
        category: "document-virtual-schema",
        version: CONFIG.version,
        file: { name: CONFIG.fileName, size: CONFIG.fileSizeBytes },
        scripts: getUdfScriptDefinitions(),
        virtualSchemaAdapterScript: ADAPTER_SCRIPT_NAME,
        scriptVersionExtractor: jarFileVersionExtractor(/document-files-virtual-schema-dist-[\d.]+-s3-(\d+\.\d+\.\d+).jar/),
        builder: createVirtualSchemaBuilder({
            connectionNameProperty: "CONNECTION_NAME",
            virtualSchemaParameters: getVirtualSchemaParameterDefinitions(),
            connectionDefinition: createJsonConnectionDefinition(getConnectionParameterDefinitions())
        })
    })
}

function getUdfScriptDefinitions(): ScriptDefinition[] {
    return [
        {
            name: ADAPTER_SCRIPT_NAME,
            type: "ADAPTER",
            scriptClass: "com.exasol.adapter.RequestDispatcher"
        },
        {
            name: IMPORT_SCRIPT_NAME,
            type: "SET",
            parameters: "DATA_LOADER VARCHAR(2000000), SCHEMA_MAPPING_REQUEST VARCHAR(2000000), CONNECTION_NAME VARCHAR(500)",
            emitParameters: "...",
            scriptClass: "com.exasol.adapter.document.UdfEntryPoint"
        }
    ]
}

function getConnectionParameterDefinitions(): Parameter[] {
    return [
        { id: "awsAccessKeyId", name: "AWS Access Key Id", type: "string", required: true },
        { id: "awsSecretAccessKey", name: "AWS Secret AccessKey", type: "string", required: true, secret: true },
        { id: "awsSessionToken", name: "AWS Session Token", type: "string", required: false, secret: true },
        { id: "awsRegion", name: "AWS Region", type: "string", required: true, placeholder: "eu-central-1" },
        { id: "s3Bucket", name: "S3 Bucket", type: "string", required: true },
        { id: "awsEndpointOverride", name: "AWS Endpoint Override", type: "string", required: false, placeholder: "s3.example.com:9000" },
        { id: "s3PathStyleAccess", name: "S3 Path Style Access", type: "boolean", required: false },
        { id: "useSsl", name: "Use SSL", type: "boolean", required: false, default: "true" },
    ];
}

function getVirtualSchemaParameterDefinitions(): Parameter[] {
    return [
        {
            id: "MAPPING", name: "EDML Mapping", type: "string",
            description: "See documentation at https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md",
            placeholder: `{
                  "$schema": "https://schemas.exasol.com/edml-1.5.0.json",
                  "source": "path/to/books.csv",
                  "destinationTable": "BOOKS",
                  "description": "Example mapping",
                  "mapping": {
                    "fields": { }
                }`, required: true, multiline: true
        }, {
            id: "MAX_PARALLEL_UDFS", name: "Max. number of parallel UDFs", type: "string", description: "Maximum number of UDFs that are executed in parallel. -1 represents unlimited.",
            required: false, placeholder: "-1", default: "-1"
        }, {
            id: "DEBUG_ADDRESS", name: "Debug address", description: "Network address and port to which to send debug output",
            type: "string", required: false, placeholder: "192.168.179.38:3000", default: ""
        }, {
            id: "LOG_LEVEL", name: "Log level", description: "Log level for debug output. Debug address must be defined for this to work.",
            type: "select", required: false, options: getJavaLogLevelOptions(), default: ""
        },
    ];
}

function getJavaLogLevelOptions(): SelectOption[] {
    const javaLogLevels: string[] = ["OFF", "SEVERE", "WARNING", "INFO", "CONFIG", "FINE", "FINER", "FINEST"];
    return javaLogLevels.map(level => { return { id: level, name: level } })
}

registerExtension(createExtension())
