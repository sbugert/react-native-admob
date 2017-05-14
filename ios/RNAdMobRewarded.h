#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#import <React/RCTEventDispatcher.h>
#import <React/RCTLog.h>
#else
#import "RCTBridgeModule.h"
#import "RCTEventDispatcher.h"
#import "RCTLog.h"
#endif

@import GoogleMobileAds;

@interface RNAdMobRewarded : NSObject <RCTBridgeModule, GADRewardBasedVideoAdDelegate>
@end

