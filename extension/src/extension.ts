/* eslint-disable @typescript-eslint/no-unused-vars */
import {
    Context, ExaMetadata,
    ExasolExtension,
    Installation,
    Instance, ParameterValues,
    registerExtension
} from "@exasol/extension-manager-interface";
import { ExtensionInfo } from "./common";
import { CONFIG } from "./extension-config";
import { findInstallations } from "./findInstallations";
import { installExtension } from "./installExtension";

function createExtensionInfo(): ExtensionInfo {
    const version = CONFIG.version;
    const fileName = CONFIG.fileName;
    return { version, fileName };
}

export function createExtension(): ExasolExtension {
    const extensionInfo = createExtensionInfo()
    const repoBaseUrl = "https://github.com/exasol/s3-document-files-virtual-schema"
    const downloadUrl = `${repoBaseUrl}/releases/download/${extensionInfo.version}/${extensionInfo.fileName}`;
    const licenseUrl = `${repoBaseUrl}/blob/main/LICENSE`;
    return {
        name: "S3 Virtual Schema",
        description: "Virtual Schema for document files on AWS S3",
        installableVersions: [extensionInfo.version],
        bucketFsUploads: [{ bucketFsFilename: extensionInfo.fileName, downloadUrl, fileSize: CONFIG.fileSizeBytes, name: "S3 VS Jar file", licenseUrl, licenseAgreementRequired: false }],
        install(context: Context, version: string) {
            installExtension(context, extensionInfo, version)
        },
        addInstance(_context: Context, _version: string, _params: ParameterValues): Instance {
            return { name: "NEW_S3_VS" };
        },
        findInstallations(_context: Context, metadata: ExaMetadata): Installation[] {
            return findInstallations(metadata.allScripts.rows);
        },
        findInstances(_context: Context, _installation: Installation): Instance[] {
            return [];
        },
        uninstall(_context: Context, _installation: Installation): void {
            //empty on purpose
        },
        deleteInstance(_context: Context, _instance: Instance): void {
            //empty on purpose
        },
        readInstanceParameters(_context: Context, _installation: Installation, _instance: Instance): ParameterValues {
            return undefined;
        }
    }
}

registerExtension(createExtension())
