package com.app.framework.sharedpref;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class SharedPref {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefsEditor;

    /**
     * @param context  Interface to global information about an application environment
     * @param prefName : Name of Preference
     */
    @SuppressLint("CommitPrefEdits")
    public SharedPref(@NonNull Context context, @NonNull String prefName) {
        this.sharedPreferences = context.getSharedPreferences(prefName, Activity.MODE_PRIVATE);
        this.prefsEditor = sharedPreferences.edit();
    }

    /**
     * Method for clearing all data of preference.
     */
    public void clearAllPreferences() {
        prefsEditor.clear();
        prefsEditor.commit();
    }

    /**
     * Method for remove data of preference
     *
     * @param key Key for value retrieval
     */
    public void removePreference(@NonNull String key) {
        prefsEditor.remove(key);
        prefsEditor.commit();
    }

    /**
     * @param key   Key for value retrieval
     * @param value : String Value
     */
    public void setPref(@NonNull String key, @NonNull String value) {
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    /**
     * @param key   Key for value retrieval
     * @param value : int Value
     */
    public void setPref(@NonNull String key, int value) {
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    /**
     * @param key   Key for value retrieval
     * @param value : long value
     */
    public void setPref(@NonNull String key, long value) {
        prefsEditor.putLong(key, value);
        prefsEditor.commit();
    }

    /**
     * @param key   Key for value retrieval
     * @param value : boolean value
     */
    public void setPref(@NonNull String key, boolean value) {
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    /**
     * @param key      Key for value retrieval
     * @param defValue The default value
     * @return String Type
     */
    public String getStringPref(@NonNull String key, @NonNull String defValue) {
        return sharedPreferences.getString(key, defValue);
    }

    /**
     * @param key      Key for value retrieval
     * @param defValue The default value
     * @return int Type
     */
    public int getIntPref(@NonNull String key, int defValue) {
        return sharedPreferences.getInt(key, defValue);
    }

    /**
     * @param key Key for value retrieval
     * @return boolean type
     */
    public boolean getBooleanPref(@NonNull String key, boolean defValue) {
        return sharedPreferences.getBoolean(key, defValue);
    }

    /**
     * @param key      Key for value retrieval
     * @param defValue The default value
     * @return long Type
     */
    public long getLongPref(@NonNull String key, long defValue) {
        return sharedPreferences.getLong(key, defValue);
    }
}
