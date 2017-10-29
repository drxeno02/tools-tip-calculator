package com.app.framework.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Address implements Parcelable {

    /**
     * Create a new instance of the Parcelable class, instantiating it from the given Parcel
     * whose data had previously been written
     */
    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @NonNull
        @Override
        public Object createFromParcel(@NonNull Parcel parcel) {
            return new Address(parcel);
        }

        @NonNull
        @Override
        public Object[] newArray(int size) {
            return new Address[size];
        }
    };
    public String placeId, addressLine1, addressLine2, streetNumber, city, state,
            stateCode, postalCode, country, countryCode, formattedAddress;
    public double latitude, longitude;

    /**
     * Constructor
     */
    public Address() {
    }

    /**
     * Set parsable data
     *
     * @param parcel Container for a message (data and object references) that can be sent through an IBinder
     */
    public Address(Parcel parcel) {
        placeId = parcel.readString();
        addressLine1 = parcel.readString();
        addressLine2 = parcel.readString();
        streetNumber = parcel.readString();
        city = parcel.readString();
        state = parcel.readString();
        stateCode = parcel.readString();
        postalCode = parcel.readString();
        country = parcel.readString();
        countryCode = parcel.readString();
        formattedAddress = parcel.readString();
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(placeId);
        parcel.writeString(addressLine1);
        parcel.writeString(addressLine2);
        parcel.writeString(streetNumber);
        parcel.writeString(city);
        parcel.writeString(state);
        parcel.writeString(stateCode);
        parcel.writeString(postalCode);
        parcel.writeString(country);
        parcel.writeString(countryCode);
        parcel.writeString(formattedAddress);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
