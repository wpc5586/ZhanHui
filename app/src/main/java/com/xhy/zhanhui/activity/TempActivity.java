package com.xhy.zhanhui.activity;

import android.view.WindowManager;

import com.aaron.aaronlibrary.utils.AppInfo;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;

/**
 * Created by Aaron on 2017/12/13.
 */

public class TempActivity extends ZhanHuiActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_temp;
    }

    @Override
    protected void init() {
        super.init();
        setActionbarVisibility(false);//隐藏状态栏
        //定义全屏参数
        int flag= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);
        findViewById(R.id.statusbar).getLayoutParams().width = (int) (AppInfo.getScreenWidthOrHeight(mContext, true) * 0.35f);
    }
}
