package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 商圈企业详情Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class TargetCompanyBean extends BaseBean implements Serializable {

    private List<Obj> data;

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    public class Obj implements Serializable {
        private String company_id;
        private String image_url;
        private String company_name;
        private String recommend_index; // 企业关联度
        private List<User> company_users;

        public List<User> getCompany_users() {
            return company_users;
        }

        public void setCompany_users(List<User> company_users) {
            this.company_users = company_users;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getRecommend_index() {
            return recommend_index;
        }

        public void setRecommend_index(String recommend_index) {
            this.recommend_index = recommend_index;
        }

        public class User implements Serializable {
            private String hx_username;
            private String icon;
            private String nickname;
            private String user_id;
            private String v_title; // 职务

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
        }
    }
}
