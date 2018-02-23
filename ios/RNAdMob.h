#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#else
#import "RCTBridgeModule.h"
#endif

@import GoogleMobileAds;

@interface RNAdMob : NSObject <RCTBridgeModule>
@end
