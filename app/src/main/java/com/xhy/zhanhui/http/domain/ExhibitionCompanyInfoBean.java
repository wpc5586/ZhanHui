package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

/**
 * 展会-企业信息Bean
 * Created by Aaron on 2017/11/1 0001.
 */
public class ExhibitionCompanyInfoBean extends BaseBean {

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj {
        private String booth_no;
        private String company_id;
        private String video_url;
        private String image_url;
        private String company_name;
        private String pdt_nums;
        private String focus;
        private String company_introduction;
        private String doc_nums;

        public String getBooth_no() {
            return booth_no;
        }

        public void setBooth_no(String booth_no) {
            this.booth_no = booth_no;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
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

        public String getPdt_nums() {
            return pdt_nums;
        }

        public void setPdt_nums(String pdt_nums) {
            this.pdt_nums = pdt_nums;
        }

        public String getFocus() {
            return focus;
        }

        public void setFocus(String focus) {
            this.focus = focus;
        }

        public String getCompany_introduction() {
            return company_introduction;
        }

        public void setCompany_introduction(String company_introduction) {
            this.company_introduction = company_introduction;
        }

        public String getDoc_nums() {
            return doc_nums;
        }

        public void setDoc_nums(String doc_nums) {
            this.doc_nums = doc_nums;
        }
    }
}
