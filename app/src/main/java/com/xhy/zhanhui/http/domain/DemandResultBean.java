package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 首页-智能商务-需求详情结果Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class DemandResultBean extends BaseBean implements Serializable{

    private List<Obj> data;

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String user_handle_state;
        private List<Matching> matchings;

        public String getUser_handle_state() {
            return user_handle_state;
        }

        public void setUser_handle_state(String user_handle_state) {
            this.user_handle_state = user_handle_state;
        }

        public List<Matching> getMatchings() {
            return matchings;
        }

        public void setMatchings(List<Matching> matchings) {
            this.matchings = matchings;
        }

        public class Matching {
            private String is_friend;
            private String company_name;
            private String matching_id;
            private String matching_time;
            private String company_icon;
            private String matched_degree;
            private List<User> company_users;

            public List<User> getCompany_users() {
                return company_users;
            }

            public void setCompany_users(List<User> company_users) {
                this.company_users = company_users;
            }

            public String getMatched_degree() {
                return matched_degree;
            }

            public void setMatched_degree(String matched_degree) {
                this.matched_degree = matched_degree;
            }

            public String getIs_friend() {
                return is_friend;
            }

            public void setIs_friend(String is_friend) {
                this.is_friend = is_friend;
            }

            public String getCompany_name() {
                return company_name;
            }

            public void setCompany_name(String company_name) {
                this.company_name = company_name;
            }

            public String getMatching_id() {
                return matching_id;
            }

            public void setMatching_id(String matching_id) {
                this.matching_id = matching_id;
            }

            public String getMatching_time() {
                return matching_time;
            }

            public void setMatching_time(String matching_time) {
                this.matching_time = matching_time;
            }

            public String getCompany_icon() {
                return company_icon;
            }

            public void setCompany_icon(String company_icon) {
                this.company_icon = company_icon;
            }

            public class User implements Serializable{
                private String hx_username;
                private String nickname;
                private String user_id;
                private String user_icon;
                private String v_title; // 职务

                public String getUser_icon() {
                    return user_icon;
                }

                public void setUser_icon(String user_icon) {
                    this.user_icon = user_icon;
                }

                public String getHx_username() {
                    return hx_username;
                }

                public void setHx_username(String hx_username) {
                    this.hx_username = hx_username;
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
}
