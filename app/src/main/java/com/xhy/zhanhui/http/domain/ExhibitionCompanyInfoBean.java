package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.util.List;

/**
 * 展会-企业信息Bean
 * Created by Aaron on 2017/11/1 0001.
 */
public class ExhibitionCompanyInfoBean extends BaseBean {

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj {
        private String booth_no;
        private String company_id;
        private String video_url;
        private String image_url;
        private String company_name;
        private String pdt_nums;
        private String focus;
        private String company_introduction;
        private String doc_nums;
        private String event_id;
        private String event_name;
        private String is_online_focus;
        private List<User> company_users;

        public String getIs_online_focus() {
            return is_online_focus;
        }

        public void setIs_online_focus(String is_online_focus) {
            this.is_online_focus = is_online_focus;
        }

        public List<User> getCompany_users() {
            return company_users;
        }

        public void setCompany_users(List<User> company_users) {
            this.company_users = company_users;
        }

        public String getEvent_id() {
            return event_id;
        }

        public void setEvent_id(String event_id) {
            this.event_id = event_id;
        }

        public String getEvent_name() {
            return event_name;
        }

        public void setEvent_name(String event_name) {
            this.event_name = event_name;
        }

        public String getBooth_no() {
            return booth_no;
        }

        public void setBooth_no(String booth_no) {
            this.booth_no = booth_no;
        }

        public String getCompany_id() {
            return company_id;
        }

        public void setCompany_id(String company_id) {
            this.company_id = company_id;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getPdt_nums() {
            return pdt_nums;
        }

        public void setPdt_nums(String pdt_nums) {
            this.pdt_nums = pdt_nums;
        }

        public String getFocus() {
            return focus;
        }

        public void setFocus(String focus) {
            this.focus = focus;
        }

        public String getCompany_introduction() {
            return company_introduction;
        }

        public void setCompany_introduction(String company_introduction) {
            this.company_introduction = company_introduction;
        }

        public String getDoc_nums() {
            return doc_nums;
        }

        public void setDoc_nums(String doc_nums) {
            this.doc_nums = doc_nums;
        }

        public class User {
            private String hx_username;
            private String icon;
            private String nickname;
            private String user_id;
            private String v_title;

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
}
