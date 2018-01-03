package com.aaron.aaronlibrary.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.aaron.aaronlibrary.web.MyWebChromeClient;
import com.aaron.aaronlibrary.web.MyWebViewClient;
import com.aaron.aaronlibrary.web.MyX5WebChromeClient;
import com.aaron.aaronlibrary.web.MyX5WebViewClient;
import com.aaron.aaronlibrary.web.X5WebView;
import com.xhy.zhanhui.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

/**
 * 公共方法类
 * Created by wangpc on 2016/8/17.
 */
public class PublicMethod {

    public static final int REQUEST_INDEX_DETAIL = 20;

    /**
     * 设置webView配置
     * @param mContext 上下文
     * @param webView webView
     * @param progressBar progressBar
     */
    public static void setWebView(final Context mContext, WebView webView, ProgressBar progressBar) {
        WebSettings settings = webView.getSettings();
        // 设置WebView支持JS脚本
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 屏幕自适应
        settings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        // 使页面支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);

        webView.requestFocusFromTouch();
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.setWebChromeClient(new MyWebChromeClient(progressBar));
        webView.setWebViewClient(new MyWebViewClient(mContext));
    }

    /**
     * 设置webView配置
     * @param mContext 上下文
     * @param webView webView
     * @param progressBar progressBar
     */
    public static void setWebView(final Context mContext, X5WebView webView, ProgressBar progressBar) {
        com.tencent.smtt.sdk.WebSettings settings = webView.getSettings();
        // 设置WebView支持JS脚本
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        // 屏幕自适应
        settings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
        settings.setLoadWithOverviewMode(true);
        // 使页面支持缩放
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);

        settings.setAllowFileAccess(true);
        settings.setLayoutAlgorithm(com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setSupportMultipleWindows(false);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setGeolocationEnabled(true);
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
        //不显示webView缩放按钮
        settings.setDisplayZoomControls(false);
        settings.setAppCachePath(mContext.getDir("appcache", 0).getPath());
        settings.setDatabasePath(mContext.getDir("databases", 0).getPath());
        settings.setGeolocationDatabasePath(mContext.getDir("geolocation", 0)
                .getPath());
        settings.setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON_DEMAND);

        webView.requestFocusFromTouch();
        webView.setDownloadListener(new com.tencent.smtt.sdk.DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                mContext.startActivity(intent);
            }
        });
        webView.setWebChromeClient(new MyX5WebChromeClient(progressBar));
        webView.setWebViewClient(new MyX5WebViewClient(mContext));
    }

//    /**
//     * 设置WebViewJs调用
//     * @param mContext 上下文
//     * @param webView webView
//     */
//    public static MyJavaScriptInterface setWebViewJs(Context mContext, WebView webView, View view, String cityId, String cityName, String endDate) {
//        Logger.d("setWebViewJs", "cityId = " + cityId + ", endDate = " + endDate);
//        MyJavaScriptInterface scriptInterface = new MyJavaScriptInterface(mContext, webView, view, cityId, cityName, endDate);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
//            webView.addJavascriptInterface(scriptInterface, ServerUrl.JS_CLASS_NAME);
//        return scriptInterface;
//    }
//
//    /**
//     * 设置WebViewJs调用
//     * @param mContext 上下文
//     * @param webView webView
//     */
//    public static MyX5JavaScriptInterface setWebViewJs(Context mContext, com.tencent.smtt.sdk.WebView webView, View view, String cityId, String cityName, String endDate) {
//        Logger.d("setWebViewJs", "cityId = " + cityId + ", endDate = " + endDate);
//        MyX5JavaScriptInterface scriptInterface = new MyX5JavaScriptInterface(mContext, webView, view, cityId, cityName, endDate);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
//            webView.addJavascriptInterface(scriptInterface, ServerUrl.JS_CLASS_NAME);
//        return scriptInterface;
//    }

