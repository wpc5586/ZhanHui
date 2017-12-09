package com.aaron.aaronlibrary.base.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.aaron.aaronlibrary.base.app.CrashApplication;

public abstract class BaseSharedPreferences {
    
    public static final String USER_ID = "userId";// user ID

    public static final String PASS_WORD = "passWord";// password

    public static final String USER_INFO = "userInfo";// user info 文件名

    public abstract String getFilename();

    public void set(String key, String value) {
        set(key, value, getFilename());
    }

    public void set(String key, String value, String filename) {
        if (key == null)
            return;

        if (value == null) {
            value = "";
        }

        SharedPreferences pref = CrashApplication.APP.getSharedPreferences(filename, android.content.Context.MODE_PRIVATE);
        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String get(String key) {
        return get(key, getFilename());
    }

    public String get(String key, String filename) {
        if (key == null)
            return "";
        SharedPreferences pref = CrashApplication.APP.getSharedPreferences(filename, android.content.Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public void setUserId(String value) {
        set(USER_ID, value, USER_INFO);
    }

    public String getUserId() {
        return get(USER_ID, USER_INFO);
    }

    public void setPassWord(String value) {
        set(PASS_WORD, value, PASS_WORD);
    }

    public String getPassWord() {
        return get(PASS_WORD, PASS_WORD);
    }

}
