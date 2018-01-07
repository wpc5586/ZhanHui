package com.xhy.zhanhui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.aaron.aaronlibrary.base.activity.BaseWebActivity;
import com.aaron.aaronlibrary.utils.ToastUtil;

/**
 * 通用可传Url的WebActivity
 * Created by wpc on 2016/11/24 0024.
 */
public class UrlWebActivity extends BaseWebActivity {

    private String url;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void init() {
        Intent intent = getIntent();
        if (intent.hasExtra("url"))
            url = intent.getStringExtra("url");
        if (intent.hasExtra("title"))
            title = intent.getStringExtra("title");
        if (TextUtils.isEmpty(url)) {
            ToastUtil.setErrorToast(this, "url为空，页面已关闭");
            finish();
            return;
        }
        System.out.println("~!~ UrlWebUrl = " + url);
        super.init();
        setActionbarTitle(TextUtils.isEmpty(title) ? url : title);
    }

    @Override
    protected void findView() {
        super.findView();
    }

    /**
     * 重新加载
     */
    private void reload() {
        String url = getUrl();
        webView.loadUrl(url);
    }

    @Override
    protected String getUrl() {
        return url;
    }
}
