#import "RNGADNativeViewManager.h"
#import "RNGADNativeView.h"

#if __has_include(<React/RCTBridge.h>)
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <React/RCTEventDispatcher.h>
#else
#import "RCTBridge.h"
#import "RCTUIManager.h"
#import "RCTEventDispatcher.h"
#endif

@implementation RNGADNativeViewManager

RCT_EXPORT_MODULE();

-(UIView *)view
{
   return [RNGADNativeView new];
}

RCT_EXPORT_METHOD(loadNativeAd:(nonnull NSNumber *)reactTag:(NSString *)adUnitId)
{
    [self.bridge.uiManager addUIBlock:^(__unused RCTUIManager *uiManager, NSDictionary<NSNumber *, RNGADNativeView *> *viewRegistry) {
        RNGADNativeView *view = viewRegistry[reactTag];
        if (![view isKindOfClass:[RNGADNativeView class]]) {
            RCTLogError(@"Invalid view returned from registry, expecting RNGADNativeView, got: %@", view);
        } else {
            [view loadNativeAd:adUnitId];
        }
    }];
}


RCT_EXPORT_VIEW_PROPERTY(adSize, NSString)
RCT_EXPORT_VIEW_PROPERTY(testDevices, NSArray)

RCT_EXPORT_VIEW_PROPERTY(onSizeChange, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAppEvent, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdLoaded, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdFailedToLoad, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdOpened, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdClosed, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdLeftApplication, RCTBubblingEventBlock)

@end
