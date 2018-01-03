package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

/**
 * 预约详情Bean
 * Created by Aaron on 2017/11/1 0001.
 */
public class OrderDetailBean extends BaseBean {

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj {
        private String booth_no;
        private String hx_username;
        private String reservation_note;
        private String reservation_no;
        private String company_name;
        private String state;
        private String create_time;
        private String reservation_id;
        private String reservation_time;
        private String event_name;

        public String getBooth_no() {
            return booth_no;
        }

        public void setBooth_no(String booth_no) {
            this.booth_no = booth_no;
        }

        public String getHx_username() {
            return hx_username;
        }

        public void setHx_username(String hx_username) {
            this.hx_username = hx_username;
        }

        public String getReservation_note() {
            return reservation_note;
        }

        public void setReservation_note(String reservation_note) {
            this.reservation_note = reservation_note;
        }

        public String getReservation_no() {
            return reservation_no;
        }

        public void setReservation_no(String reservation_no) {
            this.reservation_no = reservation_no;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getReservation_id() {
            return reservation_id;
        }

        public void setReservation_id(String reservation_id) {
            this.reservation_id = reservation_id;
        }

        public String getReservation_time() {
            return reservation_time;
        }

        public void setReservation_time(String reservation_time) {
            this.reservation_time = reservation_time;
        }

        public String getEvent_name() {
            return event_name;
        }

        public void setEvent_name(String event_name) {
            this.event_name = event_name;
        }
    }
}
