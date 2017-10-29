package com.app.framework.listeners;

import com.app.framework.entities.Place;

import java.util.List;

public interface GooglePlacesListener {

    /**
     * Interface for when Google Places API has successfully provided locations
     *
     * @param places List of location
     */
    void onSuccess(List<Place> places);

    /**
     * Interface for when Google Places API fails
     */
    void onFailure();
}
