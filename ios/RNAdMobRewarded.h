#if __has_include(<React/RCTComponent.h>)
#import <React/RCTComponent.h>
#import <React/RCTBridgeModule.h>
#else
#import "RCTComponent.h"
#import "RCTBridgeModule.h"
#endif

@import GoogleMobileAds;

@interface RNAdMobRewarded : NSObject <RCTBridgeModule, GADRewardBasedVideoAdDelegate>

@property (nonatomic, copy) RCTBubblingEventBlock onRewardedVideoDidRewardUser;
@property (nonatomic, copy) RCTBubblingEventBlock onRewardedVideoDidLoad;
@property (nonatomic, copy) RCTBubblingEventBlock onRewardedVideoDidOpen;
@property (nonatomic, copy) RCTBubblingEventBlock onRewardedVideoDidClose;
@property (nonatomic, copy) RCTBubblingEventBlock onRewardedVideoWillLeaveApplication;
@property (nonatomic, copy) RCTBubblingEventBlock onRewardedVideoDidFailToLoad;

@end

