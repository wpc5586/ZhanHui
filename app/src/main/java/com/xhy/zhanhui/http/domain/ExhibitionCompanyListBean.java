package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.util.List;

/**
 * 展会-企业列表Bean
 * Created by Aaron on 2017/11/1 0001.
 */
public class ExhibitionCompanyListBean extends BaseBean {

    private List<ExhibitionCompanyListBean.Obj> data;

    public List<ExhibitionCompanyListBean.Obj> getData() {
        return data;
    }

    public void setData(List<ExhibitionCompanyListBean.Obj> data) {
        this.data = data;
    }

    public class Obj {
        private String company_id;
        private String image_url;
        private String company_name;

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
    }
}
