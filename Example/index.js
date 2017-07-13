import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Platform,
  TouchableHighlight,
  Button,
  ScrollView,
} from 'react-native';

import {
  AdMobBanner,
  AdMobRewarded,
  AdMobInterstitial,
  PublisherBanner,
} from 'react-native-admob';

const BannerExample = ({ style, title, children, ...props }) => (
  <View {...props} style={[styles.example, style]}>
    <Text style={styles.title}>{title}</Text>
    <View>
      {children}
    </View>
  </View>
);

const bannerWidths = [200, 250, 320];

export default class Example extends Component {

  constructor() {
    super();
    this.state = {
      fluidSizeIndex: 0,
    };
  }

  componentDidMount() {
    AdMobRewarded.setTestDevices([AdMobRewarded.simulatorId]);
    AdMobRewarded.setAdUnitID('ca-app-pub-3940256099942544/1033173712');

    AdMobRewarded.addEventListener('rewardedVideoDidRewardUser',
      (type, amount) => console.log('rewardedVideoDidRewardUser', type, amount)
    );
    AdMobRewarded.addEventListener('rewardedVideoDidLoad',
      () => console.log('rewardedVideoDidLoad')
    );
    AdMobRewarded.addEventListener('rewardedVideoDidFailToLoad',
      (error) => console.log('rewardedVideoDidFailToLoad', error)
    );
    AdMobRewarded.addEventListener('rewardedVideoDidOpen',
      () => console.log('rewardedVideoDidOpen')
    );
    AdMobRewarded.addEventListener('rewardedVideoDidStartPlaying',
      () => console.log('rewardedVideoDidStartPlaying')
    );
    AdMobRewarded.addEventListener('rewardedVideoDidClose',
      () => {
        console.log('rewardedVideoDidClose');
        AdMobRewarded.requestAd((error) => error && console.log(error));
      }
    );
    AdMobRewarded.addEventListener('rewardedVideoWillLeaveApplication',
      () => console.log('rewardedVideoWillLeaveApplication')
    );

    AdMobRewarded.requestAd((error) => error && console.log(error));

    AdMobInterstitial.setTestDevices([AdMobRewarded.simulatorId]);
    AdMobInterstitial.setAdUnitID('ca-app-pub-3940256099942544/4411468910');

    AdMobInterstitial.addEventListener('interstitialDidLoad',
      () => console.log('interstitialDidLoad')
    );
    AdMobInterstitial.addEventListener('interstitialDidFailToLoad',
      (error) => console.log('interstitialDidFailToLoad', error)
    );
    AdMobInterstitial.addEventListener('interstitialDidOpen',
      () => console.log('interstitialDidOpen')
    );
    AdMobInterstitial.addEventListener('interstitialDidClose',
      () => {
        console.log('interstitialDidClose');
        AdMobInterstitial.requestAd((error) => error && console.log(error));
      }
    );
    AdMobInterstitial.addEventListener('interstitialWillLeaveApplication',
      () => console.log('interstitialWillLeaveApplication')
    );

    AdMobInterstitial.requestAd((error) => error && console.log(error));
  }

  componentWillUnmount() {
    AdMobRewarded.removeAllListeners();
    AdMobInterstitial.removeAllListeners();
  }

  showRewarded() {
    AdMobRewarded.showAd((error) => error && console.log(error));
  }

  showInterstitial() {
    AdMobInterstitial.showAd((error) => error && console.log(error));
  }

  render() {
    return (
      <View style={styles.container}>
        <ScrollView>
          <BannerExample title="AdMob - Basic">
            <AdMobBanner
              adSize="banner"
              adUnitID="ca-app-pub-3940256099942544/2934735716"
              ref={el => (this._basicExample = el)}
            />
            <Button
              title="Reload"
              onPress={() => this._basicExample.loadBanner()}
            />
          </BannerExample>
          <BannerExample title="Rewarded">
            <Button
              title="Show Rewarded Video and preload next"
              onPress={this.showRewarded}
            />
          </BannerExample>
          <BannerExample title="Interstitial">
            <Button
              title="Show Interstitial and preload next"
              onPress={this.showInterstitial}
            />
          </BannerExample>
          <BannerExample title="DFP - Multiple Ad Sizes">
            <PublisherBanner
              adSize="banner"
              validAdSizes={['banner', 'largeBanner', 'mediumRectangle']}
              adUnitID="/6499/example/APIDemo/AdSizes"
              ref={el => (this._adSizesExample = el)}
            />
            <Button
              title="Reload"
              onPress={() => this._adSizesExample.loadBanner()}
            />
          </BannerExample>
          <BannerExample title="DFP - App Events" style={this.state.appEventsExampleStyle}>
            <PublisherBanner
              style={{ height: 50 }}
              adUnitID="/6499/example/APIDemo/AppEvents"
              onAdmobDispatchAppEvent={(event) => {
                if (event.name === 'color') {
                  this.setState({
                    appEventsExampleStyle: { backgroundColor: event.info },
                  });
                }
              }}
              ref={el => (this._appEventsExample = el)}
            />
            <Button
              title="Reload"
              onPress={() => this._appEventsExample.loadBanner()}
              style={styles.button}
            />
          </BannerExample>
          <BannerExample title="DFP - Fluid Ad Size">
            <View
              style={[
                { backgroundColor: '#f3f', paddingVertical: 10 },
                this.state.fluidAdSizeExampleStyle,
              ]}
            >
              <PublisherBanner
                adSize="fluid"
                adUnitID="/6499/example/APIDemo/Fluid"
                ref={el => (this._appFluidAdSizeExample = el)}
                style={{ flex: 1 }}
              />
            </View>
            <Button
              title="Change Banner Width"
              onPress={() => this.setState(prevState => ({
                fluidSizeIndex: prevState.fluidSizeIndex + 1,
                fluidAdSizeExampleStyle: { width: bannerWidths[prevState.fluidSizeIndex % bannerWidths.length] },
              }))}
              style={styles.button}
            />
            <Button
              title="Reload"
              onPress={() => this._appFluidAdSizeExample.loadBanner()}
              style={styles.button}
            />
          </BannerExample>
        </ScrollView>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    marginTop: (Platform.OS === 'ios') ? 30 : 10,
  },
  example: {
    paddingVertical: 10,
  },
  title: {
    margin: 10,
    fontSize: 20,
  },
});

AppRegistry.registerComponent('Example', () => Example);
