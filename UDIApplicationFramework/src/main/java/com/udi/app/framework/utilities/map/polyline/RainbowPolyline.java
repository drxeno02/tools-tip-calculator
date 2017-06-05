package com.udi.app.framework.utilities.map.polyline;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Shader;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.udi.app.framework.utilities.FrameworkUtils;

import java.util.List;

/**
 * Created by leonard on 4/26/2017.
 */

public class RainbowPolyline extends RainbowPolylineShape {

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
    public RainbowPolyline(final int zIndex, final List<RainbowPoint> points, final int strokeWidth,
                           final Paint.Cap strokeCap, final Paint.Join strokeJoin, final PathEffect pathEffect,
                           final MaskFilter maskFilter, final Shader strokeShader, final boolean linearGradient,
                           final Integer strokeColor, final boolean antialias) {
        super(zIndex, points, strokeWidth, strokeCap, strokeJoin, pathEffect, maskFilter,
                strokeShader, linearGradient, strokeColor, antialias);
    }

    @Override
    public void doDraw(final Bitmap bitmap, final Projection projection,
                       final int paddingLeft, final int paddingTop,
                       final int paddingRight, final int paddingBottom) {
        drawStroke(bitmap, projection, alPoints, paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    /**
     * Method is used to draw stroke
     *
     * @param bitmap
     * @param projection
     * @param points2Draw
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    protected void drawStroke(final Bitmap bitmap, final Projection projection,
                              final List<RainbowPoint> points2Draw,
                              final int paddingLeft, final int paddingTop,
                              final int paddingRight, final int paddingBottom) {

        // create new canvas
        Canvas canvas = new Canvas(bitmap);
        Paint paint = getDefaultStrokePaint();
        // last point tracker
        RainbowPoint lastPoint = null;

        for (RainbowPoint point : points2Draw) {
            LatLng position = point.getPosition();
            if (!FrameworkUtils.checkIfNull(position)) {
                if (FrameworkUtils.checkIfNull(point.getColor())) {
                    point.color(mStrokeColor);
                }

                if (!FrameworkUtils.checkIfNull(lastPoint)) {
                    drawSegment(canvas, paint, projection, lastPoint, point,
                            paddingLeft, paddingTop, paddingRight, paddingBottom);
                }
                lastPoint = point;
            }
        }
    }

    /**
     * Method is used to draw (paint) onto the canvas
     *
     * @param canvas
     * @param paint
     * @param projection
     * @param pointFrom
     * @param pointTo
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    private void drawSegment(final Canvas canvas, final Paint paint, final Projection projection,
                             final RainbowPoint pointFrom, final RainbowPoint pointTo,
                             final int paddingLeft, final int paddingTop,
                             final int paddingRight, final int paddingBottom) {

        Point toScreenPoint = projection.toScreenLocation(pointTo.getPosition());
        Point fromScreenPoint = projection.toScreenLocation(pointFrom.getPosition());
        int fromX = fromScreenPoint.x + paddingRight / 2 - paddingLeft / 2;
        int fromY = fromScreenPoint.y + paddingBottom / 2 - paddingTop / 2;
        int toX = toScreenPoint.x + paddingRight / 2 - paddingLeft / 2;
        int toY = toScreenPoint.y + paddingBottom / 2 - paddingTop / 2;

        if (mLinearGradient) {
            int[] colors = new int[]{pointFrom.getColor(), pointTo.getColor()};
            // repeat the shader's image horizontally and vertically
            paint.setShader(new LinearGradient(fromX, fromY, toX, toY, colors,
                    null, Shader.TileMode.CLAMP));
        } else {
            paint.setColor(pointFrom.getColor());
        }

        if (!FrameworkUtils.checkIfNull(mStrokeShader)) {
            paint.setShader(mStrokeShader);
        }

        canvas.drawLine(fromX, fromY, toX, toY, paint);
    }

    /**
     * Method is used to retrieve Paint object
     *
     * @return
     */
    private Paint getDefaultStrokePaint() {
        // create new paint object
        Paint paint = new Paint();
        // set attributes
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(mStrokeColor);
        paint.setStrokeWidth(mStrokeWidth);
        paint.setAntiAlias(mAntiAlias);
        paint.setStrokeCap(mStrokeCap);
        paint.setStrokeJoin(mStrokeJoin);
        paint.setPathEffect(mPathEffect);
        paint.setMaskFilter(mMaskFilter);
        return paint;
    }
}
