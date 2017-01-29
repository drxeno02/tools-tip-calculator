package com.blog.ljtatum.tipcalculator.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.blog.ljtatum.tipcalculator.R;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class SplashActivity extends Activity {
    private final int SPLASH_TIMER = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // create an intent that will start MainActivity
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent);

                // End SplashActivity
                finish();
            }
        }, SPLASH_TIMER);
    }
}