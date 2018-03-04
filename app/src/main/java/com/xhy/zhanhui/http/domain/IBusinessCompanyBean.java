package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 商圈企业详情Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class IBusinessCompanyBean extends BaseBean implements Serializable{

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String matching_id;
        private List<User> company_users;
        private Company company;
        private Relation relation;

        public List<User> getCompany_users() {
            return company_users;
        }

        public void setCompany_users(List<User> company_users) {
            this.company_users = company_users;
        }

        public String getMatching_id() {
            return matching_id;
        }

        public void setMatching_id(String matching_id) {
            this.matching_id = matching_id;
        }

        public Company getCompany() {
            return company;
        }

        public void setCompany(Company company) {
            this.company = company;
        }

        public Relation getRelation() {
            return relation;
        }

        public void setRelation(Relation relation) {
            this.relation = relation;
        }

        public class User implements Serializable{
            private String hx_username;
            private String user_icon;
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
                return user_icon;
            }

            public void setIcon(String icon) {
                this.user_icon = icon;
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

        public class Company implements Serializable{
            private String company_name_en;
            private String company_id;
            private String company_name;
            private String company_message;
            private String company_icon;

            public String getCompany_name_en() {
                return company_name_en;
            }

            public void setCompany_name_en(String company_name_en) {
                this.company_name_en = company_name_en;
            }

            public String getCompany_id() {
                return company_id;
            }

            public void setCompany_id(String company_id) {
                this.company_id = company_id;
            }

            public String getCompany_name() {
                return company_name;
            }

            public void setCompany_name(String company_name) {
                this.company_name = company_name;
            }

            public String getCompany_message() {
                return company_message;
            }

            public void setCompany_message(String company_message) {
                this.company_message = company_message;
            }

            public String getCompany_icon() {
                return company_icon;
            }

            public void setCompany_icon(String company_icon) {
                this.company_icon = company_icon;
            }
        }

        public class Relation implements Serializable{
            private String matched_degree;
            private String recommend_index;
            private String attention_degree;

            public String getMatched_degree() {
                return matched_degree;
            }

            public void setMatched_degree(String matched_degree) {
                this.matched_degree = matched_degree;
            }

            public String getRecommend_index() {
                return recommend_index;
            }

            public void setRecommend_index(String recommend_index) {
                this.recommend_index = recommend_index;
            }

            public String getAttention_degree() {
                return attention_degree;
            }

            public void setAttention_degree(String attention_degree) {
                this.attention_degree = attention_degree;
            }
        }
    }
}
