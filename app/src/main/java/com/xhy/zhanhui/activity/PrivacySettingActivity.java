package com.xhy.zhanhui.activity;

import com.kyleduo.switchbutton.SwitchButton;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;

/**
 * 隐私设置页面
 * Created by Aaron on 2017/12/29.
 */

public class PrivacySettingActivity extends ZhanHuiActivity{

    private SwitchButton switchButton1, switchButton2, switchButton3;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_privacy_setting;
    }

    @Override
    protected void findView() {
        super.findView();
        setActionbarTitle("账号与安全");
        switchButton1 = findViewById(R.id.switchButton1);
        switchButton2 = findViewById(R.id.switchButton2);
        switchButton3 = findViewById(R.id.switchButton3);
    }

    @Override
    protected void init() {
        super.init();
    }
}
