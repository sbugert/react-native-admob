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
  RCTPromiseResolveBlock _requestAdResolve;
  RCTPromiseRejectBlock _requestAdReject;
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

RCT_EXPORT_METHOD(requestAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  _requestAdResolve = nil;
  _requestAdReject = nil;

  if ([_interstitial hasBeenUsed] || _interstitial == nil) {
    _requestAdResolve = resolve;
    _requestAdReject = reject;

    _interstitial = [[GADInterstitial alloc] initWithAdUnitID:_adUnitID];
    _interstitial.delegate = self;

    GADRequest *request = [GADRequest request];
    request.testDevices = _testDevices;
    [_interstitial loadRequest:request];
  } else {
    reject(@"E_AD_ALREADY_LOADED", @"Ad is already loaded.", nil);
  }
}

RCT_EXPORT_METHOD(showAd:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject)
{
  if ([_interstitial isReady]) {
    [_interstitial presentFromRootViewController:[UIApplication sharedApplication].delegate.window.rootViewController];
    resolve(nil);
  }
  else {
    reject(@"E_AD_NOT_READY", @"Ad is not ready.", nil);
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
}

- (void)stopObserving
{
  hasListeners = NO;
}

#pragma mark GADInterstitialDelegate

- (void)interstitialDidReceiveAd:(__unused GADInterstitial *)ad {
  if (hasListeners) {
    [self sendEventWithName:@"interstitialDidLoad" body:nil];
  }
  _requestAdResolve(nil);
}

- (void)interstitial:(__unused GADInterstitial *)interstitial didFailToReceiveAdWithError:(GADRequestError *)error {
  if (hasListeners) {
    NSDictionary *jsError = RCTJSErrorFromCodeMessageAndNSError(@"E_AD_REQUEST_FAILED", error.localizedDescription, error);
    [self sendEventWithName:@"interstitialDidFailToLoad" body:jsError];
  }
  _requestAdReject(@"E_AD_REQUEST_FAILED", error.localizedDescription, error);
}

- (void)interstitialWillPresentScreen:(__unused GADInterstitial *)ad {
  if (hasListeners){
    [self sendEventWithName:@"interstitialDidOpen" body:nil];
  }
}

- (void)interstitialDidDismissScreen:(__unused GADInterstitial *)ad {
  if (hasListeners) {
    [self sendEventWithName:@"interstitialDidClose" body:nil];
  }
}

- (void)interstitialWillLeaveApplication:(__unused GADInterstitial *)ad {
  if (hasListeners) {
    [self sendEventWithName:@"interstitialWillLeaveApplication" body:nil];
  }
}

@end
