#import <React/RCTView.h>

@import GoogleMobileAds;

@interface RNGADNativeView : RCTView <GADUnifiedNativeAdLoaderDelegate,
    GADUnifiedNativeAdDelegate>

@property(nonatomic, strong) GADUnifiedNativeAd *nativeAdView;
@property(nonatomic, strong) GADAdLoader *adLoader;

@property (nonatomic, copy) NSArray *testDevices;
@property (nonatomic, copy) NSString *adSize;

@property (nonatomic, copy) RCTBubblingEventBlock onAdLoaded;
@property (nonatomic, copy) RCTBubblingEventBlock onAdFailedToLoad;
@property (nonatomic, copy) RCTBubblingEventBlock didRecordImpression;
@property (nonatomic, copy) RCTBubblingEventBlock didRecordClick;
@property (nonatomic, copy) RCTBubblingEventBlock onAdOpened;
@property (nonatomic, copy) RCTBubblingEventBlock onAdClosed;
@property (nonatomic, copy) RCTBubblingEventBlock onAdLeftApplication;

- (void)loadNativeAd:(NSString *)adUnitId;

@end
