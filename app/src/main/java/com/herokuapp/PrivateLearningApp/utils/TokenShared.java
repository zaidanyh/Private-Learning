package com.herokuapp.PrivateLearningApp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;


public class TokenShared {
    private static final String KEY_TOKEN_SHARED = "shared_token";
    private static final String KEY_ID_USER = "shared_id";
    private final SharedPreferences sharedPreferences;

    public TokenShared(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setToken(String token) {
        sharedPreferences.edit().putString(KEY_TOKEN_SHARED, token).apply();
    }

    public void setIdUser(int id) {
        sharedPreferences.edit().putInt(KEY_ID_USER, id).apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_TOKEN_SHARED, null);
    }

    public Integer getIdUser() {
        return sharedPreferences.getInt(KEY_ID_USER, 0);
    }

    public void removeToken() {
        sharedPreferences.edit().remove(KEY_TOKEN_SHARED).apply();
        sharedPreferences.edit().remove(KEY_ID_USER).apply();
    }
}
