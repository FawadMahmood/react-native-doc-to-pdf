
#ifdef RCT_NEW_ARCH_ENABLED
#import "RNDocToPdfSpec.h"

@interface DocToPdf : NSObject <NativeDocToPdfSpec>
#else
#import <React/RCTBridgeModule.h>

@interface DocToPdf : NSObject <RCTBridgeModule>
#endif

@end
