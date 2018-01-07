package com.xhy.zhanhui.http.vo;

/**
 * 申请信任类
 * Created by Aaron on 14/12/2017.
 */

public class TrustVo {
    private String request_id;
    private String accept_id;
    private String source;

    public TrustVo(String request_id, String accept_id, String source) {
        this.request_id = request_id;
        this.accept_id = accept_id;
        this.source = source;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
