import React from 'react';
import {
  NativeModules,
  requireNativeComponent,
  View,
  NativeEventEmitter,
} from 'react-native';

import PropTypes from "prop-types";


const RNBanner = requireNativeComponent('RNAdMob', AdMobBanner);

export default class AdMobBanner extends React.Component {

  constructor() {
    super();
    this.onSizeChange = this.onSizeChange.bind(this);
    this.state = {
      style: {},
    };
  }

  onSizeChange(event) {
    const { height, width } = event.nativeEvent;
    this.setState({ style: { width, height } });
  }

  render() {
    const { adUnitID, testDeviceID, bannerSize, style, didFailToReceiveAdWithError } = this.props;
    return (
      <View style={this.props.style}>
        <RNBanner
          style={this.state.style}
          onSizeChange={this.onSizeChange.bind(this)}
          onAdViewDidReceiveAd={this.props.adViewDidReceiveAd}
          onDidFailToReceiveAdWithError={(event) => didFailToReceiveAdWithError(event.nativeEvent.error)}
          onAdViewWillPresentScreen={this.props.adViewWillPresentScreen}
          onAdViewWillDismissScreen={this.props.adViewWillDismissScreen}
          onAdViewDidDismissScreen={this.props.adViewDidDismissScreen}
          onAdViewWillLeaveApplication={this.props.adViewWillLeaveApplication}
          testDeviceID={testDeviceID}
          adUnitID={adUnitID}
          bannerSize={bannerSize} />
      </View>
    );
  }
}

AdMobBanner.propTypes = {
  bannerSize: PropTypes.string, adUnitID: PropTypes.string, testDeviceID: PropTypes.string, adViewDidReceiveAd: PropTypes.func, didFailToReceiveAdWithError: PropTypes.func, adViewWillPresentScreen: PropTypes.func, adViewWillDismissScreen: PropTypes.func, adViewDidDismissScreen: PropTypes.func, adViewWillLeaveApplication: PropTypes.func, admobDispatchAppEvent: PropTypes.func,


};

AdMobBanner.defaultProps = { bannerSize: 'smartBannerPortrait', didFailToReceiveAdWithError: () => {} };
