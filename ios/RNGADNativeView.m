#import "RNGADNativeView.h"
#import "RNAdMobUtils.h"

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#import <React/UIView+React.h>
#import <React/RCTLog.h>
#else
#import "RCTBridgeModule.h"
#import "UIView+React.h"
#import "RCTLog.h"
#endif

static NSString *const TestAdUnit = @"ca-app-pub-3940256099942544/3986624511";

@implementation RNGADNativeView
{
    GADAdLoader *adLoader;
    
    NSString *darkMode;
    BOOL adInit;
    NSString *language;
}

- (void)dealloc
{
}

- (instancetype)initWithFrame:(CGRect)frame
{
    if ((self = [super initWithFrame:frame])) {
        super.backgroundColor = [UIColor clearColor];
        darkMode = @"";
    }
    return self;
}

- (void)setAdView:(GADUnifiedNativeAdView *)view {
    self.nativeAdView = view;
    
    // Add new ad view
    [self.nativeAdView setTranslatesAutoresizingMaskIntoConstraints:YES];
    
    [self addSubview: self.nativeAdView];
}

#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wobjc-missing-super-calls"
#pragma clang diagnostic pop

- (void)loadNative
{
    
}

- (void)setTestDevices:(NSArray *)testDevices
{
    _testDevices = RNAdMobProcessTestDevices(testDevices, kGADSimulatorID);
    GADMobileAds.sharedInstance.requestConfiguration.testDeviceIdentifiers = _testDevices;
}

- (void)setAdUnitID:(NSString *)adUnitID
{
    _adUnitID = adUnitID;
    
    UIWindow *keyWindow = [[UIApplication sharedApplication] keyWindow];
    UIViewController *rootViewController = [keyWindow rootViewController];
    UIViewController *rvc = rootViewController;
    
    
    
    NSArray *nibObjects =
    [[NSBundle mainBundle] loadNibNamed:@"UnifiedNativeAdView" owner:nil options:nil];
    [self setAdView:[nibObjects firstObject]];
    
    GADVideoOptions *videoOptions = [[GADVideoOptions alloc] init];
    videoOptions.startMuted = YES;
    
    adLoader = [[GADAdLoader alloc] initWithAdUnitID:_adUnitID
                                  rootViewController:rvc
                                             adTypes:@[ kGADAdLoaderAdTypeUnifiedNative ]
                                             options:@[ videoOptions ]];
    adLoader.delegate = self;
    [adLoader loadRequest:[GADRequest request]];
    
    adInit = YES;
    language = [[NSLocale preferredLanguages] firstObject];
    if ([[language substringToIndex:2] isEqualToString:@"tr"]) {
        UILabel *adAttr = (UILabel *)[self.nativeAdView viewWithTag:1];
        [adAttr setText:@"Reklam"];
        [adAttr sizeToFit];
    }
    
    [self changeColors];
}

- (void)setNightMode:(NSString *)nightMode {
    _nightMode = nightMode;
    
    if (adInit)
        [self changeColors];
}

-(void)changeColors {
    if (![darkMode isEqualToString:_nightMode]) { // mode changed (light/dark)
        if ([_nightMode isEqual: @"X"]) { // dark mode
            self.nativeAdView.backgroundColor = [UIColor colorWithRed:48/255.0 green:48/255.0 blue:48/255.0 alpha:1.0];
            ((UILabel *)self.nativeAdView.headlineView).textColor = [UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:1.0];
            ((UILabel *)self.nativeAdView.bodyView).textColor = [UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:1.0];
            ((UILabel *)self.nativeAdView.storeView).textColor = [UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:1.0];
            ((UILabel *)self.nativeAdView.priceView).textColor = [UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:1.0];
            ((UILabel *)self.nativeAdView.advertiserView).textColor = [UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:1.0];
            UILabel *adAttr = (UILabel *)[self.nativeAdView viewWithTag:1];
            adAttr.textColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:1.0];
            adAttr.backgroundColor = [UIColor colorWithRed:77/255.0 green:77/255.0 blue:77/255.0 alpha:1.0];
        } else { // light mode
            self.nativeAdView.backgroundColor = [UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:1.0];
            ((UILabel *)self.nativeAdView.headlineView).textColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:1.0];
            ((UILabel *)self.nativeAdView.bodyView).textColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:1.0];
            ((UILabel *)self.nativeAdView.storeView).textColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:1.0];
            ((UILabel *)self.nativeAdView.priceView).textColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:1.0];
            ((UILabel *)self.nativeAdView.advertiserView).textColor = [UIColor colorWithRed:0.0 green:0.0 blue:0.0 alpha:1.0];
            UILabel *adAttr = (UILabel *)[self.nativeAdView viewWithTag:1];
            adAttr.textColor = [UIColor colorWithRed:1.0 green:1.0 blue:1.0 alpha:1.0];
            adAttr.backgroundColor = [UIColor colorWithRed:204/255.0 green:204/255.0 blue:204/255.0 alpha:1.0];
        }
    }
    darkMode = _nightMode;
}

-(void)layoutSubviews
{
    [super layoutSubviews];
    self.nativeAdView.frame = self.bounds;
}

#pragma mark GADAdLoaderDelegate implementation

- (void)adLoader:(GADAdLoader *)adLoader didFailToReceiveAdWithError:(GADRequestError *)error {
    NSLog(@"%@ failed with error: %@", adLoader, error);
    if (self.onAdFailedToLoad) {
        self.onAdFailedToLoad(@{ @"error": @{ @"message": [error localizedDescription] } });
    }
}

