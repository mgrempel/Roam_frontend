package com.pixelworks.roam;

import android.content.Context;
import android.content.SharedPreferences;

import org.jetbrains.annotations.NotNull;

public class SharedPreferencesHelper {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public SharedPreferencesHelper(@NotNull Context ctx) {
        preferences = ctx.getSharedPreferences("roam", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public static void setStringValue(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public static void setIntValue(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public static String getStringValue(String key) {
        String result = preferences.getString(key, "NOVALUE");
        return result;
    }

    public static int getIntValue(String key) {
        int result = preferences.getInt(key, -1);
        return result;
    }

    public static void logOut() {
        editor.remove("uuid");
        editor.remove("key");
        editor.apply();
    }
}
