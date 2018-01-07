package com.aaron.aaronlibrary.web;

/**
 * WebChromeClient
 * Created by wpc on 2016/11/21 0021.
 */

import android.view.View;
import android.widget.ProgressBar;

public class MyX5WebChromeClient extends com.tencent.smtt.sdk.WebChromeClient {

    private ProgressBar progressBar;

    public MyX5WebChromeClient(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void onProgressChanged(com.tencent.smtt.sdk.WebView webView, int newProgress) {
        if (progressBar == null) {
            super.onProgressChanged(webView, newProgress);
            return;
        }
        if (newProgress == 100) {
            progressBar.setVisibility(View.GONE);
        } else {
            if (progressBar.getVisibility() == View.GONE)
                progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
        }
        super.onProgressChanged(webView, newProgress);
    }
}
