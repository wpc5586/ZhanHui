package com.xhy.zhanhui.http.vo;

/**
 * 修改手机号类
 * Created by Aaron on 14/12/2017.
 */

public class UpdatePhoneVo {
    private String new_mobile;
    private String smscode;

    public UpdatePhoneVo(String new_mobile, String smscode) {
        this.new_mobile = new_mobile;
        this.smscode = smscode;
    }

    public String getNew_mobile() {
        return new_mobile;
    }

    public void setNew_mobile(String new_mobile) {
        this.new_mobile = new_mobile;
    }

    public String getSmscode() {
        return smscode;
    }

    public void setSmscode(String smscode) {
        this.smscode = smscode;
    }
}
