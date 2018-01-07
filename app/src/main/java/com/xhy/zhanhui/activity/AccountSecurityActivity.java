package com.xhy.zhanhui.activity;

import android.view.View;
import android.widget.TextView;

import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;

/**
 * 账户安全页面
 * Created by Aaron on 2017/12/27.
 */

public class AccountSecurityActivity extends ZhanHuiActivity{

    private TextView tvPhone;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account_security;
    }

    @Override
    protected void findView() {
        super.findView();
        setActionbarTitle("账号与安全");
        tvPhone = findViewById(R.id.tvPhone);
        findAndSetClickListener(R.id.rlPhone);
        findAndSetClickListener(R.id.rlPwd);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvPhone.setText(getUserName().substring(0, 3) + "****" + getUserName().substring(7, 11));
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch(view.getId()) {
            case R.id.rlPhone:
                startMyActivity(SettingPhoneActivity.class);
                break;
            case R.id.rlPwd:
                startMyActivity(SettingPwdActivity.class);
                break;
        }
    }
}
