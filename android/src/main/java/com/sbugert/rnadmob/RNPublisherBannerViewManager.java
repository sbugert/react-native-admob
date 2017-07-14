package com.sbugert.rnadmob;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.PixelUtil;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.AppEventListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.doubleclick.PublisherAdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class ReactPublisherAdView extends ReactViewGroup implements AppEventListener {

  protected PublisherAdView adView;
  String[] testDevices;
  AdSize[] validAdSizes;
  AdSize adSize;

  public ReactPublisherAdView(final Context context) {
    super(context);
    this.adView = new PublisherAdView(context);
    this.adView.setAppEventListener(this);
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
          case PublisherAdRequest.ERROR_CODE_INTERNAL_ERROR:
            event.putString("error", "ERROR_CODE_INTERNAL_ERROR");
            break;
          case PublisherAdRequest.ERROR_CODE_INVALID_REQUEST:
            event.putString("error", "ERROR_CODE_INVALID_REQUEST");
            break;
          case PublisherAdRequest.ERROR_CODE_NETWORK_ERROR:
            event.putString("error", "ERROR_CODE_NETWORK_ERROR");
            break;
          case PublisherAdRequest.ERROR_CODE_NO_FILL:
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
    ArrayList<AdSize> adSizes = new ArrayList<AdSize>();
    adSizes.add(this.adSize);
    if (this.validAdSizes != null) {
      for (int i = 0; i < this.validAdSizes.length; i++) {
        adSizes.add(this.validAdSizes[i]);
      }
    }
    AdSize[] adSizesArray = adSizes.toArray(new AdSize[adSizes.size()]);
    this.adView.setAdSizes(adSizesArray);

    // this.sendOnSizeChangeEvent();

    PublisherAdRequest.Builder adRequestBuilder = new PublisherAdRequest.Builder();
    if (testDevices != null) {
      for (int i = 0; i < testDevices.length; i++) {
        adRequestBuilder.addTestDevice(testDevices[i]);
      }
    }
    PublisherAdRequest adRequest = adRequestBuilder.build();
    this.adView.loadAd(adRequest);
  }

  public void setAdUnitID(String adUnitID) {
    this.adView.setAdUnitId(adUnitID);
  }

  public void setTestDevices(String[] testDevices) {
    this.testDevices = testDevices;
  }

  public void setAdSize(AdSize adSize) {
    this.adSize = adSize;
  }

  public void setValidAdSizes(AdSize[] adSizes) {
    this.validAdSizes = adSizes;
  }

  @Override
  public void onAppEvent(String name, String info) {
    WritableMap event = Arguments.createMap();
    event.putString("name", name);
    event.putString("info", info);
    ReactContext reactContext = (ReactContext) getContext();
    reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
            getId(),
            "onAdmobDispatchAppEvent",
            event);
    String message = String.format("Received app event (%s, %s)", name, info);
    Log.d("PublisherAdBanner", message);
  }
}

public class RNPublisherBannerViewManager extends SimpleViewManager<ReactPublisherAdView> {

  public static final String REACT_CLASS = "RNDFPBannerView";

  public static final String PROP_AD_SIZE = "adSize";
  public static final String PROP_VALID_AD_SIZES = "validAdSizes";
  public static final String PROP_AD_UNIT_ID = "adUnitID";
  public static final String PROP_TEST_DEVICES = "testDevices";

  public static final int COMMAND_LOAD_BANNER = 1;

  private ThemedReactContext mThemedReactContext;
  private RCTEventEmitter mEventEmitter;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  protected ReactPublisherAdView createViewInstance(ThemedReactContext themedReactContext) {
    ReactPublisherAdView adView = new ReactPublisherAdView(themedReactContext);
    return adView;
  }

  @Override
  @Nullable
  public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
    MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
    return builder
      .put("onSizeChange", MapBuilder.of("registrationName", "onSizeChange"))
      .put("onAdViewDidReceiveAd", MapBuilder.of("registrationName", "onAdViewDidReceiveAd"))
      .put("onDidFailToReceiveAdWithError", MapBuilder.of("registrationName", "onDidFailToReceiveAdWithError"))
      .put("onAdViewWillPresentScreen", MapBuilder.of("registrationName", "onAdViewWillPresentScreen"))
      .put("onAdViewWillDismissScreen", MapBuilder.of("registrationName", "onAdViewWillDismissScreen"))
      .put("onAdViewDidDismissScreen", MapBuilder.of("registrationName", "onAdViewDidDismissScreen"))
      .put("onAdViewWillLeaveApplication", MapBuilder.of("registrationName", "onAdViewWillLeaveApplication"))
      .put("onAdmobDispatchAppEvent", MapBuilder.of("registrationName", "onAdmobDispatchAppEvent"))
      .build();
  }

  @ReactProp(name = PROP_AD_SIZE)
  public void setPropAdSize(final ReactPublisherAdView view, final String sizeString) {
    AdSize adSize = getAdSizeFromString(sizeString);
    view.setAdSize(adSize);
  }

  @ReactProp(name = PROP_VALID_AD_SIZES)
  public void setPropValidAdSizes(final ReactPublisherAdView view, final ReadableArray adSizeStrings) {
    ArrayList<Object> list = adSizeStrings.toArrayList();
    String[] adSizeStringsArray = list.toArray(new String[list.size()]);
    AdSize[] adSizes = new AdSize[list.size()];

    for (int i = 0; i < adSizeStringsArray.length; i++) {
        String adSizeString = adSizeStringsArray[i];
        adSizes[i] = getAdSizeFromString(adSizeString);
    }
    view.setValidAdSizes(adSizes);
  }

  @ReactProp(name = PROP_AD_UNIT_ID)
  public void setPropAdUnitID(final ReactPublisherAdView view, final String adUnitID) {
    view.setAdUnitID(adUnitID);
  }

  @ReactProp(name = PROP_TEST_DEVICES)
  public void setPropTestDevices(final ReactPublisherAdView view, final ReadableArray testDevices) {
    ArrayList<Object> list = testDevices.toArrayList();
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
    constants.put("simulatorId", PublisherAdRequest.DEVICE_ID_EMULATOR);
    return constants;
  }

  @javax.annotation.Nullable
  @Override
  public Map<String, Integer> getCommandsMap() {
    return MapBuilder.of("loadBanner", COMMAND_LOAD_BANNER);
  }

  @Override
  public void receiveCommand(ReactPublisherAdView root, int commandId, @javax.annotation.Nullable ReadableArray args) {
    switch (commandId) {
      case COMMAND_LOAD_BANNER:
        root.loadBanner();
        break;
    }
  }
}
