[![npm version](https://badge.fury.io/js/react-native-admob.svg)](https://badge.fury.io/js/react-native-admob)
## react-native-admob

A react-native component for Google AdMob banners (currently only iOS.)

### Installation

1. `npm i react-native-admob -S`
2. Add [Google AdMob Framework](https://developers.google.com/admob/ios/quick-start#manually_using_the_sdk_download) to your Xcode project.
3. Add react-native-admob static library to your Xcode project like explained [here](http://facebook.github.io/react-native/docs/linking-libraries-ios.html).
4. To use it in your javascript code you can `import Banner from 'react-native-admob'` or `var Banner = require("react-native-admob")`

### Usage

```javascript
<Banner
  bannerSize={"fullBanner"}
  adUnitID={"your-admob-unit-id"}
  didFailToReceiveAdWithError={this.bannerError} />
```

### Props
##### bannerSize
*Corresponding to [iOS framework banner size constants](https://developers.google.com/admob/ios/banner)*

* *banner* (320x50, Standard Banner for Phones and Tablets)
* *largeBanner* (320x100, Large Banner for Phones and Tablets)
* *mediumRectangle* (300x250, IAB Medium Rectangle for Phones and Tablets)
* *fullBanner* (468x60, IAB Full-Size Banner for Tablets)
* *leaderboard* (728x90, IAB Leaderboard for Tablets)
* *smartBannerPortrait* (Screen width x 32|50|90, Smart Banner for Phones and Tablets)
* *smartBannerLandscape* (Screen width x 32|50|90, Smart Banner for Phones and Tablets)

##### Events
*Corresponding to [Ad lifecycle event callbacks](https://developers.google.com/admob/ios/banner)*

* adViewDidReceiveAd()
* didFailToReceiveAdWithError(errorDescription)
* adViewWillPresentScreen()
* adViewWillDismissScreen()
* adViewDidDismissScreen()
* adViewWillLeaveApplication()


---

### TODO
- [ ] Add Interstitial Ads support
- [ ] Add Android support
- [ ] Add example project
