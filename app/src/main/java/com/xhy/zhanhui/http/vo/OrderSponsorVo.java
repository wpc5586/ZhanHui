package com.xhy.zhanhui.http.vo;

/**
 * 发起预约类
 * Created by Aaron on 14/12/2017.
 */

public class OrderSponsorVo {
    private String user_id;
    private String company_id;
    private String event_id;
    private String reservation_time;
    private String reservation_note;

    public OrderSponsorVo(String user_id, String company_id, String event_id, String reservation_time, String reservation_note) {
        this.user_id = user_id;
        this.company_id = company_id;
        this.event_id = event_id;
        this.reservation_time = reservation_time;
        this.reservation_note = reservation_note;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getReservation_time() {
        return reservation_time;
    }

    public void setReservation_time(String reservation_time) {
        this.reservation_time = reservation_time;
    }

    public String getReservation_note() {
        return reservation_note;
    }

    public void setReservation_note(String reservation_note) {
        this.reservation_note = reservation_note;
    }
}
