import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-doc-to-pdf' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// @ts-expect-error
const isTurboModuleEnabled = global.__turboModuleProxy != null;

const DocToPdfModule = isTurboModuleEnabled
  ? require('./NativeDocToPdf').default
  : NativeModules.DocToPdf;

const DocToPdf = DocToPdfModule
  ? DocToPdfModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function generateThumbnail(
  filePath: string,
  fileType: string
): Promise<number> {
  return DocToPdf.generateThumbnail(filePath, fileType);
}
