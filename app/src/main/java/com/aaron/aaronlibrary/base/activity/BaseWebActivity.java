package com.aaron.aaronlibrary.base.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.aaron.aaronlibrary.utils.ToastUtil;
import com.aaron.aaronlibrary.web.X5WebView;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.tencent.smtt.utils.TbsLog;
import com.xhy.zhanhui.R;

/**
 * H5Activity基类
 * Created by wpc on 2016/11/21 0021.
 */
public class BaseWebActivity extends BaseActivity {

//    protected WebView webView;
    protected X5WebView webView;

    protected ProgressBar progressBar;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_base_web;
    }

    @Override
    protected void findView() {
        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressbar);
    }

    @Override
    protected void init() {
        super.init();
        PublicMethod.setWebView(mContext, webView, progressBar);
        loadUrl();
        CookieSyncManager.createInstance(this);
        CookieSyncManager.getInstance().sync();
    }

    /**
     * 加载url
     */
    protected void loadUrl() {
        String url = getUrl();
        webView.loadUrl(url);
    }

    protected String getUrl() {
        return "http://www.baidu.com";
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
