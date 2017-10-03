package com.app.framework.utilities.map.model;

import com.app.framework.utilities.FrameworkUtils;

/**
 * Created by LJTat on 10/2/2017.
 */

public class PlaceModel {

    public String landmark, streetNo, streetName, city, state, zipCode, country;

    /**
     * Method is used to retrieve formatted address
     *
     * @return
     */
    public String getFormattedAddress() {
        StringBuilder builder = new StringBuilder();
        builder.append(FrameworkUtils.isStringEmpty(streetNo) ? "" : streetNo);
        // address number and name present, separate address name and city with space
        if (!FrameworkUtils.isStringEmpty(streetNo) && !FrameworkUtils.isStringEmpty(streetName)) {
            builder.append(" ");
        }
        builder.append(FrameworkUtils.isStringEmpty(streetName) ? "" : streetName);
        // address name or number present, separate address name and city with comma
        if (!FrameworkUtils.isStringEmpty(streetNo) || !FrameworkUtils.isStringEmpty(streetName)) {
            builder.append(", ");
        }
        builder.append(FrameworkUtils.isStringEmpty(this.city) ? "" : this.city + ", ");
        builder.append(FrameworkUtils.isStringEmpty(this.state) ? "" : this.state);
        builder.append(FrameworkUtils.isStringEmpty(this.zipCode) ? "" : " " + this.zipCode);
        builder.append(FrameworkUtils.isStringEmpty(this.country) ? "" : ", " + this.country);
        return builder.toString();
    }

    /**
     * Method is used to retrieve formatted address minus state, zipcode and country
     *
     * @return
     */
    public String getShortFormattedAddress(boolean isIncludeCity) {
        StringBuilder builder = new StringBuilder();
        builder.append(FrameworkUtils.isStringEmpty(streetNo) ? "" : streetNo);
        // address number and name present, separate address name and city with space
        if (!FrameworkUtils.isStringEmpty(streetNo) && !FrameworkUtils.isStringEmpty(streetName)) {
            builder.append(" ");
        }
        builder.append(FrameworkUtils.isStringEmpty(streetName) ? "" : streetName);
        if (isIncludeCity) {
            // address name or number present, separate address name and city with comma
            if (!FrameworkUtils.isStringEmpty(streetNo) || !FrameworkUtils.isStringEmpty(streetName)) {
                builder.append(", ");
            }
            builder.append(FrameworkUtils.isStringEmpty(this.city) ? "" : this.city);
        }
        return builder.toString();
    }
}
