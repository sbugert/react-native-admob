#import "RNAdMobInterstitial.h"

@implementation RNAdMobInterstitial {
  GADInterstitial  *_interstitial;
  NSString *_adUnitId;
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

- (void)interstitialDidReceiveAd:(GADInterstitial *)ad {
  [ad presentFromRootViewController:[UIApplication sharedApplication].delegate.window.rootViewController];
}

RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(tryShowNewInterstitial:(nullable NSString *)testDeviceId)
{
  _interstitial = [[GADInterstitial alloc] initWithAdUnitID:_adUnitId];
  _interstitial.delegate = self;
  GADRequest *request = [GADRequest request];
  if(testDeviceId) {
    request.testDevices = @[testDeviceId];
  }
  [_interstitial loadRequest:request];
}

RCT_EXPORT_METHOD(setAdUnitId:(NSString *)adUnitId)
{
  _adUnitId = adUnitId;
}
@end