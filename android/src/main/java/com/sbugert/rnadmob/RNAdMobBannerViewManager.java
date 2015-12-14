package com.sbugert.rnadmob;

import android.support.annotation.Nullable;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ReactProp;
import com.facebook.react.uimanager.ReactShadowNode;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.ViewManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.Map;
import com.sbugert.rnadmob.RNAdMobBannerView.Events;

public class RNAdMobBannerViewManager extends SimpleViewManager<RNAdMobBannerView> {

  public static final String REACT_CLASS = "RNAdMob";

  public static final String PROP_BANNER_SIZE = "bannerSize";
  public static final String PROP_AD_UNIT_ID = "adUnitID";

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  protected RNAdMobBannerView createViewInstance(ThemedReactContext themedReactContext) {
    return new RNAdMobBannerView(themedReactContext);
  }

  @Override
  @Nullable
  public Map getExportedCustomDirectEventTypeConstants() {
    MapBuilder.Builder builder = MapBuilder.builder();
    for (Events event : Events.values()) {
      builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
    }
    return builder.build();
  }

  @ReactProp(name = PROP_BANNER_SIZE)
  public void setBannerSize(final RNAdMobBannerView bannerView, final String sizeString) {
    bannerView.setBannerSize(sizeString);
  }

  @ReactProp(name = PROP_AD_UNIT_ID)
  public void setAdUnitID(final RNAdMobBannerView bannerView, final String adUnitID) {
    bannerView.setAdUnitID(adUnitID);
  }

}
