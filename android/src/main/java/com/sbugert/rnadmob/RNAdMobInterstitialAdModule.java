package com.sbugert.rnadmob;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Pair;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RNAdMobInterstitialAdModule extends ReactContextBaseJavaModule {

    public static final String REACT_CLASS = "RNAdMobInterstitial";

    public static final String EVENT_AD_LOADED = "interstitialAdLoaded";
    public static final String EVENT_AD_FAILED_TO_LOAD = "interstitialAdFailedToLoad";
    public static final String EVENT_AD_OPENED = "interstitialAdOpened";
    public static final String EVENT_AD_CLOSED = "interstitialAdClosed";
    public static final String EVENT_AD_LEFT_APPLICATION = "interstitialAdLeftApplication";

    ReactApplicationContext mContext;
    Map<String, InterstitialAd> mInterstitialAds;
    String[] testDevices;

    private final Map<String, Promise> mRequestAdPromises;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    public RNAdMobInterstitialAdModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
        mInterstitialAds = new HashMap<>();
        mRequestAdPromises = new HashMap<>();
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @ReactMethod
    public void setAdUnitID(final String adUnitID) {
        if (!mInterstitialAds.containsKey(adUnitID)) {
            mInterstitialAds.put(adUnitID, new InterstitialAd(mContext));
            mInterstitialAds.get(adUnitID).setAdUnitId(adUnitID);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
              @Override
              public void run() {
                mInterstitialAds.get(adUnitID).setAdListener(new AdListener() {
                  @Override
                  public void onAdClosed() {
                    WritableMap params = Arguments.createMap();
                    params.putString("adUnitId", adUnitID);
                    sendEvent(EVENT_AD_CLOSED, params);
                  }
                  @Override
                  public void onAdFailedToLoad(int errorCode) {
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
                    event.putString("adUnitId", adUnitID);
                    sendEvent(EVENT_AD_FAILED_TO_LOAD, event);
                    if (mRequestAdPromises.get(adUnitID) != null) {
                      mRequestAdPromises.get(adUnitID).reject(errorString, errorMessage);
                      // todo:: check how to set promise to null
                      mRequestAdPromises.put(adUnitID, null);
                    }
                  }
                  @Override
                  public void onAdLeftApplication() {
                    WritableMap params = Arguments.createMap();
                    params.putString("adUnitId", adUnitID);
                    sendEvent(EVENT_AD_LEFT_APPLICATION, params);
                  }
                  @Override
                  public void onAdLoaded() {
                    WritableMap params = Arguments.createMap();
                    params.putString("adUnitId", adUnitID);
                    sendEvent(EVENT_AD_LOADED, params);
                    if (mRequestAdPromises.get(adUnitID) != null) {
                      mRequestAdPromises.get(adUnitID).resolve(null);
                      // todo:: check how to set promise to null
                      mRequestAdPromises.put(adUnitID, null);
                    }
                  }
                  @Override
                  public void onAdOpened() {
                    WritableMap params = Arguments.createMap();
                    params.putString("adUnitId", adUnitID);
                    sendEvent(EVENT_AD_OPENED, params);
                  }
                });
              }
            });
        }
    }

    @ReactMethod
    public void setTestDevices(ReadableArray testDevices) {
        ReadableNativeArray nativeArray = (ReadableNativeArray)testDevices;
        ArrayList<Object> list = nativeArray.toArrayList();
        this.testDevices = list.toArray(new String[list.size()]);
    }

    @ReactMethod
    public void requestAd(final String adUnitId, final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                if (mInterstitialAds.get(adUnitId).isLoaded() || mInterstitialAds.get(adUnitId).isLoading()) {
                    promise.reject("E_AD_ALREADY_LOADED", "Ad is already loaded.");
                } else {
                    mRequestAdPromises.put(adUnitId, promise);
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
                    mInterstitialAds.get(adUnitId).loadAd(adRequest);
                }
            }
        });
    }

    @ReactMethod
    public void showAd(final String adUnitId, final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                if (mInterstitialAds.get(adUnitId).isLoaded()) {
                    mInterstitialAds.get(adUnitId).show();
                    promise.resolve(null);
                } else {
                    promise.reject("E_AD_NOT_READY", "Ad is not ready.");
                }
            }
        });
    }

    @ReactMethod
    public void isReady(final String adUnitId, final Callback callback) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
              if (mInterstitialAds.get(adUnitId) != null){
                callback.invoke(mInterstitialAds.get(adUnitId).isLoaded());
              }
            }
        });
    }
}
