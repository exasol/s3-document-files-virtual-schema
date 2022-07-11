import { ExaMetadata, Installation, SqlClient } from '@exasol/extension-manager-interface';
import { ExaAllScriptsRow } from '@exasol/extension-manager-interface/dist/exasolSchema';
import { describe, expect, it } from '@jest/globals';
import { createExtension } from "./extension";
import { CONFIG } from './extension-config';

function getInstalledExtension(): any {
  return (global as any).installedExtension
}

function createMockSqlClient(): SqlClient {
  return {
    runQuery(_query) {
      // ignore
    },
  }
}

describe("S3 VS Extension", () => {

  it("creates an extension", () => {
    const ext = createExtension();
    expect(ext).not.toBeNull()
  })

  it("creates a new object for every call", () => {
    const ext1 = createExtension();
    const ext2 = createExtension();
    expect(ext1).not.toBe(ext2)
  })

  it("registers when loaded", () => {
    const installedExtension = getInstalledExtension();
    expect(installedExtension.extension).not.toBeNull()
    expect(typeof installedExtension.apiVersion).toBe('string');
    expect(installedExtension.apiVersion).not.toBe('');
  })

  describe("findInstallations()", () => {
    function findInstallations(allScripts: ExaAllScriptsRow[]): Installation[] {
      const metadata: ExaMetadata = {
        allScripts: { rows: allScripts },
        virtualSchemaProperties: { rows: [] },
        virtualSchemas: { rows: [] }
      }
      const installations = createExtension().findInstallations(createMockSqlClient(), metadata)
      expect(installations).toBeDefined()
      return installations
    }

    it("returns empty list when no adapter script is available", () => {
      expect(findInstallations([])).toHaveLength(0)
    })

    describe("returns expected installations", () => {
      function installation({ name = "schema.S3_FILES_ADAPTER", version = CONFIG.version }: Partial<Installation>): Installation {
        return { name, version, instanceParameters: [] }
      }
      function script({ schema = "schema", name = "name", inputType, resultType, type = "", text = "", comment }: Partial<ExaAllScriptsRow>): ExaAllScriptsRow {
        return { schema, name, inputType, resultType, type, text, comment }
      }
      function adapterScript({ name = "S3_FILES_ADAPTER", type = "ADAPTER" }: Partial<ExaAllScriptsRow>): ExaAllScriptsRow {
        return script({ name, type })
      }
      function importScript({ name = "IMPORT_FROM_S3_DOCUMENT_FILES", type = "UDF", inputType = "SET", resultType = "EMITS" }: Partial<ExaAllScriptsRow>): ExaAllScriptsRow {
        return script({ name, type, inputType, resultType })
      }
      const tests: { name: string; scripts: ExaAllScriptsRow[], expected?: Installation }[] = [
        { name: "all values match", scripts: [adapterScript({}), importScript({})], expected: installation({}) },
        { name: "adapter has wrong type", scripts: [adapterScript({ type: "wrong" }), importScript({})], expected: undefined },
        { name: "adapter has wrong name", scripts: [adapterScript({ name: "wrong" }), importScript({})], expected: undefined },
        { name: "adapter missing", scripts: [importScript({})], expected: undefined },
        { name: "importer has wrong type", scripts: [adapterScript({}), importScript({ type: "wrong" })], expected: undefined },
        { name: "importer has wrong input type", scripts: [adapterScript({}), importScript({ inputType: "wrong" })], expected: undefined },
        { name: "importer has wrong result type", scripts: [adapterScript({}), importScript({ resultType: "wrong" })], expected: undefined },
        { name: "importer has wrong name", scripts: [adapterScript({}), importScript({ name: "wrong" })], expected: undefined },
        { name: "importer missing", scripts: [adapterScript({})], expected: undefined },
        { name: "adapter and importer missing", scripts: [], expected: undefined },
      ]
      tests.forEach(test => {
        it(test.name, () => {
          const actual = findInstallations(test.scripts)
          if (test.expected) {
            expect(actual).toHaveLength(1)
            expect(actual[0]).toStrictEqual(test.expected)
          } else {
            expect(actual).toHaveLength(0)
          }
        })
      });
    })
  })
})

