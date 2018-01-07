package com.xhy.zhanhui.preferences;

import android.text.TextUtils;

import com.aaron.aaronlibrary.base.utils.BaseSharedPreferences;
import com.xhy.zhanhui.activity.MainActivity;

/**
 * 发出信任SharedPreferences
 * Created by Aaron on 2017/12/14 0028.
 */
public class TrustSharedPreferences extends BaseSharedPreferences {

    public static final String TRUST_DATA = "trustData";// 用户成功登录的信息

    public static TrustSharedPreferences instance = null;

    public static TrustSharedPreferences getInstance() {
        if (instance == null)
            instance = new TrustSharedPreferences();

        return instance;
    }

    @Override
    public String getFilename() {
        return USER_INFO + "_" + getUserId();
    }

    /**
     * 缓存本地最后一条发出的信任申请内容
     * @param content
     */
    public void setTrustData(String content) {
        set(TRUST_DATA, content);
        if (!TextUtils.isEmpty(content))
            MainActivity.getInstance().refreshMainMessage();
    }

    public String getTrustData() {
        return get(TRUST_DATA);
    }

    public void clean() {
        setTrustData("");
    }
}
