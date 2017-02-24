#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#import <React/RCTEventDispatcher.h>
#else
#import "RCTBridgeModule.h"
#import "RCTEventDispatcher.h"
#endif

@import GoogleMobileAds;

@interface RNAdMobInterstitial : NSObject <RCTBridgeModule, GADInterstitialDelegate>
@end
