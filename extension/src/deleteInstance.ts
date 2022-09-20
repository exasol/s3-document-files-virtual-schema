import { Context, NotFoundError } from "@exasol/extension-manager-interface";
import { convertInstanceIdToSchemaName, ExtensionInfo, getConnectionName } from "./common";

export function deleteInstance(context: Context, extension: ExtensionInfo, version: string, instanceId: string): void {
    if (extension.version !== version) {
        throw new NotFoundError(`Version '${version}' not supported, can only use '${extension.version}'.`)
    }
    const schemaName = convertInstanceIdToSchemaName(instanceId);
    context.sqlClient.execute(dropVirtualSchemaStatement(schemaName));
    context.sqlClient.execute(dropConnectionStatement(getConnectionName(schemaName)));
}

function dropVirtualSchemaStatement(schemaName: string): string {
    return `DROP VIRTUAL SCHEMA IF EXISTS "${schemaName}" CASCADE`;
}

function dropConnectionStatement(connectionName: string): string {
    return `DROP CONNECTION IF EXISTS "${connectionName}"`;
}