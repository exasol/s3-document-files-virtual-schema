import { describe, expect, it } from '@jest/globals';
import { AdapterScript } from './adapterScript';
import { script } from './test-utils';


describe("AdapterScript", () => {
    describe("properties", () => {
        it("name", () => {
            expect(new AdapterScript(script({ name: "script" })).name).toBe("script")
        })
        it("schema", () => {
            expect(new AdapterScript(script({ schema: "schema" })).schema).toBe("schema")
        })
        it("text", () => {
            expect(new AdapterScript(script({ text: "text" })).text).toBe("text")
        })
        it("qualifiedName", () => {
            expect(new AdapterScript(script({ schema: "schema", name: "name" })).qualifiedName).toBe("schema.name")
        })
    })
    describe("methods", () => {
        describe("getVersion()", () => {
            const tests: { name: string; scriptText: string, expected: string | undefined }[] = [
                { name: "found", scriptText: "CREATE ... %jar /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar; more text", expected: "1.2.3" },
                { name: "found with LF", scriptText: "CREATE ...\n %jar /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar; more text", expected: "1.2.3" },
                { name: "found with LFCR", scriptText: "CREATE ...\n\r %jar /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar; more text", expected: "1.2.3" },
                { name: "with CRLF", scriptText: "CREATE ...\r\n %jar /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar; more text", expected: "1.2.3" },
                { name: "not found in root dir", scriptText: "CREATE ... %jar /document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar; more text", expected: undefined },
                { name: "not found invalid %jar", scriptText: "CREATE ... %invalid /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar; more text", expected: undefined },
                { name: "not found missing %jar", scriptText: "CREATE ... /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar; more text", expected: undefined },
                { name: "not found missing trailing semicolon", scriptText: "CREATE ...\r\n %jar /path/to/document-files-virtual-schema-dist-0.0.0-s3-1.2.3.jar", expected: undefined },
                { name: "not found invalid vs-common-document-files version", scriptText: "CREATE ... %jar /path/to/document-files-virtual-schema-dist-a.b.c-s3-1.2.3.jar;", expected: undefined },
                { name: "not found invalid version", scriptText: "CREATE ... %jar /path/to/document-files-virtual-schema-dist-0.0.0-s3-a.b.c.jar;", expected: undefined },
                { name: "not found invalid filename", scriptText: "CREATE ... %jar /path/to/invalid-file-name-dist-0.0.0-s3-1.2.3.jar;", expected: undefined },
            ]
            tests.forEach(test => it(test.name, () => {
                expect(new AdapterScript(script({ text: test.scriptText })).getVersion()).toBe(test.expected)
            }))
        })
        describe("isValidAdapterScript()", () => {
            const tests: { name: string; type: string | undefined, expected: boolean }[] = [
                { name: "missing type", type: undefined, expected: false },
                { name: "unknown type", type: "unknown", expected: false },
                { name: "wrong case", type: "adapter", expected: false },
                { name: "correct type", type: "ADAPTER", expected: true },
            ]
            tests.forEach(test => it(test.name, () => {
                expect(new AdapterScript(script({ type: test.type })).isValidAdapterScript()).toBe(test.expected)
            }))
        })
        describe("isValidImportScript()", () => {
            const tests: { name: string; type: string | undefined, inputType: string | undefined, resultType: string | undefined, expected: boolean }[] = [
                { name: "correct type", type: "UDF", inputType: "SET", resultType: "EMITS", expected: true },
                { name: "wrong result type", type: "UDF", inputType: "SET", resultType: "wrong", expected: false },
                { name: "missing result type", type: "UDF", inputType: "SET", resultType: undefined, expected: false },
                { name: "wrong input type", type: "UDF", inputType: "wrong", resultType: "EMITS", expected: false },
                { name: "missing input type", type: "UDF", inputType: undefined, resultType: "EMITS", expected: false },
                { name: "wrong type", type: "wrong", inputType: "SET", resultType: "EMITS", expected: false },
                { name: "missing type", type: undefined, inputType: "SET", resultType: "EMITS", expected: false },
            ]
            tests.forEach(test => it(test.name, () => {
                expect(new AdapterScript(script({ type: test.type, inputType: test.inputType, resultType: test.resultType })).isValidImportScript()).toBe(test.expected)
            }))
        })
    })
})