package com.sbugert.rnadmob;

import android.support.annotation.Nullable;

import com.google.android.gms.ads.AdSize;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RNCustomAdSize {
    public static final AdSize AD_SIZE_SPON = new AdSize(220, 30);

    @Nullable
    public static AdSize parseCustomAdSize(CharSequence adSize) {
        Matcher matcher = Pattern.compile("\\{([0-9]*),([0-9]*)}").matcher(adSize);
        if (!matcher.matches()) {
            return null;
        }
        int width = Integer.parseInt(matcher.group(0));
        int height = Integer.parseInt(matcher.group(1));
        return new AdSize(width, height);
    }
}
