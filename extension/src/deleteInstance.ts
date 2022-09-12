import { Context } from "@exasol/extension-manager-interface";
import { convertInstanceIdToSchemaName, getConnectionName } from "./common";

export function deleteInstance(context: Context, instanceId: string): void {
    const schemaName = convertInstanceIdToSchemaName(instanceId);
    context.sqlClient.execute(dropVirtualSchemaStatement(schemaName));
    const connectionName = getConnectionName(schemaName);
    context.sqlClient.execute(dropConnectionStatement(connectionName));
}

function dropVirtualSchemaStatement(schemaName: string): string {
    return `DROP VIRTUAL SCHEMA IF EXISTS "${schemaName}" CASCADE`;
}

function dropConnectionStatement(connectionName: string): string {
    return `DROP CONNECTION IF EXISTS "${connectionName}"`;
}