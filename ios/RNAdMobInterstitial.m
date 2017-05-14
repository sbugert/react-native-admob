#import "RNAdMobInterstitial.h"

@implementation RNAdMobInterstitial {
  GADInterstitial  *_interstitial;
  NSString *_adUnitID;
  NSArray *_testDeviceIDs;
  RCTResponseSenderBlock _requestAdCallback;
  RCTResponseSenderBlock _showAdCallback;
}

@synthesize bridge = _bridge;

- (dispatch_queue_t)methodQueue
{
  return dispatch_get_main_queue();
}

RCT_EXPORT_MODULE();

#pragma mark exported methods

RCT_EXPORT_METHOD(setAdUnitID:(NSString *)adUnitID)
{
  _adUnitID = adUnitID;
}

RCT_EXPORT_METHOD(setTestDeviceIDs:(NSArray *)testDeviceIDs)
{
  for (NSString* testDeviceID in testDeviceIDs){
    if (testDeviceID == (id)[NSNull null] || testDeviceID.length == 0) {
      RCTLogError(@"Test device ID cannot be null.");
      return;
    }
  }
  _testDeviceIDs = testDeviceIDs;
}


RCT_EXPORT_METHOD(requestAd:(RCTResponseSenderBlock)callback)
{
  if ([_interstitial hasBeenUsed] || _interstitial == nil) {
    _requestAdCallback = callback;

    _interstitial = [[GADInterstitial alloc] initWithAdUnitID:_adUnitID];
    _interstitial.delegate = self;

    GADRequest *request = [GADRequest request];

    if (_testDeviceIDs) {
      NSMutableArray *testDevices = [NSMutableArray new];
      for (NSString* testDeviceID in _testDeviceIDs){
        if ([testDeviceID isEqualToString:@"EMULATOR"]) {
          [testDevices addObject:kGADSimulatorID];
        } else {
          [testDevices addObject:testDeviceID];
        }
      }
      request.testDevices = testDevices;
    }
    [_interstitial loadRequest:request];
  } else {
    callback(@[@"Ad is already loaded."]); // TODO: make proper error via RCTUtils.h
  }
}

RCT_EXPORT_METHOD(showAd:(RCTResponseSenderBlock)callback)
{
  if ([_interstitial isReady]) {
    _showAdCallback = callback;
    [_interstitial presentFromRootViewController:[UIApplication sharedApplication].delegate.window.rootViewController];
  }
  else {
    callback(@[@"Ad is not ready."]); // TODO: make proper error via RCTUtils.h
  }
}

RCT_EXPORT_METHOD(isReady:(RCTResponseSenderBlock)callback)
{
  callback(@[[NSNumber numberWithBool:[_interstitial isReady]]]);
}


#pragma mark delegate events

- (void)interstitialDidReceiveAd:(GADInterstitial *)ad {
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"interstitialDidLoad" body:nil];
  _requestAdCallback(@[[NSNull null]]);
}

- (void)interstitial:(GADInterstitial *)interstitial
didFailToReceiveAdWithError:(GADRequestError *)error {
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"interstitialDidFailToLoad" body:@{@"name": [error description]}];
  _requestAdCallback(@[[error description]]);
}

- (void)interstitialWillPresentScreen:(GADInterstitial *)ad {
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"interstitialDidOpen" body:nil];
  _showAdCallback(@[[NSNull null]]);
}

- (void)interstitialDidDismissScreen:(GADInterstitial *)ad {
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"interstitialDidClose" body:nil];
}

- (void)interstitialWillLeaveApplication:(GADInterstitial *)ad {
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"interstitialWillLeaveApplication" body:nil];
}

@end
