package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 企业版-收到的需求Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class IBusinessReceiveBean extends BaseBean implements Serializable{

    private List<Obj> data;

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String is_friend;
        private String user_icon;
        private String nickname;
        private String matching_id;
        private String company_handle_state;
        private String demand_title;
        private String post_time;

        public String getIs_friend() {
            return is_friend;
        }

        public void setIs_friend(String is_friend) {
            this.is_friend = is_friend;
        }

        public String getUser_icon() {
            return user_icon;
        }

        public void setUser_icon(String user_icon) {
            this.user_icon = user_icon;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getMatching_id() {
            return matching_id;
        }

        public void setMatching_id(String matching_id) {
            this.matching_id = matching_id;
        }

        public String getCompany_handle_state() {
            return company_handle_state;
        }

        public void setCompany_handle_state(String company_handle_state) {
            this.company_handle_state = company_handle_state;
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
}
