package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.util.List;

/**
 * 展会-产品列表Bean
 * Created by Aaron on 2017/12/10 0001.
 */
public class ExhibitionProductListBean extends BaseBean {

    private List<Obj> data;

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    public class Obj {
        private String industry2_id;
        private String product_id;
        private String product_name;
        private String image_url;

        public String getIndustry2_id() {
            return industry2_id;
        }

        public void setIndustry2_id(String industry2_id) {
            this.industry2_id = industry2_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

    }
}
