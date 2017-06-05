package com.udi.app.framework.listeners;

import com.udi.app.framework.entities.Address;

public interface AddressListener {
    void onAddressResponse(Address address);

    void onAddressError();

    void onZeroResults();
}
