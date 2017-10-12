#import <React/RCTConvert.h>

#if __has_include(<GoogleMobileAds/GoogleMobileAds.h>)
#import <GoogleMobileAds/GoogleMobileAds.h>
#else
@import GoogleMobileAds;
#endif

@interface RCTConvert (GADAdSize)

+ (GADAdSize)GADAdSize:(id)json;

@end
