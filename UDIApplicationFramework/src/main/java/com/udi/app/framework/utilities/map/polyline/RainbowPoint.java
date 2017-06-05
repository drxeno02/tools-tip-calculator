package com.udi.app.framework.utilities.map.polyline;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by leonard on 4/26/2017.
 */

public class RainbowPoint {
    private LatLng mPosition;
    private Integer mColor = null; // default

    /**
     * Set point
     *
     * @param position
     */
    public RainbowPoint(final LatLng position) {
        this.mPosition = position;
    }

    /**
     * Set color
     *
     * @param color
     * @return
     */
    public RainbowPoint color(final Integer color) {
        this.mColor = color;
        return this;
    }

    /**
     * Retrieve the position of the point
     *
     * @return
     */
    public LatLng getPosition() {
        return mPosition;
    }

    /**
     * Retrieve color of the point
     *
     * @return
     */
    public Integer getColor() {
        return mColor;
    }
}
