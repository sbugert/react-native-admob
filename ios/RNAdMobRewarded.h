#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>
#else
#import "RCTBridgeModule.h"
#import "RCTEventEmitter.h"
#endif

@import GoogleMobileAds;

@interface RNAdMobRewarded : RCTEventEmitter <RCTBridgeModule, GADRewardBasedVideoAdDelegate>
@end

