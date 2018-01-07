package com.aaron.aaronlibrary.web;

import android.app.Activity;
import android.content.Context;

import com.aaron.aaronlibrary.utils.ToastUtil;
import com.tencent.smtt.sdk.WebView;
import com.xhy.zhanhui.domain.StartActivityUtils;

/**
 * MyWebViewClient
 * Created by wpc on 2016/11/21 0021.
 */
public class MyX5WebViewClient extends com.tencent.smtt.sdk.WebViewClient {

    private Context mContext;

    public MyX5WebViewClient(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//        if( url.startsWith("http:") || url.startsWith("https:") ) {
//            return false;
//        }
        if (mContext != null && mContext instanceof Activity) {
            try {
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                mContext.startActivity(intent);
                if(!url.startsWith("http") && url.contains("http"))
                    url = "http" + url.split("http")[1];
                StartActivityUtils.startWebUrl(mContext, url, "网页");
                return true;
            } catch (Exception e) {
                ToastUtil.show(mContext, "异常问题");
                e.printStackTrace();
            }
            //  下面这一行保留的时候，原网页仍报错，新网页正常.所以注释掉后，也就没问了
            //  view.loadUrl(url);
        }
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onReceivedSslError(WebView webView, com.tencent.smtt.export.external.interfaces.SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
        sslErrorHandler.proceed();
    }
}