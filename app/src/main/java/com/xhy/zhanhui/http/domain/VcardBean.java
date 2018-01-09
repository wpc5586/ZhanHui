package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 名片Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class VcardBean extends BaseBean implements Serializable{

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String hx_username;
        private String v_address;
        private String v_website;
        private String vcard_no;
        private String timestp;
        private String v_fax;
        private String v_company;
        private String v_title;
        private String v_email;
        private String v_name;
        private String v_mobile;

        // 企业版字段
        private String company_name_en;
        private String company_email;
        private String company_name;
        private String company_adress;
        private String company_website;
        private String company_tel;
        private String company_scale;

        public String getCompany_name_en() {
            return company_name_en;
        }

        public void setCompany_name_en(String company_name_en) {
            this.company_name_en = company_name_en;
        }

        public String getCompany_email() {
            return company_email;
        }

        public void setCompany_email(String company_email) {
            this.company_email = company_email;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getCompany_adress() {
            return company_adress;
        }

        public void setCompany_adress(String company_adress) {
            this.company_adress = company_adress;
        }

        public String getCompany_website() {
            return company_website;
        }

        public void setCompany_website(String company_website) {
            this.company_website = company_website;
        }

        public String getCompany_tel() {
            return company_tel;
        }

        public void setCompany_tel(String company_tel) {
            this.company_tel = company_tel;
        }

        public String getCompany_scale() {
            return company_scale;
        }

        public void setCompany_scale(String company_scale) {
            this.company_scale = company_scale;
        }

        public String getHx_username() {
            return hx_username;
        }

        public void setHx_username(String hx_username) {
            this.hx_username = hx_username;
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

        public String getVcard_no() {
            return vcard_no;
        }

        public void setVcard_no(String vcard_no) {
            this.vcard_no = vcard_no;
        }

        public String getTimestp() {
            return timestp;
        }

        public void setTimestp(String timestp) {
            this.timestp = timestp;
        }

        public String getV_fax() {
            return v_fax;
        }

        public void setV_fax(String v_fax) {
            this.v_fax = v_fax;
        }

        public String getV_company() {
            return v_company;
        }

        public void setV_company(String v_company) {
            this.v_company = v_company;
        }

        public String getV_title() {
            return v_title;
        }

        public void setV_title(String v_title) {
            this.v_title = v_title;
        }

        public String getV_email() {
            return v_email;
        }

        public void setV_email(String v_email) {
            this.v_email = v_email;
        }

        public String getV_name() {
            return v_name;
        }

        public void setV_name(String v_name) {
            this.v_name = v_name;
        }

        public String getV_mobile() {
            return v_mobile;
        }

        public void setV_mobile(String v_mobile) {
            this.v_mobile = v_mobile;
        }
    }
}
