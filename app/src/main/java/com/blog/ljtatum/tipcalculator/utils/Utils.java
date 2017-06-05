package com.blog.ljtatum.tipcalculator.utils;

import android.os.SystemClock;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by LJTat on 6/4/2017.
 */

public class Utils {

    // click control threshold
    private static final int CLICK_THRESHOLD = 300;
    private static long mLastClickTime;

    /**
     * Method is used to control clicks on views. Clicking views repeatedly and quickly will
     * sometime cause crashes when objects and views are not fully animated or instantiated.
     * This helper method helps minimize and control UI interaction and flow
     *
     * @return
     */
    public static boolean isViewClickable() {
        /*
         * @Note: Android queues button clicks so it doesn't matter how fast or slow
         * your onClick() executes, simultaneous clicks will still occur. Therefore solutions
         * such as disabling button clicks via flags or conditions statements will not work.
         * The best solution is to timestamp the click processes and return back clicks
         * that occur within a designated window (currently 300 ms) --LT
         */
        long mCurrClickTimestamp = SystemClock.uptimeMillis();
        long mElapsedTimestamp = mCurrClickTimestamp - mLastClickTime;
        mLastClickTime = mCurrClickTimestamp;
        return !(mElapsedTimestamp <= CLICK_THRESHOLD);
    }

    /**
     * Method is used to encode an url string
     *
     * @param str
     * @return
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("URLEncoder.encode() failed for " + str);
        }
    }
}
