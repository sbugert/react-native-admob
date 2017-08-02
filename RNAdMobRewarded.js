import {
  Platform,
  NativeModules,
  NativeEventEmitter,
} from 'react-native';

const RNAdMobRewarded = NativeModules.RNAdMobRewarded;

const adMobRewardedEmitter = new NativeEventEmitter(RNAdMobRewarded);

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
  if (type === 'rewardedVideoDidFailToLoad') {
    eventHandlers[type].set(handler, adMobRewardedEmitter.addListener(type, error => handler(createErrorFromErrorData(error))));
  } else {
    eventHandlers[type].set(handler, adMobRewardedEmitter.addListener(type, handler));
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
  ...RNAdMobRewarded,
  addEventListener,
  removeEventListener,
  removeAllListeners,
  simulatorId: Platform.OS === 'android' ? 'EMULATOR' : RNAdMobRewarded.simulatorId,
};
