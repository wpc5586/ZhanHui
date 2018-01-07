package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 通知详情Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class CenterMsgDetailBean extends BaseBean implements Serializable {

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj implements Serializable {
        private String content;
        private String title;
        private String message_id;
        private String sender_name;
        private String is_read;
        private String push_time;
        private String sender_icon;
        private List<Item> attached;

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

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
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

        public List<Item> getAttached() {
            return attached;
        }

        public void setAttached(List<Item> attached) {
            this.attached = attached;
        }

        public class Item implements Serializable {
            private String document_name;
            private String docment_id;
            private String document_icon;

            public String getDocument_name() {
                return document_name;
            }

            public void setDocument_name(String document_name) {
                this.document_name = document_name;
            }

            public String getDocument_id() {
                return docment_id;
            }

            public void setDocument_id(String document_id) {
                this.docment_id = document_id;
            }

            public String getDocument_icon() {
                return document_icon;
            }

            public void setDocument_icon(String document_icon) {
                this.document_icon = document_icon;
            }
        }
    }
}
