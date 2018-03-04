package com.xhy.zhanhui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.fragment.FragmentAdapter;
import com.xhy.zhanhui.fragment.ibusiness.IBusinessNewestFragment;

import java.util.ArrayList;

/**
 * 企业版-收到的需求列表页面
 * Created by Aaron on 2018/2/21.
 */

public class IBusinessReceiveListActivity extends ZhanHuiActivity {

    private String[] titles = new String[]{"最新需求", "已同意"};

    private ViewPager viewPager;

    private TabLayout tabLayout;

    /**
     * 用于记录当前页码
     */
    private int currentIndex = 0;

    /**
     * 用于判断是否可以改变当前页码
     */
    private boolean isCanChange = true;

    private ArrayList<Fragment> fragmentList;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_ibusiness_receive_list;
    }

    @Override
    protected void findView() {
        viewPager =  findViewById(R.id.viewPager);
        tabLayout =  findViewById(R.id.tabLayout);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarBackgroundResource(R.mipmap.ibusiness_actionbar_bg);
        setActionbarTitleColor(R.color.white);
        getActionbarView().getBackButton().setImageResource(R.mipmap.common_back_white1);
        setActionbarTitle("收到需求");
        viewPager.setVisibility(View.VISIBLE);
        initViewPager();
        initTabLayout();
    }

    /**
     * 初始化ViewPager（加载自主分析和收藏指标页面）
     */
    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new IBusinessNewestFragment());
        fragmentList.add(new IBusinessNewestFragment().setState("1"));
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
    }

    /**
     * 初始化TabLayout
     */
    private void initTabLayout() {
        tabLayout.removeAllTabs();
        tabLayout.clearOnTabSelectedListeners();
        for (int i = 0; i < titles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles[i]));
        }
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (isCanChange) {
                    currentIndex = tab.getPosition();
                } else
                    isCanChange = true;
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                int padding = (int) ((AppInfo.getScreenWidthOrHeight(mContext, true) / 3) * 0.10f);
                int paddingDp = MathUtils.px2dip(mContext, padding);
                PublicMethod.setTabLayoutLineLength(tabLayout, paddingDp, paddingDp);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.menu:
                break;
        }
    }
}
