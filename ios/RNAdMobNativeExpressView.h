#if __has_include(<React/RCTEventDispatcher.h>)
#import <React/RCTComponent.h>
#else
#import "RCTComponent.h"
#endif

@import GoogleMobileAds;

@class RCTEventDispatcher;

@interface RNAdMobNativeExpressView : UIView <GADNativeExpressAdViewDelegate>

@property (nonatomic, copy) NSNumber *bannerWidth;
@property (nonatomic, copy) NSNumber *bannerHeight;
@property (nonatomic, copy) NSString *adUnitID;
@property (nonatomic, copy) NSString *testDeviceID;

@property (nonatomic, copy) RCTBubblingEventBlock onSizeChange;
@property (nonatomic, copy) RCTBubblingEventBlock onAdViewDidReceiveAd;
@property (nonatomic, copy) RCTBubblingEventBlock onDidFailToReceiveAdWithError;
@property (nonatomic, copy) RCTBubblingEventBlock onAdViewWillPresentScreen;
@property (nonatomic, copy) RCTBubblingEventBlock onAdViewWillDismissScreen;
@property (nonatomic, copy) RCTBubblingEventBlock onAdViewDidDismissScreen;
@property (nonatomic, copy) RCTBubblingEventBlock onAdViewWillLeaveApplication;

- (void)loadBanner;

@end