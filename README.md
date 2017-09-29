# react-native-admob [![npm version](https://badge.fury.io/js/react-native-admob.svg)](https://badge.fury.io/js/react-native-admob)

### ⚠️ Please note, the master branch tracks development of version 2 of this library, which is currently in alpha. For version 1 please check out the [1.x branch](https://github.com/sbugert/react-native-admob/tree/1.x).

A react-native module for Google AdMob GADBanner, DFPBanner and GADInterstitial (react-native v0.19.0 or newer required).

The banner is implemented as a component while the interstitial has an imperative API.

## Installation

### Automatic Installation (recommended)

1. `npm i react-native-admob -S`
2. `react-native link react-native-admob`
3. Add the [Google Mobile Ads SDK](https://developers.google.com/admob/ios/quick-start#import_the_mobile_ads_sdk) to your Xcode project with CocoaPods or manually. This is only needed for iOS and guarantees your app is using the newest AdMob version.

### Manual Installation

#### iOS

1. `npm i react-native-admob -S`
2. Add the [Google Mobile Ads SDK](https://developers.google.com/admob/ios/quick-start#import_the_mobile_ads_sdk) to your Xcode project with CocoaPods or manually. This is only needed for iOS and guarantees your app is using the newest AdMob version.
3. Add react-native-admob static library to your Xcode project like explained [here](http://facebook.github.io/react-native/docs/linking-libraries-ios.html#manual-linking). (Step 3 of this guide is not needed)

#### Android

1. `npm i react-native-admob -S`
2. Make the following additions to the given files:

**`android/settings.gradle`**

```groovy
include ':react-native-admob'
project(':react-native-admob').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-admob/android')
```

**`android/app/build.gradle`**

```groovy
dependencies {
   // ...
   compile project(':react-native-admob')
}
```

**`MainApplication.java`**

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

## Usage

```jsx
import {
  AdMobBanner,
  AdMobInterstitial,
  PublisherBanner,
  AdMobRewarded,
} from 'react-native-admob'

// Display a banner
<AdMobBanner
  adSize="fullBanner"
  adUnitID="your-admob-unit-id"
  testDevices={[AdMobBanner.simulatorId]}
  onAdFailedToLoad={error => console.error(error)}
/>

// Display a DFP Publisher banner
<PublisherBanner
  adSize="fullBanner"
  adUnitID="your-admob-unit-id"
  testDevices={[PublisherBanner.simulatorId]}
  onAdFailedToLoad={error => console.error(error)}
  onAppEvent={event => console.log(event.name, event.info)}
/>

// Display an interstitial
AdMobInterstitial.setAdUnitID('your-admob-unit-id');
AdMobInterstitial.setTestDevices([AdMobInterstitial.simulatorId]);
AdMobInterstitial.requestAd().then(() => AdMobInterstitial.showAd());

// Display a rewarded ad
AdMobRewarded.setAdUnitID('your-admob-unit-id');
AdMobRewarded.requestAd().then(() => AdMobRewarded.showAd());
```

For a full example reference to the [example project](Example).

## Reference

### AdMobBanner

#### Properties

##### `adSize`

*Corresponding to [iOS framework banner size constants](https://developers.google.com/admob/ios/banner)*

<table>
  <thead>
    <tr>
      <th>Value</th>
      <th>Description</th>
      <th>Availability</th>
      <th>Size (WxH)</th>
    </t>
  </thead>
  <tbody>
    <tr>
      <td><code>banner</code></td>
      <td>Standard Banner</td>
      <td>Phones and Tablets</td>
      <td>320x50</td>
    </tr>
    <tr>
      <td><code>largeBanner</code></td>
      <td>Large Banner</td>
      <td>Phones and Tablets</td>
      <td>320x100</td>
    </tr>
    <tr>
      <td><code>mediumRectangle</code></td>
      <td>IAB Medium Rectangle</td>
      <td>Phones and Tablets</td>
      <td>300x250</td>
    </tr>
    <tr>
      <td><code>fullBanner</code></td>
      <td>IAB Full-Size Banner</td>
      <td>Tablets</td>
      <td>468x60</td>
    </tr>
    <tr>
      <td><code>leaderboard</code></td>
      <td>IAB Leaderboard</td>
      <td>Tablets</td>
      <td>728x90</td>
    </tr>
    <tr>
      <td>
        <code>smartBannerPortrait</code><br/>
        <code>smartBannerLandscape</code>
      </td>
      <td>Smart Banner</td>
      <td>Phones and Tablets</td>
      <td>Screen width x 32|50|90</td>
    </tr>
  </tbody>
</table>

*Note: There is no `smartBannerPortrait` and `smartBannerLandscape` on Android. Both prop values will map to `smartBanner`*

##### `onAdLoaded`

Accepts a function. Called when an ad is received.

##### `onAdFailedToLoad`

Accepts a function. Called when an ad request failed.

##### `onAdOpened`

Accepts a function. Called when an ad opens an overlay that covers the screen.

##### `onAdClosed`

Accepts a function. Called when the user is about to return to the application after clicking on an ad.

##### `onAdLeftApplication`

Accepts a function. Called when a user click will open another app (such as the App Store), backgrounding the current app.

##### `onSizeChange`

Accepts a function. Called when the size of the banner changes. The function is called with an object containing the width and the height.


*Above names correspond to the [Ad lifecycle event callbacks](https://developers.google.com/admob/android/banner#ad_events)*

### PublisherBanner

#### Properties

Same as `AdMobBanner`, with the addition of 2 extra properties:

##### `onAppEvent`

Accepts a function. Called when DFP sends an event back to the app.

These events may occur at any time during the ad's lifecycle, even before `onAdLoaded` is called. The function is called with an object, containing the name of the event and an info property, containing additional information.

More info here: https://developers.google.com/mobile-ads-sdk/docs/dfp/ios/banner#app_events

##### `validAdSizes`

An array of ad sizes which may be eligible to be served.

### AdMobInterstitial

In comparison to the `AdMobBanner` and `PublisherBanner` which have a declaritive API, the `AdMobInterstitial` has an imperative API.

#### Methods

##### `setAdUnitID(adUnitID)`

Sets the AdUnit ID for all future ad requests.

##### `setTestDevices(devices)`

Sets the devices which are served test ads.

*For simulators/emulators you can use `AdMobInterstitial.simulatorId` for the test device ID.*

##### `requestAd()`

Requests an interstitial and returns a promise, which resolves on load and rejects on error.

##### `showAd()`

Shows an interstitial and returns a promise, which resolves when an ad is going to be shown, rejects when the ad is not ready to be shown.

##### `isReady(callback)`

Calls callback with a boolean value whether the interstitial is ready to be shown.


#### Events

Unfortunately, events are not consistent across iOS and Android. To have one unified API, new event names are introduced for pairs that are roughly equivalent.

<table>
  <thead>
    <tr>
      <th>This library</th>
      <th>iOS</th>
      <th>Android</th>
    </t>
  </thead>
  <tbody>
    <tr>
      <td><code>adLoaded</code></td>
      <td><code>interstitialDidReceiveAd</code></td>
      <td><code>onAdLoaded</code></td>
    </tr>
    <tr>
      <td><code>adFailedToLoad</code></td>
      <td><code>interstitial:didFailToReceiveAdWithError</code></td>
      <td><code>onAdFailedToLoad</code></td>
    </tr>
    <tr>
      <td><code>adOpened</code></td>
      <td><code>interstitialWillPresentScreen</code></td>
      <td><code>onAdOpened</code></td>
    </tr>
    <tr>
      <td><code>adFailedToOpen</code></td>
      <td><code>interstitialDidFailToPresentScreen</code></td>
      <td><em>Not supported</em></td>
    </tr>
    <tr>
      <td><code>adClosed</code></td>
      <td><code>interstitialWillDismissScreen</code></td>
      <td><code>onAdClosed</code></td>
    </tr>
    <tr>
      <td><code>adLeftApplication</code></td>
      <td><code>interstitialWillLeaveApplication</code></td>
      <td><code>onAdLeftApplication</code></td>
    </tr>
  </tbody>
</table>

### AdMobRewarded

In comparison to the `AdMobBanner` and `PublisherBanner` which have a declaritive API, the `AdMobRewarded` has an imperative API, just like the `AdMobInterstitial`.

#### Methods

##### `setAdUnitID(adUnitID)`

Sets the AdUnit ID for all future ad requests.

##### `setTestDevices(devices)`

Sets the devices which are served test ads.

*For simulators/emulators you can use `AdMobRewarded.simulatorId` for the test device ID.*

##### `requestAd()`

Requests a rewarded ad and returns a promise, which resolves on load and rejects on error.

##### `showAd()`

Shows a rewarded ad and returns a promise, which resolves when an ad is going to be shown, rejects when the ad is not ready to be shown.

##### `isReady(callback)`

Calls callback with a boolean value whether the rewarded ad is ready to be shown.

#### Events

Unfortunately, events are not consistent across iOS and Android. To have one unified API, new event names are introduced for pairs that are roughly equivalent.

<table>
  <thead>
    <tr>
      <th>This library</th>
      <th>iOS</th>
      <th>Android</th>
    </t>
  </thead>
  <tbody>
    <tr>
      <td><code>adLoaded</code></td>
      <td><code>rewardBasedVideoAdDidReceiveAd</code></td>
      <td><code>onRewardedVideoAdLoaded</code></td>
    </tr>
    <tr>
      <td><code>adFailedToLoad</code></td>
      <td><code>rewardBasedVideoAd:didFailToLoadWithError</code></td>
      <td><code>onRewardedVideoAdFailedToLoad</code></td>
    </tr>
    <tr>
      <td><code>rewarded</code></td>
      <td><code>rewardBasedVideoAd:didRewardUserWithReward</code></td>
      <td><code>onRewarded</code></td>
    </tr>
    <tr>
      <td><code>adOpened</code></td>
      <td><code>rewardBasedVideoAdDidOpen</code></td>
      <td><code>onRewardedVideoAdOpened</code></td>
    </tr>
    <tr>
      <td><code>videoStarted</code></td>
      <td><code>rewardBasedVideoAdDidStartPlaying</code></td>
      <td><code>onRewardedVideoStarted</code></td>
    </tr>
    <tr>
      <td><code>adClosed</code></td>
      <td><code>rewardBasedVideoAdDidClose</code></td>
      <td><code>onRewardedVideoAdClosed</code></td>
    </tr>
    <tr>
      <td><code>adLeftApplication</code></td>
      <td><code>rewardBasedVideoAdWillLeaveApplication</code></td>
      <td><code>onRewardedVideoAdLeftApplication</code></td>
    </tr>
  </tbody>
</table>

---

## TODO

- [ ] Support [Ad Targeting](https://developers.google.com/admob/ios/targeting)
- [ ] Also use interstitial event names for banner
