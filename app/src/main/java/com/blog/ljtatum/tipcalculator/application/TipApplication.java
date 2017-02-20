package com.blog.ljtatum.tipcalculator.application;

import android.app.Application;

import com.blog.ljtatum.tipcalculator.BuildConfig;
import com.blog.ljtatum.tipcalculator.utils.AppRaterUtil;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.firebase.FirebaseApp;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

/**
 * Created by LJTat on 1/28/2017.
 */

public class TipApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // instantiate push notifications
        FirebaseApp.initializeApp(this);
        // instantiate crashlytics
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build();
        Fabric.with(this, crashlyticsKit);
        // instantiate app rating utility class
        new AppRaterUtil(this);

    }
}
