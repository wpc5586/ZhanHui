package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 商圈企业详情Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class TrustCompanyBean extends BaseBean implements Serializable{

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String company_name_en;
        private String company_id;
        private String image_url;
        private String company_name;
        private String recommend_index; // 企业关联度
        private String attention_degree; // 企业关注度
        private List<User> company_users;
        private List<Document> documents;
        private List<Product> products;

        public List<User> getCompany_users() {
            return company_users;
        }

        public void setCompany_users(List<User> company_users) {
            this.company_users = company_users;
        }

        public String getAttention_degree() {
            return attention_degree;
        }

        public void setAttention_degree(String attention_degree) {
            this.attention_degree = attention_degree;
        }

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

        public List<Document> getDocuments() {
            return documents;
        }

        public void setDocuments(List<Document> documents) {
            this.documents = documents;
        }

        public List<Product> getProducts() {
            return products;
        }

        public void setProducts(List<Product> products) {
            this.products = products;
        }

        public class User implements Serializable{
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

        public class Document implements Serializable{
            private String document_name;
            private String document_id;
            private String d_image_url;

            public String getDocument_name() {
                return document_name;
            }

            public void setDocument_name(String document_name) {
                this.document_name = document_name;
            }

            public String getDocument_id() {
                return document_id;
            }

            public void setDocument_id(String document_id) {
                this.document_id = document_id;
            }

            public String getD_image_url() {
                return d_image_url;
            }

            public void setD_image_url(String d_image_url) {
                this.d_image_url = d_image_url;
            }
        }

        public class Product implements Serializable{
            private String product_id;
            private String p_image_url;
            private String product_name;

            public String getProduct_id() {
                return product_id;
            }

            public void setProduct_id(String product_id) {
                this.product_id = product_id;
            }

            public String getP_image_url() {
                return p_image_url;
            }

            public void setP_image_url(String p_image_url) {
                this.p_image_url = p_image_url;
            }

            public String getProduct_name() {
                return product_name;
            }

            public void setProduct_name(String product_name) {
                this.product_name = product_name;
            }
        }
    }
}
