package com.app.framework.listeners;

import com.app.framework.entities.Address;

public interface AddressListener {

    /**
     * Interface for when address information is received
     *
     * @param address The address of the latitude and longitude coordinates location
     */
    void onAddressResponse(Address address);

    /**
     * Interface for when retrieving address information fails
     */
    void onAddressError();

    /**
     * Interface for when there are no results when retrieving address information
     */
    void onZeroResults();
}
