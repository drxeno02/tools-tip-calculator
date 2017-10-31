/*
 * Copyright (c) 2014-present, ZTRIP. All rights reserved.
 */

package com.app.framework.utilities;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;

import com.app.framework.constants.Constants;
import com.app.framework.sharedpref.SharedPref;
import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * Created by leonard on 11/13/2015.
 * Utility class that provides many utility functions used in the codebase. Provides functions for
 * checking if an object as null, as well as if a string is empty. Also provides functions for formatting
 * strings and setting margins for different screens.
 */
public class FrameworkUtils {
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
     * @param calendar   Calendar object {@link java.util.Calendar} with given date and time
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
     * @param calendar Calendar object {@link java.util.Calendar} with given date and time
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
        return formatter.format(dateObj);
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
     * Method is used to check if two calendar objects have the same day of year
     * <p>To be true, the year, day of month and day of the year must all be the same</p>
     *
     * @param calendarA Calendar object {@link java.util.Calendar} with given date and time
     * @param calendarB Calendar object {@link java.util.Calendar} with given date and time
     * @return True if calendar objects have the same day of year
     */
    public static boolean isSameDay(Calendar calendarA, Calendar calendarB) {
        return calendarA.get(Calendar.YEAR) == calendarB.get(Calendar.YEAR) &&
                calendarA.get(Calendar.DAY_OF_MONTH) == calendarB.get(Calendar.DAY_OF_MONTH) &&
                calendarA.get(Calendar.DAY_OF_YEAR) == calendarB.get(Calendar.DAY_OF_YEAR);
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
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
        formatter.format(minDate.getTime());
        try {
            Date parsedDate = parseDateTime(dateTime, "MM/dd/yyyy hh:mm:ss a");
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
     * Method checks if the application is in the background (i.e behind another application's Activity).
     *
     * @param context Interface to global information about an application environment
     * @return True if application is running in the background, otherwise false
     */
    @SuppressWarnings("deprecation")
    public static boolean isApplicationSentToBackground(final Context context) {
        if (!checkIfNull(context)) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
            if (!tasks.isEmpty()) {
                ComponentName topActivity = tasks.get(0).topActivity;
                if (!topActivity.getPackageName().equals(context.getPackageName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method checks if an Activity is currently running
     *
     * @param context Interface to global information about an application environment
     * @return True if there are running tasks, otherwise false
     */
    public static boolean isActivityRunning(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(Integer.MAX_VALUE);
        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (context.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method is used to confirm that string parameter is in valid area code format
     *
     * @param areaCode Area code to confirm
     * @return True if area code has valid format, otherwise false
     */
    public static boolean isAreaCode(String areaCode) {
        return !isStringEmpty(areaCode) && (areaCode.length() >= 3 &&
                !areaCode.equalsIgnoreCase("000") && areaCode.matches("-?\\d+(\\.\\d+)?"));
    }

    /**
     * Method is used to confirm that string parameter is in valid zip code format
     *
     * @param zipCode Zip code to confirm
     * @return True if zip code has valid format, otherwise false
     */
    public static boolean isZipCode(String zipCode) {
        String zipCodePattern = "^\\d{5}(-\\d{4})?$";
        return zipCode.matches(zipCodePattern);
    }

    /**
     * Method is used to determine if the provided String has a numeric value
     *
     * @param value String value to check
     * @return True if String value contains any numeric values, otherwise false
     */
    public static boolean containsNumericValue(String value) {
        return value.matches(".*\\d+.*"); // regex to check if String has numeric value
    }

    /**
     * Method is used to convert meters into longitude values
     *
     * @param meterToEast The distance (meters) to convert to longitude values
     * @param latitude    The angular distance of a place north or south of the earth's equator,
     *                    or of a celestial object north or south of the celestial equator,
     *                    usually expressed in degrees and minutes
     * @return The angle measured in radians to an approximately equivalent angle
     * measured in degrees
     */
    public static double meterToLongitude(double meterToEast, double latitude) {
        double latArc = Math.toRadians(latitude);
        double radius = Math.cos(latArc) * Constants.EARTH_RADIUS;
        double rad = meterToEast / radius;
        return Math.toDegrees(rad);
    }

    /**
     * Method is used to convert meters into latitude values
     *
     * @param meterToNorth The distance (meters) to convert to latitude values
     * @return The angle measured in radians to an approximately equivalent angle
     * measured in degrees
     */
    public static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth / Constants.EARTH_RADIUS;
        return Math.toDegrees(rad);
    }

    /**
     * Method is used to convert meters to miles
     *
     * @param meters The distance (meters) to convert to miles
     * @return The converted miles
     */
    public static double meterToMile(double meters) {
        double miles = meters / (1609.344);
        DecimalFormat formatter = new DecimalFormat("##");
        try {
            return Double.parseDouble(formatter.format(miles));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0d;
        }
    }

    /**
     * Method is used to convert input stream into a String
     *
     * @param in The input stream from which to read characters
     * @return String value converted from input stream
     * @throws IOException Signals a general, I/O-related error
     */
    public static String convertStreamToString(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        String line;
        while (!checkIfNull((line = reader.readLine()))) {
            out.append(line);
            out.append(newLine);
        }
        return out.toString();
    }

    /**
     * Method is used to capitalize the first letter of any given string
     *
     * @param input String value to upper case first letter
     * @return The upper case equivalent for the specified character if the character
     * is a lower case letter
     */
    public static String toTitleCase(String input) {
        if (!isStringEmpty(input)) {
            String[] words = input.split(" ");
            StringBuilder sb = new StringBuilder();
            for (String w : words) {
                sb.append(Character.toUpperCase(w.charAt(0)))
                        .append(w.substring(1).toLowerCase()).append(" ");
            }
            return sb.toString().trim();
        }
        return input;
    }

    /**
     * Method is used to delay focus set on EditText view
     *
     * @param delay The delay (in milliseconds) until the Runnable will be executed
     * @param view  Views to request focus for
     *              <p>This class represents the basic building block for user interface components</p>
     */
    public static void setFocusWithDelay(int delay, View... view) {
        for (final View v : view) {
            if (!FrameworkUtils.checkIfNull(v)) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.requestFocus();
                    }
                }, delay);
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
    public static final int getColor(Context context, int id) {
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
    public static final Drawable getDrawable(Context context, int id) {
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
    @Nullable
    public static String getAndroidId(@NonNull Context context) {
        SharedPref sharedPref = new SharedPref(context, Constants.PREF_FILE_NAME);
        if (isStringEmpty(sharedPref.getStringPref(Constants.KEY_ANDROID_ID, ""))) {
            // check if android id is null
            if (isStringEmpty(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))) {
                // android id is null, try telephony device id
                // @note this does not work for phones without data plan
                if (isStringEmpty(((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId())) {
                    // return 'android' + random value 1-1000
                    Random rand = new Random();
                    String randValue = String.valueOf(rand.nextInt(1000) + 1);
                    sharedPref.setPref(Constants.KEY_ANDROID_ID, Constants.ANDROID.concat("_").concat(randValue));
                    return Constants.ANDROID.concat("_").concat(randValue);
                } else {
                    // return telephony device id
                    sharedPref.setPref(Constants.KEY_ANDROID_ID, ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
                    return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                }
            } else {
                // return android id
                sharedPref.setPref(Constants.KEY_ANDROID_ID, Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));
                return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }
        // return shared id stored in shared prefs
        return sharedPref.getStringPref(Constants.KEY_ANDROID_ID, "");
    }
}
