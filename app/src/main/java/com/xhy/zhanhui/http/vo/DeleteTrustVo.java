package com.xhy.zhanhui.http.vo;

/**
 * 删除邀请申请类
 * Created by Aaron on 14/12/2017.
 */

public class DeleteTrustVo {
    private String request_id;
    private String accept_id;

    public DeleteTrustVo(String request_id, String accept_id) {
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
