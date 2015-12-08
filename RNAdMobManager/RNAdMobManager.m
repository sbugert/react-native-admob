#import "RNAdMobManager.h"
#import "BannerView.h"

#import "RCTBridge.h"

@implementation RNAdMobManager

RCT_EXPORT_MODULE();

@synthesize bridge = _bridge;

- (UIView *)view
{
  return [[BannerView alloc] initWithEventDispatcher:self.bridge.eventDispatcher];
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
           @"onAdViewWillLeaveApplication"
           ];
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}


RCT_EXPORT_VIEW_PROPERTY(bannerSize, NSString);
RCT_EXPORT_VIEW_PROPERTY(adUnitID, NSString);

@end

