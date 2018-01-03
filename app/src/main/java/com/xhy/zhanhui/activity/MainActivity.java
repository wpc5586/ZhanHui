package com.xhy.zhanhui.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.activity.drawer.BaseListSample;
import com.aaron.aaronlibrary.base.activity.drawer.Item;
import com.aaron.aaronlibrary.base.menudrawer.MenuDrawer;
import com.aaron.aaronlibrary.base.menudrawer.Position;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiApplication;
import com.xhy.zhanhui.fragment.BusinessCircleFragment;
import com.xhy.zhanhui.fragment.CenterFragment;
import com.xhy.zhanhui.fragment.ExhibitionFragment;
import com.xhy.zhanhui.fragment.MainFragment;

/**
 * 主界面
 * Created by Aaron on 09/12/2017.
 */

public class MainActivity extends BaseListSample{

    private static MainActivity instance;
    private int index;
    // 当前fragment的index
    private int currentTabIndex;
    private Fragment[] fragments;
    private RelativeLayout[] tabButtons;
    private int[] tabDefaultBg = {R.mipmap.icon_main1, R.mipmap.icon_main2, R.mipmap.icon_main3, R.mipmap.icon_main4};
    private int[] tabSelectBg = {R.mipmap.icon_main1_p, R.mipmap.icon_main2_p, R.mipmap.icon_main3_p, R.mipmap.icon_main4_p};

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected void findView() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        mMenuDrawer.setContentView(R.layout.activity_main);
        mMenuDrawer.setTouchMode(MenuDrawer.TOUCH_MODE_BEZEL);
        mMenuDrawer.setSlideDrawable(R.mipmap.ic_drawer);
        mMenuDrawer.setDrawerIndicatorEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH && getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mMenuDrawer.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener() {
            @Override
            public boolean isViewDraggable(View v, int dx, int x, int y) {
                return v instanceof SeekBar;
            }
        });
        tabButtons = new RelativeLayout[]{findAndSetClickListener(R.id.rl1), findAndSetClickListener(R.id.rl2),
                findAndSetClickListener(R.id.rl3), findAndSetClickListener(R.id.rl4)};
        initFragment();
        setButtonSelect(0);
    }

    @Override
    protected void onMenuItemClicked(int position, Item item) {

    }

    @Override
    protected int getDragMode() {
        return MenuDrawer.MENU_DRAG_CONTENT;
    }

    @Override
    protected Position getDrawerPosition() {
        return Position.START;
    }

    @Override
    protected void init() {
        super.init();
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments = new Fragment[]{new MainFragment(), new ExhibitionFragment(), new BusinessCircleFragment(), new CenterFragment()};
        getSupportFragmentManager().beginTransaction().add(R.id.containerMain, fragments[0]).commit();
    }

    /**
     * 设置TabButton选中状态
     * @param index 角标
     */
    private void setButtonSelect(int index) {
        ImageView currentImageView  = (ImageView) tabButtons[currentTabIndex].getChildAt(0);
        currentImageView.setImageResource(tabDefaultBg[currentTabIndex]);
        TextView currentTextView = (TextView) tabButtons[currentTabIndex].getChildAt(1);
        currentTextView.setTextColor(getResources().getColor(R.color.white));
        ImageView targetImageView  = (ImageView) tabButtons[index].getChildAt(0);
        targetImageView.setImageResource(tabSelectBg[index]);
        TextView targetTextView = (TextView) tabButtons[index].getChildAt(1);
        targetTextView.setTextColor(getResources().getColor(R.color.white));

        tabButtons[currentTabIndex].setSelected(false);
        tabButtons[index].setSelected(true);
    }

    /**
     * 通过index显示Fragment
     * @param index Fragment角标
     */
    private void setIndexFragment(int index) {
        if (currentTabIndex != index) {
            setButtonSelect(index);
            FragmentTransaction trx = getSupportFragmentManager().beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            if (!fragments[index].isAdded()) {
                trx.add(R.id.containerMain, fragments[index]);
            }
            trx.show(fragments[index]).commit();
        }
        currentTabIndex = index;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl1:
                index = 0;
                break;
            case R.id.rl2:
                index = 1;
                break;
            case R.id.rl3:
                index = 2;
                break;
            case R.id.rl4:
                index = 3;
//                startActivity(new Intent(mContext, TempActivity.class));
                break;
        }
        setIndexFragment(index);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // 进入后台
            moveTaskToBack(false);
            // 延时后收回菜单
            mMenuDrawer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mMenuDrawer.isMenuVisible())
                        mMenuDrawer.closeMenu();
                }
            }, 500);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 收藏或者取消收藏后，刷新资料中心的收藏页面
     */
    public void refreshCenterCollect() {
        ((CenterFragment) fragments[3]).refreshCenterCollect();
    }

    /**
     * 打开或关闭菜单
     */
    public void openMenu() {
        mMenuDrawer.toggleMenu();
    }

    /**
     * 刷新用户名
     */
    public void refreshName() {
        tvName.setText(ZhanHuiApplication.getInstance().getNickname());
    }

    /**
     * 刷新用户头像
     */
    public void refreshAvatar() {
        ImageUtils.loadImageCircle(mContext, ZhanHuiApplication.getInstance().getIcon(), ivAvatar);
    }
}
