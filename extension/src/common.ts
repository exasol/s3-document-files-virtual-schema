import { Context } from "@exasol/extension-manager-interface";

export const ADAPTER_SCRIPT_NAME = "S3_FILES_ADAPTER";
export const IMPORT_SCRIPT_NAME = "IMPORT_FROM_S3_DOCUMENT_FILES";

export interface LocalContext {
    version: string;
    fileName: string;
}

export interface CombinedContext {
    local: LocalContext;
    global: Context;
}