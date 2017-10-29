package com.app.framework.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class Place implements Parcelable {

    /**
     * Create a new instance of the Parcelable class, instantiating it from the given Parcel
     * whose data had previously been written
     */
    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @NonNull
        @Override
        public Object createFromParcel(@NonNull Parcel parcel) {
            return new Place(parcel);
        }

        @NonNull
        @Override
        public Object[] newArray(int size) {
            return new Place[size];
        }
    };
    public String placeId, description, primaryText, secondaryText;

    /**
     * Constructor
     */
    public Place() {
    }

    /**
     * Set parsable data
     *
     * @param parcel Container for a message (data and object references) that can be sent through an IBinder
     */
    public Place(Parcel parcel) {
        placeId = parcel.readString();
        description = parcel.readString();
        primaryText = parcel.readString();
        secondaryText = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flag) {
        parcel.writeString(placeId);
        parcel.writeString(description);
        parcel.writeString(primaryText);
        parcel.writeString(secondaryText);
    }
}
