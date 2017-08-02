package com.sbugert.rnadmob;

import android.content.Context;
import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ReactAdView extends ReactViewGroup {

  protected AdView adView;
  String[] testDevices;

  public ReactAdView(final Context context) {
    super(context);
    this.adView = new AdView(context);
    this.adView.setAdListener(new AdListener() {
      @Override
      public void onAdLoaded() {
        int width = adView.getAdSize().getWidthInPixels(context);
        int height = adView.getAdSize().getHeightInPixels(context);
        int left = adView.getLeft();
        int top = adView.getTop();
        adView.measure(width, height);
        adView.layout(left, top, left + width, top + height);
        sendOnSizeChangeEvent();
        sendEvent("onAdViewDidReceiveAd", null);
      }

      @Override
      public void onAdFailedToLoad(int errorCode) {
        WritableMap event = Arguments.createMap();
        switch (errorCode) {
          case AdRequest.ERROR_CODE_INTERNAL_ERROR:
            event.putString("error", "ERROR_CODE_INTERNAL_ERROR");
            break;
          case AdRequest.ERROR_CODE_INVALID_REQUEST:
            event.putString("error", "ERROR_CODE_INVALID_REQUEST");
            break;
          case AdRequest.ERROR_CODE_NETWORK_ERROR:
            event.putString("error", "ERROR_CODE_NETWORK_ERROR");
            break;
          case AdRequest.ERROR_CODE_NO_FILL:
            event.putString("error", "ERROR_CODE_NO_FILL");
            break;
        }
        sendEvent("onDidFailToReceiveAdWithError", event);
      }

      @Override
      public void onAdOpened() {
        sendEvent("onAdViewWillPresentScreen", null);
      }

      @Override
      public void onAdClosed() {
        sendEvent("onAdViewWillDismissScreen", null);
      }

      @Override
      public void onAdLeftApplication() {
        sendEvent("onAdViewWillLeaveApplication", null);
      }
    });
    this.addView(this.adView);
  }

  private void sendOnSizeChangeEvent() {
    int width;
    int height;
    ReactContext reactContext = (ReactContext) getContext();
    WritableMap event = Arguments.createMap();
    AdSize adSize = this.adView.getAdSize();
    if (adSize == AdSize.SMART_BANNER) {
      width = (int) PixelUtil.toDIPFromPixel(adSize.getWidthInPixels(reactContext));
      height = (int) PixelUtil.toDIPFromPixel(adSize.getHeightInPixels(reactContext));
    } else {
      width = adSize.getWidth();
      height = adSize.getHeight();
    }
    event.putDouble("width", width);
    event.putDouble("height", height);
    sendEvent("onSizeChange", event);
  }

  private void sendEvent(String name, @Nullable WritableMap event) {
    ReactContext reactContext = (ReactContext) getContext();
    reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
            getId(),
            name,
            event);
  }

  public void loadBanner() {
    AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
    if (testDevices != null) {
      for (int i = 0; i < testDevices.length; i++) {
        adRequestBuilder.addTestDevice(testDevices[i]);
      }
    }
    AdRequest adRequest = adRequestBuilder.build();
    this.adView.loadAd(adRequest);
  }

  public void setAdUnitID(String adUnitID) {
    this.adView.setAdUnitId(adUnitID);
  }

  public void setTestDevices(String[] testDevices) {
    this.testDevices = testDevices;
  }

  public void setAdSize(AdSize adSize) {
    this.adView.setAdSize(adSize);
  }
}

public class RNAdMobBannerViewManager extends SimpleViewManager<ReactAdView> {

  public static final String REACT_CLASS = "RNGADBannerView";

  public static final String PROP_AD_SIZE = "adSize";
  public static final String PROP_AD_UNIT_ID = "adUnitID";
  public static final String PROP_TEST_DEVICES = "testDevices";

  public static final int COMMAND_LOAD_BANNER = 1;

  public enum Events {
    EVENT_SIZE_CHANGE("onSizeChange"),
    EVENT_RECEIVE_AD("onAdViewDidReceiveAd"),
    EVENT_ERROR("onDidFailToReceiveAdWithError"),
    EVENT_WILL_PRESENT("onAdViewWillPresentScreen"),
    EVENT_WILL_DISMISS("onAdViewWillDismissScreen"),
    EVENT_DID_DISMISS("onAdViewDidDismissScreen"),
    EVENT_WILL_LEAVE_APP("onAdViewWillLeaveApplication");

    private final String mName;

    Events(final String name) {
      mName = name;
    }

    @Override
    public String toString() {
      return mName;
    }
  }

  private ThemedReactContext mThemedReactContext;
  private RCTEventEmitter mEventEmitter;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  protected ReactAdView createViewInstance(ThemedReactContext themedReactContext) {
    ReactAdView adView = new ReactAdView(themedReactContext);
    return adView;
  }

  @Override
  @Nullable
  public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
    MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
    for (Events event : Events.values()) {
      builder.put(event.toString(), MapBuilder.of("registrationName", event.toString()));
    }
    return builder.build();
  }

  @ReactProp(name = PROP_AD_SIZE)
  public void setPropAdSize(final ReactAdView view, final String sizeString) {
    AdSize adSize = getAdSizeFromString(sizeString);
    view.setAdSize(adSize);
  }

  @ReactProp(name = PROP_AD_UNIT_ID)
  public void setPropAdUnitID(final ReactAdView view, final String adUnitID) {
    view.setAdUnitID(adUnitID);
  }

  @ReactProp(name = PROP_TEST_DEVICES)
  public void setPropTestDevices(final ReactAdView view, final ReadableArray testDevices) {
    ReadableNativeArray nativeArray = (ReadableNativeArray)testDevices;
    ArrayList<Object> list = nativeArray.toArrayList();
    view.setTestDevices(list.toArray(new String[list.size()]));
  }

  private AdSize getAdSizeFromString(String adSize) {
    switch (adSize) {
      case "banner":
        return AdSize.BANNER;
      case "largeBanner":
        return AdSize.LARGE_BANNER;
      case "mediumRectangle":
        return AdSize.MEDIUM_RECTANGLE;
      case "fullBanner":
        return AdSize.FULL_BANNER;
      case "leaderBoard":
        return AdSize.LEADERBOARD;
      case "smartBannerPortrait":
        return AdSize.SMART_BANNER;
      case "smartBannerLandscape":
        return AdSize.SMART_BANNER;
      case "smartBanner":
        return AdSize.SMART_BANNER;
      default:
        return AdSize.BANNER;
    }
  }

  @javax.annotation.Nullable
  @Override
  public Map<String, Object> getConstants() {
    final Map<String, Object> constants = new HashMap<>();
    constants.put("simulatorId", AdRequest.DEVICE_ID_EMULATOR);
    return constants;
  }

  @javax.annotation.Nullable
  @Override
  public Map<String, Integer> getCommandsMap() {
    return MapBuilder.of("loadBanner", COMMAND_LOAD_BANNER);
  }

  @Override
  public void receiveCommand(ReactAdView root, int commandId, @javax.annotation.Nullable ReadableArray args) {
    switch (commandId) {
      case COMMAND_LOAD_BANNER:
        root.loadBanner();
        break;
    }
  }
}
