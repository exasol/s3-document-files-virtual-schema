/*
 * For a detailed explanation regarding each configuration property and type check, visit:
 * https://jestjs.io/docs/configuration
 */
export default {
  clearMocks: true,
  collectCoverage: true,
  coverageDirectory: "coverage",
  coverageProvider: "v8",
  errorOnDeprecated: true,
  preset: 'ts-jest/presets/default-esm',
  testEnvironment: 'node',
  testMatch: [
    "**/?(*.)+(spec|test).ts"
  ],
};
