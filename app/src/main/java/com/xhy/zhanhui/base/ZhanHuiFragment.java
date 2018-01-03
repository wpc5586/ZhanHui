package com.xhy.zhanhui.base;

import android.view.View;

import com.aaron.aaronlibrary.base.fragment.BaseFragment;
import com.aaron.aaronlibrary.easeui.DemoHelper;

/**
 * Fragment基类
 * Created by Aaron on 09/12/2017.
 */

public class ZhanHuiFragment extends BaseFragment {
    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected void findViews(View view) {

    }

    @Override
    protected void init() {

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
