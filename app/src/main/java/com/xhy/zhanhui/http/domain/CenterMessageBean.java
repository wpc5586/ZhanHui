package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 资料消息列表Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class CenterMessageBean extends BaseBean implements Serializable{

    private List<Obj> data;

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String content;
        private String title;
        private String message_id;
        private String sender_name;
        private boolean is_read;
        private String push_time;
        private String sender_icon;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage_id() {
            return message_id;
        }

        public void setMessage_id(String message_id) {
            this.message_id = message_id;
        }

        public String getSender_name() {
            return sender_name;
        }

        public void setSender_name(String sender_name) {
            this.sender_name = sender_name;
        }

        public boolean isIs_read() {
            return is_read;
        }

        public void setIs_read(boolean is_read) {
            this.is_read = is_read;
        }

        public String getPush_time() {
            return push_time;
        }

        public void setPush_time(String push_time) {
            this.push_time = push_time;
        }

        public String getSender_icon() {
            return sender_icon;
        }

        public void setSender_icon(String sender_icon) {
            this.sender_icon = sender_icon;
        }
    }
}
