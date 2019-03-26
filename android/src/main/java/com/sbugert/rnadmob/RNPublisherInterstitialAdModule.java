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
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableNativeMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import java.util.ArrayList;

public class RNPublisherInterstitialAdModule extends ReactContextBaseJavaModule {

    public static final String REACT_CLASS = "RNDFPInterstitial";

    public static final String EVENT_AD_LOADED = "interstitialAdLoaded";
    public static final String EVENT_AD_FAILED_TO_LOAD = "interstitialAdFailedToLoad";
    public static final String EVENT_AD_OPENED = "interstitialAdOpened";
    public static final String EVENT_AD_CLOSED = "interstitialAdClosed";
    public static final String EVENT_AD_LEFT_APPLICATION = "interstitialAdLeftApplication";

    PublisherInterstitialAd mInterstitialAd;
    String[] testDevices;
    ReadableNativeMap kvs;
    String contentUrl;

    private Promise mRequestAdPromise;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    public RNPublisherInterstitialAdModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mInterstitialAd = new PublisherInterstitialAd(reactContext);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        sendEvent(EVENT_AD_CLOSED, null);
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
                        sendEvent(EVENT_AD_FAILED_TO_LOAD, event);
                        mRequestAdPromise.reject(errorString, errorMessage);
                    }
                    @Override
                    public void onAdLeftApplication() {
                        sendEvent(EVENT_AD_LEFT_APPLICATION, null);
                    }
                    @Override
                    public void onAdLoaded() {
                        sendEvent(EVENT_AD_LOADED, null);
                        mRequestAdPromise.resolve(null);
                    }
                    @Override
                    public void onAdOpened() {
                        sendEvent(EVENT_AD_OPENED, null);
                    }
                });
            }
        });
    }
    private void sendEvent(String eventName, @Nullable WritableMap params) {
        getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
    }

    @ReactMethod
    public void setAdUnitID(String adUnitID) {
        if (mInterstitialAd.getAdUnitId() == null) {
            mInterstitialAd.setAdUnitId(adUnitID);
        }
    }

    @ReactMethod
    public void setKvs(ReadableMap kvs) {
        this.kvs = (ReadableNativeMap) kvs;
    }

    @ReactMethod
    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
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
                if (mInterstitialAd.isLoaded() || mInterstitialAd.isLoading()) {
                    promise.reject("E_AD_ALREADY_LOADED", "Ad is already loaded.");
                } else {
                    mRequestAdPromise = promise;
                    PublisherAdRequest.Builder adRequestBuilder = new PublisherAdRequest.Builder();
                    if (testDevices != null) {
                        for (int i = 0; i < testDevices.length; i++) {
                            String testDevice = testDevices[i];
                            if (testDevice == "SIMULATOR") {
                                testDevice = AdRequest.DEVICE_ID_EMULATOR;
                            }
                            adRequestBuilder.addTestDevice(testDevice);
                        }
                    }
                    if (kvs != null) {
                        ReadableMapKeySetIterator iterator = kvs.keySetIterator();
                        while (iterator.hasNextKey()) {
                            String key = iterator.nextKey();
                            String value = kvs.getString(key);
                            adRequestBuilder.addCustomTargeting(key, value);
                        }
                    }
                    if (contentUrl != null) {
                        adRequestBuilder.setContentUrl(contentUrl);
                    }


                    PublisherAdRequest adRequest = adRequestBuilder.build();
                    mInterstitialAd.loadAd(adRequest);
                }
            }
        });
    }

    @ReactMethod
    public void showAd(final Promise promise) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run () {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
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
                callback.invoke(mInterstitialAd.isLoaded());
            }
        });
    }
}
