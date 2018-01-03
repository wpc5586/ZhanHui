package com.xhy.zhanhui.http.domain;

import android.text.TextUtils;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 资料中心Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class CenterBean extends BaseBean implements Serializable{

    private List<Obj> data;

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    public class Obj implements Serializable{
        private String group_name;
        private List<Item> group_items;

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public List<Item> getGroup_items() {
            return group_items;
        }

        public void setGroup_items(List<Item> group_items) {
            this.group_items = group_items;
        }

        public class Item implements Serializable {
            private String company_name;
            private String document_name;
            private String favorite_time;
            private String document_id;
            private String document_icon;
            private String is_collected;
            private String push_time;

            public String getIs_collected() {
                return is_collected;
            }

            public void setIs_collected(String is_collected) {
                this.is_collected = is_collected;
            }

            public String getPush_time() {
                return push_time;
            }

            public void setPush_time(String push_time) {
                this.push_time = push_time;
            }

            public String getCompany_name() {
                return company_name;
            }

            public void setCompany_name(String company_name) {
                this.company_name = company_name;
            }

            public String getDocument_name() {
                return document_name;
            }

            public void setDocument_name(String document_name) {
                this.document_name = document_name;
            }

            public String getFavorite_time() {
                return favorite_time;
            }

            public void setFavorite_time(String favorite_time) {
                this.favorite_time = favorite_time;
            }

            public String getDocument_id() {
                return document_id;
            }

            public void setDocument_id(String document_id) {
                this.document_id = document_id;
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
