import { ViewProps } from 'react-native';

interface AdMobBannerProps extends ViewProps {
  /**
   * AdMob iOS library banner size constants
   * (https://developers.google.com/admob/ios/banner)
   * banner (320x50, Standard Banner for Phones and Tablets)
   * largeBanner (320x100, Large Banner for Phones and Tablets)
   * mediumRectangle (300x250, IAB Medium Rectangle for Phones and Tablets)
   * fullBanner (468x60, IAB Full-Size Banner for Tablets)
   * leaderboard (728x90, IAB Leaderboard for Tablets)
   * smartBannerPortrait (Screen width x 32|50|90, Smart Banner for Phones and Tablets)
   * smartBannerLandscape (Screen width x 32|50|90, Smart Banner for Phones and Tablets)
   *
   * banner is default
   */
  adSize?:
    | 'banner'
    | 'largeBanner'
    | 'mediumRectangle'
    | 'fullBanner'
    | 'leaderboard'
    | 'smartBannerPortrait'
    | 'smartBannerLandscape';

  /**
   * AdMob ad unit ID
   */
  adUnitID: string;

  /**
   * Array of test devices. Use AdMobBanner.simulatorId for the simulator
   */
  testDevices?: string[];

  /**
   * AdMob iOS library events
   */
  onSizeChange?: Function;

  onAdLoaded?: Function;
  onAdFailedToLoad?: Function;
  onAdOpened?: Function;
  onAdClosed?: Function;
  onAdLeftApplication?: Function;
}
export const AdMobBanner: React.FC<AdMobBannerProps>;

export const AdMobInterstitial: {
  setAdUnitID: (adUnitID: string) => void;
  setTestDevices: (testDevices: string[]) => void;
  requestAd: () => Promise<unknown>;
  showAd: () => Promise<unknown>;
  isReady: (callback: Function) => void;
  addEventListener: (event: unknown, handler: unknown) => void;
  removeEventListener: (type: unknown, handler: unknown) => void;
  removeAllListeners: () => void;
  simulatorId: 'SIMULATOR';
};

interface PublisherBannerProps extends ViewProps {
  /**
   * DFP iOS library banner size constants
   * (https://developers.google.com/admob/ios/banner)
   * banner (320x50, Standard Banner for Phones and Tablets)
   * largeBanner (320x100, Large Banner for Phones and Tablets)
   * mediumRectangle (300x250, IAB Medium Rectangle for Phones and Tablets)
   * fullBanner (468x60, IAB Full-Size Banner for Tablets)
   * leaderboard (728x90, IAB Leaderboard for Tablets)
   * smartBannerPortrait (Screen width x 32|50|90, Smart Banner for Phones and Tablets)
   * smartBannerLandscape (Screen width x 32|50|90, Smart Banner for Phones and Tablets)
   *
   * banner is default
   */
  adSize?:
    | 'banner'
    | 'largeBanner'
    | 'mediumRectangle'
    | 'fullBanner'
    | 'leaderboard'
    | 'smartBannerPortrait'
    | 'smartBannerLandscape';

  /**
   * Optional array specifying all valid sizes that are appropriate for this slot.
   */
  validAdSizes?: string[];

  /**
   * DFP ad unit ID
   */
  adUnitID: string;

  /**
   * Array of test devices. Use PublisherBanner.simulatorId for the simulator
   */
  testDevices?: string[];

  onSizeChange?: Function;

  /**
   * DFP library events
   */
  onAdLoaded?: Function;
  onAdFailedToLoad?: Function;
  onAdOpened?: Function;
  onAdClosed?: Function;
  onAdLeftApplication?: Function;
  onAppEvent?: Function;
}

export const PublisherBanner: React.FC<PublisherBannerProps>;

export const AdMobRewarded: {
  setAdUnitID: (adUnitID: string) => void;
  setTestDevices: (testDevices: string[]) => void;
  requestAd: () => Promise<unknown>;
  showAd: () => Promise<unknown>;
  isReady: (callback: Function) => void;
  addEventListener: (event: unknown, handler: unknown) => void;
  removeEventListener: (type: unknown, handler: unknown) => void;
  removeAllListeners: () => void;
  simulatorId: 'SIMULATOR';
};
