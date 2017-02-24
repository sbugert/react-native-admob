'use strict';

import {
  NativeModules,
  DeviceEventEmitter,
} from 'react-native';

const RNAdMobRewarded = NativeModules.RNAdMobRewarded;

const eventHandlers = {
  rewardedVideoDidRewardUser: new Map(),
  rewardedVideoDidLoad: new Map(),
  rewardedVideoDidFailToLoad: new Map(),
  rewardedVideoDidOpen: new Map(),
  rewardedVideoDidClose: new Map(),
  rewardedVideoWillLeaveApplication: new Map(),
};

const addEventListener = (type, handler) => {
  switch (type) {
    case 'rewardedVideoDidRewardUser':
      eventHandlers[type].set(handler, DeviceEventEmitter.addListener(type, (type, amount) => {
        handler(type, amount);
      }));
      break;
    case 'rewardedVideoDidLoad':
      eventHandlers[type].set(handler, DeviceEventEmitter.addListener(type, handler));
      break;
    case 'rewardedVideoDidFailToLoad':
      eventHandlers[type].set(handler, DeviceEventEmitter.addListener(type, (error) => { handler(error); }));
      break;
    case 'rewardedVideoDidOpen':
      eventHandlers[type].set(handler, DeviceEventEmitter.addListener(type, handler));
      break;
    case 'rewardedVideoDidClose':
      eventHandlers[type].set(handler, DeviceEventEmitter.addListener(type, handler));
      break;
    case 'rewardedVideoWillLeaveApplication':
      eventHandlers[type].set(handler, DeviceEventEmitter.addListener(type, handler));
      break;
    default:
      console.log(`Event with type ${type} does not exist.`);
  }
}

const removeEventListener = (type, handler) => {
  if (!eventHandlers[type].has(handler)) {
    return;
  }
  eventHandlers[type].get(handler).remove();
  eventHandlers[type].delete(handler);
}

const removeAllListeners = () => {
  DeviceEventEmitter.removeAllListeners('rewardedVideoDidRewardUser');
  DeviceEventEmitter.removeAllListeners('rewardedVideoDidLoad');
  DeviceEventEmitter.removeAllListeners('rewardedVideoDidFailToLoad');
  DeviceEventEmitter.removeAllListeners('rewardedVideoDidOpen');
  DeviceEventEmitter.removeAllListeners('rewardedVideoDidClose');
  DeviceEventEmitter.removeAllListeners('rewardedVideoWillLeaveApplication');
};

module.exports = {
  ...RNAdMobRewarded,
  requestAd: (cb = () => {}) => RNAdMobRewarded.requestAd(cb), // requestAd callback is optional
  showAd: (cb = () => {}) => RNAdMobRewarded.showAd(cb),       // showAd callback is optional
  addEventListener,
  removeEventListener,
  removeAllListeners,
};
