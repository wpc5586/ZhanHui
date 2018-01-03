package com.aaron.aaronlibrary.base.app;

import android.app.Application;
import android.util.Log;

import com.aaron.aaronlibrary.db.PreferenceManager;
import com.aaron.aaronlibrary.utils.Constants;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.mob.MobApplication;
import com.tencent.smtt.sdk.QbSdk;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;

/**
 * <p>类名称: CrashApplication</p>
 * <p>类描述: 应用基类</p>
 * <p>所属模块: domain</p>
 * <p>创建时间: 15-5-12  9:18 </p> 
 * <p>作者: 王鹏程 </p>
 * <p>版本: 1.0 </p>
 */
public class CrashApplication extends MobApplication {
    
    public static Application APP;
    
    @Override
    public void onCreate() {
        APP = this;
        super.onCreate();
        // 初始化PreferenceManager
        PreferenceManager.init(APP);
        //应用出现异常后自动重新启动
        if (!Constants.DEBUGABLE) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }
        initClient();
    }

    /**
     * 初始化Https和X5WebView
     */
    private void initClient() {
//        }
        ClearableCookieJar cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getApplicationContext()));
//        InputStream certificates = null;
//        try {
//            certificates = getAssets().open("zhy_server.cer");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        final HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
//        CookieJarImpl cookieJar1 = new CookieJarImpl(new MemoryCookieStore());
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .readTimeout(30000L, TimeUnit.MILLISECONDS)
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .cookieJar(cookieJar1)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
//                .sslSocketFactory(sslParams.sSLSocketFactory)
                .build();
        OkHttpUtils.initClient(okHttpClient);

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {}
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);
    }
}
