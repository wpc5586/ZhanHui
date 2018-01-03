package com.aaron.aaronlibrary.utils;

import android.content.Context;

import com.aaron.aaronlibrary.base.utils.BaseSharedPreferences;

/**
 * 
 * @author Aaron Shared util
 * 
 * */

public class SharedPreferencesSetting extends BaseSharedPreferences {

    public static SharedPreferencesSetting instance = null;

    public static Context applicationContext;

    /**
     * 全局常量：登录atwill 服务器返回的值
     */

    public static final String USER_PHONE = "userPhone";// 用户手机号

    public static final String NIKENAME = "nickname";
    public static final String HEADIMAGE = "headImg";
    public static final String SIGNNAME = "signName";
    public static final String HX_PASSWORD = "hxPassword";// 环信 密码
    public static final String PROFILE = "PROFILE";
    public static final String MESSAGE = "message";
    public static final String FLOATWINDOWPMT = "FloatWindowPrompt";

    private boolean isChanged;

    @Override
    public String getFilename() {
        return USER_INFO + "_" + getUserId();
    }

    public static SharedPreferencesSetting getInstance() {
        if (instance == null)
            instance = new SharedPreferencesSetting();

        return instance;
    }

    public void sethxPassword(String value) {
        set(HX_PASSWORD, value);
    }

    public String gethxPassword() {
        return get(HX_PASSWORD);
    }

    public String getNickname() {
        return get(NIKENAME);
    }

    public String getNicknameEx() {
        String s = getNickname();
        if (s.length() > 0) {
            return s;
        } else {
            return getUserPhone();
        }
    }

    public void setNickname(String value) {
        set(NIKENAME, value);
    }

    public String getHeadImg() {
        return get(HEADIMAGE);
    }

    public void setHeadImg(String value) {
        set(HEADIMAGE, value);
    }

    public String getSignName() {
        return get(SIGNNAME);
    }

    public void setSignName(String value) {
        set(SIGNNAME, value);
    }

    public String getUserPhone() {
        return get(USER_PHONE);
    }

    public void setUserPhone(String value) {
        set(USER_PHONE, value);
    }

//    public void setMyProfile(UserProfileBean bean) {
//        Gson gson = new Gson();
//        String string = gson.toJson(bean);
//        set(PROFILE, string);
//        isChanged = true;
//    }

//    public UserProfileBean getMyProfile() {
//        UserProfileBean userProfile = null;
//        String string = get(PROFILE);
//        if (string.length() > 1) {
//            Gson gson = new Gson();
//            userProfile = gson.fromJson(string, UserProfileBean.class);
//        }
//        return userProfile;
//    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }

    public void setMessage(String num) {
        set(MESSAGE, num);
    }

    public String getMessage() {
        return get(MESSAGE);
    }
    
    public boolean isSelf(String userId) {
        return getUserId().equals(userId);
    }

    public void setFloatWindowPrompt(String num) {
        set(FLOATWINDOWPMT, num);
    }

    public String getFloatWindowPrompt() {
        return get(FLOATWINDOWPMT);
    }

}
