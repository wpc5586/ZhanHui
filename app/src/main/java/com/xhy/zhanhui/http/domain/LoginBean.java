package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

/**
 * 登录信息Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class LoginBean extends BaseBean {

    private Obj data;

    public Obj getObj() {
        return data;
    }

    public void setObj(Obj obj) {
        this.data = data;
    }

    public class Obj {
        private String hx_password;
        private String hx_username;
        private String icon;
        private String user_id;
        private String company_id;
        private String nickname;
        private String userName;
        private String vcard_id;

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getVcard_id() {
            return vcard_id;
        }

        public void setVcard_id(String vcard_id) {
            this.vcard_id = vcard_id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHx_password() {
            return hx_password;
        }

        public void setHx_password(String hx_password) {
            this.hx_password = hx_password;
        }

        public String getHx_username() {
            return hx_username;
        }

        public void setHx_username(String hx_username) {
            this.hx_username = hx_username;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }
}
