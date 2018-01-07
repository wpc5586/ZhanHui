package com.xhy.zhanhui.http.vo;

/**
 * 修改密码类
 * Created by Aaron on 14/12/2017.
 */

public class UpdatePwdVo {
    private String new_password;
    private String old_password;

    public UpdatePwdVo(String new_password, String old_password) {
        this.new_password = new_password;
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }
}
