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
        GADAdSize resultAdSize = kGADAdSizeInvalid;

        NSRange searchedRange = NSMakeRange(0, [adSize length]);
        NSString *pattern = @"^[0-9]+(x)[0-9]+$";
        NSError *error = nil;

        NSRegularExpression *regex = [NSRegularExpression regularExpressionWithPattern:pattern options:0 error:&error];
        NSArray *matches = [regex matchesInString:adSize options:0 range:searchedRange];
        NSUInteger result = [matches count];

        if (result) {
            NSArray *dimentions = [adSize componentsSeparatedByString:@"x"];
            NSUInteger width = [[dimentions objectAtIndex:0] intValue];
            NSUInteger height = [[dimentions objectAtIndex:1] intValue];
            resultAdSize = GADAdSizeFromCGSize(CGSizeMake(width, height));
        }

        return resultAdSize;
    }
}

@end
