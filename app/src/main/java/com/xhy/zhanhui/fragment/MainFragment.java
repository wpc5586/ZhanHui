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
import com.aaron.aaronlibrary.utils.Constants;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.IBusinessReceiveListActivity;
import com.xhy.zhanhui.activity.IntelligentBusinessListActivity;
import com.xhy.zhanhui.activity.IntelligentBusinessReleaseActivity;
import com.xhy.zhanhui.activity.MainActivity;
import com.xhy.zhanhui.activity.OrderActivity;
import com.xhy.zhanhui.activity.QRCodeActivity;
import com.xhy.zhanhui.activity.ReceiveTrustActivity;
import com.xhy.zhanhui.activity.RecordActivity;
import com.xhy.zhanhui.activity.SendTrustActivity;
import com.xhy.zhanhui.activity.StaticsActivity;
import com.xhy.zhanhui.activity.TicketActivity;
import com.xhy.zhanhui.base.ZhanHuiApplication;
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.HxFriendBean;
import com.xhy.zhanhui.preferences.IBusinessSharedPreferences;
import com.xhy.zhanhui.preferences.TrustSharedPreferences;

import java.util.Collections;
import java.util.List;

/**
 * 首页Fragment
 * Created by Aaron on 09/12/2017.
 */

public class MainFragment extends ZhanHuiFragment {

    private RelativeLayout rlSend, rlReceiveTrust, rlRelease, rlDemand, rlReceive, rlOrder, rlTicket, rlGuide, rlNavi, rlRecord, rlStatics;
    private LinearLayout llTrustMessage1, llTrustMessage2, llTrustMessage3, llTrustMessage4;
    private ImageView ivNewTrust, ivNewIBusiness, ivAvatar;
    private TextView tvName, tvCardIntro;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void findViews(View view) {
        findViewAndSetListener(R.id.menu);
        findViewAndSetListener(R.id.qr_code);
        findViewAndSetListener(R.id.rlCard);
        findViewAndSetListener(R.id.rlTrustMessage);
        rlSend = findViewAndSetListener(R.id.rlSend);
        rlReceiveTrust = findViewAndSetListener(R.id.rlReceive);
        rlRelease = findViewAndSetListener(R.id.rl_release);
        rlDemand = findViewAndSetListener(R.id.rl_demand);
        rlReceive = findViewAndSetListener(R.id.rl_receive);
        if (Constants.VERSION_IS_USER)
            rlReceive.setVisibility(View.GONE);
        else {
            rlDemand.setVisibility(View.GONE);
            rlRelease.setVisibility(View.GONE);
        }
        rlOrder = findViewAndSetListener(R.id.rlOrder);
        rlTicket = findViewAndSetListener(R.id.rlTicket);
        rlGuide = findViewAndSetListener(R.id.rlGuide);
        rlNavi = findViewAndSetListener(R.id.rlNavi);
        rlRecord = findViewAndSetListener(R.id.rlRecord);
        rlStatics = findViewAndSetListener(R.id.rlStatics);
        llTrustMessage1 = findViewAndSetListener(R.id.llTrustMessage1);
        llTrustMessage2 = findViewAndSetListener(R.id.llTrustMessage2);
        llTrustMessage3 = findViewAndSetListener(R.id.llTrustMessage3);
        llTrustMessage4 = findViewAndSetListener(R.id.llTrustMessage4);
        ivNewTrust = findViewById(R.id.ivNewTrust);
        ivNewIBusiness = findViewById(R.id.ivNewIBusiness);
        ivAvatar = findViewById(R.id.ivAvatar);
        tvName = findViewById(R.id.tvName);
        tvCardIntro = findViewById(R.id.tvCardIntro);
    }

    @Override
    protected void init() {
        super.init();
        initWidget();
        getUnreadMessage();
        // 加载用户头像和名称
//        ImageUtils.loadImageCircle(mContext, ZhanHuiApplication.getInstance().getIcon(), ivAvatar);
        tvName.setText(ZhanHuiApplication.getInstance().getNickname());
        if (!Constants.VERSION_IS_USER)
            tvCardIntro.setText("企业数字名片");
    }

