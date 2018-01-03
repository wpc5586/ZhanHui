package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

/**
 * 侧滑-个人信息Bean
 * Created by Aaron on 2017/11/1 0001.
 */
public class UserInfoBean extends BaseBean {

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj {
        private String icon;
        private String v_idCard;
        private String v_telephone;
        private String v_address;
        private String v_website;
        private String v_email;
        private String v_title;
        private String v_company;
        private String vcard_id;
        private String v_name;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getV_idCard() {
            return v_idCard;
        }

        public void setV_idCard(String v_idCard) {
            this.v_idCard = v_idCard;
        }

        public String getV_telephone() {
            return v_telephone;
        }

        public void setV_telephone(String v_telephone) {
            this.v_telephone = v_telephone;
        }

        public String getV_address() {
            return v_address;
        }

        public void setV_address(String v_address) {
            this.v_address = v_address;
        }

        public String getV_website() {
            return v_website;
        }

        public void setV_website(String v_website) {
            this.v_website = v_website;
        }

        public String getV_email() {
            return v_email;
        }

        public void setV_email(String v_email) {
            this.v_email = v_email;
        }

        public String getV_title() {
            return v_title;
        }

        public void setV_title(String v_title) {
            this.v_title = v_title;
        }

        public String getV_company() {
            return v_company;
        }

        public void setV_company(String v_company) {
            this.v_company = v_company;
        }

        public String getVcard_id() {
            return vcard_id;
        }

        public void setVcard_id(String vcard_id) {
            this.vcard_id = vcard_id;
        }

        public String getV_name() {
            return v_name;
        }

        public void setV_name(String v_name) {
            this.v_name = v_name;
        }
    }
}
