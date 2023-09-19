import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  generateThumbnail(filePath: string, fileType: string): Promise<number>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('DocToPdf');
