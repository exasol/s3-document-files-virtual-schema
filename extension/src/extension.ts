/* eslint-disable @typescript-eslint/no-unused-vars */
import {
    Context, ExaMetadata,
    ExasolExtension,
    Installation,
    Instance, NotFoundError, Parameter, ParameterValues,
    registerExtension
} from "@exasol/extension-manager-interface";
import { addInstance } from "./addInstance";
import { ExtensionInfo } from "./common";
import { deleteInstance } from "./deleteInstance";
import { CONFIG } from "./extension-config";
import { findInstallations } from "./findInstallations";
import { findInstances } from "./findInstances";
import { installExtension } from "./installExtension";
import { createInstanceParameters } from "./parameterDefinitions";
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
        installableVersions: [{ name: extensionInfo.version, latest: true, deprecated: false }],
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
            uninstall(context, extensionInfo, version)
        },
        deleteInstance(context: Context, version: string, instanceId: string): void {
            deleteInstance(context, extensionInfo, version, instanceId);
        },
        getInstanceParameters(context: Context, version: string): Parameter[] {
            if (extensionInfo.version !== version) {
                throw new NotFoundError(`Version '${version}' not supported, can only use '${extensionInfo.version}'.`)
            }
            return createInstanceParameters()
        },
        readInstanceParameterValues(_context: Context, _version: string, _instanceId: string): ParameterValues {
            throw new NotFoundError("Reading instance parameter values not supported")
        }
    }
}

registerExtension(createExtension())
