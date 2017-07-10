import React, { Component } from 'react';
import {
  NativeModules,
  requireNativeComponent,
  View,
  NativeEventEmitter,
  Platform,
  UIManager,
  findNodeHandle,
} from 'react-native';

class PublisherBanner extends Component {

  constructor() {
    super();
    this.handleSizeChange = this.handleSizeChange.bind(this);
    this.handleAdmobDispatchAppEvent = this.handleAdmobDispatchAppEvent.bind(this);
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
      UIManager.RNDFPBannerView.Commands.loadBanner,
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

  handleAdmobDispatchAppEvent(event) {
    if (this.props.onAdmobDispatchAppEvent) {
      const { name, info } = event.nativeEvent;
      this.props.onAdmobDispatchAppEvent({ name, info });
    }
  }

  handleDidFailToReceiveAdWithError(event) {
    if (this.props.onDidFailToReceiveAdWithError) {
      this.props.onDidFailToReceiveAdWithError(event.nativeEvent.error);
    }
  }

  render() {
    return (
      <RNDFPBannerView
        {...this.props}
        style={[this.props.style, this.state.style]}
        onSizeChange={this.handleSizeChange}
        onDidFailToReceiveAdWithError={this.handleDidFailToReceiveAdWithError}
        onAdmobDispatchAppEvent={this.handleAdmobDispatchAppEvent}
        ref={el => (this._bannerView = el)}
      />
    );
  }
}

PublisherBanner.simulatorId = Platform.OS === 'android' ? 'EMULATOR' : NativeModules.RNDFPBannerViewManager.simulatorId;

PublisherBanner.propTypes = {
  ...View.propTypes,

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
  adSize: React.PropTypes.string,

  /**
   * Optional array specifying all valid sizes that are appropriate for this slot.
   */
  validAdSizes: React.PropTypes.arrayOf(React.PropTypes.string),

  /**
   * AdMob ad unit ID
   */
  adUnitID: React.PropTypes.string,

  /**
   * Array of test devices. Use PublisherBanner.simulatorId for the simulator
   */
  testDevices: React.PropTypes.arrayOf(React.PropTypes.string),

  /**
   * AdMob iOS library events
   */
  onSizeChange: React.PropTypes.func,
  onAdViewDidReceiveAd: React.PropTypes.func,
  onDidFailToReceiveAdWithError: React.PropTypes.func,
  onAdViewWillPresentScreen: React.PropTypes.func,
  onAdViewWillDismissScreen: React.PropTypes.func,
  onAdViewDidDismissScreen: React.PropTypes.func,
  onAdViewWillLeaveApplication: React.PropTypes.func,
  onAdmobDispatchAppEvent: React.PropTypes.func,
};

PublisherBanner.defaultProps = {
};

const RNDFPBannerView = requireNativeComponent('RNDFPBannerView', PublisherBanner);

export default PublisherBanner;
