package com.udi.app.framework.utilities.map.polyline;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Shader;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLngBounds;
import com.udi.app.framework.utilities.FrameworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonard on 4/26/2017.
 */

public abstract class RainbowPolylineShape {

    protected int zIndex;
    protected int mStrokeWidth = 16; // default
    protected Paint.Cap mStrokeCap = Paint.Cap.BUTT; // default
    // @note: BEVEL means the outer edges of a join meet with a straight line
    protected Paint.Join mStrokeJoin = Paint.Join.BEVEL; // default
    protected PathEffect mPathEffect;
    protected MaskFilter mMaskFilter;
    protected Shader mStrokeShader;
    protected Integer mStrokeColor = Color.BLACK; // default
    protected boolean mLinearGradient = true; // default
    protected boolean mAntiAlias = true; // default

    // list of points
    protected List<RainbowPoint> alPoints = new ArrayList<>();

    /**
     * Constructor
     *
     * @param zIndex
     * @param points
     * @param strokeWidth
     * @param strokeCap
     * @param strokeJoin
     * @param pathEffect
     * @param maskFilter
     * @param strokeShader
     * @param linearGradient
     * @param strokeColor
     * @param antialias
     */
    RainbowPolylineShape(final int zIndex, final List<RainbowPoint> points, final int strokeWidth,
                         final Paint.Cap strokeCap, final Paint.Join strokeJoin, final PathEffect pathEffect,
                         final MaskFilter maskFilter, final Shader strokeShader, final boolean linearGradient,
                         final Integer strokeColor, final boolean antialias) {

        this.zIndex = zIndex;
        this.mStrokeWidth = strokeWidth;
        this.mStrokeCap = strokeCap;
        this.mStrokeJoin = strokeJoin;
        this.mPathEffect = pathEffect;
        this.mMaskFilter = maskFilter;
        this.mStrokeShader = strokeShader;
        this.mLinearGradient = linearGradient;
        this.mStrokeColor = strokeColor;
        this.mAntiAlias = antialias;

        // populate list of LatLng points
        if (!FrameworkUtils.checkIfNull(points)) {
            for (RainbowPoint point : points) {
                add(point);
            }
        }
    }

    /**
     * Method is used to populate polyline list. The points have color associations
     *
     * @param point
     * @return
     */
    public RainbowPolylineShape add(final RainbowPoint point) {
        if (!FrameworkUtils.checkIfNull(point)) {
            if (FrameworkUtils.checkIfNull(point.getColor())) {
                point.color(mStrokeColor); // use default if color not set
            }
            alPoints.add(point);
        }
        return this; // return this instance
    }

    /**
     * Method is used to retrieve the zIndex; the order in which this ground overlay is
     * drawn with respect to other overlays
     *
     * @return
     * @see <a href="https://developers.google.com/android/reference/com/google/android/gms/maps/model/GroundOverlay">zIndex</a>
     */
    public int getZIndex() {
        return this.zIndex;
    }

    /**
     * Method is used to retrieve LatLngBounds
     *
     * @return
     */
    public LatLngBounds getBounds() {
        if (FrameworkUtils.checkIfNull(alPoints) || alPoints.size() == 0 || alPoints.isEmpty()) {
            return null;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (RainbowPoint point : alPoints) {
            if (!FrameworkUtils.checkIfNull(point.getPosition())) {
                builder.include(point.getPosition());
            }
        }
        return builder.build();
    }

    /**
     * Method is used to draw polyline
     *
     * @param bitmap
     * @param projection
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    protected abstract void doDraw(final Bitmap bitmap, final Projection projection,
                                   final int paddingLeft, final int paddingTop,
                                   final int paddingRight, final int paddingBottom);

    /**
     * Method is used to draw polyline
     *
     * @param bitmap
     * @param projection
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    public void draw(final Bitmap bitmap, final Projection projection,
                     final int paddingLeft, final int paddingTop,
                     final int paddingRight, final int paddingBottom) {


        // confirm that mView or mMap object is not null, otherwise throw exception
        if (FrameworkUtils.checkIfNull(bitmap) || FrameworkUtils.checkIfNull(projection)) {
            throw new IllegalStateException("Bitmap and Projection cannot be null");
        }

        if (boundsIntersects(projection.getVisibleRegion().latLngBounds)) {
            doDraw(bitmap, projection, paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    /**
     * Method is used to check if bounds intercept
     *
     * @param test
     * @return
     */
    public boolean boundsIntersects(final LatLngBounds test) {
        LatLngBounds bounds = getBounds();
        if (FrameworkUtils.checkIfNull(bounds) || FrameworkUtils.checkIfNull(test)) {
            return false;
        }

        return RainbowPolylineUtils.intersectsRectangle(test.southwest.longitude, test.southwest.latitude,
                test.northeast.longitude, test.northeast.latitude,
                bounds.southwest.longitude, bounds.southwest.latitude,
                bounds.northeast.longitude, bounds.northeast.latitude);
    }
}