#pragma mark GADUnifiedNativeAdLoaderDelegate implementation

- (void)adLoader:(GADAdLoader *)adLoader didReceiveUnifiedNativeAd:(GADUnifiedNativeAd *)nativeAd {

    if (self.onAdLoaded) {
        self.onAdLoaded(@{});
    }
    
    GADUnifiedNativeAdView *nativeAdView = self.nativeAdView;
    
    // Deactivate the height constraint that was set when the previous video ad loaded.
    self.heightConstraint.active = NO;
    
    nativeAdView.nativeAd = nativeAd;
    
    // Set ourselves as the ad delegate to be notified of native ad events.
    nativeAd.delegate = self;
    
    // Populate the native ad view with the native ad assets.
    // The headline and mediaContent are guaranteed to be present in every native ad.
    ((UILabel *)nativeAdView.headlineView).text = nativeAd.headline;
    nativeAdView.mediaView.mediaContent = nativeAd.mediaContent;
    
    // This app uses a fixed width for the GADMediaView and changes its height
    // to match the aspect ratio of the media content it displays.
    if (nativeAd.mediaContent.aspectRatio > 0) {
        self.heightConstraint =
        [NSLayoutConstraint constraintWithItem:nativeAdView.mediaView
                                     attribute:NSLayoutAttributeHeight
                                     relatedBy:NSLayoutRelationEqual
                                        toItem:nativeAdView.mediaView
                                     attribute:NSLayoutAttributeWidth
                                    multiplier:(1 / nativeAd.mediaContent.aspectRatio)
                                      constant:0];
        self.heightConstraint.active = YES;
    }
    
    if (nativeAd.mediaContent.hasVideoContent) {
        // By acting as the delegate to the GADVideoController, this ViewController
        // receives messages about events in the video lifecycle.
        nativeAd.mediaContent.videoController.delegate = self;
        
    } else {
    }
    
    // These assets are not guaranteed to be present. Check that they are before
    // showing or hiding them.
    ((UILabel *)nativeAdView.bodyView).text = nativeAd.body;
    nativeAdView.bodyView.hidden = nativeAd.body ? NO : YES;
    
    /*[((UIButton *)nativeAdView.callToActionView) setTitle:nativeAd.callToAction
                                                 forState:UIControlStateNormal];
    nativeAdView.callToActionView.hidden = nativeAd.callToAction ? NO : YES;*/
    
    ((UIImageView *)nativeAdView.iconView).image = nativeAd.icon.image;
    nativeAdView.iconView.hidden = nativeAd.icon ? NO : YES;
    
    ((UIImageView *)nativeAdView.starRatingView).image = [self imageForStars:nativeAd.starRating];
    nativeAdView.starRatingView.hidden = nativeAd.starRating ? NO : YES;
    
    /*((UILabel *)nativeAdView.storeView).text = nativeAd.store;
    nativeAdView.storeView.hidden = nativeAd.store ? NO : YES;
    
    ((UILabel *)nativeAdView.priceView).text = nativeAd.price;
    nativeAdView.priceView.hidden = nativeAd.price ? NO : YES;*/
    
    ((UILabel *)nativeAdView.advertiserView).text = nativeAd.advertiser;
    nativeAdView.advertiserView.hidden = nativeAd.advertiser ? NO : YES;
    
    // In order for the SDK to process touch events properly, user interaction
    // should be disabled.
    //nativeAdView.callToActionView.userInteractionEnabled = NO;
}

/// Gets an image representing the number of stars. Returns nil if rating is
/// less than 3.5 stars.
- (UIImage *)imageForStars:(NSDecimalNumber *)numberOfStars {
    double starRating = numberOfStars.doubleValue;
    if (starRating >= 5) {
        return [UIImage imageNamed:@"stars_5"];
    } else if (starRating >= 4.5) {
        return [UIImage imageNamed:@"stars_4_5"];
    } else if (starRating >= 4) {
        return [UIImage imageNamed:@"stars_4"];
    } else if (starRating >= 3.5) {
        return [UIImage imageNamed:@"stars_3_5"];
    } else {
        return nil;
    }
}

#pragma mark GADVideoControllerDelegate implementation

- (void)videoControllerDidEndVideoPlayback:(GADVideoController *)videoController {
}

#pragma mark GADUnifiedNativeAdDelegate

- (void)nativeAdDidRecordClick:(GADUnifiedNativeAd *)nativeAd {
    NSLog(@"%s", __PRETTY_FUNCTION__);
}

- (void)nativeAdDidRecordImpression:(GADUnifiedNativeAd *)nativeAd {
    NSLog(@"%s", __PRETTY_FUNCTION__);
}

- (void)nativeAdWillPresentScreen:(GADUnifiedNativeAd *)nativeAd {
    NSLog(@"%s", __PRETTY_FUNCTION__);
}

- (void)nativeAdWillDismissScreen:(GADUnifiedNativeAd *)nativeAd {
    NSLog(@"%s", __PRETTY_FUNCTION__);
}

- (void)nativeAdDidDismissScreen:(GADUnifiedNativeAd *)nativeAd {
    NSLog(@"%s", __PRETTY_FUNCTION__);
}

- (void)nativeAdWillLeaveApplication:(GADUnifiedNativeAd *)nativeAd {
    NSLog(@"%s", __PRETTY_FUNCTION__);
}

@end
