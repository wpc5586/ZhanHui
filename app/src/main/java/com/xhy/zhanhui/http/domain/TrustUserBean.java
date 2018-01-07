package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;

/**
 * 商圈客户详情Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class TrustUserBean extends BaseBean implements Serializable{

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String hx_username;
        private String icon;
        private String nickname;
        private String user_id;
        private String v_title;
        private String recommend_index; // 关联度

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

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getV_title() {
            return v_title;
        }

        public void setV_title(String v_title) {
            this.v_title = v_title;
        }

        public String getRecommend_index() {
            return recommend_index;
        }

        public void setRecommend_index(String recommend_index) {
            this.recommend_index = recommend_index;
        }
    }
}
