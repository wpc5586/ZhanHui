package com.xhy.zhanhui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.CenterMessageActivity;
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.fragment.center.CenterAllFragment;
import com.xhy.zhanhui.fragment.center.CenterCollectionFragment;
import com.xhy.zhanhui.fragment.center.CenterNewFragment;

import java.util.ArrayList;

/**
 * 资料中心Fragment
 * Created by Aaron on 09/12/2017.
 */

public class CenterFragment extends ZhanHuiFragment {

    public static final String MODULE_TYPE_FAVORITE = "favorite"; // 收藏的
    public static final String MODULE_TYPE_RECEIVED = "received"; // 收到的
    public static final String MODULE_TYPE_RECOMMEND = "recommend"; // 平台推荐的
    public static final String[] MODULES = new String[]{MODULE_TYPE_FAVORITE, MODULE_TYPE_RECEIVED, MODULE_TYPE_RECOMMEND};

    public static final String FILTER_TYPE_ALL = "all"; // 所有的
    public static final String FILTER_TYPE_FOCUS = "focus"; // 关注企业的
    public static final String FILTER_TYPE_TRUST = "trust"; // 信任企业的
    public static final String FILTER_TYPE_OTHER = "other"; // 非关注且非信任企业的
    public static final String[] FILTERS = new String[]{FILTER_TYPE_ALL, FILTER_TYPE_FOCUS, FILTER_TYPE_TRUST, FILTER_TYPE_OTHER};

    public static final String GROUP_TYPE_DATE = "date"; // 按日期分组
    public static final String GROUP_TYPE_COMPANY = "company"; // 按企业分组
    public static final String[] GROUPS = new String[]{GROUP_TYPE_DATE, GROUP_TYPE_COMPANY};

    public static final String ORDER_TYPE_DATE = "date"; // 按日期分组
    public static final String ORDER_TYPE_NAME = "name"; // 按企业分组
    public static final String[] ORDERS = new String[]{ORDER_TYPE_DATE, ORDER_TYPE_NAME};


    private String[] titles = new String[]{"收藏的", "新收到的", "所有收到的"};

    private ViewPager viewPager;

    private TabLayout tabLayout;

    private ProgressBar progressBar;

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
        return R.layout.fragment_center;
    }

    @Override
    protected void findViews(View view) {
        viewPager =  view.findViewById(R.id.viewPager);
        tabLayout =  view.findViewById(R.id.tabLayout);
        progressBar = view.findViewById(R.id.progressbar);
        findViewAndSetListener(R.id.message);
    }

    @Override
    protected void init() {
        super.init();
        progressBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        initViewPager();
        initTabLayout();
    }

    /**
     * 初始化ViewPager（加载自主分析和收藏指标页面）
     */
    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new CenterCollectionFragment());
        fragmentList.add(new CenterNewFragment());
        fragmentList.add(new CenterAllFragment());
        FragmentAdapter adapter = new FragmentAdapter(getChildFragmentManager(), fragmentList, titles);
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
                int padding = (int) ((AppInfo.getScreenWidthOrHeight(mContext, true) / 3) * 0.2f);
                int paddingDp = MathUtils.px2dip(mContext, padding);
                PublicMethod.setTabLayoutLineLength(tabLayout, paddingDp, paddingDp);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.message:
                startMyActivity(CenterMessageActivity.class);
                break;
        }
    }

    /**
     * 收藏或者取消收藏后，刷新资料中心的收藏页面
     */
    public void refreshCenterCollect() {
        if (fragmentList != null && fragmentList.size() > 0)
            ((CenterCollectionFragment) fragmentList.get(0)).refreshCenterCollect();
    }
}
