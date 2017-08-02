import React, { Component } from 'react';
import {
  NativeModules,
  requireNativeComponent,
  Platform,
  UIManager,
  findNodeHandle,
  ViewPropTypes,
} from 'react-native';
import { string, func, arrayOf } from 'prop-types';

import { createErrorFromErrorData } from './utils';

class AdMobBanner extends Component {

  constructor() {
    super();
    this.handleSizeChange = this.handleSizeChange.bind(this);
    this.handleDidFailToReceiveAdWithError = this.handleDidFailToReceiveAdWithError.bind(this);
    this.state = {
      style: {},
    };
  }

  componentDidMount() {
    this.loadBanner();
  }

  loadBanner() {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this._bannerView),
      UIManager.RNGADBannerView.Commands.loadBanner,
      null,
    );
  }

  handleSizeChange(event) {
    const { height, width } = event.nativeEvent;
    this.setState({ style: { width, height } });
    if (this.props.onSizeChange) {
      this.props.onSizeChange({ width, height });
    }
  }

  handleDidFailToReceiveAdWithError(event) {
    if (this.props.onDidFailToReceiveAdWithError) {
      this.props.onDidFailToReceiveAdWithError(createErrorFromErrorData(event.nativeEvent.error));
    }
  }

  render() {
    return (
      <RNGADBannerView
        {...this.props}
        style={[this.props.style, this.state.style]}
        onSizeChange={this.handleSizeChange}
        onDidFailToReceiveAdWithError={this.handleDidFailToReceiveAdWithError}
        ref={el => (this._bannerView = el)}
      />
    );
  }
}

AdMobBanner.simulatorId = Platform.OS === 'android' ? 'EMULATOR' : NativeModules.RNGADBannerViewManager.simulatorId;

AdMobBanner.propTypes = {
  ...ViewPropTypes,

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
  adSize: string,

  /**
   * AdMob ad unit ID
   */
  adUnitID: string,

  /**
   * Array of test devices. Use AdMobBanner.simulatorId for the simulator
   */
  testDevices: arrayOf(string),

  /**
   * AdMob iOS library events
   */
  onSizeChange: func,
  onAdViewDidReceiveAd: func,
  onDidFailToReceiveAdWithError: func,
  onAdViewWillPresentScreen: func,
  onAdViewWillDismissScreen: func,
  onAdViewDidDismissScreen: func,
  onAdViewWillLeaveApplication: func,
};

const RNGADBannerView = requireNativeComponent('RNGADBannerView', AdMobBanner);

export default AdMobBanner;
