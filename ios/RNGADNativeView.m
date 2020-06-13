
#import "RNGADNativeView.h"
#import "RNAdMobUtils.h"
#import "GADTMediumTemplateView.h"
#import "GADTSmallTemplateView.h"
#import <React/RCTBridge.h>
#import <React/RCTRootView.h>
#import <React/RCTRootViewDelegate.h>
#import <React/RCTViewManager.h>

@import GoogleMobileAds;


@implementation RNGADNativeView

- (void)setAdSize:(NSString *)adSize
{
    NSLog(@"Setting ad size %@", adSize);
    _adSize = adSize;
}

- (void)setTestDevices:(NSArray *)testDevices
{
    _testDevices = RNAdMobProcessTestDevices(testDevices, kDFPSimulatorID);
}

- (void)loadNativeAd:(NSString *)adUnitId
{
    UIWindow *keyWindow = [[UIApplication sharedApplication] keyWindow];
    UIViewController *rootViewController = [keyWindow rootViewController];
    self.adLoader = [[GADAdLoader alloc]
            initWithAdUnitID:adUnitId
            rootViewController:rootViewController
            adTypes:@[ kGADAdLoaderAdTypeUnifiedNative ]
            options:nil];

    self.adLoader.delegate = self;
    GADRequest *request = [GADRequest request];
    GADMobileAds.sharedInstance.requestConfiguration.testDeviceIdentifiers = _testDevices;
    [self.adLoader loadRequest:request];
}

- (void)adLoader:(GADAdLoader *)adLoader didFailToReceiveAdWithError:(GADRequestError *)error {
   if (self.onAdFailedToLoad) {
       self.onAdFailedToLoad(@{ @"error": @{ @"message": [error localizedDescription] } });
   }
}

- (void)addTemplateView:(GADTTemplateView *)templateView withNativeAd:(GADUnifiedNativeAd *)nativeAd
{
    nativeAd.delegate = self;
    templateView.nativeAd = nativeAd;
    
    [self addSubview:templateView];
    
    [templateView addHorizontalConstraintsToSuperviewWidth];
    [templateView addVerticalCenterConstraintToSuperview];
}

- (void)adLoader:(GADAdLoader *)adLoader didReceiveUnifiedNativeAd:(GADUnifiedNativeAd *)nativeAd {
    if (self.onAdLoaded) {
        self.onAdLoaded(@{});
    }
    
    nativeAd.delegate = self;
    if([_adSize  isEqual: @"medium"]) {
        GADTMediumTemplateView *templateView = [[GADTMediumTemplateView alloc] init];
        [self addTemplateView:templateView withNativeAd:nativeAd];
    } else {
        GADTSmallTemplateView *templateView = [[GADTSmallTemplateView alloc] init];
        [self addTemplateView:templateView withNativeAd:nativeAd];
    }
}

- (void)nativeAdDidRecordImpression:(nonnull GADUnifiedNativeAd *)nativeAd
{
    if (self.didRecordImpression) {
        self.didRecordImpression(@{});
    }
}

- (void)nativeAdDidRecordClick:(nonnull GADUnifiedNativeAd *)nativeAd
{
    if (self.didRecordClick) {
           self.didRecordClick(@{});
       }
}

- (void)nativeAdWillPresentScreen:(nonnull GADUnifiedNativeAd *)nativeAd
{
    if (self.onAdOpened) {
        self.onAdOpened(@{});
    }
}

- (void)nativeAdWillDismissScreen:(nonnull GADUnifiedNativeAd *)nativeAd
{
    if (self.onAdClosed) {
           self.onAdClosed(@{});
       }
}

- (void)nativeAdWillLeaveApplication:(nonnull GADUnifiedNativeAd *)nativeAd
{
    if(self.onAdLeftApplication) {
        self.onAdLeftApplication(@{});
    }
}


@end
