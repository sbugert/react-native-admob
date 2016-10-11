#import "RNAdMobDFPManager.h"
#import "RNDFPBannerView.h"

#import "RCTBridge.h"

@implementation RNAdMobDFPManager

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (UIView *)view
{
  return [[RNDFPBannerView alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
}

- (NSArray *) customDirectEventTypes
{
  return @[
           @"onSizeChange",
           @"onAdViewDidReceiveAd",
           @"onDidFailToReceiveAdWithError",
           @"onAdViewWillPresentScreen",
           @"onAdViewWillDismissScreen",
           @"onAdViewDidDismissScreen",
           @"onAdViewWillLeaveApplication",
           @"onAdmobDispatchAppEvent"
           ];
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}


RCT_EXPORT_VIEW_PROPERTY(bannerSize, NSString);
RCT_EXPORT_VIEW_PROPERTY(adUnitID, NSString);
RCT_EXPORT_VIEW_PROPERTY(testDeviceID, NSString);

@end

