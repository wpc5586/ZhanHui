package com.xhy.zhanhui.http.vo;

/**
 * 线下申请信任类
 * Created by Aaron on 14/12/2017.
 */

public class OfflineTrustVo {
    private String request_id;
    private String accept_id;
    private String request_hx_username;
    private String accept_hx_username;

    public OfflineTrustVo(String request_id, String accept_id, String request_hx_username, String accept_hx_username) {
        this.request_id = request_id;
        this.accept_id = accept_id;
        this.request_hx_username = request_hx_username;
        this.accept_hx_username = accept_hx_username;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getAccept_id() {
        return accept_id;
    }

    public void setAccept_id(String accept_id) {
        this.accept_id = accept_id;
    }

    public String getRequest_hx_username() {
        return request_hx_username;
    }

    public void setRequest_hx_username(String request_hx_username) {
        this.request_hx_username = request_hx_username;
    }

    public String getAccept_hx_username() {
        return accept_hx_username;
    }

    public void setAccept_hx_username(String accept_hx_username) {
        this.accept_hx_username = accept_hx_username;
    }
}
