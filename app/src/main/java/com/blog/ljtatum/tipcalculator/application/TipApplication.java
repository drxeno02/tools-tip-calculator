package com.blog.ljtatum.tipcalculator.application;

import android.app.Application;

import com.blog.ljtatum.tipcalculator.utils.AppRaterUtil;
import com.google.firebase.FirebaseApp;

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
        //Fabric.with(this, new Crashlytics());
        // instantiate app rating utility class
        new AppRaterUtil(this);

    }
}
