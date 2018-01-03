package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

/**
 * 邀请详情信息Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class TrustDetailBean extends BaseBean {

    private Obj data;

    public Obj getObj() {
        return data;
    }

    public void setObj(Obj obj) {
        this.data = data;
    }

    public class Obj {
        private String hx_username;
        private String icon;
        private String flag;
        private String request_id;
        private String accept_id;
        private String nickname;
        private String operation_time;
        private String state;
        private String v_title;
        private String request_time;

        public String getAccept_id() {
            return accept_id;
        }

        public void setAccept_id(String accept_id) {
            this.accept_id = accept_id;
        }

        public String getHx_username() {
            return hx_username;
        }

        public void setHx_username(String hx_username) {
            this.hx_username = hx_username;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getRequest_id() {
            return request_id;
        }

        public void setRequest_id(String request_id) {
            this.request_id = request_id;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getOperation_time() {
            return operation_time;
        }

        public void setOperation_time(String operation_time) {
            this.operation_time = operation_time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getV_title() {
            return v_title;
        }

        public void setV_title(String v_title) {
            this.v_title = v_title;
        }

        public String getRequest_time() {
            return request_time;
        }

        public void setRequest_time(String request_time) {
            this.request_time = request_time;
        }
    }
}
