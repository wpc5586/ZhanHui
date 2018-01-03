package com.xhy.zhanhui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 二维码扫描页面
 * Created by Aaron on 22/12/2017.
 */

public class QRCodeActivity extends ZhanHuiActivity implements QRCodeView.Delegate {

    private ZXingView zXingView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_qrcode;
    }

    @Override
    protected void findView() {
        super.findView();
        zXingView = findViewById(R.id.zXingView);
        zXingView.setDelegate(this);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("");
        setActionbarBackground(R.color.transparent);
        setActionbarOnContent();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        System.out.println("~!~ result = " + result);
        String[] results = result.split("#");
        StartActivityUtils.startTrustUser(mContext, results[0], results[1]);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        // 申请需要的权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        } else
            zXingView.startSpotAndShowRect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                zXingView.startSpotAndShowRect();
            } else {
                showToast("请打开相机权限");
            }
        }
    }

    @Override
    protected void onStop() {
        zXingView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zXingView.onDestroy();
        super.onDestroy();
    }
}
