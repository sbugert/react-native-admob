package com.sbugert.rnadmob;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.AdRequest;

import java.util.ArrayList;

public class RNAdMobRewardedVideoAdModule extends ReactContextBaseJavaModule implements RewardedVideoAdListener {
    RewardedVideoAd mRewardedVideoAd;
    String adUnitID;
    String[] testDevices;

    private Promise mRequestAdPromise;

    @Override
    public String getName() {
        return "RNAdMobRewarded";
    }

    public RNAdMobRewardedVideoAdModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        WritableMap reward = Arguments.createMap();

        reward.putInt("amount", rewardItem.getAmount());
        reward.putString("type", rewardItem.getType());

        sendEvent("rewardedVideoDidRewardUser", reward);
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        sendEvent("rewardedVideoDidLoad", null);
        mRequestAdPromise.resolve(null);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        sendEvent("rewardedVideoDidOpen", null);
    }

    @Override
    public void onRewardedVideoStarted() {
        sendEvent("rewardedVideoDidStart", null);
    }

    @Override
    public void onRewardedVideoAdClosed() {
        sendEvent("rewardedVideoDidClose", null);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        sendEvent("rewardedVideoWillLeaveApplication", null);
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        WritableMap event = Arguments.createMap();
        String errorString = null;

        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
            errorString = "ERROR_CODE_INTERNAL_ERROR";
            break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
            errorString = "ERROR_CODE_INVALID_REQUEST";
            break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
            errorString = "ERROR_CODE_NETWORK_ERROR";
            break;
            case AdRequest.ERROR_CODE_NO_FILL:
            errorString = "ERROR_CODE_NO_FILL";
            break;
        }

        event.putString("message", errorString);
        sendEvent("rewardedVideoDidFailToLoad", event);
        mRequestAdPromise.reject(errorString, errorString);
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @ReactMethod
    public void setAdUnitID(String adUnitID) {
        this.adUnitID = adUnitID;
    }

    @ReactMethod
    public void setTestDevices(ReadableArray testDevices) {
      ReadableNativeArray nativeArray = (ReadableNativeArray)testDevices;
      ArrayList<Object> list = nativeArray.toArrayList();
      this.testDevices = list.toArray(new String[list.size()]);
    }

    @ReactMethod
    public void requestAd(final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                RNAdMobRewardedVideoAdModule.this.mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getCurrentActivity());

                RNAdMobRewardedVideoAdModule.this.mRewardedVideoAd.setRewardedVideoAdListener(RNAdMobRewardedVideoAdModule.this);

                if (mRewardedVideoAd.isLoaded()) {
                    promise.reject("E_AD_ALREADY_LOADED", "Ad is already loaded.");
                } else {
                    mRequestAdPromise = promise;

                    AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

                    if (testDevices != null) {
                        for (int i = 0; i < testDevices.length; i++) {
                            adRequestBuilder.addTestDevice(testDevices[i]);
                        }
                    }

                    AdRequest adRequest = adRequestBuilder.build();
                    mRewardedVideoAd.loadAd(adUnitID, adRequest);
                }
            }
        });
    }

    @ReactMethod
    public void showAd(final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                    promise.resolve(null);
                } else {
                    promise.reject("E_AD_NOT_READY", "Ad is not ready.");
                }
            }
        });
    }

    @ReactMethod
    public void isReady(final Callback callback) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                callback.invoke(mRewardedVideoAd.isLoaded());
            }
        });
    }
}
