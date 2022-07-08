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
    function findInstallations(allScripts: ExaAllScriptsRow[]) {
      const metadata: ExaMetadata = {
        allScripts: { rows: allScripts },
        virtualSchemaProperties: { rows: [] },
        virtualSchemas: { rows: [] }
      }
      return createExtension().findInstallations(createMockSqlClient(), metadata)
    }

    it("returns empty list when no adapter script is available", () => {
      expect(findInstallations([])).toHaveLength(0)
    })

    describe("returns expected installations", () => {
      function installation({ name = "schema.name", version = CONFIG.version }: Partial<Installation>): Installation {
        return { name, version, instanceParameters: [] }
      }
      function script({ schema = "schema", name = "name", inputType, resultType, type = "", text = "", comment }: Partial<ExaAllScriptsRow>): ExaAllScriptsRow {
        return { schema, name, inputType, resultType, type, text, comment }
      }
      const tests: { name: string; scripts: ExaAllScriptsRow[], expected?: Installation }[] = [
        { name: "all default values", scripts: [script({})], expected: installation({}) },
        { name: "custom schema and name", scripts: [script({ schema: "MY_SCHEMA", name: "MY_NAME" })], expected: installation({ name: "MY_SCHEMA.MY_NAME" }) }
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

