package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.easeui.utils.EaseCommonUtils;

import java.util.List;

/**
 * 朋友列表Bean
 * Created by Aaron on 2017/12/10 0001.
 */
public class FriendBean extends BaseBean {

    private List<Obj> data;

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    public class Obj {
        private String hx_username;
        private String icon;
        private String nickname;
        private String user_id;
        private String v_title;

        /**
         * initial letter for nickname
         */
        protected String initialLetter;

        public String getInitialLetter() {
            if(initialLetter == null){
                EaseCommonUtils.setUserInitialLetter(this);
            }
            return initialLetter;
        }

        public void setInitialLetter(String initialLetter) {
            this.initialLetter = initialLetter;
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

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getV_title() {
            return v_title;
        }

        public void setV_title(String v_title) {
            this.v_title = v_title;
        }
    }
}
