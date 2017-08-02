import {
  NativeModules,
  NativeEventEmitter,
} from 'react-native';

const RNAdMobInterstitial = NativeModules.RNAdMobInterstitial;

const adMobInterstitialEmitter = new NativeEventEmitter(RNAdMobInterstitial);

const eventHandlers = {};

const createErrorFromErrorData = (errorData) => {
  const {
    message,
    ...extraErrorInfo
  } = errorData || {};
  const error = new Error(message);
  error.framesToPop = 1;
  return Object.assign(error, extraErrorInfo);
}

const addEventListener = (type, handler) => {
  eventHandlers[type] = eventHandlers[type] || new Map();
  if (type === 'interstitialDidFailToLoad') {
    eventHandlers[type].set(handler, adMobInterstitialEmitter.addListener(type, error => handler(createErrorFromErrorData(error))));
  } else {
    eventHandlers[type].set(handler, adMobInterstitialEmitter.addListener(type, handler));
  }
};

const removeEventListener = (type, handler) => {
  if (!eventHandlers[type].has(handler)) {
    return;
  }
  eventHandlers[type].get(handler).remove();
  eventHandlers[type].delete(handler);
};

const removeAllListeners = () => {
  const types = Object.keys(eventHandlers);
  types.forEach(type => (
    eventHandlers[type].forEach((subscription, key, map) => {
      subscription.remove();
      map.delete(key);
    })
  ));
};

export default {
  ...RNAdMobInterstitial,
  addEventListener,
  removeEventListener,
  removeAllListeners,
};
