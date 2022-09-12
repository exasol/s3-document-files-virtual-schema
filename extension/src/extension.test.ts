import { ExaMetadata, Installation, Instance, ParameterValue, QueryResult, Row, SqlClient } from '@exasol/extension-manager-interface';
import { ExaScriptsRow } from '@exasol/extension-manager-interface/dist/exasolSchema';
import { describe, expect, it } from '@jest/globals';
import * as jestMock from "jest-mock";
import { createExtension } from "./extension";
import { CONFIG } from './extension-config';

const EXTENSION_SCHEMA_NAME = "ext-schema"

function getInstalledExtension(): any {
  return (global as any).installedExtension
}

function createMockContext() {
  const execute = jestMock.fn<(query: string, ...args: any) => void>()
  const query = jestMock.fn<(query: string, ...args: any) => QueryResult>()

  const sqlClient: SqlClient = {
    execute: execute,
    query: query
  }

  return {
    extensionSchemaName: EXTENSION_SCHEMA_NAME,
    sqlClient,
    bucketFs: {
      resolvePath(fileName: string) {
        return "/bucketfs/" + fileName;
      },
    },
    executeMock: execute,
    queryMock: query
  }
}

describe("S3 VS Extension", () => {

  describe("extension registration", () => {
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
  })

  describe("findInstallations()", () => {
    function findInstallations(allScripts: ExaScriptsRow[]): Installation[] {
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
      function script({ schema = "schema", name = "name", inputType, resultType, type = "", text = "", comment }: Partial<ExaScriptsRow>): ExaScriptsRow {
        return { schema, name, inputType, resultType, type, text, comment }
      }
      function adapterScript({ name = "S3_FILES_ADAPTER", type = "ADAPTER", text = "adapter script" }: Partial<ExaScriptsRow>): ExaScriptsRow {
        return script({ name, type, text })
      }
      function importScript({ name = "IMPORT_FROM_S3_DOCUMENT_FILES", type = "UDF", inputType = "SET", resultType = "EMITS" }: Partial<ExaScriptsRow>): ExaScriptsRow {
        return script({ name, type, inputType, resultType })
      }
      const tests: { name: string; scripts: ExaScriptsRow[], expected?: Installation }[] = [
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
        { name: "script contains LF", scripts: [adapterScript({ text: "CREATE ...\n %jar /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar; more text" }), importScript({})], expected: installation({ version: "1.2.3" }) },
        { name: "script contains CRLF", scripts: [adapterScript({ text: "CREATE ...\r\n %jar /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar; more text" }), importScript({})], expected: installation({ version: "1.2.3" }) },
        { name: "version not found in filename", scripts: [adapterScript({ text: "CREATE ... %jar /path/to/invalid-file-name-dist-0.0.0-s3-1.2.3.jar;" }), importScript({})], expected: installation({ version: "(unknown)" }) },
        { name: "filename not found in script", scripts: [adapterScript({ text: "CREATE ... %wrong /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar;" }), importScript({})], expected: installation({ version: "(unknown)" }) },
      ]
      tests.forEach(test => {
        it(test.name, () => {
          const actual = findInstallations(test.scripts)
          if (test.expected) {
            expect(actual).toHaveLength(1)
            expect(actual[0].name).toStrictEqual(test.expected.name)
            expect(actual[0].version).toStrictEqual(test.expected.version)
          } else {
            expect(actual).toHaveLength(0)
          }
        })
      });
      it("returns expected parameters", () => {
        const actual = findInstallations([adapterScript({}), importScript({})])
        expect(actual).toHaveLength(1)
        expect(actual[0].instanceParameters).toHaveLength(10)
        expect(actual[0].instanceParameters[0]).toStrictEqual({
          id: "virtualSchemaName", name: "Name of the new virtual schema", required: true, type: "string", scope: "general",
        })
        expect(actual[0].instanceParameters[1]).toStrictEqual({
          id: "awsAccessKeyId", name: "AWS Access Key Id", required: true, type: "string", scope: "connection",
        })
      })
    })
  })

  describe("install()", () => {
    it("executes expected statements", () => {
      const context = createMockContext();
      createExtension().install(context, CONFIG.version);
      const executeCalls = context.executeMock.mock.calls
      expect(executeCalls.length).toBe(4)
      const adapterScript = executeCalls[0][0]
      const setScript = executeCalls[1][0]
      expect(adapterScript).toContain(`CREATE OR REPLACE JAVA ADAPTER SCRIPT "ext-schema"."S3_FILES_ADAPTER" AS`)
      expect(adapterScript).toContain(`%jar /bucketfs/${CONFIG.fileName};`)
      expect(setScript).toContain(`CREATE OR REPLACE JAVA SET SCRIPT "ext-schema"."IMPORT_FROM_S3_DOCUMENT_FILES"`)
      expect(setScript).toContain(`%jar /bucketfs/${CONFIG.fileName};`)
      const expectedComment = `Created by extension manager for S3 virtual schema extension ${CONFIG.version}`
      expect(executeCalls[2][0]).toBe(`COMMENT ON SCRIPT "ext-schema"."IMPORT_FROM_S3_DOCUMENT_FILES" IS '${expectedComment}'`)
      expect(executeCalls[3][0]).toBe(`COMMENT ON SCRIPT "ext-schema"."S3_FILES_ADAPTER\" IS '${expectedComment}'`)
    })
    it("fails for wrong version", () => {
      expect(() => { createExtension().install(createMockContext(), "wrongVersion") })
        .toThrow(`Installing version 'wrongVersion' not supported, try '${CONFIG.version}'.`)
    })
  })


  describe("addInstance()", () => {
    describe("connection parameters converted", () => {
      type TestCase = { name: string, paramValues: ParameterValue[], expected: string }
      function booleanMapped(value: string, expected: string): TestCase {
        return { name: `boolean parameter '${value}' mapped to ${expected}`, paramValues: [{ name: "useSsl", value: value }], expected: `{"useSsl":${expected}}` };
      }
      const tests: TestCase[] = [
        { name: "no parameters", paramValues: [], expected: '{}' },
        { name: "unknown parameter", paramValues: [{ name: "unknown", value: "ignore" }], expected: '{}' },
        { name: "parameter with single quote", paramValues: [{ name: "awsAccessKeyId", value: "abc'123''xyz" }], expected: `{"awsAccessKeyId":"abc''123''''xyz"}` },
        { name: "parameter with double quote", paramValues: [{ name: "awsAccessKeyId", value: 'abc"123""xyz' }], expected: `{"awsAccessKeyId":"abc\\"123\\"\\"xyz"}` },
        { name: "multiple parameters", paramValues: [{ name: "awsAccessKeyId", value: 'id' }, { name: "awsSecretAccessKey", value: "key" }], expected: `{"awsAccessKeyId":"id","awsSecretAccessKey":"key"}` },
        { name: "mixed parameters", paramValues: [{ name: "awsAccessKeyId", value: 'id' }, { name: "unknown", value: "ignored" }], expected: `{"awsAccessKeyId":"id"}` },
        booleanMapped("true", "true"),
        booleanMapped("TRUE", "false"),
        booleanMapped("false", "false"),
        booleanMapped("FALSE", "false"),
        booleanMapped("invalid", "false"),
      ];
      for (const test of tests) {
        it(test.name, () => {
          const context = createMockContext();
          const parameters = test.paramValues.concat([{ name: "virtualSchemaName", value: "NEW_S3_VS" }, { name: "mapping", value: "my mapping" }])
          const instance = createExtension().addInstance(context, CONFIG.version, { values: parameters });
          expect(instance.name).toBe("NEW_S3_VS")
          expect(context.executeMock.mock.calls[0][0]).toBe(`CREATE OR REPLACE CONNECTION "NEW_S3_VS_CONNECTION" TO '' USER '' IDENTIFIED BY '${test.expected}'`)
        })
      }
    })
    it("executes expected statements", () => {
      const context = createMockContext();
      const parameters = [{ name: "virtualSchemaName", value: "NEW_S3_VS" }, { name: "mapping", value: "my mapping" }, { name: "awsAccessKeyId", value: "id" }]
      const instance = createExtension().addInstance(context, CONFIG.version, { values: parameters });
      expect(instance.name).toBe("NEW_S3_VS")
      const calls = context.executeMock.mock.calls
      expect(calls.length).toBe(4)
      expect(calls[0][0]).toBe(`CREATE OR REPLACE CONNECTION "NEW_S3_VS_CONNECTION" TO '' USER '' IDENTIFIED BY '{"awsAccessKeyId":"id"}'`)
      expect(calls[1][0]).toBe(`CREATE VIRTUAL SCHEMA "NEW_S3_VS" USING "ext-schema"."S3_FILES_ADAPTER" WITH CONNECTION_NAME = 'NEW_S3_VS_CONNECTION' MAPPING = 'my mapping';`)
      const comment = `Created by extension manager for S3 virtual schema NEW_S3_VS`
      expect(calls[2][0]).toBe(`COMMENT ON CONNECTION \"NEW_S3_VS_CONNECTION\" IS '${comment}'`)
      expect(calls[3][0]).toBe(`COMMENT ON SCHEMA \"NEW_S3_VS\" IS '${comment}'`)
    })

    it("returns id and name", () => {
      const context = createMockContext();
      const parameters = [{ name: "virtualSchemaName", value: "NEW_S3_VS" }, { name: "mapping", value: "my mapping" }, { name: "awsAccessKeyId", value: "id" }]
      const instance = createExtension().addInstance(context, CONFIG.version, { values: parameters });
      expect(instance).toStrictEqual({ id: "NEW_S3_VS", name: "NEW_S3_VS" })
    })

    it("escapes single quotes", () => {
      const context = createMockContext();
      const parameters = [{ name: "virtualSchemaName", value: "vs'name" }, { name: "mapping", value: "mapping'with''quotes" }, { name: "awsAccessKeyId", value: "access'key" }]
      const instance = createExtension().addInstance(context, CONFIG.version, { values: parameters });
      const calls = context.executeMock.mock.calls
      expect(calls[0][0]).toBe(`CREATE OR REPLACE CONNECTION "vs'name_CONNECTION" TO '' USER '' IDENTIFIED BY '{"awsAccessKeyId":"access''key"}'`)
      expect(calls[1][0]).toBe(`CREATE VIRTUAL SCHEMA "vs'name" USING "ext-schema"."S3_FILES_ADAPTER" WITH CONNECTION_NAME = 'vs''name_CONNECTION' MAPPING = 'mapping''with''''quotes';`)
      const comment = `Created by extension manager for S3 virtual schema vs''name`
      expect(calls[2][0]).toBe(`COMMENT ON CONNECTION \"vs'name_CONNECTION\" IS '${comment}'`)
      expect(calls[3][0]).toBe(`COMMENT ON SCHEMA \"vs'name\" IS '${comment}'`)
    })

    it("fails for wrong version", () => {
      expect(() => { createExtension().addInstance(createMockContext(), "wrongVersion", { values: [] }) })
        .toThrow(`Version 'wrongVersion' not supported, can only use ${CONFIG.version}.`)
    })
  })

  describe("findInstances()", () => {
    function findInstances(rows: Row[]): Instance[] {
      const context = createMockContext();
      context.queryMock.mockReturnValue({ columns: [], rows });
      return createExtension().findInstances(context, "version")
    }
    it("returns empty list for empty metadata", () => {
      expect(findInstances([])).toEqual([])
    })
    it("returns single instance", () => {
      expect(findInstances([["s3_vs"]]))
        .toEqual([{ id: "s3_vs", name: "s3_vs" }])
    })
    it("returns multiple instance", () => {
      expect(findInstances([["vs1"], ["vs2"], ["vs3"]]))
        .toEqual([{ id: "vs1", name: "vs1" }, { id: "vs2", name: "vs2" }, { id: "vs3", name: "vs3" }])
    })
    it("filters by schema and script name", () => {
      const context = createMockContext();
      context.queryMock.mockReturnValue({ columns: [], rows: [] });
      createExtension().findInstances(context, "version")
      const queryCalls = context.queryMock.mock.calls
      expect(queryCalls.length).toEqual(1)
      expect(queryCalls[0][0]).toEqual("SELECT SCHEMA_NAME FROM SYS.EXA_ALL_VIRTUAL_SCHEMAS WHERE ADAPTER_SCRIPT = ?||'.'||?  ORDER BY SCHEMA_NAME")
      expect(queryCalls[0][1]).toEqual("ext-schema")
      expect(queryCalls[0][2]).toEqual("S3_FILES_ADAPTER")
    })
  })

  describe("deleteInstance()", () => {
    it("drops connection and virtual schema", () => {
      const context = createMockContext();
      createExtension().deleteInstance(context, "instId")
      const executeCalls = context.executeMock.mock.calls
      expect(executeCalls.length).toEqual(2)
      expect(executeCalls[0][0]).toEqual("DROP VIRTUAL SCHEMA IF EXISTS \"instId\" CASCADE")
      expect(executeCalls[1][0]).toEqual("DROP CONNECTION IF EXISTS \"instId_CONNECTION\"")
    })
  })
})

