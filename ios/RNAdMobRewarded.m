#import "RNAdMobRewarded.h"

@implementation RNAdMobRewarded {
  NSString *_adUnitID;
  NSString *_testDeviceID;
  RCTResponseSenderBlock _requestAdCallback;
  RCTResponseSenderBlock _showAdCallback;
}

+ (void)initialize
{
  NSLog(@"initialize");
  [GADRewardBasedVideoAd sharedInstance].delegate = self;
}

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

RCT_EXPORT_METHOD(setTestDeviceID:(NSString *)testDeviceID)
{
  _testDeviceID = testDeviceID;
}

RCT_EXPORT_METHOD(requestAd:(RCTResponseSenderBlock)callback)
{
  _requestAdCallback = callback;
  [GADRewardBasedVideoAd sharedInstance].delegate = self;
  GADRequest *request = [GADRequest request];
  if(_testDeviceID) {
    if([_testDeviceID isEqualToString:@"EMULATOR"]) {
      request.testDevices = @[kGADSimulatorID];
    } else {
      request.testDevices = @[_testDeviceID];
    }
  }
  [[GADRewardBasedVideoAd sharedInstance] loadRequest:request
                                         withAdUnitID:_adUnitID];
}

RCT_EXPORT_METHOD(showAd:(RCTResponseSenderBlock)callback)
{
  if ([[GADRewardBasedVideoAd sharedInstance] isReady]) {
    _showAdCallback = callback;
    [[GADRewardBasedVideoAd sharedInstance] presentFromRootViewController:[UIApplication sharedApplication].delegate.window.rootViewController];
  }
  else {
    callback(@[@"Ad is not ready."]); // TODO: make proper error via RCTUtils.h
  }
}

RCT_EXPORT_METHOD(isReady:(RCTResponseSenderBlock)callback)
{
  callback(@[[NSNumber numberWithBool:[[GADRewardBasedVideoAd sharedInstance] isReady]]]);
}


#pragma mark delegate events

- (void)rewardBasedVideoAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd
   didRewardUserWithReward:(GADAdReward *)reward {
  if (self.onRewardedVideoDidRewardUser) {
    self.onRewardedVideoDidRewardUser(@{@"type": reward.type, @"amount": reward.amount});
  }
}

- (void)rewardBasedVideoAdDidReceiveAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  if (self.onRewardedVideoDidLoad) {
    self.onRewardedVideoDidLoad(@{});
  }
  _requestAdCallback(@[[NSNull null]]);
}

- (void)rewardBasedVideoAdDidOpen:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  if (self.onRewardedVideoDidOpen) {
    self.onRewardedVideoDidOpen(@{});
  }
  _showAdCallback(@[[NSNull null]]);
}

- (void)rewardBasedVideoAdDidStartPlaying:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  NSLog(@"Reward based video ad started playing.");
}

- (void)rewardBasedVideoAdDidClose:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  if (self.onRewardedVideoDidClose) {
    self.onRewardedVideoDidClose(@{});
  }
}

- (void)rewardBasedVideoAdWillLeaveApplication:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  if (self.onRewardedVideoWillLeaveApplication) {
    self.onRewardedVideoWillLeaveApplication(@{});
  }
}

- (void)rewardBasedVideoAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd
    didFailToLoadWithError:(NSError *)error {
  if (self.onRewardedVideoDidFailToLoad) {
    self.onRewardedVideoDidFailToLoad(@{@"name": [error description]});
  }
  _requestAdCallback(@[[error description]]);
}

@end
