#if __has_include(<React/RCTView.h>)
#import <React/RCTView.h>
#else
#import "RCTView.h"
#endif

@import GoogleMobileAds;

@class RCTEventDispatcher;

@interface RNGADNativeView : RCTView <GADUnifiedNativeAdLoaderDelegate, GADVideoControllerDelegate, GADUnifiedNativeAdDelegate>


/// You must keep a strong reference to the GADAdLoader during the ad loading
/// process.
//@property(nonatomic, strong) GADAdLoader *adLoader;

/// The native ad view that is being presented.
@property(nonatomic, strong) GADUnifiedNativeAdView *nativeAdView;

/// The height constraint applied to the ad view, where necessary.
@property(nonatomic, strong) NSLayoutConstraint *heightConstraint;

@property (nonatomic, copy) NSArray *testDevices;
@property (nonatomic, copy) NSString *adUnitID;
@property (nonatomic, copy) NSString *nightMode;

@property (nonatomic, copy) RCTBubblingEventBlock onSizeChange;
@property (nonatomic, copy) RCTBubblingEventBlock onAdLoaded;
@property (nonatomic, copy) RCTBubblingEventBlock onAdFailedToLoad;
@property (nonatomic, copy) RCTBubblingEventBlock onAdOpened;
@property (nonatomic, copy) RCTBubblingEventBlock onAdClosed;
@property (nonatomic, copy) RCTBubblingEventBlock onAdLeftApplication;

- (void)loadNative;

@end
