package com.app.framework.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;

public class DeviceUtils {

    /**
     * Method is used to show virtual keyboard
     *
     * @param context Interface to global information about an application environment
     */
    public static void showKeyboard(@NonNull Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Method is used to hide virtual keyboard
     *
     * @param context Interface to global information about an application environment
     * @param binder  Base interface for a remotable object, the core part of a lightweight remote
     *                procedure call mechanism designed for high performance when performing
     *                in-process and cross-process calls
     */
    public static void hideKeyboard(@NonNull Context context, @NonNull IBinder binder) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(binder, 0);
    }

    /**
     * Method is used to check if device has location services enabled
     *
     * @param context Interface to global information about an application environment
     * @return True if location services enable
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isLocationServiceEnabled(@NonNull Context context) {
        int locationMode;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !FrameworkUtils.isStringEmpty(locationProviders);
        }
    }

    /**
     * Method is used to get the device width in pixels
     *
     * @return Return the current display metrics (Width) that are in effect for this resource object
     */
    public static int getDeviceWidthPx() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * Method is used to get the device height in pixels
     *
     * @return Return the current display metrics (Height) that are in effect for this resource object
     */
    public static int getDeviceHeightPx() {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return metrics.heightPixels;
    }

}
