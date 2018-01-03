package com.xhy.zhanhui.http.vo;

/**
 * 登录类
 * Created by Aaron on 14/12/2017.
 */

public class LoginVo {
    private String mobile;
    private String password;

    public LoginVo(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
