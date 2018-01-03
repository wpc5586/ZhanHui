package com.xhy.zhanhui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.DownloadUtils;
import com.aaron.aaronlibrary.utils.ToastUtil;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.xhy.zhanhui.BuildConfig;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.base.ZhanHuiApplication;
import com.xhy.zhanhui.http.domain.FriendBean;
import com.xhy.zhanhui.http.domain.LoginBean;
import com.xhy.zhanhui.http.domain.VersionBean;
import com.xhy.zhanhui.preferences.UserSharedPreferences;

import java.io.File;

/**
 * 启动页面
 * Cby Aaron on 2017/12/09.
 */

public class SplashActivity extends ZhanHuiActivity {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 11;
    private static final int END_REQUEST_CODE = 111;

    private RelativeLayout rlDownload;
    private String url;
    private NumberProgressBar bar;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void findView() {
        rlDownload = findViewById(R.id.rl_download);
    }

    @Override
    protected void init() {
        super.init();
        isNeedBackEnd = false;
        setActionbarVisibility(false);
        if (MainActivity.getInstance() != null) {
            startMyActivity(MainActivity.class);
            finish();
            return;
        }
        if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT)
            getWindow().setFlags(WindowManager.LayoutParams. FLAG_TRANSLUCENT_STATUS ,
                    WindowManager.LayoutParams. FLAG_TRANSLUCENT_STATUS);
//        loadVersion();
        requestPermission();
    }

    /**
     * 申请需要的权限
     */
    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, END_REQUEST_CODE);
        } else
            delayedLogin();

    }

    /**
     * 获取版本信息
     */
    private void loadVersion() {
        String[] toastContent = {"", ""};
        PostCall.post(mContext, ServerUrl.getVersion(), null, new PostCall.PostResponse<VersionBean>() {
            @Override
            public void onSuccess(int i, byte[] bytes, VersionBean versionBean) {
                ZhanHuiApplication.getInstance().setVersionBean(versionBean);
                url = versionBean.getObj().getAndroidUrl();
                try {
                    int currentCode = Integer.parseInt(AppInfo.getVersionNameOrCode(mContext, false));
                    int newestCode = Integer.parseInt(versionBean.getObj().getAndroidVersion());
                    if (newestCode > currentCode)
                        showUpdateDialog(versionBean);
                    else
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                File file = new File(DownloadUtils.getFileSavePath(url));
                                if (file.exists())
                                    file.delete();
                                autoLogin();
                            }
                        }, 1000);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int i, byte[] bytes) {
                startMyActivity(MainActivity.class);
            }
        }, toastContent, false, VersionBean.class);
    }

    /**
     * 自动登录
     */
    private void autoLogin() {
        LoginBean loginBean = UserSharedPreferences.getInstance().getLoginData();
        if (loginBean != null && !TextUtils.isEmpty(loginBean.getObj().getUser_id())) {
            // 已登录
            ZhanHuiApplication.getInstance().login(loginBean);
//            getFriends();
            loginEmchat(loginBean.getObj().getHx_username(), loginBean.getObj().getHx_password());
            startMyActivity(MainActivity.class);
            finish();
        } else {
            // 未登录
            startMyActivity(LoginActivity.class);
            finish();
        }
    }

    /**
     * 登录环信
     *
     * @param userName 用户名
     * @param password 密码
     */
    private void loginEmchat(String userName, String password) {
        EMClient.getInstance().login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                showLog("登录聊天服务器成功!", "");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                showLog("登录聊天服务器失败!", "");
            }
        });
    }

    /**
     * 显示版本更新对话框
     */
    private void showUpdateDialog(final VersionBean versionBean) {
        showAlertDialog("版本有更新", versionBean.getObj().getAndroidContent(), "立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtil.show(mContext, "开始下载");
                downloadApk();
            }
        }, "退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(1);
                dialog.dismiss();
            }
        }, false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                download();
            } else {
                finish();
                System.exit(1);
            }
        } else if (requestCode == END_REQUEST_CODE){
            delayedLogin();
        }
    }

    /**
     * 延迟1秒进入主界面
     */
    private void delayedLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoLogin();
            }
        }, 1000);
    }

    /**
     * 下载新版Apk
     */
    private void downloadApk() {
        if (TextUtils.isEmpty(url)) {
            ToastUtil.show(mContext, "下载地址为空");
            return;
        }
        if (!url.contains("http"))
            url = ServerUrl.getService() + url;
        bar = (NumberProgressBar) rlDownload.findViewById(R.id.progressbar);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else
            download();
    }

    private void download() {
        rlDownload.setVisibility(View.VISIBLE);
        DownloadUtils.downloadFileWithListener(mContext, url, new DownloadUtils.OnDownloadListener() {
            @Override
            public void onProgress(long l, long l1, int i, int i1) {
                bar.setProgress(i);
            }

            @Override
            public void onFinished(final File file, boolean b) {
                rlDownload.setVisibility(View.GONE);
                showAlertDialog("新版APK下载完毕，是否安装？", "", "立即安装", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        installApk(file);
                        finish();
                        System.exit(1);
                        dialog.dismiss();
                    }
                }, "退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        System.exit(1);
                        dialog.dismiss();
                    }
                }, false);
            }

            @Override
            public void onError(String s) {
                System.out.println("~!~ onError = " + s);
                ToastUtil.show(mContext, "下载出错，请重试");
                new File(DownloadUtils.getFileSavePath(url)).delete();
                downloadApk();
            }
        });
    }

    /**
     * 安装Apk
     */
    private void installApk(File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileProvider", file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

    /**
     * 获取朋友列表
     */
    private void getFriends() {
        PostCall.get(mContext, ServerUrl.trustFriends(), null, new PostCall.PostResponse<FriendBean>() {
            @Override
            public void onSuccess(int i, byte[] bytes, FriendBean bean) {
                ZhanHuiApplication.getInstance().setFriendBean(bean);
            }

            @Override
            public void onFailure(int i, byte[] bytes) {
                startMyActivity(MainActivity.class);
            }
        }, new String[]{"", ""}, false, FriendBean.class);
    }
}
