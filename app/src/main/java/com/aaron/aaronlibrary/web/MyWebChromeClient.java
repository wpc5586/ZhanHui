package com.aaron.aaronlibrary.web;

/**
 * WebChromeClient
 * Created by wpc on 2016/11/21 0021.
 */

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class MyWebChromeClient extends WebChromeClient {

    private ProgressBar progressBar;

    public MyWebChromeClient(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (progressBar == null) {
            super.onProgressChanged(view, newProgress);
            return;
        }
        if (newProgress == 100) {
            progressBar.setVisibility(View.GONE);
        } else {
            if (progressBar.getVisibility() == View.GONE)
                progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
        }
        super.onProgressChanged(view, newProgress);
    }
}
