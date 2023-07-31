
import { NotFoundError, PreconditionFailedError } from '@exasol/extension-manager-interface';
import { describe, expect, it } from '@jest/globals';
import { createExtension } from "./extension";
import { createMockContext, script } from './test-utils';

describe("upgrade", () => {
    describe("fails", () => {

        it("both scripts missing", () => {
            const context = createMockContext()
            context.mocks.simulateScripts(null, null)
            expect(() => createExtension().upgrade(context))
                .toThrow(new NotFoundError("extension is not installed, the following scripts are missing: S3_FILES_ADAPTER, IMPORT_FROM_S3_DOCUMENT_FILES"))
        })
        it("import script missing", () => {
            const context = createMockContext()
            context.mocks.simulateScripts(script({}), null)
            expect(() => createExtension().upgrade(context))
                .toThrow(new NotFoundError("extension is not installed, the following scripts are missing: IMPORT_FROM_S3_DOCUMENT_FILES"))
        })
        it("adapter script missing", () => {
            const context = createMockContext()
            context.mocks.simulateScripts(null, script({}))
            expect(() => createExtension().upgrade(context))
                .toThrow(new NotFoundError("extension is not installed, the following scripts are missing: S3_FILES_ADAPTER"))
        })
        it("unknown adapter script version", () => {
            const context = createMockContext()
            context.mocks.simulateScripts(script({ text: "dummy" }), script({}))
            expect(() => createExtension().upgrade(context))
                .toThrow(new PreconditionFailedError("Failed to extract version from adapter script schema.name, script text: 'dummy'"))
        })
        it("unknown import script version", () => {
            const context = createMockContext()
            context.mocks.simulateScripts(script({}), script({}))
            expect(() => createExtension().upgrade(context))
                .toThrow(new PreconditionFailedError("Failed to extract version from import script schema.name, script text: 'dummy'"))
        })
    });
})