package com.sbugert.rnadmob;

import com.amazon.device.ads.AdRegistration;
import com.amazon.device.ads.MRAIDPolicy;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RNAdMobAPSModule extends ReactContextBaseJavaModule {
    public static final String REACT_CLASS = "RNAdMobAPS";

    private ReactApplicationContext reactContext;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    public RNAdMobAPSModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }
    @ReactMethod
    public void setAppKey(String appKey) {
        if(!APSUtil.shouldUseAPS()) return;
        AdRegistration.getInstance(appKey, this.reactContext.getApplicationContext());
        AdRegistration.setMRAIDSupportedVersions(new String[] {"1.0", "2.0", "3.0"});
        AdRegistration.setMRAIDPolicy(MRAIDPolicy.CUSTOM);
    }

    @ReactMethod
    public void setEnableTesting(boolean enableTesting){
        AdRegistration.enableTesting(enableTesting);
    }

    @ReactMethod
    public void setEnableLogging(boolean enableLogging){
        AdRegistration.enableLogging(enableLogging);
    }

}
