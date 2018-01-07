package com.aaron.aaronlibrary.base.utils;

public class UserInfo {

    
    private static UserInfo instance = null;
    
    public static UserInfo getInstance() {
        if (instance == null) {
            instance = new UserInfo();
        }
        return instance;
    }
    
    public boolean isLoggedIn() {
        // TODO
        return true;
    }
}
