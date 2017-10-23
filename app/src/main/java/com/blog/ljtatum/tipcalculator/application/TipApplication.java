package com.blog.ljtatum.tipcalculator.application;

import android.app.Application;

import com.blog.ljtatum.tipcalculator.BuildConfig;
import com.app.framework.utilities.FirebaseUtils;
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
        // instantiate FireBase
        FirebaseApp.initializeApp(this);
        // instantiate FirebaseUtils
        new FirebaseUtils(getApplicationContext());
        // instantiate crashlytics
        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build();
        Fabric.with(this, crashlyticsKit);
    }
}
