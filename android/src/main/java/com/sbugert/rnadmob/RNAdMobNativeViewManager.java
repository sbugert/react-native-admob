package com.sbugert.rnadmob;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Button;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.views.view.ReactViewGroup;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.gms.ads.formats.NativeAd.Image;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


class ReactNativeView extends ReactViewGroup {

    String adUnitID;
    String adSize = "small";
    UnifiedNativeAdView nativeAdView;

    private LinearLayout primaryParentView;
    private TextView primaryView;
    private LinearLayout secondaryParentView;
    private TextView secondaryView;
    private LinearLayout tertiaryParentView;
    private RatingBar ratingBar;
    private TextView tertiaryView;
    private ImageView iconView;
    private MediaView mediaView;
    private LinearLayout callToActionParentView;
    private Button callToActionView;
    private LinearLayout background;

    public ReactNativeView(final Context context) {
        super(context);
    }

    private void createAdView() {
        final Context context = getContext();

        AdLoader.Builder builder = new AdLoader.Builder(context, this.adUnitID);
                builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // Show the ad.
                        sendEvent(RNAdMobNativeViewManager.EVENT_AD_LOADED, null);

                        populateUnifiedNativeAdView(unifiedNativeAd, nativeAdView);

                        //calculate ad size and layout
                        nativeAdView.measure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
                                MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.EXACTLY));
                        nativeAdView.layout(0, 0, nativeAdView.getMeasuredWidth(), nativeAdView.getMeasuredHeight());
                    }
                })
                .withAdListener(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // Handle the failure by logging, altering the UI, and so on.
                        String errorMessage = "Unknown error";
                        switch (errorCode) {
                            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                                errorMessage = "Internal error, an invalid response was received from the ad server.";
                                break;
                            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                                errorMessage = "Invalid ad request, possibly an incorrect ad unit ID was given.";
                                break;
                            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                                errorMessage = "The ad request was unsuccessful due to network connectivity.";
                                break;
                            case AdRequest.ERROR_CODE_NO_FILL:
                                errorMessage = "The ad request was successful, but no ad was returned due to lack of ad inventory.";
                                break;
                        }
                        WritableMap event = Arguments.createMap();
                        WritableMap error = Arguments.createMap();
                        error.putString("message", errorMessage);
                        event.putMap("error", error);
                        sendEvent(RNAdMobNativeViewManager.EVENT_AD_FAILED_TO_LOAD, event);
                    }
                })
                .withNativeAdOptions(new NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build())
                .build();


        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
            }
        }).build();

        adLoader.loadAd(new PublisherAdRequest.Builder().build());

        int template;

        if(adSize.equals("medium")) {
            template = R.layout.medium_template;
        } else {
            template = R.layout.small_template;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        this.nativeAdView = (UnifiedNativeAdView) inflater.inflate(template, null);
        this.addView(nativeAdView);

    }

    private boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }

    private boolean adHasOnlyStore(UnifiedNativeAd nativeAd) {
        String store = nativeAd.getStore();
        String advertiser = nativeAd.getAdvertiser();
        return !isNullOrEmpty(store) && isNullOrEmpty(advertiser);
    }

    private boolean adHasOnlyAdvertiser(UnifiedNativeAd nativeAd) {
        String store = nativeAd.getStore();
        String advertiser = nativeAd.getAdvertiser();
        return !isNullOrEmpty(advertiser) && isNullOrEmpty(store);
    }

    private boolean adHasBothStoreAndAdvertiser(UnifiedNativeAd nativeAd) {
        String store = nativeAd.getStore();
        String advertiser = nativeAd.getAdvertiser();
        return (!isNullOrEmpty(advertiser)) && (!isNullOrEmpty(store));
    }

    /**
     * Populates a {@link UnifiedNativeAdView} object with data from a given
     * {@link UnifiedNativeAd}.
     *
     * @param nativeAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        nativeAdView = (UnifiedNativeAdView) findViewById(R.id.native_ad_view);
        primaryView = (TextView) findViewById(R.id.primary);
        secondaryView = (TextView) findViewById(R.id.secondary);
        secondaryParentView = (LinearLayout) findViewById(R.id.body);
        ratingBar = (RatingBar) findViewById(R.id.rating_bar);
        ratingBar.setEnabled(false);
        tertiaryView = (TextView) findViewById(R.id.tertiary);
        tertiaryParentView = (LinearLayout) findViewById(R.id.third_line);
        callToActionView = (Button) findViewById(R.id.cta);
        iconView = (ImageView) findViewById(R.id.icon);
        mediaView = (MediaView) findViewById(R.id.media_view);
        primaryParentView = (LinearLayout) findViewById(R.id.headline);
        callToActionParentView = (LinearLayout) findViewById(R.id.cta_parent);
        background = (LinearLayout) findViewById(R.id.background);


        String store = nativeAd.getStore();
        String advertiser = nativeAd.getAdvertiser();
        String headline = nativeAd.getHeadline();
        String body = nativeAd.getBody();
        String cta = nativeAd.getCallToAction();
        Double starRating = nativeAd.getStarRating();
        Image icon = nativeAd.getIcon();

        String tertiaryText;

        nativeAdView.setCallToActionView(callToActionParentView);
        nativeAdView.setHeadlineView(primaryParentView);
        nativeAdView.setMediaView(mediaView);

        if (adHasOnlyStore(nativeAd)) {
            nativeAdView.setStoreView(tertiaryView);
            tertiaryParentView.setVisibility(VISIBLE);
            tertiaryText = store;
        } else if (adHasOnlyAdvertiser(nativeAd)) {
            nativeAdView.setAdvertiserView(tertiaryView);
            tertiaryParentView.setVisibility(VISIBLE);
            secondaryView.setLines(1);
            tertiaryText = advertiser;
        } else if (adHasBothStoreAndAdvertiser(nativeAd)) {
            nativeAdView.setAdvertiserView(tertiaryView);
            tertiaryParentView.setVisibility(VISIBLE);
            secondaryView.setLines(1);
            tertiaryText = advertiser;
        } else {
            tertiaryText = "";
            tertiaryParentView.setVisibility(GONE);
            secondaryView.setLines(3);
        }

        primaryView.setText(headline);
        tertiaryView.setText(tertiaryText);
        callToActionView.setText(cta);

        // Set the secondary view to be the star rating if available.
        // Otherwise fall back to the body text.
        if (starRating != null && starRating > 0) {
            secondaryView.setVisibility(GONE);
            ratingBar.setVisibility(VISIBLE);
            ratingBar.setMax(5);
            nativeAdView.setStarRatingView(ratingBar);
        } else {
            secondaryView.setText(body);
            secondaryView.setVisibility(VISIBLE);
            ratingBar.setVisibility(GONE);
            nativeAdView.setBodyView(secondaryView);
        }

        if (icon != null) {
            iconView.setVisibility(VISIBLE);
            iconView.setImageDrawable(icon.getDrawable());
        } else {
            iconView.setVisibility(GONE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();
    }

    private void sendEvent(String name, @Nullable WritableMap event) {
        ReactContext reactContext = (ReactContext) getContext();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                getId(),
                name,
                event);
    }

    public void loadNativeAd(String adUnitID) {
        this.createAdView();
    }

    public void setAdUnitID(String adUnitID) {
        this.adUnitID = adUnitID;
    }

    public void setAdSize(String adSize) {
        this.adSize = adSize;
    }

    public void setTestDevices(String[] testDevices) {
        List<String> testDeviceIds = Arrays.asList(testDevices);
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);
    }
}

public class RNAdMobNativeViewManager extends ViewGroupManager<ReactNativeView> {

    public static final String REACT_CLASS = "RNGADNativeView";

    //public static final String PROP_AD_SIZE = "adSize";
    public static final String PROP_AD_UNIT_ID = "adUnitID";
    public static final String PROP_AD_SIZE = "adSize";
    public static final String PROP_TEST_DEVICES = "testDevices";

    public static final String EVENT_SIZE_CHANGE = "onSizeChange";
    public static final String EVENT_AD_LOADED = "onAdLoaded";
    public static final String EVENT_AD_LOADING = "onAdLoading";
    public static final String EVENT_AD_FAILED_TO_LOAD = "onAdFailedToLoad";
    public static final String EVENT_AD_OPENED = "onAdOpened";
    public static final String EVENT_AD_CLOSED = "onAdClosed";
    public static final String EVENT_AD_LEFT_APPLICATION = "onAdLeftApplication";

    public static final int COMMAND_LOAD_NATIVE = 1;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    @Override
    protected ReactNativeView createViewInstance(ThemedReactContext themedReactContext) {
        ReactNativeView adView = new ReactNativeView(themedReactContext);
        return adView;
    }

    @Override
    public void addView(ReactNativeView parent, View child, int index) {
        throw new RuntimeException("RNAdMobNativeView cannot have subviews");
    }

    @Override
    @Nullable
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        MapBuilder.Builder<String, Object> builder = MapBuilder.builder();
        String[] events = {
                EVENT_SIZE_CHANGE,
                EVENT_AD_LOADED,
                EVENT_AD_LOADING,
                EVENT_AD_FAILED_TO_LOAD,
                EVENT_AD_OPENED,
                EVENT_AD_CLOSED,
                EVENT_AD_LEFT_APPLICATION
        };
        for (int i = 0; i < events.length; i++) {
            builder.put(events[i], MapBuilder.of("registrationName", events[i]));
        }
        return builder.build();
    }

    @ReactProp(name = PROP_AD_UNIT_ID)
    public void setPropAdUnitID(final ReactNativeView view, final String adUnitID) {
        view.setAdUnitID(adUnitID);
    }

    @ReactProp(name = PROP_AD_SIZE)
    public void setPropAdSize(final ReactNativeView view, final String adSize) {
        view.setAdSize(adSize);
    }

    @ReactProp(name = PROP_TEST_DEVICES)
    public void setPropTestDevices(final ReactNativeView view, final ReadableArray testDevices) {
        ReadableNativeArray nativeArray = (ReadableNativeArray)testDevices;
        ArrayList<Object> list = nativeArray.toArrayList();
        view.setTestDevices(list.toArray(new String[list.size()]));
    }

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        return MapBuilder.of("loadNativeAd", COMMAND_LOAD_NATIVE);
    }

    @Override
    public void receiveCommand(ReactNativeView root, int commandId, @javax.annotation.Nullable ReadableArray args) {
        switch (commandId) {
            case COMMAND_LOAD_NATIVE:
                root.loadNativeAd(args.getString(0));
                break;
        }
    }
}
