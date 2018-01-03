package com.xhy.zhanhui.preferences;

import android.text.TextUtils;

import com.aaron.aaronlibrary.base.utils.BaseSharedPreferences;
import com.google.gson.Gson;
import com.xhy.zhanhui.http.domain.LoginBean;

/**
 * 用户信息SharedPreferences
 * Created by Aaron on 2017/12/14 0028.
 */
public class UserSharedPreferences extends BaseSharedPreferences {

    public static final String LOGIN_DATA = "loginData";// 用户成功登录的信息

    public static final String LOGIN_USER_NAME = "loginUserName";// 用户输入的用户名（userId是引导接口返回的用户Id）

    public static UserSharedPreferences instance = null;

    public static UserSharedPreferences getInstance() {
        if (instance == null)
            instance = new UserSharedPreferences();

        return instance;
    }

    @Override
    public String getFilename() {
        return USER_INFO + "_" + getUserId();
    }

    public void setLoginData(LoginBean bean) {
        String data = new Gson().toJson(bean);
        System.out.println("~!~ data = " + data);
        set(LOGIN_DATA, data);
    }

    public LoginBean getLoginData() {
        String data = "";
        data = get(LOGIN_DATA);
        return new Gson().fromJson(data, LoginBean.class);
    }

    /**
     * 设置用户输入的用户名
     * @param name
     */
    public void setLoginName(String name) {
        set(LOGIN_USER_NAME, name);
    }

    /**
     * 获取用户输入的用户名
     * @return
     */
    public String getLoginName() {
        String name = "";
        name = get(LOGIN_USER_NAME);
        return name;
    }

    public void clean() {
        setLoginName("");
        setLoginData(null);
    }
}
