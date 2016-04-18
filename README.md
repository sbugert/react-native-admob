[![npm version](https://badge.fury.io/js/react-native-admob.svg)](https://badge.fury.io/js/react-native-admob)
## react-native-admob

A react-native module for Google AdMob GADBanner and GADInterstitial (react-native v0.19.0 or newer required).

The banner is implemented as a component while the interstitial has an imperative API.

### Installation

#### With [`rnpm`](https://github.com/rnpm/rnpm) (recommended)

1. `npm i react-native-admob -S`
2. `rnpm link`
3. Add [Google AdMob Framework](https://developers.google.com/admob/ios/quick-start#manually_using_the_sdk_download) to your Xcode project. This is only needed for iOS and guarantees your  app is using the newest admob version.

#### Manual Installation

##### iOS

1. `npm i react-native-admob -S`
2. Add [Google AdMob Framework](https://developers.google.com/admob/ios/quick-start#manually_using_the_sdk_download) to your Xcode project.
3. Add react-native-admob static library to your Xcode project like explained [here](http://facebook.github.io/react-native/docs/linking-libraries-ios.html#manual-linking). (Step 3 of this guide is not needed)

##### Android

Make the following additions to the given files.

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

**MainActivity.java**

On top, where imports are:  
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
import { AdMobBanner, AdMobInterstitial } from 'react-native-admob'

// Display a banner
<AdMobBanner
  bannerSize={"fullBanner"}
  adUnitID={"your-admob-unit-id"}
  didFailToReceiveAdWithError={this.bannerError} />

// Display an intersticial
// if in DEV mode, display a test interstitial, otherwise display a real ad
AdMobInterstitial.setAdUnitId('your-admob-unit-id');
AdMobInterstitial.tryShowNewInterstitial(__DEV__ ? "your-device-id" : null);
```

For a full example reference to the [example project](Example).


### Reference

#### AdMobBanner

##### bannerSize property
*Corresponding to [iOS framework banner size constants](https://developers.google.com/admob/ios/banner)*

| Prop value | Description | Size |
|---|---|---|
|`banner`|Standard Banner for Phones and Tablets|`320x50`|
|`largeBanner`|Large Banner for Phones and Tablets|`320x100`|
|`mediumRectangle`|IAB Medium Rectangle for Phones and Tablets|`300x250`|
|`fullBanner`|IAB Full-Size Banner for Tablet|`468x60`|
|`leaderboard`|IAB Leaderboard for Tablets|`728x90`|
|**`smartBannerPortrait`**|Smart Banner for Phones and Tablets (default)|`Screen width x 32|50|90`|
|`smartBannerLandscape`|Smart Banner for Phones and Tablets|`Screen width x 32|50|90`|


##### Events as function props
*Corresponding to [Ad lifecycle event callbacks](https://developers.google.com/admob/ios/banner)*

| Prop |
|---|
|`adViewDidReceiveAd()`|
|`didFailToReceiveAdWithError(errorDescription)`|
|`adViewWillPresentScreen()`|
|`adViewWillDismissScreen()`|
|`adViewDidDismissScreen()`|
|`adViewWillLeaveApplication()`|


---

### TODO
- [ ] Expose full GADInterstitial API 
- [ ] Support [Ad Targeting](https://developers.google.com/admob/ios/targeting)
