package com.sbugert.rnadmob;

import android.annotation.SuppressLint;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.ads.MobileAds;

public class RNAdMobModule extends ReactContextBaseJavaModule {

    private static final String REACT_CLASS = "RNAdMob";

    RNAdMobModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @SuppressLint("MissingPermission")
    @ReactMethod
    public void setAppId(String id) {
        MobileAds.initialize(getReactApplicationContext(), id);
    }
}
