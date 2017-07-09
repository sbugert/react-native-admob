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
import { AdMobRewarded, PublisherBanner } from 'react-native-admob';

const BannerExample = ({ style, title, children, ...props }) => (
  <View {...props} style={[styles.example, style]}>
    <Text style={styles.title}>{title}</Text>
    <View>
      {children}
    </View>
  </View>
);

export default class Example extends Component {

  constructor() {
    super();
    this.state = {};
  }

  componentDidMount() {
    AdMobRewarded.setTestDeviceID('EMULATOR');
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
  }

  componentWillUnmount() {
    AdMobRewarded.removeAllListeners();
  }

  showRewarded() {
    AdMobRewarded.showAd((error) => error && console.log(error));
  }

  render() {
    return (
      <View style={styles.container}>
        <ScrollView>
          <BannerExample title="Rewarded">
            <Button
              title="Show Rewarded Video and preload next"
              onPress={this.showRewarded}
            />
          </BannerExample>
          <BannerExample title="Ad Sizes">
            <PublisherBanner
              adSize="banner"
              validAdSizes={['banner', 'largeBanner', 'mediumRectangle']}
              adUnitID="/6499/example/APIDemo/AdSizes"
              ref={el => (this._adSizesExample = el)}
            />
            <Button
              title="Load"
              onPress={() => this._adSizesExample.loadBanner()}
            />
          </BannerExample>
          <BannerExample title="App Events" style={this.state.appEventsExampleStyle}>
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
              title="Load"
              onPress={() => this._appEventsExample.loadBanner()}
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
