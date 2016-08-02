#import "RCTBridgeModule.h"
#import "RCTEventDispatcher.h"
@import GoogleMobileAds;

@interface RNAdMobRewarded : NSObject <RCTBridgeModule, GADRewardBasedVideoAdDelegate>
@end

