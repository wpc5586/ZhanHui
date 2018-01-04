package com.xhy.zhanhui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.easeui.db.InviteMessgeDao;
import com.aaron.aaronlibrary.easeui.domain.InviteMessage;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.MainActivity;
import com.xhy.zhanhui.activity.OrderActivity;
import com.xhy.zhanhui.activity.QRCodeActivity;
import com.xhy.zhanhui.activity.ReceiveTrustActivity;
import com.xhy.zhanhui.activity.SendTrustActivity;
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.HxFriendBean;
import com.xhy.zhanhui.preferences.TrustSharedPreferences;

import java.util.Collections;
import java.util.List;

/**
 * 首页Fragment
 * Created by Aaron on 09/12/2017.
 */

public class MainFragment extends ZhanHuiFragment {

    private RelativeLayout rlSend, rlReceiveTrust, rlRelease, rlDemand, rlReceive, rlOrder, rlTicket, rlGuide, rlNavi, rlRecord, rlStatics;
    private LinearLayout llTrustMessage1, llTrustMessage2;
    private ImageView ivNewTrust;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void findViews(View view) {
        findViewAndSetListener(R.id.menu);
        findViewAndSetListener(R.id.qr_code);
        findViewAndSetListener(R.id.rlUp);
        findViewAndSetListener(R.id.rlTrustMessage);
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
        llTrustMessage1 = findViewById(R.id.llTrustMessage1);
        llTrustMessage2 = findViewById(R.id.llTrustMessage2);
        ivNewTrust = findViewById(R.id.ivNewTrust);
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
     * 获取未读消息（设置显示收到的和发出的邀请）
     */
    private void getUnreadMessage() {
        InviteMessgeDao dao = new InviteMessgeDao(mContext);
        List<InviteMessage> msgs = dao.getMessagesList();
        Collections.reverse(msgs);
        if (msgs.size() > 0) {
            ivNewTrust.setVisibility(View.VISIBLE);
            llTrustMessage1.setVisibility(View.VISIBLE);
            final InviteMessage msg = msgs.get(0);
            String username = msg.getFrom();
            final TextView textView = (TextView) llTrustMessage1.getChildAt(1);
            PostCall.get(mContext, ServerUrl.hxidFriend(username), new BaseMap(), new PostCall.PostResponse<HxFriendBean>() {
                @Override
                public void onSuccess(int i, byte[] bytes, HxFriendBean bean) {
                    HxFriendBean.Obj data = bean.getData();
                    switch (msg.getStatus()) {
                        case BEINVITEED:
                            textView.setText("等待" + data.getNickname() + "同意申请");
                            break;
                        case BEREFUSED:
                            textView.setText(data.getNickname() + "已拒绝申请");
                            break;
                        case BEAGREED:
                            textView.setText(data.getNickname() + "已同意申请");
                            break;
                    }
                }

                @Override
                public void onFailure(int i, byte[] bytes) {

                }
            }, new String[]{"", ""}, false, HxFriendBean.class);
        } else {

        }
        String newSendTrust = TrustSharedPreferences.getInstance().getTrustData();
        if (!TextUtils.isEmpty(newSendTrust)) {
            ivNewTrust.setVisibility(View.VISIBLE);
            llTrustMessage2.setVisibility(View.VISIBLE);
            TextView textView = (TextView) llTrustMessage2.getChildAt(1);
            textView.setText(newSendTrust);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.menu: //  菜单
                MainActivity.getInstance().openMenu();
                break;
            case R.id.rlTrustMessage: // 商务社交-信任消息
                ivNewTrust.setVisibility(View.INVISIBLE);
                break;
            case R.id.qr_code: // 二维码
                startMyActivity(QRCodeActivity.class);
                break;
            case R.id.rlUp: // 上面点击  个人名片
                if (!isVcardIdZero())
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

    /**
     * 刷新最新发出的信任邀请
     */
    public void refresh() {
        getUnreadMessage();
    }
}
