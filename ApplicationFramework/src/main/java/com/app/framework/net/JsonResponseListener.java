package com.app.framework.net;

import com.android.volley.Response;

import org.json.JSONObject;

public abstract class JsonResponseListener implements Response.Listener<JSONObject> {

    /**
     * Interface for when request responses are received
     *
     * @param response   Requests do the parsing of raw responses and Volley takes care of
     *                   dispatching the parsed response back to the main thread for delivery
     * @param resultCode ResultCode for the failed request
     */
    public abstract void onResponse(JSONObject response, int resultCode);
}
