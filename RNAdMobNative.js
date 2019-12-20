import { arrayOf, func, string } from 'prop-types';
import React, { Component } from 'react';
import {
  findNodeHandle,
  requireNativeComponent,
  UIManager,
  ViewPropTypes,
} from 'react-native';
import { createErrorFromErrorData } from './utils';

class AdMobNative extends Component {
  constructor() {
    super();
    this.handleAdFailedToLoad = this.handleAdFailedToLoad.bind(this);
    this.state = {
      style: {},
    };
  }

  componentDidMount() {
    //this.loadNative();
  }

  loadNative() {
    UIManager.dispatchViewManagerCommand(
      findNodeHandle(this._nativeView),
      UIManager.getViewManagerConfig('RNGADNativeView').Commands.loadNative,
      null
    );
  }

  handleAdFailedToLoad(event) {
    if (this.props.onAdFailedToLoad) {
      this.props.onAdFailedToLoad(
        createErrorFromErrorData(event.nativeEvent.error)
      );
    }
  }

  render() {
    return (
      <RNGADNativeView
        {...this.props}
        style={[this.props.style, this.state.style]}
        onAdFailedToLoad={this.handleAdFailedToLoad}
        ref={(el) => (this._nativeView = el)}
      />
    );
  }
}

AdMobNative.simulatorId = 'SIMULATOR';

AdMobNative.propTypes = {
  ...ViewPropTypes,

  /**
   * AdMob ad unit ID
   */
  adUnitID: string,

  /**
   * Night mode: X or ''
   */
  nightMode: string,

  /**
   * Array of test devices. Use AdMobBanner.simulatorId for the simulator
   */
  testDevices: arrayOf(string),

  /**
   * AdMob iOS library events
   */
  onSizeChange: func,

  onAdLoaded: func,
  onAdFailedToLoad: func,
  onAdOpened: func,
  onAdClosed: func,
  onAdLeftApplication: func,
};

const RNGADNativeView = requireNativeComponent('RNGADNativeView', AdMobNative);

export default AdMobNative;
