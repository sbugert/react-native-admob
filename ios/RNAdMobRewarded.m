#import "RNAdMobRewarded.h"

#if __has_include(<React/RCTUtils.h>)
#import <React/RCTUtils.h>
#else
#import "RCTUtils.h"
#endif

@implementation RNAdMobRewarded {
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
             @"rewardedVideoDidRewardUser",
             @"rewardedVideoDidLoad",
             @"rewardedVideoDidFailToLoad",
             @"rewardedVideoDidOpen",
             @"rewardedVideoDidStartPlaying",
             @"rewardedVideoDidClose",
             @"rewardedVideoWillLeaveApplication"];
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
    _requestAdCallback = callback;
    [GADRewardBasedVideoAd sharedInstance].delegate = self;
    GADRequest *request = [GADRequest request];
    request.testDevices = _testDevices;
    [[GADRewardBasedVideoAd sharedInstance] loadRequest:request
                                           withAdUnitID:_adUnitID];
}

RCT_EXPORT_METHOD(showAd:(RCTResponseSenderBlock)callback)
{
    if ([[GADRewardBasedVideoAd sharedInstance] isReady]) {
        _showAdCallback = callback;
        UIWindow *keyWindow = [[UIApplication sharedApplication] keyWindow];
        UIViewController *rootViewController = [keyWindow rootViewController];
        [[GADRewardBasedVideoAd sharedInstance] presentFromRootViewController:rootViewController];
    }
    else {
        callback(@[RCTMakeError(@"Ad is not ready", nil, nil)]);
    }
}

RCT_EXPORT_METHOD(isReady:(RCTResponseSenderBlock)callback)
{
    callback(@[[NSNumber numberWithBool:[[GADRewardBasedVideoAd sharedInstance] isReady]]]);
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
    [GADRewardBasedVideoAd sharedInstance].delegate = self;
}

- (void)stopObserving
{
    hasListeners = NO;
    [GADRewardBasedVideoAd sharedInstance].delegate = nil;
}

#pragma mark GADRewardBasedVideoAdDelegate

- (void)rewardBasedVideoAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd
   didRewardUserWithReward:(GADAdReward *)reward {
    if (hasListeners) {
        [self sendEventWithName:@"rewardedVideoDidRewardUser" body:@{@"type": reward.type, @"amount": reward.amount}];
    }
}

- (void)rewardBasedVideoAdDidReceiveAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
    if (hasListeners) {
        [self sendEventWithName:@"rewardedVideoDidLoad" body:nil];
    }
    _requestAdCallback(@[[NSNull null]]);
}

- (void)rewardBasedVideoAdDidOpen:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
    if (hasListeners) {
        [self sendEventWithName:@"rewardedVideoDidOpen" body:nil];
    }
    _showAdCallback(@[[NSNull null]]);
}

- (void)rewardBasedVideoAdDidStartPlaying:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
    if (hasListeners) {
        [self sendEventWithName:@"rewardedVideoDidStartPlaying" body:nil];
    }
}

- (void)rewardBasedVideoAdDidClose:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
    if (hasListeners) {
        [self sendEventWithName:@"rewardedVideoDidClose" body:nil];
    }
}

- (void)rewardBasedVideoAdWillLeaveApplication:(GADRewardBasedVideoAd *)rewardBasedVideoAd {
    if (hasListeners) {
        [self sendEventWithName:@"rewardedVideoWillLeaveApplication" body:nil];
    }
}

- (void)rewardBasedVideoAd:(GADRewardBasedVideoAd *)rewardBasedVideoAd
    didFailToLoadWithError:(NSError *)error {
    if (hasListeners) {
        [self sendEventWithName:@"rewardedVideoDidFailToLoad" body:@{@"name": [error description]}];
    }
    _requestAdCallback(@[RCTJSErrorFromNSError(error)]);
}

@end
