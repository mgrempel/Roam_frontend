package com.pixelworks.roam;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

//Helper class for interacting with shared preferences
public class SharedPreferencesHelper {
    //Shared preference references
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public SharedPreferencesHelper(@NotNull Context ctx) {
        preferences = ctx.getSharedPreferences("roam", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    //Sets a string value
    public static void setStringValue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    //Sets an integer value
    public static void setIntValue(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    //Retrieves a string value
    public static String getStringValue(String key) {
        String result = preferences.getString(key, "NOVALUE");
        return result;
    }

    //Retrieves an integer value
    public static int getIntValue(String key) {
        int result = preferences.getInt(key, -1);
        return result;
    }

    //Clears persisted user data.
    public static void logOut() {
        editor.remove("uuid");
        editor.remove("key");
        editor.apply();
    }
}
