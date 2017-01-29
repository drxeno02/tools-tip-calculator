package com.blog.ljtatum.tipcalculator.constants;

import com.blog.ljtatum.tipcalculator.BuildConfig;

/**
 * Created by LJTat on 12/18/2016.
 */

public class Constants {

    // debuggable mode; true to see debug logs otherwise false
    public static final boolean DEBUG = BuildConfig.DEBUG_MODE;

    // shared prefs
    public static final String PREF_FILE_NAME = "prefFileName";
    public static final String KEY_APP_LAUNCH = "appLaunch";
    public static final String KEY_APP_LAUNCH_COUNT = "appLaunchCount";
    public static final String KEY_APP_LAUNCH_DATE = "appLaunchDate";
}
