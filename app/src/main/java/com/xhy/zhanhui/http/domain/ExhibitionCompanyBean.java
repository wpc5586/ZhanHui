package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.util.List;

/**
 * 展会-企业Bean
 * Created by Aaron on 2017/11/1 0001.
 */
public class ExhibitionCompanyBean extends BaseBean {

    private List<ExhibitionCompanyBean.Obj> data;

    public List<ExhibitionCompanyBean.Obj> getData() {
        return data;
    }

    public void setData(List<ExhibitionCompanyBean.Obj> data) {
        this.data = data;
    }

    public class Obj {
        private String hall_name;
        private String area_name;
        private String hall_id;
        private List<Company> companys;

        public List<Company> getCompanys() {
            return companys;
        }

        public void setCompanys(List<Company> companys) {
            this.companys = companys;
        }

        public String getHall_name() {
            return hall_name;
        }

        public void setHall_name(String hall_name) {
            this.hall_name = hall_name;
        }

        public String getArea_name() {
            return area_name;
        }

        public void setArea_name(String area_name) {
            this.area_name = area_name;
        }

        public String getHall_id() {
            return hall_id;
        }

        public void setHall_id(String hall_id) {
            this.hall_id = hall_id;
        }

        public class Company {
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
}
