package com.sbugert.rnadmob;

import android.os.Build;

public class APSUtil {
    static boolean shouldUseAPS(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
