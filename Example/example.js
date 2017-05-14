import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View,
  Platform,
  TouchableHighlight,
} from 'react-native';
import { AdMobRewarded, AdMobBanner } from 'react-native-admob';

export default class Example extends Component {

  componentDidMount() {
    AdMobRewarded.setTestDeviceIDs(['EMULATOR']);
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
        <View style={{ flex: 1 }}>
          <TouchableHighlight>
            <Text onPress={this.showRewarded} style={styles.button}>
              Show Rewarded Video and preload next
            </Text>
          </TouchableHighlight>
        </View>

        <AdMobBanner
          testDeviceIDs={['EMULATOR']}
          adUnitID="ca-app-pub-3940256099942544/2934735716"
        />
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    marginTop: (Platform.OS === 'ios') ? 30 : 10,
    flex: 1,
    alignItems: 'center',
  },
  button: {
    color: '#333333',
    marginBottom: 15,
  },
});

AppRegistry.registerComponent('Example', () => Example);
