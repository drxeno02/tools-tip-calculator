package com.udi.app.framework.utilities.map.polyline;

/**
 * Created by leonard on 4/26/2017.
 */

public class RainbowPolylineUtils {

    /**
     * Utility method is used to check if bounds intercept
     *
     * @param checkMinX
     * @param checkMinY
     * @param checkMaxX
     * @param checkMaxY
     * @param againstMinX
     * @param againstMinY
     * @param againstMaxX
     * @param againstMaxY
     * @return
     */
    public static boolean intersectsRectangle(final double checkMinX, final double checkMinY,
                                              final double checkMaxX, final double checkMaxY,
                                              final double againstMinX, final double againstMinY,
                                              final double againstMaxX, final double againstMaxY) {

        boolean output = false; // default
        if (againstMaxX > checkMinX && againstMinX < checkMaxX
                && againstMaxY > checkMinY && againstMinY < checkMaxY) {
            output = true;
        }
        return output;
    }
}
