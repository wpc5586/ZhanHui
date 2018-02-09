package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.util.List;

/**
 * 首页-需求匹配详情Bean
 * Created by Aaron on 2017/11/1 0001.
 */
public class DemandDetailBean extends BaseBean {

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj {
        private String demand_id;
        private String demand_title;
        private String category_name;
        private String demand_nums;
        private String demand_brand;
        private String demand_notes;
        private String post_time;
        private List<Image> image_url;

        public String getDemand_id() {
            return demand_id;
        }

        public void setDemand_id(String demand_id) {
            this.demand_id = demand_id;
        }

        public String getDemand_title() {
            return demand_title;
        }

        public void setDemand_title(String demand_title) {
            this.demand_title = demand_title;
        }

        public String getCategory_name() {
            return category_name;
        }

        public void setCategory_name(String category_name) {
            this.category_name = category_name;
        }

        public String getDemand_nums() {
            return demand_nums;
        }

        public void setDemand_nums(String demand_nums) {
            this.demand_nums = demand_nums;
        }

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

        public String getPost_time() {
            return post_time;
        }

        public void setPost_time(String post_time) {
            this.post_time = post_time;
        }

        public List<Image> getImage_url() {
            return image_url;
        }

        public void setImage_url(List<Image> image_url) {
            this.image_url = image_url;
        }

        public class Image {
            private String image_url;

            public String getImage_url() {
                return image_url;
            }

            public void setImage_url(String image_url) {
                this.image_url = image_url;
            }
        }
    }
}
