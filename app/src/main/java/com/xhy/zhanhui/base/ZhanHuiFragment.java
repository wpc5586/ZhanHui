package com.xhy.zhanhui.base;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;

import com.aaron.aaronlibrary.base.fragment.BaseFragment;
import com.aaron.aaronlibrary.easeui.DemoHelper;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.EditVcardActivity;

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
     * 根据size判断是否显示无数据背景
     * @param size 数据数量
     */
    protected void showNoDataBg(int size) {
        ImageView imageView = findViewById(R.id.ivNoData);
        if (size == 0)
            imageView.setVisibility(View.VISIBLE);
        else
            imageView.setVisibility(View.GONE);
    }

    /**
     * 获取用户ID
     * @return
     */
    protected String getUserId() {
        return ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 获取企业用户企业ID
     *
     * @return
     */
    protected String getCompanyId() {
        return ZhanHuiApplication.getInstance().getCompanyId();
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
     * 判断当前账户的vcard_id是否为0（系统默认值），如果为0，需要先去设置个人名片，才可显示个人名片或者添加他人信任
     * @return
     */
    protected boolean isVcardIdZero() {
        String vcardId = ZhanHuiApplication.getInstance().getVcardId();
        if ("0".equals(vcardId)) {
            showAlertDialog("", "个人名片信息不完善，请先完善个人名片信息。", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startMyActivity(EditVcardActivity.class);
                }
            }, "", null, false);
            return true;
        } else
            return false;
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
