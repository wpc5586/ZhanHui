package com.xhy.zhanhui.http.vo;

/**
 * 删除匹配结果类
 * Created by Aaron on 14/12/2017.
 */

public class DemandDeleteVo {
    private String type;

    public DemandDeleteVo(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
