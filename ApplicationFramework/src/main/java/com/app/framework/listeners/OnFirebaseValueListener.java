package com.app.framework.listeners;

import com.app.framework.model.HistoryModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.HashMap;

/**
 * Created by LJTat on 6/3/2017.
 */

public interface OnFirebaseValueListener {

    void onUpdateDataChange(DataSnapshot dataSnapshot);

    void onUpdateDatabaseError(DatabaseError databaseError);

    void onRetrieveDataChangeWithFilter(HashMap<String, HistoryModel> map);

    void onRetrieveDataChange(DataSnapshot dataSnapshot);

    void onRetrieveDataError(DatabaseError databaseError);
}
