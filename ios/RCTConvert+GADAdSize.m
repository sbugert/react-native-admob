#import "RCTConvert+GADAdSize.h"

@implementation RCTConvert (GADAdSize)

+ (GADAdSize)GADAdSize:(id)json
{
    NSString *adSize = [self NSString:json];
    if ([adSize isEqualToString:@"banner"]) {
        return kGADAdSizeBanner;
    } else if ([adSize isEqualToString:@"fullBanner"]) {
        return kGADAdSizeFullBanner;
    } else if ([adSize isEqualToString:@"largeBanner"]) {
        return kGADAdSizeLargeBanner;
    } else if ([adSize isEqualToString:@"fluid"]) {
        return kGADAdSizeFluid;
    } else if ([adSize isEqualToString:@"skyscraper"]) {
        return kGADAdSizeSkyscraper;
    } else if ([adSize isEqualToString:@"leaderboard"]) {
        return kGADAdSizeLeaderboard;
    } else if ([adSize isEqualToString:@"mediumRectangle"]) {
        return kGADAdSizeMediumRectangle;
    } else if ([adSize isEqualToString:@"smartBannerPortrait"]) {
        return kGADAdSizeSmartBannerPortrait;
    } else if ([adSize isEqualToString:@"smartBannerLandscape"]) {
        return kGADAdSizeSmartBannerLandscape;
    }
    else {
        return kGADAdSizeInvalid;
    }
}

+ (GADAdSize)parseCustomAdSize:(NSString *)bannerSizeString
{
    GADAdSize _localSize = kGADAdSizeBanner;
    @try {
        //try parse width x height string to GADAdSize
        if ([bannerSizeString rangeOfString:@"x"].location != NSNotFound)
        {
            NSArray *array = [bannerSizeString componentsSeparatedByString:@"x"];
            int width = [ array[0] intValue];
            int height = [ array[1] intValue];
            _localSize = GADAdSizeFromCGSize(CGSizeMake(width, height));
        }
    }
    @catch (NSException *exception) {
        NSLog(@"%@", exception.reason);
    }
    return _localSize;
}

@end
