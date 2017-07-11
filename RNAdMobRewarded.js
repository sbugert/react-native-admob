'use strict';

import {
  Platform,
  NativeModules,
  NativeEventEmitter,
} from 'react-native';

const RNAdMobRewarded = NativeModules.RNAdMobRewarded;

const adMobRewardedEmitter = new NativeEventEmitter(RNAdMobRewarded);

const eventHandlers = {};

const addEventListener = (type, handler) => {
  eventHandlers[type] = eventHandlers[type] || new Map();
  eventHandlers[type].set(handler, adMobRewardedEmitter.addListener(type, handler));
}

const removeEventListener = (type, handler) => {
  if (!eventHandlers[type].has(handler)) {
    return;
  }
  eventHandlers[type].get(handler).remove();
  eventHandlers[type].delete(handler);
}

const removeAllListeners = () => {
  const types = Object.keys(eventHandlers);
  types.forEach(type => (
    eventHandlers[type].forEach((subscription, key, map) => {
      subscription.remove();
      map.delete(key);
    })
  ));
};

module.exports = {
  ...RNAdMobRewarded,
  requestAd: (cb = () => {}) => RNAdMobRewarded.requestAd(cb), // requestAd callback is optional
  showAd: (cb = () => {}) => RNAdMobRewarded.showAd(cb),       // showAd callback is optional
  addEventListener,
  removeEventListener,
  removeAllListeners,
  simulatorId: Platform.OS === 'android' ? 'EMULATOR' : RNAdMobRewarded.simulatorId,
};
