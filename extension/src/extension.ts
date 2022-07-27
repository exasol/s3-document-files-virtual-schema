import {
    Context, ExaMetadata,
    ExasolExtension,
    Installation,
    Instance, ParameterValues,
    registerExtension
} from "@exasol/extension-manager-interface";
import { CombinedContext, LocalContext } from "./common";
import { CONFIG } from "./extension-config";
import { findInstallations } from "./findInstallations";
import { installExtension } from "./installExtension";

function createLocalContext(): LocalContext {
    const version = CONFIG.version;
    const fileName = CONFIG.fileName;
    return { version, fileName };
}

export function createExtension(): ExasolExtension {
    const localContext = createLocalContext()
    const repoBaseUrl = "https://github.com/exasol/s3-document-files-virtual-schema"
    const downloadUrl = `${repoBaseUrl}/releases/download/${localContext.version}/${localContext.fileName}`;
    const licenseUrl = `${repoBaseUrl}/blob/main/LICENSE`;
    function combineContext(context: Context): CombinedContext {
        return { local: localContext, global: context }
    }
    return {
        name: "S3 Virtual Schema",
        description: "Virtual Schema for document files on AWS S3",
        installableVersions: [localContext.version],
        bucketFsUploads: [{ bucketFsFilename: localContext.fileName, downloadUrl, fileSize: CONFIG.fileSizeBytes, name: "S3 VS Jar file", licenseUrl, licenseAgreementRequired: false }],
        install(context: Context, version: string) {
            installExtension(combineContext(context), version)
        },
        addInstance(_context: Context, _installation: Installation, _params: ParameterValues): Instance {
            return undefined;
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
