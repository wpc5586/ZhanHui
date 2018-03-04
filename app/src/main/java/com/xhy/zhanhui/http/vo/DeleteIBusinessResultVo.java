package com.xhy.zhanhui.http.vo;

/**
 * 删除需求匹配结果企业类
 * Created by Aaron on 14/12/2017.
 */

public class DeleteIBusinessResultVo {
    private String type;

    public DeleteIBusinessResultVo(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