    private void initWidget() {
        int width = (AppInfo.getScreenWidthOrHeight(mContext, true) - 4) / 3;
        int width1 = (AppInfo.getScreenWidthOrHeight(mContext, true) - 2) / 2;
        rlRelease.getLayoutParams().width = width1;
        rlDemand.getLayoutParams().width = width1;
        rlReceive.getLayoutParams().width = AppInfo.getScreenWidthOrHeight(mContext, true);
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
            final InviteMessage msg = msgs.get(0);
            final String username = msg.getFrom();
            final TextView textView = (TextView) llTrustMessage2.getChildAt(1);
            PostCall.get(mContext, ServerUrl.hxidFriend(username), new BaseMap(), new PostCall.PostResponse<HxFriendBean>() {
                @Override
                public void onSuccess(int i, byte[] bytes, HxFriendBean bean) {
                    HxFriendBean.Obj data = bean.getData();
                    switch (msg.getStatus()) {
                        case BEINVITEED:
                            if (isFriend(username, true))
                                textView.setText("已同意" + data.getNickname() + "的申请");
                            else
                                textView.setText(data.getNickname() + "等待您处理申请");
                            break;
                        case BEREFUSED:
                            textView.setText("已拒绝" + data.getNickname() + "的申请");
                            break;
                        case BEAGREED:
                            textView.setText("已同意" + data.getNickname() + "的申请");
                            break;
                    }
                    ivNewTrust.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(int i, byte[] bytes) {

                }
            }, new String[]{"", ""}, false, HxFriendBean.class);
        } else {

        }
        String newSendTrust = TrustSharedPreferences.getInstance().getTrustData();
        if (!TextUtils.isEmpty(newSendTrust)) {
            TextView textView = (TextView) llTrustMessage1.getChildAt(1);
            textView.setText(newSendTrust);
        }
        String newIBusiness = IBusinessSharedPreferences.getInstance().getIBusinessData();
        if (!TextUtils.isEmpty(newIBusiness)) {
            TextView textView = (TextView) llTrustMessage3.getChildAt(1);
            textView.setText(newIBusiness);
            ivNewIBusiness.setVisibility(View.VISIBLE);
        }
        llTrustMessage1.setVisibility(View.VISIBLE);
        llTrustMessage2.setVisibility(View.VISIBLE);
        llTrustMessage3.setVisibility(View.VISIBLE);
        llTrustMessage4.setVisibility(View.VISIBLE);
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
            case R.id.rlCard: // 上面点击  个人名片
                if (!isVcardIdZero())
                    StartActivityUtils.startVcard(mContext, getUserId());
                break;
            case R.id.llTrustMessage1:
            case R.id.rlSend: // 发出的申请
                startMyActivity(SendTrustActivity.class);
                ivNewTrust.setVisibility(View.INVISIBLE);
                break;
            case R.id.llTrustMessage2:
            case R.id.rlReceive: // 收到的邀请
                startMyActivity(ReceiveTrustActivity.class);
                ivNewTrust.setVisibility(View.INVISIBLE);
                break;
            case R.id.rl_release: // 智能商务-需求发布
                startMyActivity(IntelligentBusinessReleaseActivity.class);
                break;
            case R.id.rl_demand: // 智能商务-需求匹配
                startMyActivity(IntelligentBusinessListActivity.class);
                break;
            case R.id.rl_receive: // 智能商务-收到的申请
                startMyActivity(IBusinessReceiveListActivity.class);
                break;
            case R.id.rlOrder: // 我的预约
                startMyActivity(OrderActivity.class);
                break;
            case R.id.rlTicket: // 电子门票
                startMyActivity(TicketActivity.class);
                break;
            case R.id.rlGuide: // 参展指南
                StartActivityUtils.startWebUrl(mContext, ServerUrl.expoGuide(), "参展指南");
                break;
            case R.id.rlNavi: // 电子导览
                StartActivityUtils.startWebUrl(mContext, ServerUrl.expoMap(), "电子导览");
                break;
            case R.id.rlRecord: // 参展记录
                startMyActivity(RecordActivity.class);
                break;
            case R.id.rlStatics: // 统计分析
                startMyActivity(StaticsActivity.class);
                break;
        }
    }

    /**
     * 刷新最新发出的信任邀请
     */
    public void refresh() {
        getUnreadMessage();
    }

    /**
     * 刷新新设置的昵称
     */
    public void refreshName() {
        tvName.setText(ZhanHuiApplication.getInstance().getNickname());
    }
}
