#import "RCTConvert+GADMaxAdContentRating.h"

@implementation RCTConvert (GoogleMobileAds)

+ (GADMaxAdContentRating)GADMaxAdContentRating:(id)json
{
    NSString *rating = [self NSString:json];
    if ([rating isEqualToString:@"parentalGuidance"]) {
        return GADMaxAdContentRatingParentalGuidance;
    } else if ([rating isEqualToString:@"teen"]) {
        return GADMaxAdContentRatingTeen;
    } else if ([rating isEqualToString:@"matureAudience"]) {
        return GADMaxAdContentRatingMatureAudience;
    } else {
        return GADMaxAdContentRatingGeneral;
    }
}

@end
