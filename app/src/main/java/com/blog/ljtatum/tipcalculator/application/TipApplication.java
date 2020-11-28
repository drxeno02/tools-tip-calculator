package com.blog.ljtatum.tipcalculator.application;

import android.app.Application;

import com.app.framework.utilities.FirebaseUtils;
import com.google.firebase.FirebaseApp;

/**
 * Created by LJTat on 1/28/2017.
 */
public class TipApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            // instantiate FireBase
            FirebaseApp.initializeApp(this);
            // instantiate FirebaseUtils
            new FirebaseUtils(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
