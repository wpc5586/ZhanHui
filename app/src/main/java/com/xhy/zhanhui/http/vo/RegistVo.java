package com.xhy.zhanhui.http.vo;

import com.aaron.aaronlibrary.utils.Constants;

/**
 * 注册类
 * Created by Aaron on 14/12/2017.
 */

public class RegistVo {
    private String mobile;
    private String password;
    private String smscode;
    private String flag = Constants.VERSION_IS_USER ? "1" : "2"; //  1 观众版 2 企业版

    public RegistVo(String mobile, String password, String smscode) {
        this.mobile = mobile;
        this.password = password;
        this.smscode = smscode;
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

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }

}
