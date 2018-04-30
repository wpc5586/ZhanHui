package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 智能商务客户需求详情Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class IBusinessUserBean extends BaseBean implements Serializable{

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String matching_id;
        private List<String> demand_images;
        private User user;
        private Demand demand;
        private Relation relation;

        public String getMatching_id() {
            return matching_id;
        }

        public void setMatching_id(String matching_id) {
            this.matching_id = matching_id;
        }

        public List<String> getDemand_images() {
            return demand_images;
        }

        public void setDemand_images(List<String> demand_images) {
            this.demand_images = demand_images;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public Demand getDemand() {
            return demand;
        }

        public void setDemand(Demand demand) {
            this.demand = demand;
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
            private String v_company;
            private String v_title; // 职务

            public String getV_company() {
                return v_company;
            }

            public void setV_company(String v_company) {
                this.v_company = v_company;
            }

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

        public class Demand implements Serializable{
            private String demand_brand;
            private String demand_notes;
            private String category_name;
            private String demand_id;
            private String demand_nums;
            private String demand_title;
            private String post_time;

            public String getDemand_brand() {
                return demand_brand;
            }

            public void setDemand_brand(String demand_brand) {
                this.demand_brand = demand_brand;
            }

            public String getDemand_notes() {
                return demand_notes;
            }

            public void setDemand_notes(String demand_notes) {
                this.demand_notes = demand_notes;
            }

            public String getCategory_name() {
                return category_name;
            }

            public void setCategory_name(String category_name) {
                this.category_name = category_name;
            }

            public String getDemand_id() {
                return demand_id;
            }

            public void setDemand_id(String demand_id) {
                this.demand_id = demand_id;
            }

            public String getDemand_nums() {
                return demand_nums;
            }

            public void setDemand_nums(String demand_nums) {
                this.demand_nums = demand_nums;
            }

            public String getDemand_title() {
                return demand_title;
            }

            public void setDemand_title(String demand_title) {
                this.demand_title = demand_title;
            }

            public String getPost_time() {
                return post_time;
            }

            public void setPost_time(String post_time) {
                this.post_time = post_time;
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