//    /**
//     * 自定义JavascriptInterface，只要这个类里面的方法带有@JavaScriptInterface注解，才可以被js调用
//     */
//    public static class MyJavaScriptInterface implements View.OnClickListener{
//
//        private Context mContext;
//        private WebView webView;
//        private View view;
//        private String cityId, cityName, endDate;
//        private WarnPopu warnPopu;
//        private OnIndexDetailListener onIndexDetailListener;
//
//        /**
//         * 用于记录异步请求是否结束
//         */
//        private boolean isFinish;
//
//        /**
//         * 用于记录异步请求的结果
//         */
//        private boolean result = true;
//
//        public MyJavaScriptInterface(Context context, WebView webView, View view, String cityId, String cityName, String endDate){
//            this.mContext = context;
//            this.webView = webView;
//            this.view = view;
//            this.cityId = cityId;
//            this.cityName = cityName;
//            this.endDate = endDate;
//            init();
//        }
//
//        private void init() {
//            warnPopu = new WarnPopu(mContext);
//            warnPopu.setOkListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            WarningSettingBean.Obj data = (WarningSettingBean.Obj) v.getTag();
//            webView.loadUrl("javascript:isWarning(" + data.getTargetId() + ")");
//            MainActivity.getInstance().refreshMainCollect();
//            warnPopu.dismiss();
//        }
//
//        /**
//         * 显示提示信息
//         * @param message 提示内容
//         * @return true：成功  false：失败
//         */
//        @JavascriptInterface
//        public boolean showMessage(String message, String type){
//            System.out.println("~!~ showMessage " + message + ", " + type);
//            if ("undefined".equals(message))
//                return true;
//            if ("startFeed".equals(message)) {
//                return startFeed();
//            }
//            try {
//                if ("1".equals(type))
//                    ToastUtil.setOKToast(mContext, message);
//                else
//                    ToastUtil.setErrorToast(mContext, message);
//                return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        /**
//         * 收藏指标
//         * @param targetId 指标id
//         * @return true：成功  false：失败
//         */
//        @JavascriptInterface
//        public boolean collect(String targetId, String targetName){
//            System.out.println("~!~ collect " + targetId);
//            isFinish = false;
//            try {
//                BaseMap params = new BaseMap();
//                params.put("collectId", targetId);
//                params.put("collectName", targetName);
//                String[] toastContent = {"", ""};
//                PostCall.post(mContext, ServerUrl.attentionCollection(), params, new PostCall.PostResponse<BaseBean>() {
//                    @Override
//                    public void onSuccess(int i, byte[] bytes, BaseBean baseBean) {
//                        ToastUtil.show(mContext, "收藏成功");
//                        MainActivity.getInstance().refreshMainCollect();
//                        if (onIndexDetailListener != null)
//                            onIndexDetailListener.onCollect();
//                        isFinish = true;
//                        result = true;
//                    }
//
//                    @Override
//                    public void onFailure(int i, byte[] bytes) {
//                        isFinish = true;
//                        result = false;
//                    }
//                }, toastContent, true, BaseBean.class);
//                System.out.println("~!~ isFinish 1 = " + isFinish);
//                while (!isFinish) {
//                    System.out.println("~!~ isFinish 1 = " + isFinish);
//                    Thread.sleep(200);
//                }
//                System.out.println("~!~ result 1 = " + result);
//                return result;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        /**
//         * 取消收藏
//         * @param targetId 指标id
//         * @return true：成功  false：失败
//         */
//        @JavascriptInterface
//        public boolean cancelCollect(String targetId){
//            System.out.println("~!~ cancelCollect " + targetId);
//            isFinish = false;
//            try {
//                BaseMap params = new BaseMap();
//                params.put("collectId", targetId);
//                String[] toastContent = {"", ""};
//                PostCall.post(mContext, ServerUrl.cancelCollection(), params, new PostCall.PostResponse<BaseBean>() {
//                    @Override
//                    public void onSuccess(int i, byte[] bytes, BaseBean baseBean) {
//                        ToastUtil.show(mContext, "取消成功");
//                        MainActivity.getInstance().refreshMainCollect();
//                        if (onIndexDetailListener != null)
//                            onIndexDetailListener.onCancelCollect();
//                        isFinish = true;
//                        result = true;
//                    }
//
//                    @Override
//                    public void onFailure(int i, byte[] bytes) {
//                        isFinish = true;
//                        result = false;
//                    }
//                }, toastContent, true, BaseBean.class);
//                System.out.println("~!~ isFinish = " + isFinish);
//                while (!isFinish) {
//                    System.out.println("~!~ isFinish = " + isFinish);
//                    Thread.sleep(200);
//                }
//                System.out.println("~!~ result = " + result);
//                return result;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        /**
//         * 设置预警
//         * @param targetId 指标id
//         * @return true：成功  false：失败
//         */
//        @JavascriptInterface
//        public boolean setWarning(String targetId){
//            System.out.println("~!~ setWarning " + targetId);
//            try {
//                BaseMap params = new BaseMap();
//                params.put("targetId", targetId);
//                String[] toastContent = {"", ""};
//                PostCall.post(mContext, ServerUrl.getWarning(), params, new PostCall.PostResponse<WarningSettingBean>() {
//
//                    @Override
//                    public void onSuccess(int i, byte[] bytes, WarningSettingBean warningSettingBean) {
//                        warnPopu.setData(warningSettingBean);
////                        warnPopu.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//                        warnPopu.showAsDropDown(view);
//                    }
//
//                    @Override
//                    public void onFailure(int i, byte[] bytes) {
//
//                    }
//                }, toastContent, true, WarningSettingBean.class);
//                return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        /**
//         * 跳转指标详情
//         * @param targetId 指标id
//         * @param targetName 指标名称
//         * @return true：成功  false：失败
//         */
//        @JavascriptInterface
//        public boolean targetDetail(String targetId, String targetName){
//            System.out.println("~!~ targetDetail " + targetId + " " + targetName);
//            try {
//                startIndexDetail(mContext, targetName, targetId, cityId, cityName, endDate);
//                return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        /**
//         * 跳转指标详情
//         * @param targetId 指标id
//         * @param targetName 指标名称
//         * @param cityId 城市
//         * @return true：成功  false：失败
//         */
//        @JavascriptInterface
//        public boolean showUnderTarget(String cityId, String targetId, String targetName, String cityName){
//            System.out.println("~!~ showUnderTarget city " + cityId + " " + targetId + " " + targetName + " " + cityName);
//            try {
//                startIndexDetail(mContext, targetName, targetId, cityId, cityName, endDate);
//                return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        /**
//         * 跳转反馈意见界面
//         * @return true：成功  false：失败
//         */
//        @JavascriptInterface
//        public boolean startFeed(){
//            System.out.println("~!~ startFeed ");
//            try {
//                mContext.startActivity(new Intent(mContext, FeedActivity.class));
//                return true;
//            } catch (Exception e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//
//        public void setOnIndexDetailListener(OnIndexDetailListener onIndexDetailListener) {
//            this.onIndexDetailListener = onIndexDetailListener;
//        }
//    }

    public static List<String> getTempList(int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < (size <= 0 ? 10 : size); i++) {
            list.add("http://awb.img.xmtbang.com/img/uploadnew/201510/23/e3030dcd17ac43a2898862c7bb19df0d.jpg");
        }
        return list;
    }

    /**
     * 设置状态栏颜色
     * @param activity 上下文
     * @param colorResId 颜色id
     */
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本机电话信息（电话号、imsi、isei）
     * @return PhoneInfo 电话信息
     */
    public static PhoneInfo getLocalPhoneNum(Context mContext) {
        PhoneInfo phoneInfo = new PhoneInfo();
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            phoneInfo.setPhoneNum(tm.getLine1Number());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            phoneInfo.setImsi(tm.getSubscriberId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            phoneInfo.setImei(tm.getDeviceId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return phoneInfo;
    }

    /**
     * 设置上拉下拉控件
     * @param mContext 上下文
     * @param ptrFrameLayout 上拉下拉控件
     */
    public static void setPullView(Context mContext, final PtrClassicFrameLayout ptrFrameLayout, PtrFrameLayout.Mode mode, PtrDefaultHandler2 handler) {
        ptrFrameLayout.setMode(mode);
        if (mode == PtrFrameLayout.Mode.BOTH || mode == PtrFrameLayout.Mode.REFRESH) {
            // 下拉刷新
            StoreHouseHeader header = new StoreHouseHeader(mContext);
            header.setTextColor(mContext.getResources().getColor(R.color.black));
            header.setPadding(0, MathUtils.dip2px(mContext, 7), 0, MathUtils.dip2px(mContext, 7));
            header.initWithString("Loading", 11);
            ptrFrameLayout.setHeaderView(header);
            ptrFrameLayout.addPtrUIHandler(header);
            ptrFrameLayout.setDurationToCloseHeader(1000);
            ptrFrameLayout.setPullToRefresh(false);
        }
        if (mode == PtrFrameLayout.Mode.BOTH || mode == PtrFrameLayout.Mode.LOAD_MORE) {
            // 上拉加载
            StoreHouseHeader footer = new StoreHouseHeader(mContext);
            footer.setTextColor(mContext.getResources().getColor(R.color.black));
            footer.setPadding(0, MathUtils.dip2px(mContext, 7), 0, MathUtils.dip2px(mContext, 7));
            footer.initWithString("Loading", 11);
            ptrFrameLayout.setFooterView(footer);
            ptrFrameLayout.addPtrUIHandler(footer);
            ptrFrameLayout.setDurationToCloseFooter(1000);
        }
        ptrFrameLayout.setDurationToClose(200);
        // 设置监听
        ptrFrameLayout.setPtrHandler(handler);
    }

    /**
     * 设置是否可以上拉加载
     * @param ptrFrameLayout
     * @param listHeight
     * @param itemHeight
     * @param itemCount
     */
    public static final void setPullViewCanLoadMore(final PtrClassicFrameLayout ptrFrameLayout, int listHeight, int itemHeight, int itemCount) {
        if (itemHeight * itemCount >= listHeight)
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
    }

    public static class PhoneInfo {
        private String phoneNum;
        private String imsi;
        private String imei;

        public String getPhoneNum() {
            return phoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            this.phoneNum = phoneNum;
        }

        public String getImsi() {
            return imsi;
        }

        public void setImsi(String imsi) {
            this.imsi = imsi;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }
    }

    public static OnSmsListener onSmsListener;

    public interface OnSmsListener {
        public void receiveSms(String content);
    }

    /**
     * 设置播放器
     * @param jzVideoPlayerStandard
     */
    public static void setVideoPlayer(JZVideoPlayerStandard jzVideoPlayerStandard) {
        jzVideoPlayerStandard.getLayoutParams().height = (int) (AppInfo.getScreenWidthOrHeight(jzVideoPlayerStandard.getContext(), true) * 0.5625f);
    }

    /**
     * 设置TabLayout的横线长度
     * @param tabs TabLayout对象
     * @param leftDip 左边距
     * @param rightDip 右边距
     */
    public static void setTabLayoutLineLength(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }
}
