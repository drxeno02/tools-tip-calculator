package com.app.framework.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;

import android.telephony.TelephonyManager;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.app.framework.constants.Constants;
import com.app.framework.sharedpref.SharedPref;
import com.google.android.gms.maps.model.LatLng;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class FrameworkUtils {
    private static final String DATE_FORMAT = "MM/dd/yyyy hh:mm:ss a";

    private static final String EMPTY = "";
    private static final String NULL = "null";

    // click control threshold
    private static final int CLICK_THRESHOLD = 300;
    private static long mLastClickTime;

    /**
     * Method checks if String value is empty
     *
     * @param str String value to check if null or empty
     * @return True if String value is null or empty
     */
    public static boolean isStringEmpty(String str) {
        return str == null || str.length() == 0 || EMPTY.equals(str.trim()) || NULL.equals(str);
    }

    /**
     * Method is used to check if objects are null
     *
     * @param objectToCheck Object to check if null or empty
     * @param <T>           Generic data value
     * @return True if object is null or empty
     */
    public static <T> boolean checkIfNull(T objectToCheck) {
        return objectToCheck == null;
    }

    /**
     * Determine whether you have been granted a particular permission
     *
     * @param context        Interface to global information about an application environment
     * @param strPermissions The name of the permission being checked
     * @return True if permissions are enabled, otherwise false
     */
    public static boolean checkAppPermissions(Context context, String... strPermissions) {
        for (String permissions : strPermissions) {
            if (!FrameworkUtils.isStringEmpty(permissions)) {
                int result = ContextCompat.checkSelfPermission(context, permissions);
                if (result == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method is used to get formatted date and time
     *
     * @param dateFormat The format of the date
     * @return Current date and time
     */
    public static String getCurrentDateTime(String dateFormat) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to parse formatted date
     *
     * @param calendar   Calendar object {@see java.util.Calendar} with given date and time
     * @param dateFormat Method is used to parse formatted date
     * @return Formatted date and time
     */
    public static String parseDateTime(Calendar calendar, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to parse formatted date
     *
     * @param date       The date to parse
     * @param dateFormat Method is used to parse formatted date
     * @return Formatted date and time
     * @throws ParseException Thrown when the string being parsed is not in the correct form
     */
    public static Date parseDateTime(String date, String dateFormat) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        return formatter.parse(date);
    }

    /**
     * Method is used to parse day of the week
     *
     * @param calendar Calendar object {@see java.util.Calendar} with given date and time
     * @return Day of the week
     */
    public static String parseDayOfTheWeek(Calendar calendar) {
        Date date = calendar.getTime();
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
    }

    /**
     * Method is used to convert date to another formatted date
     *
     * @param date       The date to parse
     * @param dateFormat Method is used to parse formatted date
     * @return The date string value converted from Date object
     */
    public static String convertDateFormat(String date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        Date dateObj = null;
        try {
            dateObj = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return !FrameworkUtils.checkIfNull(dateObj) ? formatter.format(dateObj) : "";
    }

    /**
     * Method is used to add set amount of minutes to current date; mm:ss
     *
     * @param minutesToAdd Minutes to add to current date and time
     * @return Calendar object {@link java.util.Calendar} with updated date and time
     */
    public static Calendar addMinutesToCurrentDate(int minutesToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutesToAdd);
        return calendar;
    }

    /**
     * Method is used to compare any date passed in as paramater to current date to see
     * which date-time combination is sooner or later
     *
     * @param minDate    A specific moment in time, with millisecond precision
     * @param dateTime   String value representation of date and time
     * @param dateFormat Method is used to parse formatted date
     * @return True if input date is after the current date
     */
    public static boolean isDateAfterCurrentDate(@NonNull Date minDate, @NonNull String dateTime, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        formatter.format(minDate.getTime());
        try {
            Date parsedDate = parseDateTime(dateTime, DATE_FORMAT);
            return parsedDate.after(minDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Method is used to convert double to dollar format
     *
     * @param value Value to convert to dollar format
     * @return Dollar formatted value
     */
    public static String convertToDollarFormat(double value) {
        DecimalFormat formater = new DecimalFormat("0.00");
        return formater.format(value);
    }

    /**
     * Method is used to set visibility of views to VISIBLE
     *
     * @param params Views to set visibility to VISIBLE
     *               <p>This class represents the basic building block for user interface components</p>
     */
    public static void setViewVisible(View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Method is used to set visibility of views to GONE
     *
     * @param params Views to set visibility to GONE
     *               <p>This class represents the basic building block for user interface components</p>
     */
    public static void setViewGone(View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                v.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Method is used to set visibility of views to INVISIBLE
     *
     * @param params Views to set visibility to INVISIBLE
     *               <p>This class represents the basic building block for user interface components</p>
     */
    public static void setViewInvisible(View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Method is used to get color by id
     *
     * @param context Interface to global information about an application environment
     * @param id      The desired resource identifier, as generated by the aapt tool
     * @return A color integer associated with a particular resource ID
     */
    public static int getColor(@NonNull Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }

    /**
     * Method is used to get drawable by id
     *
     * @param context Interface to global information about an application environment
     * @param id      The desired resource identifier, as generated by the aapt tool
     * @return A drawable object associated with a particular resource ID
     */
    public static Drawable getDrawable(@NonNull Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    /**
     * Method is used to check if 2 given LatLngs are equal
     * Rounds each latitude and longitude to 6 decimal places before comparing
     *
     * @param latLng1 An immutable class representing a pair of latitude and longitude coordinates,
     *                stored as degrees
     * @param latLng2 An immutable class representing a pair of latitude and longitude coordinates,
     *                stored as degrees
     */
    public static boolean isLatLngEqual(LatLng latLng1, LatLng latLng2) {
        return ((double) Math.round(latLng1.latitude * 1000000d) / 1000000d ==
                (double) Math.round(latLng2.latitude * 1000000d) / 1000000d) &&
                ((double) Math.round(latLng1.longitude * 1000000d) / 1000000d ==
                        (double) Math.round(latLng2.longitude * 1000000d) / 1000000d);
    }

    /**
     * Method is used to control clicks on views. Clicking views repeatedly and quickly will
     * sometime cause crashes when objects and views are not fully animated or instantiated.
     * This helper method helps minimize and control UI interaction and flow
     *
     * @return True if view interaction has not been interacted with for set time
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
     * @param str Token to encode
     * @return Value that has been encoded using the format required by
     * application/x-www-form-urlencoded MIME content type
     */
    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException("URLEncoder.encode() failed for " + str);
        }
    }

    /**
     * Return an unique UDID for the current android device. As with all UDIDs, this
     * unique ID is likely to be unique across all devices. The UDID is generated
     * using ANDROID_ID as the base key if appropriate, fallback on TelephoneManager.getDeviceId().
     * If both of these fail, the hardcoded value of 'android' with concatenated random value.
     * For example android_1723
     *
     * @param context Interface to global information about an application environment
     * @return Unique identifier
     */
    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static String getAndroidId(@NonNull Context context) {
        SharedPref sharedPref = new SharedPref(context, Constants.PREF_FILE_NAME);
        // 'android' + random value 1-1000
        Random rand = new Random();
        String randValue = String.valueOf(rand.nextInt(1000) + 1);

        if (isStringEmpty(sharedPref.getStringPref(Constants.KEY_ANDROID_ID, ""))) {
            try {
                // check if android id is null
                if (isStringEmpty(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))) {
                    // android id is null, try telephony device id
                    // @note this does not work for phones without data plan
                    if (isStringEmpty(((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId())) {
                        // return 'android' + random value 1-1000
                        sharedPref.setPref(Constants.KEY_ANDROID_ID, Constants.ANDROID.concat("_").concat(randValue));
                        return Constants.ANDROID.concat("_").concat(randValue);
                    } else {
                        // return telephony device id
                        sharedPref.setPref(Constants.KEY_ANDROID_ID, ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
                        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                    }
                } else {
                    // return android id (ideal scenario)
                    sharedPref.setPref(Constants.KEY_ANDROID_ID, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                    return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                // return 'android' + random value 1-1000
                sharedPref.setPref(Constants.KEY_ANDROID_ID, Constants.ANDROID.concat("_").concat(randValue));
                return Constants.ANDROID.concat("_").concat(randValue);
            }
        }
        // return shared id stored in shared prefs
        return sharedPref.getStringPref(Constants.KEY_ANDROID_ID, "");
    }
}
