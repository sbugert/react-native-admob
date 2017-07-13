'use strict';

import {
  Platform,
  NativeModules,
  NativeEventEmitter,
} from 'react-native';

const RNAdMobInterstitial = NativeModules.RNAdMobInterstitial;

const adMobInterstitialEmitter = new NativeEventEmitter(RNAdMobInterstitial);

const eventHandlers = {};

const addEventListener = (type, handler) => {
  eventHandlers[type] = eventHandlers[type] || new Map();
  eventHandlers[type].set(handler, adMobInterstitialEmitter.addListener(type, handler));
};

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
  ...RNAdMobInterstitial,
  requestAd: (cb = () => {}) => RNAdMobInterstitial.requestAd(cb), // requestAd callback is optional
  showAd: (cb = () => {}) => RNAdMobInterstitial.showAd(cb),       // showAd callback is optional
  addEventListener,
  removeEventListener,
  removeAllListeners,
};
