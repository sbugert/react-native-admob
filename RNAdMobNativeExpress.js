import React from 'react';
import {
  NativeModules,
  requireNativeComponent,
  View,
  NativeEventEmitter,
} from 'react-native';

const RNBanner = requireNativeComponent('RNAdMobNativeExpress', AdMobNativeExpress);

export default class AdMobNativeExpress extends React.Component {

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
    const { adUnitID, testDeviceID, bannerWidth, bannerHeight, style, didFailToReceiveAdWithError } = this.props;
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
          bannerWidth={bannerWidth}
          bannerHeight={bannerHeight}
          />
      </View>
    );
  }
}

AdMobNativeExpress.propTypes = {
  style: View.propTypes.style,

  /**
   * Native Express size
   * (https://firebase.google.com/docs/admob/android/native-express#choose_a_size)
   */
  bannerWidth: React.PropTypes.number,
  bannerHeight: React.PropTypes.number,

  /**
   * AdMob ad unit ID
   */
  adUnitID: React.PropTypes.string,

  /**
   * Test device ID
   */
  testDeviceID: React.PropTypes.string,

  /**
   * AdMob iOS library events
   */
  adViewDidReceiveAd: React.PropTypes.func,
  didFailToReceiveAdWithError: React.PropTypes.func,
  adViewWillPresentScreen: React.PropTypes.func,
  adViewWillDismissScreen: React.PropTypes.func,
  adViewDidDismissScreen: React.PropTypes.func,
  adViewWillLeaveApplication: React.PropTypes.func,
  ...View.propTypes,
};

AdMobNativeExpress.defaultProps = {
  bannerWidth: 400,
  bannerHeight: 300,
  didFailToReceiveAdWithError: () => {}
};
