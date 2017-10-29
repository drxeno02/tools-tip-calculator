package com.app.framework.net;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyClient {
    private static final String TAG = VolleyClient.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private static VolleyClient mInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;

    /**
     * Constructor
     *
     * @param context Interface to global information about an application environment
     */
    private VolleyClient(Context context) {
        mContext = context;
    }

    /**
     * Singleton pattern
     * <p>Implemented a singleton class that encapsulates RequestQueue and other Volley functionality</p>
     *
     * @param context Interface to global information about an application environment
     * @return Instance of {@link com.udi.app.framework.net.VolleyClient}
     */
    public synchronized static VolleyClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleyClient(context);
        }

        return mInstance;
    }

    /**
     * The RequestQueue manages worker threads for running the network operations, reading from
     * and writing to the cache, and parsing responses
     * <p>A RequestQueue needs two things to do its job: a network to perform transport of the
     * requests, and a cache to handle caching</p>
     *
     * @return The {@link com.android.volley.RequestQueue} object
     */
    private RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * @param request Requests do the parsing of raw responses and Volley takes care of
     *                dispatching the parsed response back to the main thread for delivery
     * @param tag     The tag of the fragment you want to log
     * @param <T>     Generics
     */
    public <T> void addRequest(Request<T> request, String tag) {
        Log.i(getLoggingTag(),
                String.format("addRequest() - %s", request.getUrl()));

        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(request);
    }

    /**
     * @param request Requests do the parsing of raw responses and Volley takes care of
     *                dispatching the parsed response back to the main thread for delivery
     * @param <T>     Generics
     */
    public <T> void addRequest(Request<T> request) {
        Log.i(getLoggingTag(),
                String.format("addRequest() - %s", request.getUrl()));

        getRequestQueue().add(request);
    }

    /**
     * @return Logging of request information
     */
    private String getLoggingTag() {
        return String.format("%s::VolleyClient", TAG);
    }
}
