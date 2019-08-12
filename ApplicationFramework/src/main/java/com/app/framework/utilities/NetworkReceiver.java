package com.app.framework.utilities;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NetworkReceiver extends BroadcastReceiver {
    private static final String TAG = NetworkReceiver.class.getSimpleName();

    private static final List<NetworkStatusObserver> mObserverList = new ArrayList<>();
    private static boolean isNetworkConnected = true;

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        Log.i(TAG, "onReceive() broadcast");
        boolean disconnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        boolean isNetworkConnectedCurrent;

        if (disconnected) {
            isNetworkConnectedCurrent = false;
        } else {
            NetworkInfo networkInfo;

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //noinspection deprecation
                networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            } else {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                networkInfo = cm.getActiveNetworkInfo();
            }

            isNetworkConnectedCurrent = !FrameworkUtils.checkIfNull(networkInfo) && networkInfo.isConnectedOrConnecting();
        }

        if (isNetworkConnectedCurrent != isNetworkConnected) {
            isNetworkConnected = isNetworkConnectedCurrent;
            Log.d(TAG, "NetworkStatus.onReceive - isNetworkConnected: " + isNetworkConnected);
            notifyObservers(isNetworkConnected);
        }
    }

    /**
     * Lets all {@link NetworkStatusObserver}s know if the DEVICE is connected to a network.
     *
     * @param isNetworkConnectedCurrent True if network connection is current, otherwise false
     */
    private void notifyObservers(@NonNull Boolean isNetworkConnectedCurrent) {
        for (NetworkStatusObserver networkStatusObserver : mObserverList) {
            networkStatusObserver.notifyConnectionChange(isNetworkConnectedCurrent);
        }
    }

    /**
     * Add observer to observer list
     *
     * @param observer Network observer that monitors changes in network activity
     */
    public void addObserver(@NonNull NetworkStatusObserver observer) {
        mObserverList.add(observer);
    }

    /**
     * Remove observer from observer list
     *
     * @param observer Network observer that monitors changes in network activity
     */
    public void removeObserver(@NonNull NetworkStatusObserver observer) {
        mObserverList.remove(observer);
    }

    /**
     * Retrieve observer list size
     *
     * @return List of network observers
     */
    public int getObserverSize() {
        return mObserverList.size();
    }

    /**
     * Check if receiver is added to observer list
     *
     * @param observer Network observer that monitors changes in network activity
     * @return True if observer is already on observer list
     */
    public boolean contains(@NonNull NetworkStatusObserver observer) {
        return mObserverList.contains(observer);
    }

    /**
     * Method is used to print observer list
     */
    public void printObserverList() {
        Log.i(TAG, "===== PRINT OBSERVER LIST ===== ");
        for (int i = 0; i < mObserverList.size(); i++) {
            Log.i(TAG, String.format(Locale.US, "item(%d): %s", i, mObserverList.get(i).toString()));
        }
    }

    /**
     * Interface for monitoring network status change
     */
    public interface NetworkStatusObserver {
        void notifyConnectionChange(boolean isConnected);
    }
}
