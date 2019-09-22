/* eslint-disable global-require */
module.exports = {
  get AdMobRequestConfig() {
    return require('./RNAdMobRequestConfig').default;
  },
  get AdMobBanner() {
    return require('./RNAdMobBanner').default;
  },
  get AdMobInterstitial() {
    return require('./RNAdMobInterstitial').default;
  },
  get PublisherBanner() {
    return require('./RNPublisherBanner').default;
  },
  get AdMobRewarded() {
    return require('./RNAdMobRewarded').default;
  },
};
