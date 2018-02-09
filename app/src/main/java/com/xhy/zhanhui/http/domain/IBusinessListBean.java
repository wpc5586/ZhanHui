package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 首页-我的需求列表Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class IBusinessListBean extends BaseBean implements Serializable{

    private List<Obj> data;

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String demand_id;
        private String matching_nums;
        private String matching_accept_nums;
        private String matching_active_nums;
        private String matching_success_nums;
        private String demand_title;
        private String post_time;

        public String getDemand_id() {
            return demand_id;
        }

        public void setDemand_id(String demand_id) {
            this.demand_id = demand_id;
        }

        public String getMatching_nums() {
            return matching_nums;
        }

        public void setMatching_nums(String matching_nums) {
            this.matching_nums = matching_nums;
        }

        public String getMatching_accept_nums() {
            return matching_accept_nums;
        }

        public void setMatching_accept_nums(String matching_accept_nums) {
            this.matching_accept_nums = matching_accept_nums;
        }

        public String getMatching_active_nums() {
            return matching_active_nums;
        }

        public void setMatching_active_nums(String matching_active_nums) {
            this.matching_active_nums = matching_active_nums;
        }

        public String getMatching_success_nums() {
            return matching_success_nums;
        }

        public void setMatching_success_nums(String matching_success_nums) {
            this.matching_success_nums = matching_success_nums;
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
