#import "RCTEventDispatcher.h"
@import GoogleMobileAds;

@class RCTEventDispatcher;

@interface BannerView : UIView <GADBannerViewDelegate>

@property (nonatomic, copy) NSString *bannerSize;
@property (nonatomic, copy) NSString *adUnitID;

- (instancetype)initWithEventDispatcher:(RCTEventDispatcher *)eventDispatcher NS_DESIGNATED_INITIALIZER;
- (GADAdSize)getAdSizeFromString:(NSString *)bannerSize;
- (void)loadBanner;

@end
