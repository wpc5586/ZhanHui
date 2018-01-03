package com.xhy.zhanhui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.fragment.exhibition.CompanyFragment;
import com.xhy.zhanhui.fragment.exhibition.ExhibitionDetailFragment;
import com.xhy.zhanhui.fragment.exhibition.ProductFragment;

import java.util.ArrayList;

/**
 * 展&会Fragment
 * Created by Aaron on 09/12/2017.
 */

public class ExhibitionFragment extends ZhanHuiFragment {

    private String[] titles = new String[]{"展会", "企业", "产品"};

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
        return R.layout.fragment_exhibition;
    }

    @Override
    protected void findViews(View view) {
        viewPager =  view.findViewById(R.id.viewPager);
        tabLayout =  view.findViewById(R.id.tabLayout);
        progressBar = view.findViewById(R.id.progressbar);
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
        fragmentList.add(new ExhibitionDetailFragment());
        fragmentList.add(new CompanyFragment());
        fragmentList.add(new ProductFragment());
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
                int padding = (int) ((AppInfo.getScreenWidthOrHeight(mContext, true) / 3) * 0.3f);
                int paddingDp = MathUtils.px2dip(mContext, padding);
                PublicMethod.setTabLayoutLineLength(tabLayout, paddingDp, paddingDp);
            }
        });
    }
}
