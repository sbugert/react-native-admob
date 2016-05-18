'use strict';

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
} from 'react-native';
import { AdMobBanner, AdMobInterstitial } from 'react-native-admob';

AdMobInterstitial.setAdUnitId('ca-app-pub-3940256099942544/1033173712');

var Example = React.createClass({
  getInitialState: function() {
    return {
      size: 'banner',
    };
  },
  press(size) {
    this.setState({
      size: size,
    });
  },
  showInterstital() {
    AdMobInterstitial.tryShowNewInterstitial(null);
  },
  render: function() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to react-native-admob!
        </Text>
        <Text onPress={this.showInterstital} style={styles.instructions}>
          Show Interstital
        </Text>
        <Text onPress={this.press.bind(this, 'banner')} style={styles.instructions}>
          Set banner size to 'banner'
        </Text>
        <Text onPress={this.press.bind(this, 'largeBanner')} style={styles.instructions}>
          Set banner size to 'largeBanner'
        </Text>
        <Text onPress={this.press.bind(this, 'mediumRectangle')} style={styles.instructions}>
          Set banner size to 'mediumRectangle'
        </Text>
        <Text onPress={this.press.bind(this, 'smartBannerPortrait')} style={styles.instructions}>
          Set banner size to 'smartBannerPortrait'
        </Text>
        <Text onPress={this.press.bind(this, 'smartBannerLandscape')} style={styles.instructions}>
          Set banner size to 'smartBannerLandscape'
        </Text>
        <AdMobBanner
          style={styles.banner}
          bannerSize={this.state.size}
          adUnitID={"ca-app-pub-3940256099942544/2934735716"} />
      </View>
    );
  }
});

var styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 15,
  },
  banner: {
    backgroundColor: '#055',
  },
});

AppRegistry.registerComponent('Example', () => Example);
