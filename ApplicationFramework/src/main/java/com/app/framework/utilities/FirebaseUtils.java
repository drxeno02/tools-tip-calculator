package com.app.framework.utilities;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.app.framework.listeners.OnFirebaseValueListener;
import com.app.framework.model.HistoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by LJTat on 6/3/2017.
 */

public class FirebaseUtils {
    private static final String TAG = FirebaseUtils.class.getSimpleName();

    private static Context mContext;
    private static DatabaseReference mWriteDbReference;
    private static DatabaseReference mQueryDbReference;

    // listeners
    private static OnFirebaseValueListener mFirebaseValueListener;

    /**
     * Method is used to set callback to listen to value changes in Firebase DB
     *
     * @param listener Callback to listen to value changes in Firebase DB
     */
    public static void onFirebaseValueListener(OnFirebaseValueListener listener) {
        mFirebaseValueListener = listener;
    }

    /**
     * Constructor
     */
    public FirebaseUtils(Context context) {
        mContext = context;
        mWriteDbReference = FirebaseDatabase.getInstance().getReference();
        mQueryDbReference = FirebaseDatabase.getInstance().getReference(FrameworkUtils.getAndroidId(mContext));
    }

    /**
     * Method is used to add values to Firebase DB. All data will be wiped and replaced with
     * the data object content
     *
     * @param data Data to store to Firebase
     */
    public static void addValueReplace(Object data) {
        if (!FrameworkUtils.checkIfNull(mWriteDbReference)) {
            // set value
            mWriteDbReference.child(FrameworkUtils.getAndroidId(mContext)).setValue(data);
            mWriteDbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                        mFirebaseValueListener.onUpdateDataChange(dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (!FrameworkUtils.checkIfNull(databaseError) && !FrameworkUtils.isStringEmpty(databaseError.getMessage())) {
                        if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                            mFirebaseValueListener.onUpdateDatabaseError(databaseError);
                        }
                    }
                }
            });
        }
    }

    /**
     * Method is used to add values to Firebase DB. All data will be retained and updated
     *
     * @param data Data to store to Firebase
     */
    public static void addValueContinuous(final Object data) {
        if (!FrameworkUtils.checkIfNull(mQueryDbReference)) {
            mQueryDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, HistoryModel> map = new HashMap<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        HistoryModel historyModel = snapshot.getValue(HistoryModel.class);
                        map.put(snapshot.getKey(), historyModel);
                    }
                    // add final data
                    map.put(String.valueOf(map.size()), (HistoryModel) data);
                    addValues(new ArrayList<>(map.values()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (!FrameworkUtils.checkIfNull(databaseError) && !FrameworkUtils.isStringEmpty(databaseError.getMessage())) {
                        if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                            mFirebaseValueListener.onUpdateDatabaseError(databaseError);
                        }
                    }
                }
            });
        }
    }

    /**
     * Method is usd to add values to Firebase DB. All data will be retained and updated. The data
     * can be filtered hy max number of items and timestamp
     *
     * @param data     Data to store to Firebase
     * @param maxItems Maximum number of items allowed to store to Firebase DB
     * @param minDate  The latest date to allow history of data to store to Firebase DB
     */
    public static void addValueContinuousWithFilter(final Object data, @Nullable final Integer maxItems, @Nullable final Date minDate) {
        if (!FrameworkUtils.checkIfNull(mQueryDbReference)) {
            mQueryDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, HistoryModel> map = new HashMap<>();
                    if (!FrameworkUtils.checkIfNull(maxItems) || !FrameworkUtils.checkIfNull(minDate)) {
                        map = filterData(dataSnapshot, maxItems, minDate);
                    } else {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            HistoryModel historyModel = snapshot.getValue(HistoryModel.class);
                            map.put(snapshot.getKey(), historyModel);
                        }
                        // add final data
                        map.put(String.valueOf(map.size()), (HistoryModel) data);
                    }
                    addValues(new ArrayList<>(map.values()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (!FrameworkUtils.checkIfNull(databaseError) && !FrameworkUtils.isStringEmpty(databaseError.getMessage())) {
                        if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                            mFirebaseValueListener.onUpdateDatabaseError(databaseError);
                        }
                    }
                }
            });
        }
    }

    /**
     * Method is used to add a list of values to Firebase DB
     *
     * @param alData Data to store to Firebase
     */
    private static void addValues(List<?> alData) {
        if (!FrameworkUtils.checkIfNull(mWriteDbReference)) {
            // set value
            mWriteDbReference.child(FrameworkUtils.getAndroidId(mContext)).setValue(alData);
            mWriteDbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                        mFirebaseValueListener.onUpdateDataChange(dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (!FrameworkUtils.checkIfNull(databaseError) && !FrameworkUtils.isStringEmpty(databaseError.getMessage())) {
                        if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                            mFirebaseValueListener.onUpdateDatabaseError(databaseError);
                        }
                    }
                }
            });
        }
    }

    /**
     * Method is used to retrieve a list of values from Firebase DB
     *
     * @param maxItems Maximum number of items allowed to store to Firebase DB
     * @param minDate  The latest date to allow history of data to store to Firebase DB
     */
    public static void retrieveValuesWithFilter(@Nullable final Integer maxItems, @Nullable final Date minDate) {
        if (!FrameworkUtils.checkIfNull(mQueryDbReference)) {
            mQueryDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                        mFirebaseValueListener.onRetrieveDataChangeWithFilter(filterData(dataSnapshot, maxItems, minDate));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (!FrameworkUtils.checkIfNull(databaseError) && !FrameworkUtils.isStringEmpty(databaseError.getMessage())) {
                        if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                            mFirebaseValueListener.onRetrieveDataError(databaseError);
                        }
                    }
                }
            });
        }
    }

    /**
     * Method is used to retrieve a list of values from Firebase DB
     */
    public static void retrieveValues() {
        if (!FrameworkUtils.checkIfNull(mQueryDbReference)) {
            mQueryDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                        mFirebaseValueListener.onRetrieveDataChange(dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (!FrameworkUtils.checkIfNull(databaseError) && !FrameworkUtils.isStringEmpty(databaseError.getMessage())) {
                        if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                            mFirebaseValueListener.onRetrieveDataError(databaseError);
                        }
                    }
                }
            });
        }
    }

    /**
     * @param dataSnapshot Retrieved data from Firebase DB
     * @param maxItems     Maximum number of items allowed to store to Firebase DB
     * @param minDate      The latest date to allow history of data to store to Firebase DB
     * @return Hashmap of filtered data
     */
    private static HashMap<String, HistoryModel> filterData(@NonNull DataSnapshot dataSnapshot, @Nullable final Integer maxItems, @Nullable final Date minDate) {
        HashMap<String, HistoryModel> map = new HashMap<>();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            HistoryModel historyModel = snapshot.getValue(HistoryModel.class);
            // break loop if no history data
            if (FrameworkUtils.checkIfNull(historyModel)) {
                break;
            }

            if (!FrameworkUtils.checkIfNull(maxItems) && !FrameworkUtils.checkIfNull(minDate)) {
                // check size and calendar date
                if (map.size() < maxItems && FrameworkUtils.isDateAfterCurrentDate(minDate,
                        historyModel.date, "MM/dd/yyyy hh:mm:ss a")) {
                    map.put(snapshot.getKey(), historyModel);
                }
            } else if (!FrameworkUtils.checkIfNull(maxItems)) {
                // check size
                if (map.size() < maxItems) {
                    map.put(snapshot.getKey(), historyModel);
                }
            } else if (!FrameworkUtils.checkIfNull(minDate)) {
                // check calendar date
                if (FrameworkUtils.isDateAfterCurrentDate(minDate, historyModel.date,
                        "MM/dd/yyyy hh:mm:ss a")) {
                    map.put(snapshot.getKey(), historyModel);
                }
            } else {
                // no filter
                map.put(snapshot.getKey(), historyModel);
            }
        }
        return map;
    }
}
