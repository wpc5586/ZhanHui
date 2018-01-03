package com.xhy.zhanhui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.aaron.aaronlibrary.easeui.DemoHelper;
import com.aaron.aaronlibrary.easeui.db.InviteMessgeDao;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.hyphenate.EMContactListener;
import com.hyphenate.chat.EMClient;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.MainActivity;
import com.xhy.zhanhui.activity.OrderActivity;
import com.xhy.zhanhui.activity.QRCodeActivity;
import com.xhy.zhanhui.activity.ReceiveTrustActivity;
import com.xhy.zhanhui.activity.SendTrustActivity;
import com.xhy.zhanhui.activity.VcardUserActivity;
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.domain.StartActivityUtils;

import java.util.ArrayList;

/**
 * 首页Fragment
 * Created by Aaron on 09/12/2017.
 */

public class MainFragment extends ZhanHuiFragment {

    private RelativeLayout rlSend, rlReceiveTrust, rlRelease, rlDemand, rlReceive, rlOrder, rlTicket, rlGuide, rlNavi, rlRecord, rlStatics;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void findViews(View view) {
        findViewAndSetListener(R.id.menu);
        findViewAndSetListener(R.id.qr_code);
        findViewAndSetListener(R.id.rlUp);
        rlSend = findViewAndSetListener(R.id.rlSend);
        rlReceiveTrust = findViewAndSetListener(R.id.rlReceive);
        rlRelease = findViewAndSetListener(R.id.rl_release);
        rlDemand = findViewAndSetListener(R.id.rl_demand);
        rlReceive = findViewAndSetListener(R.id.rl_receive);
        rlOrder = findViewAndSetListener(R.id.rlOrder);
        rlTicket = findViewAndSetListener(R.id.rlTicket);
        rlGuide = findViewAndSetListener(R.id.rlGuide);
        rlNavi = findViewAndSetListener(R.id.rlNavi);
        rlRecord = findViewAndSetListener(R.id.rlRecord);
        rlStatics = findViewAndSetListener(R.id.rlStatics);
    }

    @Override
    protected void init() {
        super.init();
        initWidget();
        getUnreadMessage();
    }

    private void initWidget() {
        int width = (AppInfo.getScreenWidthOrHeight(mContext, true) - 4) / 3;
        rlRelease.getLayoutParams().width = width;
        rlDemand.getLayoutParams().width = width;
        rlReceive.getLayoutParams().width = width;
        rlOrder.getLayoutParams().width = width;
        rlTicket.getLayoutParams().width = width;
        rlGuide.getLayoutParams().width = width;
        rlNavi.getLayoutParams().width = width;
        rlRecord.getLayoutParams().width = width;
        rlStatics.getLayoutParams().width = width;
    }

    /**
     * 获取未读消息（收到的和发出的邀请）
     */
    private void getUnreadMessage() {
        EMClient.getInstance().contactManager().setContactListener(new EMContactListener() {
            @Override
            public void onContactAdded(String username) {
                
            }

            @Override
            public void onContactDeleted(String username) {

            }

            @Override
            public void onContactInvited(String username, String reason) {
                System.out.println("~!~ invite name = " + username + ", reason = " + reason);
            }

            @Override
            public void onFriendRequestAccepted(String username) {

            }

            @Override
            public void onFriendRequestDeclined(String username) {

            }
        });
        int unreadCount = new InviteMessgeDao(getActivity()).getUnreadMessagesCount();
        System.out.println("~!~ unreadCount = " + unreadCount);
        if (unreadCount > 0) {

        } else {

        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.menu:
                MainActivity.getInstance().openMenu();
                break;
            case R.id.qr_code: // 二维码
                startMyActivity(QRCodeActivity.class);
                break;
            case R.id.rlUp: // 上面点击  个人名片
                StartActivityUtils.startVcard(mContext, getUserId());
                break;
            case R.id.rlSend: // 发出的申请
                startMyActivity(SendTrustActivity.class);
                break;
            case R.id.rlReceive: // 收到的邀请
                startMyActivity(ReceiveTrustActivity.class);
                break;
            case R.id.rlOrder: // 我的预约
                startMyActivity(OrderActivity.class);
                break;
        }
    }
}
