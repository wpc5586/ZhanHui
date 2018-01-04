package com.xhy.zhanhui.http.vo;

/**
 * 删除好友类
 * Created by Aaron on 14/12/2017.
 */

public class DeleteFriendVo {
    private String user_id;
    private String delete_id;

    public DeleteFriendVo(String user_id, String delete_id) {
        this.user_id = user_id;
        this.delete_id = delete_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDelete_id() {
        return delete_id;
    }

    public void setDelete_id(String delete_id) {
        this.delete_id = delete_id;
    }
}
