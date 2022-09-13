/* eslint-disable @typescript-eslint/no-unused-vars */
import {
    Context, ExaMetadata,
    ExasolExtension,
    Installation,
    Instance, ParameterValues,
    registerExtension
} from "@exasol/extension-manager-interface";
import { addInstance } from "./addInstance";
import { ExtensionInfo } from "./common";
import { deleteInstance } from "./deleteInstance";
import { CONFIG } from "./extension-config";
import { findInstallations } from "./findInstallations";
import { findInstances } from "./findInstances";
import { installExtension } from "./installExtension";
import { uninstall } from "./uninstallExtension";

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
        addInstance(context: Context, version: string, params: ParameterValues): Instance {
            return addInstance(context, extensionInfo, version, params);
        },
        findInstallations(_context: Context, metadata: ExaMetadata): Installation[] {
            return findInstallations(metadata.allScripts.rows);
        },
        findInstances(context: Context, version: string): Instance[] {
            return findInstances(context);
        },
        uninstall(context: Context, version: string): void {
            uninstall(context, version)
        },
        deleteInstance(context: Context, instanceId: string): void {
            deleteInstance(context, instanceId);
        },
        readInstanceParameters(_context: Context, _instanceId: string): ParameterValues {
            return { values: [] };
        }
    }
}

registerExtension(createExtension())
