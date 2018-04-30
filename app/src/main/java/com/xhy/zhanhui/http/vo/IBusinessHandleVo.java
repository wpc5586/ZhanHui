package com.xhy.zhanhui.http.vo;

/**
 * 处理匹配结果类
 * Created by Aaron on 14/12/2017.
 */

public class IBusinessHandleVo {
    private String type;
    private String company_message;
    private int company_handle_state;

    public IBusinessHandleVo(String type, String company_message, int company_handle_state) {
        this.type = type;
        this.company_message = company_message;
        this.company_handle_state = company_handle_state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCompany_message() {
        return company_message;
    }

    public void setCompany_message(String company_message) {
        this.company_message = company_message;
    }

    public int getCompany_handle_state() {
        return company_handle_state;
    }

    public void setCompany_handle_state(int company_handle_state) {
        this.company_handle_state = company_handle_state;
    }
}
