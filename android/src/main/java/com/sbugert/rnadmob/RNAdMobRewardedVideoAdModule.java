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

    public static final String REACT_CLASS = "RNAdMobRewarded";

    public static final String EVENT_AD_LOADED = "rewardedVideoAdLoaded";
    public static final String EVENT_AD_FAILED_TO_LOAD = "rewardedVideoAdFailedToLoad";
    public static final String EVENT_AD_OPENED = "rewardedVideoAdOpened";
    public static final String EVENT_AD_CLOSED = "rewardedVideoAdClosed";
    public static final String EVENT_AD_LEFT_APPLICATION = "rewardedVideoAdLeftApplication";
    public static final String EVENT_REWARDED = "rewardedVideoAdRewarded";
    public static final String EVENT_VIDEO_STARTED = "rewardedVideoAdVideoStarted";
    public static final String EVENT_VIDEO_COMPLETED = "rewardedVideoAdVideoCompleted";

    RewardedVideoAd mRewardedVideoAd;
    String adUnitID;
    String[] testDevices;

    private Promise mRequestAdPromise;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    public RNAdMobRewardedVideoAdModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        WritableMap reward = Arguments.createMap();

        reward.putInt("amount", rewardItem.getAmount());
        reward.putString("type", rewardItem.getType());

        sendEvent(EVENT_REWARDED, reward);
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        sendEvent(EVENT_AD_LOADED, null);
        mRequestAdPromise.resolve(null);
    }

    @Override
    public void onRewardedVideoAdOpened() {
        sendEvent(EVENT_AD_OPENED, null);
    }

    @Override
    public void onRewardedVideoStarted() {
        sendEvent(EVENT_VIDEO_STARTED, null);
    }

    @Override
    public void onRewardedVideoAdClosed() {
        sendEvent(EVENT_AD_CLOSED, null);
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        sendEvent(EVENT_AD_LEFT_APPLICATION, null);
    }

    @Override
    public void onRewardedVideoCompleted() {
        sendEvent(EVENT_VIDEO_COMPLETED, null);
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        String errorString = "ERROR_UNKNOWN";
        String errorMessage = "Unknown error";
        switch (errorCode) {
            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
              errorString = "ERROR_CODE_INTERNAL_ERROR";
              errorMessage = "Internal error, an invalid response was received from the ad server.";
              break;
            case AdRequest.ERROR_CODE_INVALID_REQUEST:
              errorString = "ERROR_CODE_INVALID_REQUEST";
              errorMessage = "Invalid ad request, possibly an incorrect ad unit ID was given.";
              break;
            case AdRequest.ERROR_CODE_NETWORK_ERROR:
              errorString = "ERROR_CODE_NETWORK_ERROR";
              errorMessage = "The ad request was unsuccessful due to network connectivity.";
              break;
            case AdRequest.ERROR_CODE_NO_FILL:
              errorString = "ERROR_CODE_NO_FILL";
              errorMessage = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
              break;
        }
        WritableMap event = Arguments.createMap();
        WritableMap error = Arguments.createMap();
        event.putString("message", errorMessage);
        sendEvent(EVENT_AD_FAILED_TO_LOAD, event);
        mRequestAdPromise.reject(errorString, errorMessage);
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
            public void run() {
                RNAdMobRewardedVideoAdModule.this.mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(getCurrentActivity());

                RNAdMobRewardedVideoAdModule.this.mRewardedVideoAd.setRewardedVideoAdListener(RNAdMobRewardedVideoAdModule.this);

                if (mRewardedVideoAd.isLoaded()) {
                    promise.reject("E_AD_ALREADY_LOADED", "Ad is already loaded.");
                } else {
                    mRequestAdPromise = promise;

                    AdRequest.Builder adRequestBuilder = new AdRequest.Builder();

                    if (testDevices != null) {
                        for (int i = 0; i < testDevices.length; i++) {
                            String testDevice = testDevices[i];
                            if (testDevice == "SIMULATOR") {
                                testDevice = AdRequest.DEVICE_ID_EMULATOR;
                            }
                            adRequestBuilder.addTestDevice(testDevice);
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
            public void run() {
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
            public void run() {
                callback.invoke(mRewardedVideoAd.isLoaded());
            }
        });
    }
}
