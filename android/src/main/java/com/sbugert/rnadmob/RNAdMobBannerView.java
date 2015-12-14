package com.sbugert.rnadmob;

import android.util.DisplayMetrics;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

class RNAdMobBannerView extends ReactViewGroup {


  private ThemedReactContext mThemedReactContext;
  private RCTEventEmitter mEventEmitter;

  private AdView mAdView;
  private String mAdUnitID;
  private AdSize mAdSize;
  private AdRequest mAdRequest;

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

  public RNAdMobBannerView(ThemedReactContext themedReactContext) {
    super(themedReactContext);

    mThemedReactContext = themedReactContext;
    mEventEmitter = themedReactContext.getJSModule(RCTEventEmitter.class);
  }

  private void initAdView() {
    removeAllViews();
    if (mAdSize != null && mAdUnitID != null) {
      mAdView = new AdView(mThemedReactContext);
      mAdRequest = new AdRequest.Builder().build();
      mAdView.setAdSize(mAdSize);
      mAdView.setAdUnitId(mAdUnitID);
      mAdView.setAdListener(new AdListener() {
        @Override
        public void onAdLoaded() {
          super.onAdLoaded();
          addAdView();
          mEventEmitter.receiveEvent(getId(), Events.EVENT_RECEIVE_AD.toString(), null);
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

          mEventEmitter.receiveEvent(getId(), Events.EVENT_ERROR.toString(), event);
        }

        @Override
        public void onAdOpened() {
          mEventEmitter.receiveEvent(getId(), Events.EVENT_WILL_PRESENT.toString(), null);
        }

        @Override
        public void onAdClosed() {
          mEventEmitter.receiveEvent(getId(), Events.EVENT_WILL_DISMISS.toString(), null);
        }

        @Override
        public void onAdLeftApplication() {
          mEventEmitter.receiveEvent(getId(), Events.EVENT_WILL_LEAVE_APP.toString(), null);
        }
      });

      mAdView.loadAd(mAdRequest);
    }
  }

  protected void addAdView() {
    removeAllViews();
    int width = mAdSize.getWidthInPixels(mThemedReactContext);
    int height = mAdSize.getHeightInPixels(mThemedReactContext);
    mAdView.measure(width, height);
    mAdView.layout(0, 0, width, height);


    this.layout(0, 0, width, height);
    this.addView(mAdView);
    mAdView.setVisibility(VISIBLE);
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

  //http://stackoverflow.com/a/17410076/1895395
  public int pxToDp(int px) {
    DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    return dp;
  }

  protected void setBannerSize(String adSize) {
    mAdSize = getAdSizeFromString(adSize);

    int width;
    int height;

    WritableMap event = Arguments.createMap();

    if (mAdSize == AdSize.SMART_BANNER) {
      width = pxToDp(mAdSize.getWidthInPixels(mThemedReactContext));
      height = pxToDp(mAdSize.getHeightInPixels(mThemedReactContext));
    }
    else {
      width = mAdSize.getWidth();
      height = mAdSize.getHeight();
    }
    event.putInt("width", width);
    event.putInt("height", height);
    mEventEmitter.receiveEvent(getId(), Events.EVENT_SIZE_CHANGE.toString(), event);

    initAdView();
  }

  protected void setAdUnitID(String adUnitID) {
    mAdUnitID = adUnitID;
    initAdView();
  }
}
