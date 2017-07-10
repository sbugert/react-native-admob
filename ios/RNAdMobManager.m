#import "RNAdMobManager.h"
#import "BannerView.h"
#import "RCTConvert+GADAdSize.h"

#if __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#else
#import "RCTBridge.h"
#import "RCTUIManager.h"
#endif

@implementation RNAdMobManager

RCT_EXPORT_MODULE();

- (UIView *)view
{
  return [[BannerView alloc] init];
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_METHOD(loadBanner:(nonnull NSNumber *)reactTag)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, BannerView *> *viewRegistry) {
        BannerView *view = viewRegistry[reactTag];
        if (![view isKindOfClass:[BannerView class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting BannerView, got: %@", view);
        } else {
            [view loadBanner];
        }
    }];
}

RCT_EXPORT_VIEW_PROPERTY(adSize, GADAdSize)
RCT_EXPORT_VIEW_PROPERTY(adUnitID, NSString)
RCT_EXPORT_VIEW_PROPERTY(testDevices, NSArray)

RCT_EXPORT_VIEW_PROPERTY(onSizeChange, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdViewDidReceiveAd, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onDidFailToReceiveAdWithError, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdViewWillPresentScreen, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdViewWillDismissScreen, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdViewDidDismissScreen, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdViewWillLeaveApplication, RCTBubblingEventBlock)

- (NSDictionary<NSString *,id> *)constantsToExport
{
    return @{
        @"simulatorId": kGADSimulatorID
    };
}

@end
