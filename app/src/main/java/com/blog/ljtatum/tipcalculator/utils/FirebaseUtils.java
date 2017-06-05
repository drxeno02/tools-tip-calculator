package com.blog.ljtatum.tipcalculator.utils;

import android.util.Log;

import com.blog.ljtatum.tipcalculator.listeners.OnFirebaseValueListener;
import com.blog.ljtatum.tipcalculator.logger.Logger;
import com.blog.ljtatum.tipcalculator.model.HistoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udi.app.framework.utilities.FrameworkUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LJTat on 6/3/2017.
 */

public class FirebaseUtils {
    private static final String TAG = FirebaseUtils.class.getSimpleName();

    private static FirebaseDatabase mFirebaseDatabase;
    private static DatabaseReference mDbReference;

    // listeners
    private static OnFirebaseValueListener mFirebaseValueListener;

    /**
     * Method is used to set callback to listen to value changes in Firebase DB
     *
     * @param listener
     */
    public static void onFirebaseValueListener(OnFirebaseValueListener listener) {
        mFirebaseValueListener = listener;
    }

    /**
     * Constructor
     */
    public FirebaseUtils() {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDbReference = mFirebaseDatabase.getReference();
    }

    /**
     * Method is used to add a list of values to Firebase DB
     *
     * @param alHistory
     */
    public static void addValues(List<HistoryModel> alHistory) {
        if (!FrameworkUtils.checkIfNull(mDbReference)) {
            // set value
            mDbReference.setValue(alHistory);
            mDbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Logger.i(TAG, "<onDataChange> successful!");
                    if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                        mFirebaseValueListener.onDataChange(dataSnapshot);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (!FrameworkUtils.checkIfNull(databaseError) && !FrameworkUtils.isStringEmpty(databaseError.getMessage())) {
                        Logger.i(TAG, "<onCancelled> error: " + databaseError.getMessage());
                        if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                            mFirebaseValueListener.onCancelled(databaseError);
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
        mDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Logger.i(TAG, "<onDataChange> successful!");
                if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                    mFirebaseValueListener.onDataChange(dataSnapshot);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (!FrameworkUtils.checkIfNull(databaseError) && !FrameworkUtils.isStringEmpty(databaseError.getMessage())) {
                    Logger.i(TAG, "<onCancelled> error: " + databaseError.getMessage());
                    if (!FrameworkUtils.checkIfNull(mFirebaseValueListener)) {
                        mFirebaseValueListener.onCancelled(databaseError);
                    }
                }
            }
        });
    }
}
