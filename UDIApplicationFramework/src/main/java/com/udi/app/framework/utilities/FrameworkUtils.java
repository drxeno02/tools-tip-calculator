/*
 * Copyright (c) 2014-present, ZTRIP. All rights reserved.
 */

package com.udi.app.framework.utilities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberUtils;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.maps.android.PolyUtil;
import com.udi.app.framework.constants.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by leonard on 11/13/2015.
 * Utility class that provides many utility functions used in the codebase. Provides functions for
 * checking if an object as null, as well as if a string is empty. Also provides functions for formatting
 * strings and setting margins for different screens.
 */
public class FrameworkUtils {
    private static final int MINIMUM_PASSWORD_LENGTH = 6;
    private static final int MINIMUM_PHONE_LENGTH = 10;
    private static final String EMPTY = "";
    private static final String NULL = "null";

    // cohort types
    private static final int COHORT_IDLE = 1;
    private static final int COHORT_ON_CAMPUS = 2;
    private static final int COHORT_INBOUND_TO_CAMPUS = 4;
    private static final int COHORT_OUTBOUND_FROM_CAMPUS = 8;

    /**
     * Method checks if String value is empty
     *
     * @param str
     * @return string
     */
    public static boolean isStringEmpty(String str) {
        return str == null || str.length() == 0 || EMPTY.equals(str.trim()) || NULL.equals(str);
    }

    /**
     * Method is used to check if objects are null
     *
     * @param objectToCheck
     * @param <T>
     * @return true if objectToCheck is null
     */
    public static <T> boolean checkIfNull(T objectToCheck) {
        return objectToCheck == null;
    }

    /**
     * @param context
     * @param strPermissions
     * @return
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
     * Method is used to format phone number
     *
     * @param phone
     * @return string
     */
    @SuppressWarnings("deprecation")
    public static String formatPhone(String phone) {
        if (isStringEmpty(phone)) {
            return "";
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(phone);
        } else {
            return PhoneNumberUtils.formatNumber(phone, Constants.COUNTRY);
        }
    }

