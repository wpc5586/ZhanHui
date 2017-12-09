package com.aaron.aaronlibrary.http;

import com.aaron.aaronlibrary.utils.AppInfo;

/**
 * 服务Url
 * Created by wpc on 2016/12/1 0001.
 */
public class ServerUrl {

    /**
     * 服务器地址
     */
    public static final String SERVICE = "http://192.168.0.112:8080/mobile_jf/";
//    public static final String SERVICES = "http://47.93.28.100:8080/aaron_world/"; // 外网
    public static final String SERVICES = "http://192.168.1.105:8080/"; // 本地

    /**
     * JS调原生 类名
     */
    public static final String JS_CLASS_NAME = "AARONWORLD";

    /**
     * 判断系统版本是否大于等于7.0，7.0以上使用https
     * @return true 需要Https false 不需要
     */
    public static boolean isUseHttps() {
        return AppInfo.isNeedHttps();
    }

    public static String getService() {
        return isUseHttps() ? SERVICES : SERVICE;
    }

    /**
     * 版本信息接口
     * @return 数据最新日期
     */
    public static String getVersion() {
        return getService() + "aaron/getVersion.do";
    }

    /**
     * 登录接口
     * @return 数据最新日期
     */
    public static String login() {
        return getService() + "aaron/login.do";
    }

    /**
     * 注册接口
     * @return 数据最新日期
     */
    public static String regist() {
        return getService() + "aaron/regist.do";
    }

    /**
     * 主界面内容接口
     * @return 数据最新日期
     */
    public static String getMainContent() {
        return getService() + "aaron/getMainContent.do";
    }
}

