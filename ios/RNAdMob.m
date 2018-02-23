#import "RNAdMob.h"

@implementation RNAdMob

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(setAppId:(NSString *)id)
{
    [GADMobileAds configureWithApplicationID:id];
}

@end
