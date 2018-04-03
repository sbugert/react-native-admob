#import "RCTConvert+GADGender.h"

@implementation RCTConvert (GoogleMobileAds)

+ (GADGender)GADGender:(id)json
{
    NSString *gender = [self NSString:json];
    if ([gender isEqualToString:@"male"]) {
        return kGADGenderMale;
    } else if ([gender isEqualToString:@"female"]) {
        return kGADGenderFemale;
    } else {
        return kGADGenderUnknown;;
    }
}

@end
