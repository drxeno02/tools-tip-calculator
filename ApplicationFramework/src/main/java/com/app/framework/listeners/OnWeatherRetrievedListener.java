package com.app.framework.listeners;

import org.json.JSONObject;

/**
 * Created by leonard on 4/10/2017.
 */

public interface OnWeatherRetrievedListener {

    /**
     * Interface for when Open Weather API has successfully provided weather information
     *
     * @param weatherObj A modifiable set of name/value mappings
     */
    void onSuccess(JSONObject weatherObj);

    /**
     * Interface for when Open Weather API fails
     */
    void onFailure();
}
