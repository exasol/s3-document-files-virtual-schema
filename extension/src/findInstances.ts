import { ExaMetadata, Instance } from "@exasol/extension-manager-interface";

export function findInstances(metadata: ExaMetadata): Instance[] {
    return metadata.virtualSchemas.rows
        .map(row => { return { id: row.name, name: row.name } })
}