package com.xhy.zhanhui.http.vo;

/**
 * 接受信任邀请类
 * Created by Aaron on 14/12/2017.
 */

public class ReceiveTrustVo {
    private String request_hx_id;
    private String accept_hx_id;

    public ReceiveTrustVo(String request_hx_id, String accept_hx_id) {
        this.request_hx_id = request_hx_id;
        this.accept_hx_id = accept_hx_id;
    }

    public String getRequest_hx_id() {
        return request_hx_id;
    }

    public void setRequest_hx_id(String request_hx_id) {
        this.request_hx_id = request_hx_id;
    }

    public String getAccept_hx_id() {
        return accept_hx_id;
    }

    public void setAccept_hx_id(String accept_hx_id) {
        this.accept_hx_id = accept_hx_id;
    }
}
