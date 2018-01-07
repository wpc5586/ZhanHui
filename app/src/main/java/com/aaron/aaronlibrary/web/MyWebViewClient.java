package com.aaron.aaronlibrary.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * MyWebViewClient
 * Created by wpc on 2016/11/21 0021.
 */
public class MyWebViewClient extends WebViewClient {

    private Context mContext;

    public MyWebViewClient(Context mContext) {
        this.mContext = mContext;
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if( url.startsWith("http:") || url.startsWith("https:") ) {
            return false;
        }
        if (mContext != null && mContext instanceof Activity) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            mContext.startActivity(intent);
            //  下面这一行保留的时候，原网页仍报错，新网页正常.所以注释掉后，也就没问了
            //  view.loadUrl(url);
        }
        return true;
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }
}