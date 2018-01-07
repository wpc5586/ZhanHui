package com.xhy.zhanhui.base;

import android.os.Build;

import com.aaron.aaronlibrary.base.activity.BaseActivity;
import com.aaron.aaronlibrary.easeui.DemoHelper;
import com.aaron.aaronlibrary.easeui.EaseUI;
import com.aaron.aaronlibrary.easeui.db.DemoDBManager;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.hyphenate.chat.EMClient;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.EditVcardActivity;

/**
 * Activity基类
 * Created by Aaron on 09/12/2017.
 */

public class ZhanHuiActivity extends BaseActivity {
    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void init() {
        super.init();
        setActionbarBackground(R.color.white);
        setActionbarDividerVisibility(true);
        // 设置状态栏背景颜色
        if (Build.VERSION.SDK_INT < 21)
            initSystemBar();
        EaseUI.getInstance().pushActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(this);
    }

    /**
     * 获取用户ID
     * @return
     */
    protected String getUserId() {
        return ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 根据环信ID获取用户ID
     * @return
     */
    protected String getUserIdFromHxId(String hxId) {
        return DemoHelper.getInstance().getContactList().get(hxId).getUserId();
    }

    /**
     * 获取环信用户ID
     * @return
     */
    protected String getHxUserId() {
        return ZhanHuiApplication.getInstance().getHxUserId();
    }

    /**
     * 根据用户ID获取环信用户ID
     * @return
     */
    protected String getHxUserIdFromId(String id) {
        return DemoHelper.getInstance().getContactListNe().get(id).getUserId();
    }

    /**
     * 获取用户账号
     * @return
     */
    protected String getUserName() {
        return ZhanHuiApplication.getInstance().getUserName();
    }

    /**
     * 判断当前账户的vcard_id是否为0（系统默认值），如果为0，需要先去设置个人名片，才可显示个人名片或者添加他人信任
     * @return
     */
    protected boolean isVcardIdZero() {
        String vcardId = ZhanHuiApplication.getInstance().getVcardId();
        if ("0".equals(vcardId)) {
            showToast("您需要先设置个人名片");
            startMyActivity(EditVcardActivity.class);
            return true;
        } else
            return false;
    }

    /**
     * 获取用户头像
     * @return
     */
    protected String getUserAvatar() {
        return ZhanHuiApplication.getInstance().getIcon();
    }

    /**
     * 判断是否是朋友关系
     * @param userId id
     * @param isHx true：userId是环信ID false：是本系统ID
     * @return
     */
    protected boolean isFriend(String userId, boolean isHx) {
        if (isHx) {
            return DemoHelper.getInstance().getContactList().get(userId) != null;
        } else {
            return DemoHelper.getInstance().getContactListNe().get(userId) != null;
        }
    }

}
