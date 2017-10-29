package com.app.framework.listeners;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by al-weeam on 6/24/15.
 */
public interface DirectionsListener {

    /**
     * Interface for when Google Directions API has successfully provided direction points
     *
     * @param points List of data points representing a geographic location
     */
    void onSuccess(List<LatLng> points);

    /**
     * Interface for when Google Directions API fails
     */
    void onFailure();
}
