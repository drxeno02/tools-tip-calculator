package com.app.framework.listeners;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by leonard on 9/9/2016.
 */
public interface ErrorListener extends Response.ErrorListener {

    /**
     * Interface for when Google requests fail
     *
     * @param googleError Google error
     * @param resultCode  ResultCode for the failed request
     */
    void onErrorResponse(VolleyError googleError, int resultCode);

    /**
     * Interface for when Volley requests fail
     *
     * @param volleyError Volley error
     */
    void onErrorResponse(VolleyError volleyError);

}
