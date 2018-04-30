package com.xhy.zhanhui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.transformations.RoundedCornersTransformation;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.Constants;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.base.ZhanHuiApplication;
import com.xhy.zhanhui.preferences.IBusinessSharedPreferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 智能商务-发布需求页面
 * Created by Administrator on 2018/1/25.
 */

public class IntelligentBusinessReleaseActivity extends ZhanHuiActivity{

    private EditText etName, etClass, etBrand, etNum, etRemark;
    private List<File> files = new ArrayList<>();
    private LinearLayout llImage;
    private ImageView image1, image2, image3, image4;
    private ImageView[] imageViews;
    private String fileTempPath; // 临时文件
    private String fileOutTempPath; // 裁剪后文件
    private boolean isSelete = true; // true：选择图片  false：修改图片
    private int currentModifyIndex; // 当前修改或删除图片的角标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contentIsBelow = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_intelligent_business_release;
    }

    @Override
    protected void findView() {
        etName = findViewById(R.id.et1);
        etClass = findViewById(R.id.et2);
        etBrand = findViewById(R.id.et3);
        etNum = findViewById(R.id.et4);
        etRemark = findViewById(R.id.et5);
        llImage = findAndSetClickListener(R.id.llImage1);
        image1 = findAndSetClickListener(R.id.image1);
        image2 = findAndSetClickListener(R.id.image2);
        image3 = findAndSetClickListener(R.id.image3);
        image4 = findViewById(R.id.image4);
        imageViews = new ImageView[]{image1, image2, image3};
        findAndSetClickListener(R.id.release);
    }


    @Override
    protected void init() {
        super.init();
        setActionbarTitle("需求发布");
        setActionbarBackground(R.color.transparent);
        setActionbarDividerVisibility(false);
        setStatusBarVisibility(false);
        getActionbarView().getBackButton().setImageResource(R.mipmap.common_back_white1);
        setActionbarTitleColor(R.color.white);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_MY_USERIMAGE_CAMERA:
                    fileOutTempPath = AppInfo.openPhotoZoom(mContext, fileTempPath);
                    break;
                case Constants.REQUEST_PHOTO:
                    if (data.getData() != null)
                        fileOutTempPath = AppInfo.openPhotoZoom(mContext, data.getData());
                    break;
                case Constants.REQUEST_MY_USERIMAGE_CUT:
                    if (!TextUtils.isEmpty(fileOutTempPath))
                        if (isSelete)
                            saveTempImage();
                        else
                            modifyTempImage();
                    break;
            }
        }
    }

    /**
     * 选择图片
     */
    private void seleteImage() {
        showAlertListDialog(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        fileTempPath = AppInfo.openCamera(mContext);
                        break;
                    case 1:
                        AppInfo.openPhoto(mContext);
                        break;
                }
            }
        }, true);
    }

    /**
     * 保存临时图片
     */
    private void saveTempImage() {
        files.add(new File(fileOutTempPath));
        update();
    }

    /**
     * 修改临时图片
     */
    private void modifyTempImage() {
        files.set(currentModifyIndex, new File(fileOutTempPath));
        loadImage();
    }

    /**
     * 删除临时图片
     */
    private void deleteTempImage() {
        files.remove(currentModifyIndex);
        update();
    }

    /**
     * 刷新图片选择区域的界面
     */
    private void update() {
        switch (files.size()) {
            case 0:
                image1.setVisibility(View.GONE);
                image2.setVisibility(View.GONE);
                image3.setVisibility(View.VISIBLE);
                image4.setVisibility(View.VISIBLE);
                llImage.setVisibility(View.VISIBLE);
                ((LinearLayout.LayoutParams) llImage.getLayoutParams()).leftMargin = MathUtils.dip2px(mContext, 0);
                break;
            case 1:
                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.GONE);
                image3.setVisibility(View.VISIBLE);
                image4.setVisibility(View.GONE);
                llImage.setVisibility(View.VISIBLE);
                ((LinearLayout.LayoutParams) llImage.getLayoutParams()).leftMargin = MathUtils.dip2px(mContext, 20);
                break;
            case 2:
                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.GONE);
                image4.setVisibility(View.GONE);
                llImage.setVisibility(View.VISIBLE);
                break;
            case 3:
                image1.setVisibility(View.VISIBLE);
                image2.setVisibility(View.VISIBLE);
                image3.setVisibility(View.VISIBLE);
                image4.setVisibility(View.GONE);
                llImage.setVisibility(View.GONE);
                break;
        }
        loadImage();
    }

    /**
     * 加载图片
     */
    private void loadImage() {
        for (int i = 0; i < files.size(); i++) {
            ImageUtils.loadImageRoundedCorners(mContext, files.get(i).getAbsolutePath(), imageViews[i], RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 3));
        }
    }

    /**
     * 长按图片弹出修改或删除图片对话框
     * @param index 角标
     */
    private void modifyOrDelete(final int index) {
        isSelete = false;
        currentModifyIndex = index;
        showAlertListDialog(new String[]{"修改", "删除"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        seleteImage();
                        break;
                    case 1:
                        deleteTempImage();
                        break;
                }
            }
        }, true);
    }

    /**
     * 需求发布
     */
    private void release() {
        File[] files = new File[this.files.size()];
        for (int i = 0; i < this.files.size(); i++) {
            files[i] = this.files.get(i);
        }
        BaseMap params = new BaseMap();
        params.put("user_id", getUserId());
        params.put("demand_title", etName.getText().toString());
//        params.put("category_id", etClass.getText().toString());
        params.put("category_id", "1");
        params.put("demand_brand", etBrand.getText().toString());
        params.put("demand_nums", etNum.getText().toString());
        params.put("demand_notes", etRemark.getText().toString());
        PostCall.postFiles(mContext, ServerUrl.demands(), params, "files", files, new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                showToast("需求发布成功");
                IBusinessSharedPreferences.getInstance().setIBusinessData("您发布的需求匹配成功");
                finish();
                MainActivity.getInstance().refreshMainMessage();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, BaseBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.llImage1:
                isSelete = true;
                seleteImage();
                break;
            case R.id.image1:
                modifyOrDelete(0);
                break;
            case R.id.image2:
                modifyOrDelete(1);
                break;
            case R.id.image3:
                modifyOrDelete(2);
                break;
            case R.id.release:
                release();
                break;
        }
    }
}
