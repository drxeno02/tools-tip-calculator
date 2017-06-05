package com.udi.app.framework.utilities.map.polyline;

import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Shader;

import com.udi.app.framework.utilities.FrameworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leonard on 4/26/2017.
 */

public class RainbowPolylineOptions {

    private int zIndex;
    private int mStrokeWidth = 16; //default
    private Paint.Cap mStrokeCap = Paint.Cap.BUTT; // default
    // @note: BEVEL means the outer edges of a join meet with a straight line
    private Paint.Join mStrokeJoin = Paint.Join.BEVEL; // default
    private PathEffect mPathEffect;
    private MaskFilter mMaskFilter;
    private Shader mStrokeShader;
    private Integer mStrokeColor = Color.BLACK; // default
    private boolean mLinearGradient = true; // default
    private boolean mAntiAlias = true; // default

    // list of alPoints
    private List<RainbowPoint> alPoints = new ArrayList<>();

    /**
     * Add list of points
     *
     * @param alPoints
     */
    public RainbowPolylineOptions(final List<RainbowPoint> alPoints) {
        add(alPoints);
    }

    /**
     * Add point
     *
     * @param point
     * @return
     */
    public RainbowPolylineOptions add(final RainbowPoint point) {
        if (point != null) {
            alPoints.add(point);
        }
        return this; // return this instance
    }

    /**
     * Add list of points
     *
     * @param alPoints
     * @return
     */
    public RainbowPolylineOptions add(final List<RainbowPoint> alPoints) {
        if (!FrameworkUtils.checkIfNull(alPoints)) {
            for (RainbowPoint newPoint : alPoints) {
                add(newPoint);
            }
        }
        return this; // return this instance
    }

    /**
     * Set zIndex
     *
     * @param zIndex
     * @return
     */
    public RainbowPolylineOptions zIndex(final int zIndex) {
        this.zIndex = zIndex;
        return this; // return this instance
    }

    /**
     * Set stroke width
     *
     * @param strokeWidth
     * @return
     */
    public RainbowPolylineOptions strokeWidth(final int strokeWidth) {
        this.mStrokeWidth = strokeWidth;
        return this; // return this instance
    }

    /**
     * Set stroke cap
     *
     * @param strokeCap
     * @return
     */
    public RainbowPolylineOptions strokeCap(final Paint.Cap strokeCap) {
        this.mStrokeCap = strokeCap;
        return this; // return this instance
    }

    /**
     * Set stroke join
     *
     * @param strokeJoin
     * @return
     */
    public RainbowPolylineOptions strokeJoin(final Paint.Join strokeJoin) {
        this.mStrokeJoin = strokeJoin;
        return this; // return this instance
    }

    /**
     * Set path effect
     *
     * @param pathEffect
     * @return
     */
    public RainbowPolylineOptions pathEffect(final PathEffect pathEffect) {
        this.mPathEffect = pathEffect;
        return this; // return this instance
    }

    /**
     * Set mask filter
     *
     * @param maskFilter
     * @return
     */
    public RainbowPolylineOptions maskFilter(final MaskFilter maskFilter) {
        this.mMaskFilter = maskFilter;
        return this; // return this instance
    }

    /**
     * Set stroke shader
     *
     * @param strokeShader
     * @return
     */
    public RainbowPolylineOptions strokeShader(final Shader strokeShader) {
        this.mStrokeShader = strokeShader;
        return this; // return this instance
    }

    /**
     * Set linear gradient
     *
     * @param linearGradient
     * @return
     */
    public RainbowPolylineOptions linearGradient(final boolean linearGradient) {
        this.mLinearGradient = linearGradient;
        return this; // return this instance
    }

    /**
     * Set stroke color
     *
     * @param strokeColor
     * @return
     */
    public RainbowPolylineOptions strokeColor(final Integer strokeColor) {
        this.mStrokeColor = strokeColor;
        return this; // return this instance
    }

    /**
     * Set mAntiAlias
     *
     * @param antialias
     * @return
     */
    public RainbowPolylineOptions antiAlias(final boolean antialias) {
        this.mAntiAlias = antialias;
        return this; // return this instance
    }

    /**
     * RainbowPolylineOptions builder
     *
     * @return
     */
    public RainbowPolyline build() {
        return new RainbowPolyline(zIndex, alPoints, mStrokeWidth, mStrokeCap, mStrokeJoin, mPathEffect,
                mMaskFilter, mStrokeShader, mLinearGradient, mStrokeColor, mAntiAlias);
    }
}
