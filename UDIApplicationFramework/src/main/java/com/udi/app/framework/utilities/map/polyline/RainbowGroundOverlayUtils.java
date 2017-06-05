package com.udi.app.framework.utilities.map.polyline;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.maps.android.SphericalUtil;
import com.udi.app.framework.utilities.FrameworkUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by leonard on 4/26/2017.
 * A ground mOverlay is an image that is fixed to a mMap. A ground mOverlay has the following
 * properties
 *
 * @see <a href="https://developers.google.com/android/reference/com/google/android/gms/maps/model/GroundOverlay">GroundOverlay</a>
 */
public class RainbowGroundOverlayUtils {
    // represents the minimum zoom level allowed before polylines stop being redrawn (stop refreshing)
    private static final float MINIMUM_ZOOM_LEVEL = 8f; // arbitrary value. Added for optimization reasons

    private View mView;
    private GoogleMap mMap;
    private GroundOverlay mOverlay;
    private Bitmap mBitmap;
    private float zIndex;
    private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

    // sorted mMap of rainbow shapes
    private SortedMap<Integer, List<RainbowPolylineShape>> mShapes = new TreeMap<>();


    /**
     * Constructor
     *
     * @param view
     * @param map
     * @param zIndex
     * @param paddingLeft
     * @param paddingTop
     * @param paddingRight
     * @param paddingBottom
     */
    private RainbowGroundOverlayUtils(final View view, final GoogleMap map, final float zIndex,
                                      final int paddingLeft, final int paddingTop,
                                      final int paddingRight, final int paddingBottom) {

        // confirm that mView or mMap object is not null, otherwise throw exception
        if (FrameworkUtils.checkIfNull(view) || FrameworkUtils.checkIfNull(map)) {
            throw new IllegalArgumentException("View and GoogleMap cannot be null");
        }

        this.mView = view;
        this.mMap = map;
        this.zIndex = zIndex;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingTop = paddingTop;
        this.mPaddingRight = paddingRight;
        this.mPaddingBottom = paddingBottom;

        // currently tilt gestures is not handled. Disabling for now --LT
        map.getUiSettings().setTiltGesturesEnabled(false);
    }

    /**
     * Method is used to refresh
     */
    public void refresh() {
        CameraPosition cameraPosition = mMap.getCameraPosition();
        if (cameraPosition.zoom >= MINIMUM_ZOOM_LEVEL) {
            Projection projection = mMap.getProjection();

            // prepare empty new bitmap
            prepareBitmap();
            // draw on top of bitmap
            draw(mBitmap, projection);

            float mapWidth = (float) SphericalUtil.computeDistanceBetween(
                    projection.getVisibleRegion().nearLeft,
                    projection.getVisibleRegion().nearRight);

            if (FrameworkUtils.checkIfNull(mOverlay)) {
                GroundOverlayOptions background = new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromBitmap(mBitmap))
                        .position(cameraPosition.target, mapWidth)
                        .bearing(cameraPosition.bearing)
                        .zIndex(zIndex);
                mOverlay = mMap.addGroundOverlay(background);
            } else {
                mOverlay.setImage(BitmapDescriptorFactory.fromBitmap(mBitmap));
                mOverlay.setPosition(cameraPosition.target);
                mOverlay.setDimensions(mapWidth);
                mOverlay.setBearing(cameraPosition.bearing);
            }
        } else {
            if (!FrameworkUtils.checkIfNull(mOverlay)) {
                // release resources
                mOverlay.remove();
                mOverlay = null;
            }
        }
    }

    /**
     * Method is used to add polyline 'shape'
     *
     * @param shape
     */
    public void addPolylineShape(final RainbowPolylineShape shape) {
        if (!FrameworkUtils.checkIfNull(shape)) {
            if (!mShapes.containsKey(shape.getZIndex())) {
                mShapes.put(shape.getZIndex(), new ArrayList<RainbowPolylineShape>());
            }
            List<RainbowPolylineShape> shapesZIndez = mShapes.get(shape.getZIndex());
            shapesZIndez.add(shape);
        }
    }

    /**
     * Method is used to remove polyline 'shape'
     *
     * @param shape
     */
    public void removePolylineShape(final RainbowPolylineShape shape) {
        if (!FrameworkUtils.checkIfNull(shape)) {
            Set<Integer> zIndices = mShapes.keySet();
            for (Integer zIndex : zIndices) {
                List<RainbowPolylineShape> shapesZIndex = mShapes.get(zIndex);
                shapesZIndex.remove(shape);
            }
        }
    }

    /**
     * Method is used to fetch new bitmap
     */
    private void prepareBitmap() {
        if (FrameworkUtils.checkIfNull(mBitmap) || mBitmap.getWidth() != mView.getWidth()
                || mBitmap.getHeight() != mView.getHeight()) {
            // create new bitmap
            mBitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            // erase bitmap; fill bitmap with transparent color
            mBitmap.eraseColor(Color.TRANSPARENT);
        }
    }

    /**
     * Method is used to draw onto bitmap
     *
     * @param bitmap
     * @param projection
     * @return
     */
    private Bitmap draw(final Bitmap bitmap, final Projection projection) {
        Set<Integer> zIndices = mShapes.keySet();
        for (Integer zIndex : zIndices) {
            draw(bitmap, projection, mShapes.get(zIndex));
        }
        return bitmap;
    }

    /**
     * Method is used to draw onto bitmap
     *
     * @param bitmap
     * @param projection
     * @param shapes
     * @return
     */
    private Bitmap draw(final Bitmap bitmap, final Projection projection,
                        final List<RainbowPolylineShape> shapes) {
        for (RainbowPolylineShape shape : shapes) {
            shape.draw(bitmap, projection, mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        }
        return bitmap;
    }

    /**
     * RainbowGroundOverlayUtils builder
     */
    public static class Builder {
        private View mView;
        private GoogleMap mMap;
        private float zIndex;
        private int mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom;

        public Builder(final View view, final GoogleMap map) {
            this.mView = view;
            this.mMap = map;
        }

        public Builder zIndex(final float zIndex) {
            this.zIndex = zIndex;
            return this;
        }

        public Builder padding(final int top, final int bottom, final int left, final int right) {
            this.mPaddingTop = top;
            this.mPaddingBottom = bottom;
            this.mPaddingLeft = left;
            this.mPaddingRight = right;
            return this;
        }

        public RainbowGroundOverlayUtils build() {
            return new RainbowGroundOverlayUtils(mView, mMap, zIndex,
                    mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
        }
    }
}
