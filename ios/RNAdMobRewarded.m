#import "RNAdMobRewarded.h"

@implementation RNAdMobRewarded {
  NSString *_adUnitID;
  NSArray *_testDeviceIDs;
  RCTResponseSenderBlock _requestAdCallback;
  RCTResponseSenderBlock _showAdCallback;
}

@synthesize bridge = _bridge;

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
  _requestAdCallback = callback;
  [GADRewardBasedVideoAd sharedInstance].delegate = self;
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
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoDidRewardUser" body:@{@"type": reward.type, @"amount": reward.amount}];
}

- (void)rewardBasedVideoAdDidReceiveAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoDidLoad" body:nil];
  _requestAdCallback(@[[NSNull null]]);
}

- (void)rewardBasedVideoAdDidOpen:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoDidOpen" body:nil];
  _showAdCallback(@[[NSNull null]]);
}

- (void)rewardBasedVideoAdDidStartPlaying:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  NSLog(@"Reward based video ad started playing.");
}

- (void)rewardBasedVideoAdDidClose:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoDidClose" body:nil];
}

- (void)rewardBasedVideoAdWillLeaveApplication:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoWillLeaveApplication" body:nil];
}

- (void)rewardBasedVideoAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd
    didFailToLoadWithError:(NSError *)error {
  [self.bridge.eventDispatcher sendDeviceEventWithName:@"rewardedVideoDidFailToLoad" body:@{@"name": [error description]}];
  _requestAdCallback(@[[error description]]);
}

@end
