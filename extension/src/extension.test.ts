import { ExaMetadata, Installation } from '@exasol/extension-manager-interface';
import { ExaAllScriptsRow } from '@exasol/extension-manager-interface/dist/exasolSchema';
import { describe, expect, it } from '@jest/globals';
import * as jestMock from "jest-mock";
import { createExtension } from "./extension";
import { CONFIG } from './extension-config';

const EXTENSION_SCHEMA_NAME = "ext-schema"

function getInstalledExtension(): any {
  return (global as any).installedExtension
}

function createMockContext() {
  const runQuery = jestMock.fn()
  return {
    extensionSchemaName: EXTENSION_SCHEMA_NAME,
    sqlClient: {
      runQuery: runQuery
    },
    bucketFs: {
      resolvePath(fileName: string) {
        return "/bucketfs/" + fileName;
      },
    },
    runQueryMock: runQuery.mock
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
      const installations = createExtension().findInstallations(createMockContext(), metadata)
      expect(installations).toBeDefined()
      return installations
    }

    it("returns empty list when no adapter script is available", () => {
      expect(findInstallations([])).toHaveLength(0)
    })

    describe("returns expected installations", () => {
      function installation({ name = "schema.S3_FILES_ADAPTER", version = "(unknown)" }: Partial<Installation>): Installation {
        return { name, version, instanceParameters: [] }
      }
      function script({ schema = "schema", name = "name", inputType, resultType, type = "", text = "", comment }: Partial<ExaAllScriptsRow>): ExaAllScriptsRow {
        return { schema, name, inputType, resultType, type, text, comment }
      }
      function adapterScript({ name = "S3_FILES_ADAPTER", type = "ADAPTER", text = "adapter script" }: Partial<ExaAllScriptsRow>): ExaAllScriptsRow {
        return script({ name, type, text })
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
        { name: "version found in filename", scripts: [adapterScript({ text: "CREATE ... %jar /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar; more text" }), importScript({})], expected: installation({ version: "1.2.3" }) },
        { name: "version not found in filename", scripts: [adapterScript({ text: "CREATE ... %jar /path/to/invalid-file-name-dist-0.0.0-s3-1.2.3.jar;" }), importScript({})], expected: installation({ version: "(unknown)" }) },
        { name: "filename not found in script", scripts: [adapterScript({ text: "CREATE ... %wrong /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar;" }), importScript({})], expected: installation({ version: "(unknown)" }) },
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

  describe("install()", () => {
    it("executes expected statements", () => {
      const context = createMockContext();
      createExtension().install(context, CONFIG.version);
      expect(context.runQueryMock.calls.length).toBe(3)
      const adapterScript = context.runQueryMock.calls[0][0]
      const setScript = context.runQueryMock.calls[1][0]
      expect(adapterScript).toContain(`CREATE OR REPLACE JAVA ADAPTER SCRIPT "ext-schema"."S3_FILES_ADAPTER" AS`)
      expect(adapterScript).toContain(`%jar /bucketfs/${CONFIG.fileName};`)
      expect(setScript).toContain(`CREATE OR REPLACE JAVA SET SCRIPT "ext-schema"."IMPORT_FROM_S3_DOCUMENT_FILES"`)
      expect(setScript).toContain(`%jar /bucketfs/${CONFIG.fileName};`)
      expect(context.runQueryMock.calls[2][0]).toBe("COMMIT")
    })
    it("fails for wrong version", () => {
      expect(() => { createExtension().install(createMockContext(), "wrongVersion") })
        .toThrow(`Installing version 'wrongVersion' not supported, try '${CONFIG.version}'.`)
    })
  })
})