    /**
     * Method is used to convert String date time to Calendar object; MM/dd/yyyy hh:mm:ss a
     *
     * @param dateTime
     * @return
     */
    public static Calendar convertStringDateTimeToCalendar(String dateTime, String timezone) {
        // remove T from dateTime string
        // e.g. 2017-05-11T17:34:36.999
        dateTime = dateTime.replace('T', ' ');
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        formatter.setTimeZone(!FrameworkUtils.isStringEmpty(timezone) ?
                TimeZone.getTimeZone(timezone) : TimeZone.getDefault());
        try {
            calendar.setTime(formatter.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * Method is used to get formatted date and time; MM/dd/yyyy hh:mm:ss a
     *
     * @return string
     */
    public static String getCurrentDateMonthDayYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to get formatted date and time; yyyy-MM-dd HH:mm:ss.SSS
     *
     * @return string
     */
    public static String getCurrentDateYearMonthDay() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to get formatted date and time; yyyy-MM-dd HH:mm:ss.SSS
     *
     * @return string
     * @oaram timezone
     */
    public static String getCurrentDateYearMonthDay(String timezone) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        if (!FrameworkUtils.isStringEmpty(timezone)) {
            formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        }
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to get formatted date and time in UTC. This is primarily used for access token
     * expiration date and time; EEE MMM dd HH:mm:ss zzz yyyy
     *
     * @param dateTime
     * @return
     */
    public static String getFormattedAccessTokenDateTimeUTC(String dateTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat readFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            // first parse dateTime object in the format is currently is
            date = readFormat.parse(dateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // format parsed date object
        return formatter.format(date);
    }

    /**
     * Method is used to get formatted date and time; dd/MM/yyyy hh:mm:ss
     *
     * @return string
     */
    public static String getCurrentDateDayMonthYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to parse formatted date; MM/dd/yyyy
     *
     * @param calendar
     * @return string
     */
    public static String parseDate(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to parse month and day; MM dd
     *
     * @param calendar
     * @return
     */
    public static String parseMonthDay(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }


    /**
     * Method is used to parse formatted time; HH:mm
     *
     * @param calendar
     * @return string
     */
    public static String parseTime(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        return formatter.format(calendar.getTime());
    }

    /**
     * Method is used to parse day of the week
     *
     * @param calendar
     * @return
     */
    public static String parseDayOfTheWeek(Calendar calendar) {
        Date date = calendar.getTime();
        return new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
    }

    /**
     * Method is used to add set amount of minutes to current date; mm:ss
     *
     * @param minutesToAdd
     * @return
     */
    public static Calendar addMinutesToCurrentDate(int minutesToAdd) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutesToAdd);
        return calendar;
    }

    /**
     * Method is used to check if two calendar objects have the same day of year
     *
     * @param calendarA
     * @param calendarB
     * @return
     */
    public static boolean isSameDay(Calendar calendarA, Calendar calendarB) {
        return calendarA.get(Calendar.YEAR) == calendarB.get(Calendar.YEAR) &&
                calendarA.get(Calendar.DAY_OF_MONTH) == calendarB.get(Calendar.DAY_OF_MONTH) &&
                calendarA.get(Calendar.DAY_OF_YEAR) == calendarB.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * Method is used to set visibility of views to VISIBLE
     *
     * @param params views to set visibility to VISIBLE
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
     * @param params views to set visibility to GONE
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
     * @param params views to set visibility to INVISIBLE
     */
    public static void setViewInvisible(View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                v.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * Method is used to set OnClickListener of the views to listener
     *
     * @param params views to set OnClickListener to listener
     */
    public static void setOnClickListener(View.OnClickListener listener, View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                v.setOnClickListener(listener);
            }
        }
    }

    /**
     * Method is used to enable/disable fields. Mainly for editText fields
     *
     * @param isEnabled
     * @param params
     */
    public static void enableDisableEditText(boolean isEnabled, View... params) {
        for (View v : params) {
            if (!checkIfNull(v)) {
                if (isEnabled) {
                    v.setFocusable(true);
                    v.setEnabled(true);
                    v.setFocusableInTouchMode(true);
                    v.setClickable(true);
                } else {
                    v.setFocusable(false);
                    v.setEnabled(false);
                    v.setFocusableInTouchMode(false);
                    v.setClickable(false);
                }
            }
        }
    }

    /**
     * Method checks if the application is in the background (i.e behind another application's Activity).
     *
     * @param context context
     * @return true if application is running in the background
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
     * @param context
     * @return false if tasks list size is zero
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
     * Method is used to confirm that string parameter is in valid email format
     *
     * @param email
     * @return true if email is valid
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /**
     * Method is used to confirm that string parameter is in valid phone number format
     *
     * @param phoneNumber
     * @return true if phone number is valid
     */
    public static boolean isValidPhoneNumber(String phoneNumber) {
        return !isStringEmpty(phoneNumber) && Patterns.PHONE.matcher(phoneNumber).matches() &&
                (phoneNumber.length() >= MINIMUM_PHONE_LENGTH);
    }

    /**
     * Method is used to confirm that string parameter is in valid area code format
     *
     * @param areaCode
     * @return true if area code is valid
     */
    public static boolean isAreaCode(String areaCode) {
        return !isStringEmpty(areaCode) && (areaCode.length() >= 3 &&
                !areaCode.equalsIgnoreCase("000") && areaCode.matches("-?\\d+(\\.\\d+)?"));
    }

    /**
     * Method is used to confirm that string parameter is in valid zip code format
     *
     * @param zipCode
     * @return true if zipcode is valid
     */
    public static boolean isZipCode(String zipCode) {
        String zipCodePattern = "^\\d{5}(-\\d{4})?$";
        return zipCode.matches(zipCodePattern);
    }

    /**
     * Method is used to confirm that a password was entered and is at least 6 characters long
     *
     * @param password
     * @return
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isValidPassword(String password) {
        return !isStringEmpty(password) && (password.length() >= MINIMUM_PASSWORD_LENGTH);
    }

    /**
     * Method is used to determine if the provided String has a numeric value
     *
     * @param value
     * @return
     */
    public static boolean containsNumericValue(String value) {
        return value.matches(".*\\d+.*"); // regex to check if String has numeric value
    }

    /**
     * Method is used to convert meters into longitude values
     *
     * @param meterToEast
     * @param latitude
     * @return
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
     * @param meterToNorth
     * @return
     */
    public static double meterToLatitude(double meterToNorth) {
        double rad = meterToNorth / Constants.EARTH_RADIUS;
        return Math.toDegrees(rad);
    }

    /**
     * Method is used to convert meters to miles
     *
     * @param meters
     * @return
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
     * @param in input stream
     * @return String value converted from input stream
     * @throws IOException
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
     * @param input
     * @return
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
     * Method is used to expand any inputted views by changing the height parameters
     *
     * @param v
     */
    public static void expandView(final View v) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 1;
        setViewVisible(v);

        Animation a = new Animation() {
            @Override
            public boolean willChangeBounds() {
                return true;
            }

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ?
                        ViewGroup.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
                super.applyTransformation(interpolatedTime, t);
            }


        };
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    /**
     * Method is used to collapse any inputted views by changing the height parameters
     *
     * @param v
     */
    public static void collapseView(final View v) {
        final int initialHeight = v.getHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    setViewGone(v);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }

                super.applyTransformation(interpolatedTime, t);
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    /**
     * Method is used to delay focus set on EditText view
     *
     * @param delay
     * @param view
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
     * @param context
     * @param id
     * @return
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
     * @param context
     * @param id
     * @return
     */
    public static final Drawable getDrawable(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }

    /**
     * Method is used to create a bitmap from a view
     *
     * @param context
     * @param view
     * @return
     */
    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(),
                view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    /**
     * Method is used to check if 2 given LatLngs are equal
     * Rounds each latitude and longitude to 6 decimal places before comparing
     *
     * @param latLng1
     * @param latLng2
     */
    public static boolean isLatLngEqual(LatLng latLng1, LatLng latLng2) {
        return ((double) Math.round(latLng1.latitude * 1000000d) / 1000000d ==
                (double) Math.round(latLng2.latitude * 1000000d) / 1000000d) &&
                ((double) Math.round(latLng1.longitude * 1000000d) / 1000000d ==
                        (double) Math.round(latLng2.longitude * 1000000d) / 1000000d);
    }

    /**
     * Method is used to convert a WKT string to a list of LatLng
     *
     * @param polygonWKT
     */
    public static ArrayList<LatLng> parsePolygonWKT(String polygonWKT) {
        ArrayList<LatLng> polygon = new ArrayList<>();
        if (!FrameworkUtils.isStringEmpty(polygonWKT)) {
            polygonWKT = polygonWKT.substring(polygonWKT.lastIndexOf('(') + 1, polygonWKT.indexOf(')'));
            String[] latLngStrings = polygonWKT.trim().split(",");
            for (String latLngString : latLngStrings) {
                String[] latLng = latLngString.trim().split(" ");
                try {
                    // longitude goes first in WKT format
                    double longitude = Double.parseDouble(latLng[0]);
                    double latitude = Double.parseDouble(latLng[1]);
                    LatLng latLng1 = new LatLng(latitude, longitude);
                    polygon.add(latLng1);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
        return polygon;
    }

    /**
     * Method is used to get the appropriate cohort given onCampus and aroundCampus Polygons,
     * and pickup and dropoff LatLng
     *
     * @param campusPolygons
     * @param aroundCampusPolygons
     * @param pickupLatLng
     * @param dropoffLatLng
     */
    public static int getCohort(ArrayList<Polygon> campusPolygons, ArrayList<Polygon> aroundCampusPolygons,
                                LatLng pickupLatLng, LatLng dropoffLatLng) {
        boolean isPickupOnCampus = false;
        boolean isDropoffOnCampus = false;
        boolean isPickupAroundCampus = false;
        boolean isDropoffAroundCampus = false;

        for (Polygon polygon : campusPolygons) {
            if (PolyUtil.containsLocation(pickupLatLng, polygon.getPoints(), false)) {
                isPickupOnCampus = true;
            }
            if (PolyUtil.containsLocation(dropoffLatLng, polygon.getPoints(), false)) {
                isDropoffOnCampus = true;
            }
        }

        for (Polygon polygon : aroundCampusPolygons) {
            if (PolyUtil.containsLocation(pickupLatLng, polygon.getPoints(), false)) {
                isPickupAroundCampus = true;
            }
            if (PolyUtil.containsLocation(dropoffLatLng, polygon.getPoints(), false)) {
                isDropoffAroundCampus = true;
            }
        }

        if (isPickupOnCampus && isDropoffOnCampus) {
            // check if building to building
            return COHORT_ON_CAMPUS;
        } else if (isPickupAroundCampus && isDropoffOnCampus) {
            // check if inbound to campus
            return COHORT_INBOUND_TO_CAMPUS;
        } else if (isPickupOnCampus && isDropoffAroundCampus) {
            // check if outbound from campus
            return COHORT_OUTBOUND_FROM_CAMPUS;
        } else {
            return COHORT_IDLE;
        }
    }
}
