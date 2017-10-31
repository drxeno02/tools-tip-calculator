package com.app.framework.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.app.framework.R;
import com.app.framework.constants.Constants;
import com.app.framework.sharedpref.SharedPref;

/**
 * Created by LJTat on 2/23/2017.
 */
public class AppRaterUtil {

    private static final int DAYS_UNTIL_PROMPT = 4;
    private static final int LAUNCHES_UNTIL_PROMPT = 8;
    private String mPackageName;
    private Context mContext;
    private SharedPref mSharedPref;

    /**
     * Constructor
     *
     * @param context     Interface to global information about an application environment
     * @param packageName Namespace because Intents are used globally in the system
     */
    public AppRaterUtil(Context context, String packageName) {
        mContext = context;
        mPackageName = packageName;
        recordAppLaunchDate();
    }

    /**
     * Method is used to record when app is first launched
     */
    private void recordAppLaunchDate() {
        mSharedPref = new SharedPref(mContext, Constants.PREF_FILE_NAME);
        if (mSharedPref.getBooleanPref(Constants.KEY_APP_RATED, false)) {
            // return if app has already been rated
            return;
        }

        // increment launch counter
        Long launchCount = mSharedPref.getLongPref(Constants.KEY_APP_LAUNCH_COUNT, 0L) + 1;
        mSharedPref.setPref(Constants.KEY_APP_LAUNCH_COUNT, launchCount);

        // get date of first launch
        Long dateFirstLaunch = mSharedPref.getLongPref(Constants.KEY_APP_LAUNCH_DATE, 0L);
        if (dateFirstLaunch == 0) {
            dateFirstLaunch = System.currentTimeMillis();
            mSharedPref.setPref(Constants.KEY_APP_LAUNCH_DATE, dateFirstLaunch);
        }

        // wait at least n days before opening
        if (launchCount >= LAUNCHES_UNTIL_PROMPT && System.currentTimeMillis() >=
                dateFirstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
            // show rate dialog
            showRateDialog();
        }
    }

    /**
     * Method is used to display dialog
     */
    private void showRateDialog() {
        // instantiate dialog builder
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        View v = inflater.inflate(R.layout.rate_app, null);
        dialogBuilder.setView(v);
        dialogBuilder.setCancelable(false);
        // instantiate alert dialog
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView tvRateApp = (TextView) v.findViewById(R.id.tv_rate_app);
        TextView tvRemindLater = (TextView) v.findViewById(R.id.tv_remind_later);
        TextView tvNoThanks = (TextView) v.findViewById(R.id.tv_no_thanks);

        // set listeners
        tvRateApp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // flag app rated (potentially)
                mSharedPref.setPref(Constants.KEY_APP_RATED, true);

                try {
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=".concat(mPackageName))));
                } catch (android.content.ActivityNotFoundException e) {
                    e.printStackTrace();
                    // try to redirect with updated link
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=".concat(mPackageName))));
                }
                // dismiss dialog
                alertDialog.dismiss();
            }
        });

        tvRemindLater.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // reset shared pref values
                resetSharedPref();
                // dismiss dialog
                alertDialog.dismiss();
            }
        });

        tvNoThanks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // reset shared pref values
                resetSharedPref();
                // dismiss dialog
                alertDialog.dismiss();
            }
        });

        // display alert dialog
        alertDialog.show();
    }

    /**
     * Method is used to reset shared pref values
     */
    private void resetSharedPref() {
        // reset
        mSharedPref.setPref(Constants.KEY_APP_LAUNCH_COUNT, 0L);
        // update launch date
        mSharedPref.setPref(Constants.KEY_APP_LAUNCH_DATE, System.currentTimeMillis());
    }
}
