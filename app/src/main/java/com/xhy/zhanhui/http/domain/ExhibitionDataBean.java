package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.util.List;

/**
 * 展会资料Bean
 * Created by Aaron on 2017/12/10 0001.
 */
public class ExhibitionDataBean extends BaseBean {

    private List<Obj> data;

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    public class Obj {
        private String image_url;
        private String document_name;
        private String document_id;
        private String file_url;

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

        public String getDocument_id() {
            return document_id;
        }

        public void setDocument_id(String document_id) {
            this.document_id = document_id;
        }

        public String getFile_url() {
            return file_url;
        }

        public void setFile_url(String file_url) {
            this.file_url = file_url;
        }
    }
}
