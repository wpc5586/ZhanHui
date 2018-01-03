package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.ServerUrl;

/**
 * 版本信息Bean
 * Created by Aaron on 2017/11/1 0001.
 */
public class VersionBean extends BaseBean {

    private Obj obj;

    public Obj getObj() {
        return obj;
    }

    public void setObj(Obj obj) {
        this.obj = obj;
    }

    public class Obj {
        private String iPhoneVersion;
        private String androidVersion;
        private String iPhoneUrl;
        private String androidUrl;
        private String iPhoneContent;
        private String androidContent;

        public String getiPhoneVersion() {
            return iPhoneVersion;
        }

        public void setiPhoneVersion(String iPhoneVersion) {
            this.iPhoneVersion = iPhoneVersion;
        }

        public String getAndroidVersion() {
            return androidVersion;
        }

        public void setAndroidVersion(String androidVersion) {
            this.androidVersion = androidVersion;
        }

        public String getiPhoneUrl() {
            return ServerUrl.getService() + iPhoneUrl;
        }

        public void setiPhoneUrl(String iPhoneUrl) {
            this.iPhoneUrl = iPhoneUrl;
        }

        public String getAndroidUrl() {
            return ServerUrl.getService() + androidUrl;
        }

        public void setAndroidUrl(String androidUrl) {
            this.androidUrl = androidUrl;
        }

        public String getAndroidContent() {
            return androidContent;
        }

        public void setAndroidContent(String androidContent) {
            this.androidContent = androidContent;
        }

        public String getiPhoneContent() {
            return iPhoneContent;
        }

        public void setiPhoneContent(String iPhoneContent) {
            this.iPhoneContent = iPhoneContent;
        }
    }
}
