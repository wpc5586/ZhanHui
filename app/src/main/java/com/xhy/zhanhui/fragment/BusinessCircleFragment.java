package com.xhy.zhanhui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.aaron.aaronlibrary.easeui.ui.ContactListFragment;
import com.aaron.aaronlibrary.easeui.ui.ConversationListFragment;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.Constants;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.MainActivity;
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.fragment.businesscircle.BusinessCompanyFragment;
import com.xhy.zhanhui.fragment.businesscircle.BusinessProductFragment;
import com.xhy.zhanhui.fragment.businesscircle.BusinessUserFragment;

import java.util.ArrayList;

/**
 * 商圈Fragment
 * Created by Aaron on 09/12/2017.
 */

public class BusinessCircleFragment extends ZhanHuiFragment {

    private String[] titles = Constants.VERSION_IS_USER ? new String[]{"消息", "企业", "朋友", "关注产品"} : new String[]{"消息", "客户", "企业伙伴"};

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
        return R.layout.fragment_business_circle;
    }

    @Override
    protected void findViews(View view) {
        viewPager =  view.findViewById(R.id.viewPager);
        tabLayout =  view.findViewById(R.id.tabLayout);
        findViewAndSetListener(R.id.menu);
    }

    @Override
    protected void init() {
        super.init();
        viewPager.setVisibility(View.VISIBLE);
        initViewPager();
        initTabLayout();
    }

    /**
     * 初始化ViewPager（加载自主分析和收藏指标页面）
     */
    private void initViewPager() {
        fragmentList = new ArrayList<>();
        if (Constants.VERSION_IS_USER) {
            fragmentList.add(new ConversationListFragment());
            fragmentList.add(new BusinessCompanyFragment());
            fragmentList.add(new ContactListFragment());
            fragmentList.add(new BusinessProductFragment());
        } else {
            fragmentList.add(new ConversationListFragment());
            fragmentList.add(new BusinessUserFragment());
            fragmentList.add(new ContactListFragment());
        }
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
                MainActivity.getInstance().openMenu();
                break;
        }
    }

    /**
     * 刷新会话列表
     */
    public void refreshConversation() {
        if (fragmentList != null && fragmentList.size() > 0)
            ((ConversationListFragment) fragmentList.get(0)).refresh();
    }

    /**
     * 刷新信任客户列表
     */
    public void refreshTrustUser() {
        if (fragmentList != null && fragmentList.size() > 0)
            ((BusinessUserFragment) fragmentList.get(1)).refresh();
    }

    /**
     * 刷新联系人列表
     */
    public void refreshContact() {
        if (fragmentList != null && fragmentList.size() > 0)
            ((ContactListFragment) fragmentList.get(2)).refresh();
    }

    /**
     * 关注产品或者取消关注后，刷新商圈-关注产品页面
     */
    public void refreshAttentionProduct() {
        if (fragmentList != null && fragmentList.size() > 0)
            ((BusinessProductFragment) fragmentList.get(3)).refresh();
    }
}
