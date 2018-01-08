package com.aaron.aaronlibrary.base.activity.drawer;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.activity.BaseActivity;
import com.aaron.aaronlibrary.base.menudrawer.MenuDrawer;
import com.aaron.aaronlibrary.base.menudrawer.Position;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.Constants;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.hyphenate.EMCallBack;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.AccountSecurityActivity;
import com.xhy.zhanhui.activity.EditVcardActivity;
import com.xhy.zhanhui.activity.LoginActivity;
import com.xhy.zhanhui.activity.PrivacySettingActivity;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.base.ZhanHuiApplication;

public abstract class BaseListSample extends ZhanHuiActivity implements MenuAdapter.MenuListener, View.OnClickListener {

    private static final String STATE_ACTIVE_POSITION =
            "net.simonvt.menudrawer.samples.LeftDrawerSample.activePosition";

    protected MenuDrawer mMenuDrawer;

    protected MenuAdapter mAdapter;
    protected ListView mList;
    protected ImageView ivAvatar;
    protected TextView tvName;

    private int mActivePosition = 0;

    @Override
    protected void onCreate(Bundle inState) {
        isNotLoadParent = true;
        super.onCreate(inState);

        if (inState != null) {
            mActivePosition = inState.getInt(STATE_ACTIVE_POSITION);
        }

        mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.OVERLAY, getDrawerPosition(), getDragMode());

        LinearLayout linearLayout = (LinearLayout) View.inflate(this, R.layout.drawer_listview, null);

        ivAvatar = linearLayout.findViewById(R.id.ivThum);
        tvName = linearLayout.findViewById(R.id.tvName);
        ImageUtils.loadImageCircle(mContext, ZhanHuiApplication.getInstance().getIcon(), ivAvatar);
        tvName.setText(ZhanHuiApplication.getInstance().getNickname());
        if (!Constants.VERSION_IS_USER)
            linearLayout.findViewById(R.id.rlCard).setVisibility(View.GONE);
        else
            linearLayout.findViewById(R.id.rlCard).setOnClickListener(this);
        linearLayout.findViewById(R.id.rlPrivacy).setOnClickListener(this);
        linearLayout.findViewById(R.id.rlSafe).setOnClickListener(this);
        linearLayout.findViewById(R.id.rlLogout).setOnClickListener(this);

//        mList = (ListView) relativeLayout.findViewById(R.id.listView);
//        relativeLayout.removeView(mList);
//
//        mAdapter = new MenuAdapter(this, items);
//        mAdapter.setListener(this);
//        mAdapter.setActivePosition(mActivePosition);
//
//        mList.setAdapter(mAdapter);
//        mList.setOnItemClickListener(mItemClickListener);
//
//        mList.setDivider(new ColorDrawable(Color.rgb(16, 120, 206)));
//        mList.setDividerHeight(MathUtils.dip2px(this, 0));

        mMenuDrawer.setMenuView(linearLayout);
        mMenuDrawer.getMenuContainer().setBackgroundColor(getColorFromResuource(R.color.transparent));
        mMenuDrawer.setMenuSize((int) (AppInfo.getScreenWidthOrHeight(this, true) * 0.666f));
    }

    @Override
    protected void init() {
        super.init();
    }

    protected abstract void onMenuItemClicked(int position, Item item);

    protected abstract int getDragMode();

    protected abstract Position getDrawerPosition();

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            mActivePosition = position;
            mMenuDrawer.setActiveView(view, position);
            mAdapter.setActivePosition(position);
            onMenuItemClicked(position, (Item) mAdapter.getItem(position));
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_ACTIVE_POSITION, mActivePosition);
    }

    @Override
    public void onActiveViewChanged(View v) {
        mMenuDrawer.setActiveView(v, mActivePosition);
    }

    @SuppressWarnings("TypeParameterUnusedInFormals")
    protected <T extends View> T findAndSetClickListener(@IdRes int id) {
        View view = findViewById(id);
        view.setOnClickListener(this);
        return (T) view;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rlCard: // 编辑名片
                startMyActivity(EditVcardActivity.class);
                break;
            case R.id.rlPrivacy: // 隐私设置
                startMyActivity(PrivacySettingActivity.class);
                break;
            case R.id.rlSafe: // 账户安全
                startMyActivity(AccountSecurityActivity.class);
                break;
            case R.id.rlLogout: // 注销
                logout();
                break;
        }
    }

    /**
     * 注销登出
     */
    public void logout() {
        ZhanHuiApplication.getInstance().logout();
        startMyActivity(LoginActivity.class);
        finish();
    }

    /**
     * 有提示信息确认的注销（例如：已在其他设备登录）
     * @param message
     */
    public void logoutWithInfo(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showAlertDialog("", message, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }, "", null, false);
            }
        });
    }
}
