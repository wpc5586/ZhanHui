package com.xhy.zhanhui.activity;

import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;

/**
 * 首页-统计分析页面
 * Created by Aaron on 2018/1/7.
 */

public class StaticsActivity extends ZhanHuiActivity{

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_statics;
    }

    @Override
    protected void findView() {
        super.findView();
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("统计分析");
    }
}
