package com.aaron.aaronlibrary.bean;

public class BaseBean {

    private int code;

    private String msg = "";

    public int getStatusCode() {
        return code;
    }

    public void setStatusCode(int statusCode) {
        this.code = statusCode;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String message) {
        this.msg = message;
    }
}
