package com.blog.ljtatum.tipcalculator.listeners;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by LJTat on 6/3/2017.
 */

public interface OnFirebaseValueListener {

    void onDataChange(DataSnapshot dataSnapshot);

    void onCancelled(DatabaseError databaseError);
}
