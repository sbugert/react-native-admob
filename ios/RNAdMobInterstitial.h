#if __has_include(<React/RCTComponent.h>)
#import <React/RCTComponent.h>
#import <React/RCTBridgeModule.h>
#else
#import "RCTComponent.h"
#import "RCTBridgeModule.h"
#endif

@import GoogleMobileAds;

@interface RNAdMobInterstitial : NSObject <RCTBridgeModule, GADInterstitialDelegate>

@property (nonatomic, copy) RCTBubblingEventBlock onInterstitialDidLoad;
@property (nonatomic, copy) RCTBubblingEventBlock onInterstitialDidFailToLoad;
@property (nonatomic, copy) RCTBubblingEventBlock onInterstitialDidOpen;
@property (nonatomic, copy) RCTBubblingEventBlock onInterstitialDidClose;
@property (nonatomic, copy) RCTBubblingEventBlock onInterstitialWillLeaveApplication;

@end
