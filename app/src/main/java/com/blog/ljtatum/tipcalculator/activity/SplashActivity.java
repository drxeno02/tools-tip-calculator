package com.blog.ljtatum.tipcalculator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.blog.ljtatum.tipcalculator.R;
import com.blog.ljtatum.tipcalculator.utils.Utils;

/**
 * Created by LJTat on 2/23/2017.
 */
public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int SPLASH_TIMER = 2000;
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // create an intent that will start MainActivity
                Intent intent = new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // print memory
        Utils.printMemory(TAG);
        // print app info
        Utils.printInfo(this, this);
    }
}