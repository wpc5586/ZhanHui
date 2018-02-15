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
        }
    }
}
