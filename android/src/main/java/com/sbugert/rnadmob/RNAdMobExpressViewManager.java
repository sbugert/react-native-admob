package com.sbugert.rnadmob;

import android.support.annotation.Nullable;
import android.view.View.OnLayoutChangeListener;
import android.view.View;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
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
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.Map;

public class RNAdMobExpressViewManager extends SimpleViewManager<ReactViewGroup> {

  public static final String REACT_CLASS = "RNAdMobExpress";

  public static final String PROP_BANNER_SIZE = "bannerSize";
  public static final String PROP_AD_UNIT_ID = "adUnitID";
  public static final String PROP_TEST_DEVICE_ID = "testDeviceID";

  private String testDeviceID = null;

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
  private ReactViewGroup mView;
  private String mSizeString;

  @Override
  public String getName() {
    return REACT_CLASS;
  }

  @Override
  protected ReactViewGroup createViewInstance(ThemedReactContext themedReactContext) {
    mThemedReactContext = themedReactContext;
    mEventEmitter = themedReactContext.getJSModule(RCTEventEmitter.class);
    mView = new ReactViewGroup(themedReactContext);
    attachNewAdView(mView);

    mView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
      public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (left != oldLeft || right != oldRight || top != oldTop || bottom != oldBottom) {
          setExpressSize(mView, mSizeString);
        }
      }
    });

    return mView;
  }

  protected void attachNewAdView(final ReactViewGroup view) {
    final NativeExpressAdView adView = new NativeExpressAdView(mThemedReactContext);

    // destroy old AdView if present
    NativeExpressAdView oldAdView = (NativeExpressAdView) view.getChildAt(0);
    view.removeAllViews();
    if (oldAdView != null) oldAdView.destroy();
    view.addView(adView);
    attachEvents(view);
  }

  protected void attachEvents(final ReactViewGroup view) {
    final NativeExpressAdView adView = (NativeExpressAdView) view.getChildAt(0);
    adView.setAdListener(new AdListener() {
      @Override
      public void onAdLoaded() {
        int width = adView.getAdSize().getWidthInPixels(mThemedReactContext);
        int height = adView.getAdSize().getHeightInPixels(mThemedReactContext);
        int left = adView.getLeft();
        int top = adView.getTop();
        adView.measure(width, height);
        adView.layout(left, top, left + width, top + height);
        mEventEmitter.receiveEvent(view.getId(), Events.EVENT_RECEIVE_AD.toString(), null);
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

        mEventEmitter.receiveEvent(view.getId(), Events.EVENT_ERROR.toString(), event);
      }

      @Override
      public void onAdOpened() {
        mEventEmitter.receiveEvent(view.getId(), Events.EVENT_WILL_PRESENT.toString(), null);
      }

      @Override
      public void onAdClosed() {
        mEventEmitter.receiveEvent(view.getId(), Events.EVENT_WILL_DISMISS.toString(), null);
      }

      @Override
      public void onAdLeftApplication() {
        mEventEmitter.receiveEvent(view.getId(), Events.EVENT_WILL_LEAVE_APP.toString(), null);
      }
    });
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

  @ReactProp(name = PROP_BANNER_SIZE)
  public void setExpressSize(final ReactViewGroup view, final String sizeString) {
    mSizeString = sizeString;
    String[] dimension = sizeString.split("x");
    int widthad = Integer.parseInt(dimension[0]);
    int heightad = Integer.parseInt(dimension[1]);
    AdSize adSize = new AdSize(widthad, heightad);

    // store old ad unit ID (even if not yet present and thus null)
    NativeExpressAdView oldAdView = (NativeExpressAdView) view.getChildAt(0);
    String adUnitId = oldAdView.getAdUnitId();

    attachNewAdView(view);
    NativeExpressAdView newAdView = (NativeExpressAdView) view.getChildAt(0);
    newAdView.setAdSize(adSize);
    newAdView.setAdUnitId(adUnitId);

    // send measurements to js to style the AdView in react
    int width;
    int height;
    WritableMap event = Arguments.createMap();
    width = Integer.parseInt(dimension[0]);
    height = Integer.parseInt(dimension[1]);
    event.putDouble("width", width);
    event.putDouble("height", height);
    mEventEmitter.receiveEvent(view.getId(), Events.EVENT_SIZE_CHANGE.toString(), event);

    loadAd(newAdView);
  }

  @ReactProp(name = PROP_AD_UNIT_ID)
  public void setAdUnitID(final ReactViewGroup view, final String adUnitID) {
    // store old banner size (even if not yet present and thus null)
    NativeExpressAdView oldAdView = (NativeExpressAdView) view.getChildAt(0);
    AdSize adSize = oldAdView.getAdSize();

    attachNewAdView(view);
    NativeExpressAdView newAdView = (NativeExpressAdView) view.getChildAt(0);
    newAdView.setAdUnitId(adUnitID);
    newAdView.setAdSize(adSize);
    loadAd(newAdView);
  }

  @ReactProp(name = PROP_TEST_DEVICE_ID)
  public void setPropTestDeviceID(final ReactViewGroup view, final String testDeviceID) {
    this.testDeviceID = testDeviceID;
  }

  private void loadAd(final NativeExpressAdView adView) {
    if (adView.getAdSize() != null && adView.getAdUnitId() != null) {
      AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
      if (testDeviceID != null){
        if (testDeviceID.equals("EMULATOR")) {
          adRequestBuilder = adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        } else {
          adRequestBuilder = adRequestBuilder.addTestDevice(testDeviceID);
        }
      }
      AdRequest adRequest = adRequestBuilder.build();
      adView.loadAd(adRequest);
    }
  }
}
