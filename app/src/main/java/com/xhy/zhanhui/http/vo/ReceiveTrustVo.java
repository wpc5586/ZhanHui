package com.xhy.zhanhui.http.vo;

/**
 * 接受信任邀请类
 * Created by Aaron on 14/12/2017.
 */

public class ReceiveTrustVo {
    private String request_id;
    private String accept_id;

    public ReceiveTrustVo(String request_id, String accept_id) {
        this.request_id = request_id;
        this.accept_id = accept_id;
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
}
