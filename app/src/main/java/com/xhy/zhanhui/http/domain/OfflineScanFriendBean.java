package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

/**
 * 线下关注朋友信息Bean
 * Created by Aaron on 2017/11/1 0001.
 */
public class OfflineScanFriendBean extends BaseBean {

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj {
        private String flag;
        private String user_id;
        private String hx_username;
        private String icon;
        private String nickname;
        private String company_name;
        private String image_url;

        public String getAvatar() {
            return "1".equals(flag) ? icon : image_url;
        }

        public String getName() {
            return "1".equals(flag) ? nickname : company_name;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }
}
