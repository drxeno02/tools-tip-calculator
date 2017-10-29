package com.app.framework.listeners;

import com.app.framework.model.HistoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;

/**
 * Created by LJTat on 6/3/2017.
 */

public interface OnFirebaseValueListener {

    /**
     * Interface to retrieve data from firebase
     *
     * @param dataSnapshot An instance that contains data from a Firebase location
     */
    void onUpdateDataChange(DataSnapshot dataSnapshot);

    /**
     * Interface for when data retrieval fails
     *
     * @param databaseError The Firebase error
     */
    void onUpdateDatabaseError(DatabaseError databaseError);

    /**
     * Interface to retrieve data from firebase that has been filtered
     *
     * @param map HashMap of filtered data
     */
    void onRetrieveDataChangeWithFilter(HashMap<String, HistoryModel> map);

    /**
     * Interface to retrieve data from firebase that has been filtered
     *
     * @param dataSnapshot An instance that contains data from a Firebase location
     */
    void onRetrieveDataChange(DataSnapshot dataSnapshot);

    /**
     * Interface for when data retrieval fails while trying to filter
     *
     * @param databaseError The Firebase error
     */
    void onRetrieveDataError(DatabaseError databaseError);
}
