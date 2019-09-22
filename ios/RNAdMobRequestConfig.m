#import <React/RCTUtils.h>

#import "RNAdMobRequestConfig.h"

@import GoogleMobileAds;

@implementation RNAdMobRequestConfig

RCT_EXPORT_MODULE();

+ (BOOL)requiresMainQueueSetup
{
    return NO;
}

RCT_EXPORT_METHOD(setMaxAdContentRating:(GADMaxAdContentRating)maxAdContentRating)
{
    [GADMobileAds.sharedInstance.requestConfiguration
     setMaxAdContentRating:maxAdContentRating];
}

RCT_EXPORT_METHOD(tagForChildDirectedTreatment:(BOOL)value)
{
    [GADMobileAds.sharedInstance.requestConfiguration
     tagForChildDirectedTreatment:value];
}

RCT_EXPORT_METHOD(tagForUnderAgeOfConsent:(BOOL)value)
{
    [GADMobileAds.sharedInstance.requestConfiguration
     tagForUnderAgeOfConsent:value];
}



@end
