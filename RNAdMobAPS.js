import { NativeModules } from 'react-native';

const RNAdMobAPS = NativeModules.RNAdMobAPS;
console.error({ RNAdMobAPS });
export default {
  ...RNAdMobAPS,
};
