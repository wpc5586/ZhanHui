package com.xhy.zhanhui.http.vo;

/**
 * 预约状态更新类
 * Created by Aaron on 14/12/2017.
 */

public class OrderStateVo {
    private String state;

    public OrderStateVo(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
