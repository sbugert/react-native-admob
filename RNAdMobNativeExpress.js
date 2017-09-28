import React, { Component } from 'react';
import { Platform, requireNativeComponent, View, ViewPropTypes } from 'react-native';
import _isEqual from 'lodash/isEqual';
import PropTypes from 'prop-types';

const RNBanner = requireNativeComponent('RNAdMobNativeExpress', AdMobNativeExpress);

export default class AdMobNativeExpress extends Component {
  constructor() {
    super();
    this.onSizeChange = this.onSizeChange.bind(this);
    this.state = {
      style: {}
    };
  }

  onSizeChange(event) {
    const { height, width } = event.nativeEvent;
    this.setState({ style: { width, height } });
  }

  shouldComponentUpdate (nextProps, nextState) {
    return !_isEqual(this.props, nextProps) || !_isEqual(this.state, nextState)
  }

  render() {
    const { adUnitID, testDeviceID, bannerWidth, bannerHeight, didFailToReceiveAdWithError } = this.props;
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
          bannerWidth={parseInt(bannerWidth)}
          bannerHeight={parseInt(bannerHeight)}
          testDeviceID={testDeviceID}
          adUnitID={adUnitID}
          />
      </View>
    );
  }
}

AdMobNativeExpress.propTypes = {
  style: ViewPropTypes.style,

  /**
   * Native Express size
   * (https://firebase.google.com/docs/admob/android/native-express#choose_a_size)
   */
  bannerWidth: PropTypes.number,
  bannerHeight: PropTypes.number,

  /**
   * AdMob ad unit ID
   */
  adUnitID: PropTypes.string,

  /**
   * Test device ID
   */
  testDeviceID: PropTypes.string,

  /**
   * AdMob library events
   */
  adViewDidReceiveAd: PropTypes.func,
  didFailToReceiveAdWithError: PropTypes.func,
  adViewWillPresentScreen: PropTypes.func,
  adViewWillDismissScreen: PropTypes.func,
  adViewDidDismissScreen: PropTypes.func,
  adViewWillLeaveApplication: PropTypes.func,
  ...ViewPropTypes,
};

AdMobNativeExpress.defaultProps = {
  bannerWidth: 400,
  bannerHeight: 300,
  didFailToReceiveAdWithError: () => {}
};