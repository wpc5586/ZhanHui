package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.io.Serializable;
import java.util.List;

/**
 * 资料详情Bean
 * Created by Aaron on 2017/12/14 0001.
 */
public class CenterDetailBean extends BaseBean implements Serializable {

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj implements Serializable {
        private String flag;
        private String file_size;
        private String image_url;
        private String document_name;
        private String belongs_id;
        private String belongs_name;
        private String document_id;
        private String document_time;
        private String file_url;
        private String is_fav;

        public String getIs_fav() {
            return is_fav;
        }

        public void setIs_fav(String is_fav) {
            this.is_fav = is_fav;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getFile_size() {
            return file_size;
        }

        public void setFile_size(String file_size) {
            this.file_size = file_size;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getDocument_name() {
            return document_name;
        }

        public void setDocument_name(String document_name) {
            this.document_name = document_name;
        }

        public String getBelongs_id() {
            return belongs_id;
        }

        public void setBelongs_id(String belongs_id) {
            this.belongs_id = belongs_id;
        }

        public String getBelongs_name() {
            return belongs_name;
        }

        public void setBelongs_name(String belongs_name) {
            this.belongs_name = belongs_name;
        }

        public String getDocument_id() {
            return document_id;
        }

        public void setDocument_id(String document_id) {
            this.document_id = document_id;
        }

        public String getDocument_time() {
            return document_time;
        }

        public void setDocument_time(String document_time) {
            this.document_time = document_time;
        }

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }
    }
}
