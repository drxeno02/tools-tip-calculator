package com.app.framework.listeners;

import org.json.JSONObject;

public interface DistanceListener {

    /**
     * Interface for when distance between multiple points have been determined
     *
     * @param response  A modifiable set of name/value mappings
     * @param googleEta The amount of time it takes to go from one point to another
     */
    void onDistanceResponse(JSONObject response, Integer googleEta);

    /**
     * Interface for when request to retrieve ETA fails
     */
    void onDistanceError();
}
