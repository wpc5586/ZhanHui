package com.xhy.zhanhui.preferences;

import android.text.TextUtils;

import com.aaron.aaronlibrary.base.utils.BaseSharedPreferences;
import com.xhy.zhanhui.activity.MainActivity;

/**
 * 发出信任SharedPreferences
 * Created by Aaron on 2017/12/14 0028.
 */
public class IBusinessSharedPreferences extends BaseSharedPreferences {

    public static final String IBUSINESS_DATA = "ibusinessData";// 用户发布需求

    public static IBusinessSharedPreferences instance = null;

    public static IBusinessSharedPreferences getInstance() {
        if (instance == null)
            instance = new IBusinessSharedPreferences();

        return instance;
    }

    @Override
    public String getFilename() {
        return USER_INFO + "_" + getUserId();
    }

    /**
     * 缓存本地最后一条发出的需求内容
     * @param content
     */
    public void setIBusinessData(String content) {
        set(IBUSINESS_DATA, content);
        if (!TextUtils.isEmpty(content))
            MainActivity.getInstance().refreshMainMessage();
    }

    public String getIBusinessData() {
        return get(IBUSINESS_DATA);
    }

    public void clean() {
        setIBusinessData("");
    }
}
