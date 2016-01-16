package com.sbugert.rnadmob;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by andri on 16/01/2016.
 */
public class RNAdMobInterstitialAdModule extends ReactContextBaseJavaModule {
    InterstitialAd mInterstitialAd;

    public RNAdMobInterstitialAdModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mInterstitialAd = new InterstitialAd(reactContext);
    }

    @Override
    public String getName() {
        return "RNAdMobInterstitial";
    }

    @ReactMethod
    public void
    setAdUnitId(final String adUnitID) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d("RNAdMobInterstitial", "Settings adUnitId:" + adUnitID);
                mInterstitialAd.setAdUnitId(adUnitID);

                mInterstitialAd.setAdListener(new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        mInterstitialAd.show();
                    }
                });
            }
        });
    }

    @ReactMethod
    public void tryShowNewInterstitial(final String testDeviceId) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
                if (testDeviceId!=null){
                    adRequestBuilder = adRequestBuilder.addTestDevice(testDeviceId);
                    Log.d("RNAdMobInterstitial", "Setting testDeviceId:"+testDeviceId);
                }

                AdRequest adRequest = adRequestBuilder.build();
                mInterstitialAd.loadAd(adRequest);
                Log.d("RNAdMobInterstitial", "loadAd started");
            }
        });
    }
}
