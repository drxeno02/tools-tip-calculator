package com.app.framework.utilities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkUtils {
    private static String TAG = NetworkUtils.class.getSimpleName();

    /**
     * Method {@see com.app.framework.utilities.NetworkUtils#isNetworkAvailable(Context)}
     * is used to check is network is available e.g. both connected and available
     *
     * @param context Interface to global information about an application environment
     * @return True if network is available otherwise false
     */
    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        //check general connectivity
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!FrameworkUtils.checkIfNull(conMgr) &&
                !FrameworkUtils.checkIfNull(conMgr.getActiveNetworkInfo()) &&
                conMgr.getActiveNetworkInfo().isConnected() &&
                conMgr.getActiveNetworkInfo().isAvailable()) {
            Log.i(TAG, "Active Connection");
            return true;
        }
        Log.i(TAG, "No Connection");
        return false;
    }

    /**
     * Method {@see com.app.framework.utilities.NetworkUtils#getNetworkInfo(Context)}
     * is used to get the network info
     *
     * @param context Interface to global information about an application environment
     * @return Details about the currently active default data network
     */
    @SuppressLint("MissingPermission")
    private static NetworkInfo getNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check {@see com.app.framework.utilities.NetworkUtils#isConnected(Context)}
     * if there is any connectivity
     *
     * @param context Interface to global information about an application environment
     * @return True if network is active otherwise false
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnectedOrConnecting());
    }

    /**
     * Check {@see com.app.framework.utilities.NetworkUtils#isConnectedWifi(Context)}
     * if there is any connectivity to a WIFI network
     *
     * @param context Interface to global information about an application environment
     * @return True if network is active otherwise false
     */
    public static boolean isConnectedWifi(Context context) {
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnectedOrConnecting() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * Check {@see com.app.framework.utilities.NetworkUtils#isConnectedMobile(Context)}
     * if there is any connectivity to a mobile network
     *
     * @param context Interface to global information about an application environment
     * @return True if network is active otherwise false
     */
    public static boolean isConnectedMobile(Context context) {
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnectedOrConnecting() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Check {@see com.app.framework.utilities.NetworkUtils#isConnectedFast(Context)}
     * if there is fast connectivity
     *
     * @param context Interface to global information about an application environment
     * @return True if network is active otherwise false
     */
    public static boolean isConnectedFast(Context context) {
        NetworkInfo info = NetworkUtils.getNetworkInfo(context);
        return (info != null && info.isConnectedOrConnecting() && NetworkUtils.isConnectionFast(info.getType(), info.getSubtype()));
    }

    /**
     * Check {@see com.app.framework.utilities.NetworkUtils#isConnectionFast(int, int)}
     * if there is fast connectivity. Used for {@see com.udi.app.framework.utilities.NetworkUtils#isConnectedFast(Context)}
     *
     * @param type    Mobile data type
     * @param subType Network-type-specific integer describing the subtype of the network
     * @return True if connection is fast
     */
    private static boolean isConnectionFast(int type, int subType) {
        if (type == ConnectivityManager.TYPE_WIFI) {
            Log.i(TAG, "WIFI Connection");
            return true;
        } else if (type == ConnectivityManager.TYPE_MOBILE) {
            switch (subType) {
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                    Log.v(TAG, "WEAK: ~ 50-100 kbps");
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    Log.v(TAG, "WEAK: ~ 14-64 kbps");
                    return false; // ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    Log.v(TAG, "WEAK: ~ 50-100 kbps");
                    return false; // ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    Log.v(TAG, "STRONG: ~ 400-1000 kbps");
                    return true; // ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    Log.v(TAG, "STRONG: ~ 600-1400 kbps");
                    return true; // ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    Log.v(TAG, "WEAK: ~ 100 kbps");
                    return false; // ~ 100 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    Log.v(TAG, "STRONG: ~ 2-14 Mbps");
                    return true; // ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    Log.v(TAG, "STRONG: ~ 700-1700 kbps");
                    return true; // ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                    Log.v(TAG, "STRONG: ~ 1-23 Mbps");
                    return true; // ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    Log.v(TAG, "STRONG: ~ 400-7000 kbps");
                    return true; // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to set android:targetSdkVersion
                 * to appropriate level to use these
                 */
                case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                    Log.v(TAG, "STRONG: ~ 1-2 Mbps");
                    return true; // ~ 1-2 Mbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                    Log.v(TAG, "STRONG: ~ 5 Mbps");
                    return true; // ~ 5 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                    Log.v(TAG, "STRONG: ~ 10-20 Mbps");
                    return true; // ~ 10-20 Mbps
                case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                    Log.v(TAG, "WEAK: ~25 kbps");
                    return false; // ~25 kbps
                case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                    Log.v(TAG, "STRONG: ~ 10+ Mbps");
                    return true; // ~ 10+ Mbps
                // Unknown
                case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                default:
                    return false;
            }
        } else {
            Log.e(TAG, "No Network Info");
            return false;
        }
    }
}
