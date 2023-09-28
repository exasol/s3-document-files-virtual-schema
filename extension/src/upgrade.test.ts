
import { ExaScriptsRow, NotFoundError, PreconditionFailedError } from '@exasol/extension-manager-interface';
import { describe, expect, it } from '@jest/globals';
import { createExtension } from "./extension";
import { CONFIG } from './extension-config';
import { createMockContext, script } from './test-utils';

function scriptWithText(text: string): ExaScriptsRow {
    return script({ text });
}

function scriptWithVersion(version: string): ExaScriptsRow {
    return scriptWithText(`CREATE ... %jar /path/to/document-files-virtual-schema-dist-0.0.0-s3-${version}.jar;`)
}

describe("upgrade()", () => {
    describe("preconditions fail", () => {
        const tests: { name: string; adapterScript: ExaScriptsRow | null, importScript: ExaScriptsRow | null, expectedError: Error }[] = [
            { name: "both scripts missing", adapterScript: null, importScript: null, expectedError: new NotFoundError("extension is not installed, the following scripts are missing: S3_FILES_ADAPTER, IMPORT_FROM_S3_DOCUMENT_FILES") },
            { name: "import script missing", adapterScript: script({}), importScript: null, expectedError: new NotFoundError("extension is not installed, the following scripts are missing: IMPORT_FROM_S3_DOCUMENT_FILES") },
            { name: "adapter script missing", adapterScript: null, importScript: script({}), expectedError: new NotFoundError("extension is not installed, the following scripts are missing: S3_FILES_ADAPTER") },
            { name: "unknown adapter script version", adapterScript: scriptWithText("dummy"), importScript: script({}), expectedError: new PreconditionFailedError("Failed to extract version from adapter script schema.name, script text: 'dummy'") },
            { name: "unknown import script version", adapterScript: scriptWithVersion("1.2.3"), importScript: scriptWithText("dummy"), expectedError: new PreconditionFailedError("Failed to extract version from import script schema.name, script text: 'dummy'") },
            { name: "inconsistent script versions", adapterScript: scriptWithVersion("1.2.3"), importScript: scriptWithVersion("1.2.4"), expectedError: new PreconditionFailedError("Scripts have inconsistent versions. schema.name: 1.2.3, schema.name: 1.2.4") },
            { name: "latest version already installed", adapterScript: scriptWithVersion(CONFIG.version), importScript: scriptWithVersion(CONFIG.version), expectedError: new PreconditionFailedError("Extension is already installed in latest version " + CONFIG.version) },
        ]

        tests.forEach(test => it(test.name, () => {
            const context = createMockContext()
            context.mocks.simulateScripts(test.adapterScript, test.importScript)
            expect(() => createExtension().upgrade(context))
                .toThrow(test.expectedError)
        }))
    })
    describe("success", () => {
        it("returns versions", () => {
            const context = createMockContext()
            context.mocks.simulateScripts(scriptWithVersion("1.2.3"), scriptWithVersion("1.2.3"))
            expect(createExtension().upgrade(context)).toStrictEqual({ previousVersion: "1.2.3", newVersion: CONFIG.version, })
        })
        it("executes CREATE SCRIPT statements", () => {
            const context = createMockContext()
            context.mocks.simulateScripts(scriptWithVersion("1.2.3"), scriptWithVersion("1.2.3"))
            createExtension().upgrade(context)

            const executeCalls = context.mocks.sqlExecute.mock.calls
            expect(executeCalls.length).toBe(4)
            const adapterScript = executeCalls[0][0]
            const importScript = executeCalls[1][0]
            expect(adapterScript).toContain(`CREATE OR REPLACE JAVA ADAPTER SCRIPT "ext-schema"."S3_FILES_ADAPTER" AS`)
            expect(adapterScript).toContain(`%jar /bucketfs/${CONFIG.fileName};`)
            expect(importScript).toContain(`CREATE OR REPLACE JAVA SET SCRIPT "ext-schema"."IMPORT_FROM_S3_DOCUMENT_FILES"`)
            expect(importScript).toContain(`%jar /bucketfs/${CONFIG.fileName};`)
            const expectedComment = `Created by Extension Manager for S3 Virtual Schema extension ${CONFIG.version}`
            expect(executeCalls[2]).toEqual([`COMMENT ON SCRIPT "ext-schema"."IMPORT_FROM_S3_DOCUMENT_FILES" IS '${expectedComment}'`])
            expect(executeCalls[3]).toEqual([`COMMENT ON SCRIPT "ext-schema"."S3_FILES_ADAPTER" IS '${expectedComment}'`])
        })
    })
})