#if __has_include(<React/RCTEventDispatcher.h>)
#import <React/RCTEventDispatcher.h>
#import <React/RCTBridgeModule.h>
#else
#import "RCTEventDispatcher.h"
#import "RCTBridgeModule.h"
#endif

@import GoogleMobileAds;

@interface RNAdMobRewarded : NSObject <RCTBridgeModule, GADRewardBasedVideoAdDelegate>
@end

