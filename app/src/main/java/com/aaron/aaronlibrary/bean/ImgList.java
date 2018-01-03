package com.aaron.aaronlibrary.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * 图片数据
 * @author wangpc
 *
 */
public class ImgList implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -2674583185000082501L;

    private String urlSmall;
    
    private String fsize;
    
    private String urlNormal;
    
    private String params;
    
    private String fheight;
    
    private String fwidth;
    
    private String ftype;
    
    private String id;

    public ImgList() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlSmall() {
        return urlSmall;
    }

    public void setUrlSmall(String urlSmall) {
        this.urlSmall = urlSmall;
    }

    public String getFsize() {
        return fsize;
    }

    public void setFsize(String fsize) {
        this.fsize = fsize;
    }

    public String getUrlNormal() {
        return urlNormal;
    }

    public void setUrlNormal(String urlNormal) {
        this.urlNormal = urlNormal;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
    
    public int getFheightEx() {
        return Integer.parseInt(getFheight());
    }

    public String getFheight() {
        return TextUtils.isEmpty(fheight) ? "0" : fheight;
    }

    public void setFheight(String fheight) {
        this.fheight = fheight;
    }
    
    public int getFwidthEx() {
        return Integer.parseInt(getFwidth());
    }

    public String getFwidth() {
        return TextUtils.isEmpty(fwidth) ? "0" : fwidth;
    }

    public void setFwidth(String fwidth) {
        this.fwidth = fwidth;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }
    
}
