# react-native-admob [![npm](https://img.shields.io/npm/v/react-native-admob.svg)](https://www.npmjs.com/package/react-native-admob) [![npm (next)](https://img.shields.io/npm/v/react-native-admob/next.svg)](https://www.npmjs.com/package/react-native-admob)

### ⚠️ Please note, the master branch tracks development of version 2 of this library, which is currently in beta. For version 1 please check out the [1.x branch](https://github.com/sbugert/react-native-admob/tree/1.x).

A react-native module for Google AdMob Banners, Interstitials, Rewarded Videos and also DFP Banners.

The banner types are implemented as components while the interstitial and rewarded video have an imperative API.

## Installation

You can use npm or Yarn to install the latest beta version:

**npm:**

    npm i --save react-native-admob@next

**Yarn:**

    yarn add react-native-admob@next

In order to use this library, you have to link it to your project first. There's excellent documentation on how to do this in the [React Native Docs](https://facebook.github.io/react-native/docs/linking-libraries-ios.html#content).

Alternatively for iOS you can install the library with CocoaPods by adding a line to your `Podfile`;

    pod 'react-native-admob', path: '../node_modules/react-native-admob'

### iOS

For iOS you will have to add the [Google Mobile Ads SDK](https://developers.google.com/admob/ios/quick-start#import_the_mobile_ads_sdk) to your Xcode project.

##### Additional Setup  for Native Ads
Download `GADTSmallTemplateView.xib` and `GADTMediumTemplateView.xib` files from this repo [googleads-mobile-ios-native-templates](https://github.com/googleads/googleads-mobile-ios-native-templates) 

Once downloaded, drag these files into the root directory of your react-native xcode  project.


### Android

On Android the AdMob library code is part of Play Services, which is automatically added when this library is linked.

But you still have to manually update your `AndroidManifest.xml`, as described in the [Google Mobile Ads SDK documentation](https://developers.google.com/admob/android/quick-start#update_your_androidmanifestxml).

## Usage

```jsx
import {
  AdMobBanner,
  AdMobInterstitial,
  PublisherBanner,
  AdMobRewarded,
  AdMobNative
} from 'react-native-admob'

// Display a native ad
<AdMobNative
  adUnitID="your-admob-unit-id"
  adSize="small" | "medium"
  style={{
     width: '100%',
     height: 370, // You need to provide fixed height or else ad will not show properly
     // medium sized ad is 370 & small ad is 100 or 91 to be exact.
  }}
/>

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

#### Native Ads Styling
Native ads are displayed using native ad template files. These can be changed to alter to visual appearance of ads.
##### IOS Template files
`GADTSmallTemplateView.xib` and `GADTMediumTemplateView.xib`
Added to the root dir of you react-native xcode project

** Refer to Installation section above if you have not downloaded these files.
##### Android template files
`small_template.xml` and`medium_template.xml` files are included in the `res/layout` directory of this library's android project.

## Reference

### AdMobNative

#### Properties

##### `adSize`
String of value "small" or "medium". Default is "small"

##### `adUnitID`
Unique ad mob unit id

##### `testDevices`
Array of strings

#### Events

##### `onAdOpened`
Called when an ad opens an overlay that covers the screen.

##### `onAdClosed`
Called when the user is about to return to the application after clicking on an ad.

##### `onAdLeftApplication`
Called when an ad leaves the application (e.g., to go to the browser).

##### `didRecordImpression`
Called when an impression is recorded for an ad. At the current time, this method is only used with native ads originating from Google in one of the system-defined formats (App Install or Content).

##### `didRecordClick`
Called when a click is recorded for an ad. At the current time, this method is only used with native ads originating from Google in one of the system-defined formats (App Install or Content).

##### `onAdLoaded`
Called when an ad is received.

##### `onAdFailedToLoad`
Called when an ad request failed. The error code is usually `ERROR_CODE_INTERNAL_ERROR`, `ERROR_CODE_INVALID_REQUEST`, `ERROR_CODE_NETWORK_ERROR`, or `ERROR_CODE_NO_FILL`.


### AdMobBanner

#### Properties

##### `adSize`

_Corresponding to [iOS framework banner size constants](https://developers.google.com/admob/ios/banner)_

<table>
  <thead>
    <tr>
      <th>Value</th>
      <th>Description</th>
      <th>Availability</th>
      <th>Size (WxH)</th>
    </tr>
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

_Note: There is no `smartBannerPortrait` and `smartBannerLandscape` on Android. Both prop values will map to `smartBanner`_

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

_Above names correspond to the [Ad lifecycle event callbacks](https://developers.google.com/admob/android/banner#ad_events)_

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

_For simulators/emulators you can use `AdMobInterstitial.simulatorId` for the test device ID._

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

_For simulators/emulators you can use `AdMobRewarded.simulatorId` for the test device ID._

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
    </tr>
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
      <td><code>videoCompleted</code></td>
      <td><code>rewardBasedVideoAdDidCompletePlaying</code></td>
      <td><code>rewardedVideoAdVideoCompleted</code></td>
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

- [ ] Support [Ad Targeting](https://developers.google.com/admob/ios/targeting) (RFC: [#166](https://github.com/sbugert/react-native-admob/pull/166))
