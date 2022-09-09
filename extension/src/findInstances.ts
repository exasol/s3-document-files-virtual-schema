import { Context, Instance } from "@exasol/extension-manager-interface";
import { ADAPTER_SCRIPT_NAME } from "./common";

export function findInstances(context: Context): Instance[] {
    const result = context.sqlClient.query("SELECT SCHEMA_NAME FROM SYS.EXA_ALL_VIRTUAL_SCHEMAS "
        + " WHERE ADAPTER_SCRIPT = ?||'.'||? "
        + " ORDER BY SCHEMA_NAME", context.extensionSchemaName, ADAPTER_SCRIPT_NAME)
    return result.rows.map(row => {
        const schemaName = row[0];
        return { id: schemaName, name: schemaName }
    })
}