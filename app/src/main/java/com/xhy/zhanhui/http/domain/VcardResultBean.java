package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

/**
 * 第一次编辑个人信息返回Bean
 * Created by Aaron on 2017/12/10 0001.
 */
public class VcardResultBean extends BaseBean {

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj {
        private String vcard_id;

        public String getVcard_id() {
            return vcard_id;
        }

        public void setVcard_id(String vcard_id) {
            this.vcard_id = vcard_id;
        }
    }
}
