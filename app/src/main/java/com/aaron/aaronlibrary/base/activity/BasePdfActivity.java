package com.aaron.aaronlibrary.base.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.aaron.aaronlibrary.utils.DownloadUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.aaron.aaronlibrary.utils.ToastUtil;
import com.aaron.aaronlibrary.widget.GenericToast;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.xhy.zhanhui.R;

import java.io.File;

/**
 * 打开PDF文件页面，支持网络地址查看
 * Created by Aaron on 13/12/2017.
 */

public class BasePdfActivity extends BaseActivity implements OnPageChangeListener {

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 11;
    protected PDFView pdfView;
    private Toast toast;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_base_pdf;
    }

    @Override
    protected void findView() {
        pdfView = findViewById(R.id.pdfView);
    }

    @Override
    protected void init() {
        super.init();
        loadUrl();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == WRITE_EXTERNAL_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                downloadPdf();
            }
        }
    }

    /**
     * 加载url
     */
    protected void loadUrl() {
        showProgressDialog("加载中");
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions((Activity) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else
            downloadPdf();
    }

    private void downloadPdf() {
        final String url = getUrl();
        DownloadUtils.downloadFileWithListener(mContext, url, new DownloadUtils.OnDownloadListener() {
            @Override
            public void onProgress(long l, long l1, int i, int i1) {

            }

            @Override
            public void onFinished(final File file, boolean b) {
                dismissProgressDialog();
                pdfView.fromFile(file)
                        .swipeHorizontal(true)
                        .onPageChange(BasePdfActivity.this)
                        .enableAntialiasing(true)
                        .spacing(MathUtils.dip2px(mContext, 5))
                        .load();
            }

            @Override
            public void onError(String s) {
                System.out.println("~!~ onError = " + s);
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ToastUtil.show(mContext, "请打开权限，再进行查看");
                } else
                    ToastUtil.show(mContext, "下载出错，请重试");
                new File(DownloadUtils.getFileSavePath(url)).delete();
            }
        });
    }

    protected String getUrl() {
        return "http://www.baidu.com";
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        if (toast != null)
            toast.cancel();
        toast = ToastUtil.show(mContext, (page + 1) + " of " + pageCount);
    }
}