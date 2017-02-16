[![npm version](https://badge.fury.io/js/react-native-admob.svg)](https://badge.fury.io/js/react-native-admob)
## react-native-admob

A react-native module for Google AdMob GADBanner, DFPBanner and GADInterstitial (react-native v0.19.0 or newer required).

The banner is implemented as a component while the interstitial has an imperative API.

### Installation

#### Automatic Installation (recommended)

1. `npm i react-native-admob -S`
2. `react-native link`
3. Add [Google AdMob Framework](https://firebase.google.com/docs/ios/setup#frameworks) to your Xcode project with CocoaPods or manually. This is only needed for iOS and guarantees your app is using the newest admob version.

#### Manual Installation

##### iOS

1. `npm i react-native-admob -S`
2. Add [Google AdMob Framework](https://firebase.google.com/docs/ios/setup#frameworks) to your Xcode project with CocoaPods or manually. This is only needed for iOS and guarantees your app is using the newest admob version.
3. Add react-native-admob static library to your Xcode project like explained [here](http://facebook.github.io/react-native/docs/linking-libraries-ios.html#manual-linking). (Step 3 of this guide is not needed)

##### Android

1. `npm i react-native-admob -S`
2. Make the following additions to the given files:

**android/settings.gradle**

```
include ':RNAdMob', ':app'
project(':RNAdMob').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-admob/android')
```

**android/app/build.gradle**

```
dependencies {
   ...
   compile project(':RNAdMob')
}
```

**MainApplication.java**

#### RN < 0.29

In **MainActivity.java** on top, where imports are:
```java
import com.sbugert.rnadmob.RNAdMobPackage;
```

Under `protected List<ReactPackage> getPackages() {`:  
```java
  return Arrays.<ReactPackage>asList(
    new MainReactPackage(),
    new RNAdMobPackage()
  );
```

#### RN >= 0.29

In **MainAplication.java** on top, where imports are:
```java
import com.sbugert.rnadmob.RNAdMobPackage;
```

Under `protected List<ReactPackage> getPackages() {`:  
```java
  return Arrays.<ReactPackage>asList(
    new MainReactPackage(),
    new RNAdMobPackage()
  );
```

### Usage

```javascript
import { 
  AdMobBanner, 
  AdMobInterstitial, 
  PublisherBanner,
  AdMobRewarded
} from 'react-native-admob'

// Display a banner
<AdMobBanner
  bannerSize="fullBanner"
  adUnitID="your-admob-unit-id"
  testDeviceID="EMULATOR"
  didFailToReceiveAdWithError={this.bannerError} />

// Display a DFP Publisher banner
<PublisherBanner
  bannerSize="fullBanner"
  adUnitID="your-admob-unit-id"
  testDeviceID="EMULATOR"
  didFailToReceiveAdWithError={this.bannerError}
  admobDispatchAppEvent={this.adMobEvent} />

// Display an interstitial
AdMobInterstitial.setAdUnitID('your-admob-unit-id');
AdMobInterstitial.setTestDeviceID('EMULATOR');
AdMobInterstitial.requestAd(AdMobInterstitial.showAd);

// Display a rewarded ad
AdMobRewarded.setAdUnitID('your-admob-unit-id');
AdMobRewarded.requestAd(AdMobRewarded.showAd);
```

For a full example reference to the [example project](Example).

### Reference

#### AdMobBanner

##### bannerSize property
*Corresponding to [iOS framework banner size constants](https://developers.google.com/admob/ios/banner)*

| Prop value              | Description                                 | Size                  |
|-------------------------|---------------------------------------------|-----------------------|
|`banner`                 |Standard Banner for Phones and Tablets       |320x50                 |
|`largeBanner`            |Large Banner for Phones and Tablets          |320x100                |
|`mediumRectangle`        |IAB Medium Rectangle for Phones and Tablets  |300x250                |
|`fullBanner`             |IAB Full-Size Banner for Tablet              |468x60                 |
|`leaderboard`            |IAB Leaderboard for Tablets                  |728x90                 |
|**`smartBannerPortrait`**|Smart Banner for Phones and Tablets (default)|Screen width x 32|50|90|
|`smartBannerLandscape`   |Smart Banner for Phones and Tablets          |Screen width x 32|50|90|

*Note: There is no `smartBannerPortrait` and `smartBannerLandscape` on Android. Both prop values will map to `smartBanner`*


##### Events as function props
*Corresponding to [Ad lifecycle event callbacks](https://developers.google.com/admob/ios/banner)*

| Prop                                           |
|------------------------------------------------|
|`onSizeChange(width, height)`                   |
|`adViewDidReceiveAd()`                          |
|`didFailToReceiveAdWithError(errorDescription)` |
|`adViewWillPresentScreen()`                     |
|`adViewWillDismissScreen()`                     |
|`adViewDidDismissScreen()`                      |
|`adViewWillLeaveApplication()`                  |


#### PublisherBanner

Same as AdMobBanner, except it has an extra event prop:

|'admobDispatchAppEvent()' |

This handles App events that Admob/DFP can send back to the app.
More info here: https://developers.google.com/mobile-ads-sdk/docs/dfp/android/banner#ios_app-events


#### AdMobInterstitials

##### Methods

| Name                      | Description                                                                                                     |
|---------------------------|-----------------------------------------------------------------------------------------------------------------|
|`setAdUnitID(adUnitID)`    | sets the AdUnit ID for all future ad requests.                                                                  |
|`setTestDeviceID(deviceID)`| sets the test device ID                                                                                         |
|`requestAd(callback)`      | requests an interstitial and calls callback when `interstitialDidLoad` or`interstitialDidFailToLoad` event fires|
|`showAd(callback)`         | shows an interstitial if it is ready and calls callback when `interstitialDidOpen` event fires                  |
|`isReady(callback)`        | calls callback with boolean whether interstitial is ready to be shown                                           |

*For simulators/emulators you can use `'EMULATOR'` for the test device ID.*  
*Note: `tryShowNewInterstitial()` is deprecated as of v1.1.0 and can be replaced by calling `requestAd` with `showAd` as callback.*

##### Events
Unfortunately, events are not consistent across iOS and Android. To have one unified API, new event names are introduced for pairs that are roughly equivalent.

| iOS                                      | *this library*                   | Android             |
|------------------------------------------|----------------------------------|---------------------|
|`interstitialDidReceiveAd`                |`interstitialDidLoad`             |`onAdLoaded`         |
|`interstitial:didFailToReceiveAdWithError`|`interstitialDidFailToLoad`       |`onAdFailedToLoad`   |
|`interstitialWillPresentScreen`           |`interstitialDidOpen`             |`onAdOpened`         |
|`interstitialDidFailToPresentScreen`      |                                  |                     |
|`interstitialWillDismissScreen`           |                                  |                     |
|`interstitialDidDismissScreen`            |`interstitialDidClose`            |`onAdClosed`         |
|`interstitialWillLeaveApplication`        |`interstitialWillLeaveApplication`|`onAdLeftApplication`|

*Note that `interstitialWillLeaveApplication` and `onAdLeftApplication` are not exactly the same but share one event in this library.*


#### AdMobRewarded

Opens a rewarded AdMob ad.

##### Methods
| Name                      | Description                                                                                                     |
|---------------------------|-----------------------------------------------------------------------------------------------------------------|
|`setAdUnitID(adUnitID)`    | sets the AdUnit ID for all future ad requests.                                                                  |
|`setTestDeviceID(deviceID)`| sets the test device ID                                                                                         |
|`requestAd(callback)`      | requests a rewarded ad|
|`showAd(callback)`         | shows a rewarded if it is ready                  |

##### Events

| iOS                                        | *this library*                    | Android                          |
|--------------------------------------------|-----------------------------------|----------------------------------|
|`rewardBasedVideoAd:didRewardUserWithReward`|`rewardedVideoDidRewardUser`       |`onRewarded`                      |
|`rewardBasedVideoAdDidReceiveAd`            |`rewardedVideoDidLoad`             |`onRewardedVideoAdLoaded`         |
|`rewardBasedVideoAd:didFailToLoadWithError` |`rewardedVideoDidFailToLoad`       |`onRewardedVideoAdFailedToLoad`   |
|`rewardBasedVideoAdDidOpen`                 |`rewardedVideoDidOpen`             |`onRewardedVideoAdOpened`         |
|`rewardBasedVideoAdDidClose`                |`rewardedVideoDidClose`            |`onRewardedVideoAdClosed`         |
|`rewardBasedVideoAdWillLeaveApplication`    |`rewardedVideoWillLeaveApplication`|`onRewardedVideoAdLeftApplication`|


---

### TODO
- [ ] Support [Ad Targeting](https://developers.google.com/admob/ios/targeting)
- [ ] Also use interstitial event names for banner
- [ ] PublisherBanner [DFPBanner/PublisherAdView should be able to accept multiple adSizes. Currently only caters for a single size]
