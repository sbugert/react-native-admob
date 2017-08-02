#import "RNGADBannerView.h"

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#import <React/UIView+React.h>
#else
#import "RCTBridgeModule.h"
#import "UIView+React.h"
#endif

@implementation RNGADBannerView
{
    GADBannerView *_bannerView;
}

- (void)dealloc
{
    _bannerView.delegate = nil;
    _bannerView.adSizeDelegate = nil;
}

- (instancetype)initWithFrame:(CGRect)frame
{
    if ((self = [super initWithFrame:frame])) {
        super.backgroundColor = [UIColor clearColor];
        UIWindow *keyWindow = [[UIApplication sharedApplication] keyWindow];
        UIViewController *rootViewController = [keyWindow rootViewController];
        _bannerView = [[GADBannerView alloc] initWithAdSize:kGADAdSizeBanner];
        _bannerView.delegate = self;
        _bannerView.adSizeDelegate = self;
        _bannerView.rootViewController = rootViewController;
        [self addSubview:_bannerView];
    }
    return self;
}

- (void)loadBanner {
    if(self.onSizeChange) {
        CGSize size = CGSizeFromGADAdSize(_bannerView.adSize);
        if(!CGSizeEqualToSize(size, self.bounds.size)) {
            self.onSizeChange(@{
                                @"width": @(size.width),
                                @"height": @(size.height)
                                });
        }
    }
    GADRequest *request = [GADRequest request];
    request.testDevices = _testDevices;
    [_bannerView loadRequest:request];
}

-(void)layoutSubviews
{
    [super layoutSubviews];
    _bannerView.frame = self.bounds;
}

# pragma mark GADBannerViewDelegate

/// Tells the delegate an ad request loaded an ad.
- (void)adViewDidReceiveAd:(__unused GADBannerView *)adView {
   if (self.onAdViewDidReceiveAd) {
       self.onAdViewDidReceiveAd(@{});
   }
}

/// Tells the delegate an ad request failed.
- (void)adView:(__unused GADBannerView *)adView
didFailToReceiveAdWithError:(GADRequestError *)error {
    if (self.onDidFailToReceiveAdWithError) {
        self.onDidFailToReceiveAdWithError(@{@"error": [error localizedDescription]});
    }
}

/// Tells the delegate that a full screen view will be presented in response
/// to the user clicking on an ad.
- (void)adViewWillPresentScreen:(__unused GADBannerView *)adView {
    if (self.onAdViewWillPresentScreen) {
        self.onAdViewWillPresentScreen(@{});
    }
}

/// Tells the delegate that the full screen view will be dismissed.
- (void)adViewWillDismissScreen:(__unused GADBannerView *)adView {
    if (self.onAdViewWillDismissScreen) {
        self.onAdViewWillDismissScreen(@{});
    }
}

/// Tells the delegate that the full screen view has been dismissed.
- (void)adViewDidDismissScreen:(__unused GADBannerView *)adView {
    if (self.onAdViewDidDismissScreen) {
        self.onAdViewDidDismissScreen(@{});
    }
}

/// Tells the delegate that a user click will open another app (such as
/// the App Store), backgrounding the current app.
- (void)adViewWillLeaveApplication:(__unused GADBannerView *)adView {
    if (self.onAdViewWillLeaveApplication) {
        self.onAdViewWillLeaveApplication(@{});
    }
}

# pragma mark GADAdSizeDelegate

- (void)adView:(__unused GADBannerView *)bannerView willChangeAdSizeTo:(GADAdSize)size
{
    CGSize adSize = CGSizeFromGADAdSize(size);
    self.onSizeChange(@{
                              @"width": @(adSize.width),
                              @"height": @(adSize.height) });
}

@end
