package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 商圈信任企业Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class BusinessTrustBean extends BaseBean implements Serializable{

    private List<Obj> data;

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String icon;
        private String company_id;
        private String company_name;
        private String hx_username;
        private String user_id;
        private String recommend_index;
        private String attention_degree;
        private String user_name;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getHx_username() {
            return hx_username;
        }

        public void setHx_username(String hx_username) {
            this.hx_username = hx_username;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getAttention_degree() {
            return attention_degree == null ? "0" : (Float.valueOf(attention_degree) == 0 ? "0" : attention_degree);
        }

        public void setAttention_degree(String attention_degree) {
            this.attention_degree = attention_degree;
        }

        public String getRecommend_index() {
            return recommend_index == null ? "0" : (Float.valueOf(recommend_index) == 0 ? "0" : recommend_index);
        }

        public void setRecommend_index(String recommend_index) {
            this.recommend_index = recommend_index;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
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
    }
}
