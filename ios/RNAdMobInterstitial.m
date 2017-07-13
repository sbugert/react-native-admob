#import "RNAdMobInterstitial.h"

#if __has_include(<React/RCTUtils.h>)
#import <React/RCTUtils.h>
#else
#import "RCTUtils.h"
#endif

@implementation RNAdMobInterstitial {
  GADInterstitial  *_interstitial;
  NSString *_adUnitID;
  NSArray *_testDevices;
  RCTResponseSenderBlock _requestAdCallback;
  RCTResponseSenderBlock _showAdCallback;
  BOOL hasListeners;
}

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

- (NSArray<NSString *> *)supportedEvents
{
  return @[
           @"interstitialDidLoad",
           @"interstitialDidFailToLoad",
           @"interstitialDidOpen",
           @"interstitialDidClose",
           @"interstitialWillLeaveApplication"];
}

#pragma mark exported methods

RCT_EXPORT_METHOD(setAdUnitID:(NSString *)adUnitID)
{
  _adUnitID = adUnitID;
}

RCT_EXPORT_METHOD(setTestDevices:(NSArray *)testDevices)
{
  _testDevices = testDevices;
}

RCT_EXPORT_METHOD(requestAd:(RCTResponseSenderBlock)callback)
{
  if ([_interstitial hasBeenUsed] || _interstitial == nil) {
    _requestAdCallback = callback;

    _interstitial = [[GADInterstitial alloc] initWithAdUnitID:_adUnitID];
    if (hasListeners) {
      _interstitial.delegate = self;
    }

    GADRequest *request = [GADRequest request];
    request.testDevices = _testDevices;
    [_interstitial loadRequest:request];
  } else {
    callback(@[RCTMakeError(@"Ad is already loaded.", nil, nil)]);
  }
}

RCT_EXPORT_METHOD(showAd:(RCTResponseSenderBlock)callback)
{
  if ([_interstitial isReady]) {
    _showAdCallback = callback;
    [_interstitial presentFromRootViewController:[UIApplication sharedApplication].delegate.window.rootViewController];
  }
  else {
    callback(@[RCTMakeError(@"Ad is not ready.", nil, nil)]);
  }
}

RCT_EXPORT_METHOD(isReady:(RCTResponseSenderBlock)callback)
{
  callback(@[[NSNumber numberWithBool:[_interstitial isReady]]]);
}

- (NSDictionary<NSString *,id> *)constantsToExport
{
  return @{
           @"simulatorId": kGADSimulatorID
           };
}

- (void)startObserving
{
  hasListeners = YES;
  if (_interstitial) {
    _interstitial.delegate = self;
  }
}

- (void)stopObserving
{
  hasListeners = NO;
  if (_interstitial) {
    _interstitial.delegate = nil;
  }
}

#pragma mark GADInterstitialDelegate

- (void)interstitialDidReceiveAd:(GADInterstitial *)ad {
  if (hasListeners) {
    [self sendEventWithName:@"interstitialDidLoad" body:nil];
  }
  _requestAdCallback(@[[NSNull null]]);
}

- (void)interstitial:(GADInterstitial *)interstitial
didFailToReceiveAdWithError:(GADRequestError *)error {
  if (hasListeners) {
    [self sendEventWithName:@"interstitialDidFailToLoad" body:@{@"name": [error description]}];
  }
  _requestAdCallback(@[[error description]]);
}

- (void)interstitialWillPresentScreen:(GADInterstitial *)ad {
  if (hasListeners){
    [self sendEventWithName:@"interstitialDidOpen" body:nil];
  }
  _showAdCallback(@[[NSNull null]]);
}

- (void)interstitialDidDismissScreen:(GADInterstitial *)ad {
  if (hasListeners) {
    [self sendEventWithName:@"interstitialDidClose" body:nil];
  }
}

- (void)interstitialWillLeaveApplication:(GADInterstitial *)ad {
  if (hasListeners) {
    [self sendEventWithName:@"interstitialWillLeaveApplication" body:nil];
  }
}

@end
