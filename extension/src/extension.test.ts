import { describe, expect, it } from '@jest/globals';
import { createExtension } from "./extension";

function getInstalledExtension(): any {
  return (global as any).installedExtension
}

describe("Extension", () => {
  
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
    const installedExtension  = getInstalledExtension();
    expect(installedExtension.extension).not.toBeNull()
    expect(typeof installedExtension.apiVersion).toBe('string');
    expect(installedExtension.apiVersion).not.toBe('');
  })
})
