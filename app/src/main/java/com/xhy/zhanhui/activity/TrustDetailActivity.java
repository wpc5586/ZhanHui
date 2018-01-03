package com.xhy.zhanhui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.google.gson.Gson;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.TrustDetailBean;
import com.xhy.zhanhui.http.vo.ReceiveTrustVo;

/**
 * 邀请详情页面
 * Created by Aaron on 25/12/2017.
 */

public class TrustDetailActivity extends ZhanHuiActivity{

    private TrustDetailBean.Obj data;
    private boolean isChange = false; // 是否已经接受或者拒绝改变了状态，是的话finish返回OK
    private String requestId, acceptId, nickName;
    private boolean isReceive; // 是否为收到邀请详情
    private String state = "1"; // 1:好友申请中 2:已同意好友关系 3:已拒绝好友申请 4:已解除好友关系
    private String flag = "1"; //  1:公司收到用户申请 2:用户收到公司邀请
    private ImageView ivAvatar, iv1, iv2, iv3;
    private LinearLayout llButton, llIntro;
    private RelativeLayout rlBottom;
    private View divider;
    private Button btn1, btn2;
    private TextView tvName, tvIntro, tvInfo, tvDate1, tvTime1, tvDate2, tvTime2, tvDate3, tvTime3, tvContent1, tvContent2, tvContent3;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_trust_detail;
    }

    @Override
    protected void findView() {
        super.findView();
        ivAvatar = findAndSetClickListener(R.id.ivAvatar);
        tvName = findViewById(R.id.tvName);
        tvIntro = findViewById(R.id.tvIntro);
        tvInfo = findViewById(R.id.tvInfo);
        tvDate1 = findViewById(R.id.tvDate1);
        tvTime1 = findViewById(R.id.tvTime1);
        tvDate2 = findViewById(R.id.tvDate2);
        tvTime2 = findViewById(R.id.tvTime2);
        tvDate3 = findViewById(R.id.tvDate3);
        tvTime3 = findViewById(R.id.tvTime3);
        tvContent1 = findViewById(R.id.tv1);
        tvContent2 = findViewById(R.id.tv2);
        tvContent3 = findViewById(R.id.tv3);
        iv1 = findViewById(R.id.iv1);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        llButton = findViewById(R.id.llButton);
        llIntro = findViewById(R.id.llIntro);
        btn1 = findAndSetClickListener(R.id.btn1);
        btn2 = findAndSetClickListener(R.id.btn2);
        rlBottom = findViewById(R.id.rlBottom);
        divider = findViewById(R.id.divider);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("");
        if (getIntent().hasExtra("request_id"))
            requestId = getIntent().getStringExtra("request_id");
        if (getIntent().hasExtra("accept_id"))
            acceptId = getIntent().getStringExtra("accept_id");
        if (getIntent().hasExtra("is_receive"))
            isReceive = getIntent().getBooleanExtra("is_receive", true);
        if (isReceive) {

        } else {
            llIntro.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            ((RelativeLayout.LayoutParams) tvInfo.getLayoutParams()).bottomMargin = MathUtils.dip2px(mContext, 15);
            ((RelativeLayout.LayoutParams) llButton.getLayoutParams()).bottomMargin = MathUtils.dip2px(mContext, 15);
        }
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        PostCall.get(mContext, isReceive ? ServerUrl.acceptInvitations(requestId) : ServerUrl.requestInvitations(acceptId), new BaseMap(), new PostCall.PostResponse<TrustDetailBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, TrustDetailBean bean) {
                data = bean.getObj();
                ImageUtils.loadImageCircle(mContext, data.getIcon(), ivAvatar);
                nickName = data.getNickname();
                tvName.setText(nickName);
                state = data.getState();
                flag = data.getFlag();
                if (isReceive)
                    setReceive();
                else
                    setSend();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, TrustDetailBean.class);
    }

    /**
     * 设置收到的申请数据
     */
    private void setReceive() {
        if ("1".equals(flag)) {
            // 公司收到用户申请
            tvIntro.setText(data.getV_title());
            tvIntro.setVisibility(View.VISIBLE);
            ((LinearLayout.LayoutParams) rlBottom.getLayoutParams()).topMargin = 0;
            tvContent1.setText("收到信任申请");
            tvInfo.setText("向您发送信任申请");
        } else {
            // 用户收到公司邀请
            tvIntro.setVisibility(View.GONE);
            ((LinearLayout.LayoutParams) rlBottom.getLayoutParams()).topMargin = MathUtils.dip2px(mContext, 15);
            tvContent1.setText("收到信任邀请");
            tvInfo.setText("向您发送信任邀请");
        }
        String[] time1 = data.getRequest_time().split("\n");
        tvDate1.setText(time1[0]);
        tvTime1.setText(time1[1]);
        String[] time2 = null;
        if (!TextUtils.isEmpty(data.getOperation_time()))
            time2 = data.getOperation_time().split("\n");
        switch (state) {
            case "1":
                llButton.setVisibility(View.VISIBLE);
                iv2.setImageResource(R.mipmap.icon_clock);
                iv3.setImageResource(R.mipmap.icon_clock);
                tvContent2.setTextColor(getColorFromResuource(R.color.theme_black));
                tvContent3.setTextColor(getColorFromResuource(R.color.theme_black));
                if ("1".equals(flag)) {
                    tvContent2.setText("等待同意申请");
                } else {
                    tvContent2.setText("等待您接受");
                }
                tvContent3.setText("等待互换电子名片\n等待建立信任关系");
                break;
            case "2":
                btn2.setVisibility(View.GONE);
                btn1.getLayoutParams().width = (int) (AppInfo.getScreenWidthOrHeight(mContext, true) * 0.46f);
                ((LinearLayout.LayoutParams) btn1.getLayoutParams()).weight = 0;
                btn1.setText("交谈");
                llButton.setVisibility(View.VISIBLE);
                tvInfo.setVisibility(View.GONE);
                if (time2 != null) {
                    tvDate2.setVisibility(View.VISIBLE);
                    tvTime2.setVisibility(View.VISIBLE);
                    tvDate3.setVisibility(View.VISIBLE);
                    tvTime3.setVisibility(View.VISIBLE);
                    tvDate2.setText(time2[0]);
                    tvTime2.setText(time2[1]);
                    tvDate3.setText(time2[0]);
                    tvTime3.setText(time2[1]);
                }
                iv2.setImageResource(R.mipmap.icon_right);
                iv3.setImageResource(R.mipmap.icon_right);
                if ("1".equals(flag)) {
                    tvContent2.setText("确认申请");
                } else {
                    tvContent2.setText("已接受邀请");
                }
                tvContent3.setText("互换电子名片成功\n建立信任关系成功");
                break;
            case "3":
                llButton.setVisibility(View.GONE);
                if (time2 != null) {
                    tvDate2.setVisibility(View.VISIBLE);
                    tvTime2.setVisibility(View.VISIBLE);
                    tvDate3.setVisibility(View.VISIBLE);
                    tvTime3.setVisibility(View.VISIBLE);
                    tvDate2.setTextColor(getColorFromResuource(R.color.theme));
                    tvTime2.setTextColor(getColorFromResuource(R.color.theme));
                    tvDate3.setTextColor(getColorFromResuource(R.color.theme));
                    tvTime3.setTextColor(getColorFromResuource(R.color.theme));
                    tvDate2.setText(time2[0]);
                    tvTime2.setText(time2[1]);
                    tvDate3.setText(time2[0]);
                    tvTime3.setText(time2[1]);
                }
                iv2.setImageResource(R.mipmap.icon_refuse);
                iv3.setImageResource(R.mipmap.icon_refuse);
                tvContent2.setTextColor(getColorFromResuource(R.color.theme));
                tvContent3.setTextColor(getColorFromResuource(R.color.theme));
                if ("1".equals(flag)) {
                    tvInfo.setText("您已拒绝对方信任申请");
                    tvContent2.setText("已拒绝对方申请");
                } else {
                    tvInfo.setText("您已拒绝对方信任邀请");
                    tvContent2.setText("已拒绝对方邀请");
                }
                tvContent3.setText("互换电子名片失败\n建立信任关系失败");
                break;
            case "4":
                llButton.setVisibility(View.GONE);
                tvInfo.setText("已解除与对方好友关系");
                if (time2 != null) {
                    tvDate2.setVisibility(View.VISIBLE);
                    tvTime2.setVisibility(View.VISIBLE);
                    tvDate3.setVisibility(View.VISIBLE);
                    tvTime3.setVisibility(View.VISIBLE);
                    tvDate2.setText(time2[0]);
                    tvTime2.setText(time2[1]);
                    tvDate3.setText(time2[0]);
                    tvTime3.setText(time2[1]);
                }
                iv2.setImageResource(R.mipmap.icon_right);
                iv3.setImageResource(R.mipmap.icon_right);
                if ("1".equals(flag)) {
                    tvContent2.setText("确认申请");
                } else {
                    tvContent2.setText("已接受邀请");
                }
                tvContent3.setText("互换电子名片成功\n建立信任关系成功");
                break;
        }
    }

    /**
     * 设置发送的申请数据
     */
    private void setSend() {
        if ("2".equals(flag)) {
            // 发送用户邀请
            tvIntro.setText(data.getV_title());
            tvIntro.setVisibility(View.VISIBLE);
//            ((LinearLayout.LayoutParams) rlBottom.getLayoutParams()).topMargin = 0;


            tvContent1.setText("发送信任申请");
            tvInfo.setText("正在处理您的信任申请");
        } else {
            // 发送公司申请
            tvIntro.setVisibility(View.GONE);
//            ((LinearLayout.LayoutParams) rlBottom.getLayoutParams()).topMargin = MathUtils.dip2px(mContext, 15);
            tvContent1.setText("发送信任邀请");
            tvInfo.setText("正在处理您的信任邀请");
        }
        String[] time1 = data.getRequest_time().split("\n");
        tvDate1.setText(time1[0]);
        tvTime1.setText(time1[1]);
        String[] time2 = null;
        if (!TextUtils.isEmpty(data.getOperation_time()))
            time2 = data.getOperation_time().split("\n");
        switch (state) {
            case "1":
                llButton.setVisibility(View.GONE);
                tvIntro.setVisibility(View.GONE);
                iv2.setImageResource(R.mipmap.icon_clock);
                iv3.setImageResource(R.mipmap.icon_clock);
                tvContent2.setTextColor(getColorFromResuource(R.color.theme_black));
                tvContent3.setTextColor(getColorFromResuource(R.color.theme_black));
                if ("2".equals(flag)) {
                    tvContent2.setText("等待对方接受");
                } else {
                    tvContent2.setText("等待对方同意");
                }
                tvContent3.setText("等待互换电子名片\n等待建立信任关系");
                break;
            case "2":
                btn2.setVisibility(View.GONE);
                btn1.getLayoutParams().width = (int) (AppInfo.getScreenWidthOrHeight(mContext, true) * 0.46f);
                ((LinearLayout.LayoutParams) btn1.getLayoutParams()).weight = 0;
                btn1.setText("交谈");
                llButton.setVisibility(View.VISIBLE);
                tvInfo.setVisibility(View.GONE);
                if (time2 != null) {
                    tvDate2.setVisibility(View.VISIBLE);
                    tvTime2.setVisibility(View.VISIBLE);
                    tvDate3.setVisibility(View.VISIBLE);
                    tvTime3.setVisibility(View.VISIBLE);
                    tvDate2.setText(time2[0]);
                    tvTime2.setText(time2[1]);
                    tvDate3.setText(time2[0]);
                    tvTime3.setText(time2[1]);
                }
                iv2.setImageResource(R.mipmap.icon_right);
                iv3.setImageResource(R.mipmap.icon_right);
                if ("2".equals(flag)) {
                    tvContent2.setText("对方已接受");
                } else {
                    tvContent2.setText("对方已同意");
                }
                tvContent3.setText("互换电子名片成功\n建立信任关系成功");
                break;
            case "3":
                llButton.setVisibility(View.GONE);
                if (time2 != null) {
                    tvDate2.setVisibility(View.VISIBLE);
                    tvTime2.setVisibility(View.VISIBLE);
                    tvDate3.setVisibility(View.VISIBLE);
                    tvTime3.setVisibility(View.VISIBLE);
                    tvDate2.setTextColor(getColorFromResuource(R.color.theme));
                    tvTime2.setTextColor(getColorFromResuource(R.color.theme));
                    tvDate3.setTextColor(getColorFromResuource(R.color.theme));
                    tvTime3.setTextColor(getColorFromResuource(R.color.theme));
                    tvDate2.setText(time2[0]);
                    tvTime2.setText(time2[1]);
                    tvDate3.setText(time2[0]);
                    tvTime3.setText(time2[1]);
                }
                iv2.setImageResource(R.mipmap.icon_refuse);
                iv3.setImageResource(R.mipmap.icon_refuse);
                tvContent2.setTextColor(getColorFromResuource(R.color.theme));
                tvContent3.setTextColor(getColorFromResuource(R.color.theme));
                if ("2".equals(flag)) {
                    tvInfo.setText("已拒绝您的信任申请");
                    tvContent2.setText("对方已拒绝");
                } else {
                    tvInfo.setText("已拒绝您的信任邀请");
                    tvContent2.setText("对方已拒绝");
                }
                tvContent3.setText("互换电子名片失败\n建立信任关系失败");
                break;
            case "4":
                llButton.setVisibility(View.GONE);
                tvInfo.setText("已解除与对方好友关系");
                if (time2 != null) {
                    tvDate2.setVisibility(View.VISIBLE);
                    tvTime2.setVisibility(View.VISIBLE);
                    tvDate3.setVisibility(View.VISIBLE);
                    tvTime3.setVisibility(View.VISIBLE);
                    tvDate2.setText(time2[0]);
                    tvTime2.setText(time2[1]);
                    tvDate3.setText(time2[0]);
                    tvTime3.setText(time2[1]);
                }
                iv2.setImageResource(R.mipmap.icon_right);
                iv3.setImageResource(R.mipmap.icon_right);
                if ("2".equals(flag)) {
                    tvContent2.setText("对方已接受");
                } else {
                    tvContent2.setText("对方已同意");
                }
                tvContent3.setText("互换电子名片成功\n建立信任关系成功");
                break;
        }
    }

    /**
     * 接受
     */
    private void accept() { //ServerUrl.getTrustDetail(requestId, acceptId)
        PostCall.putJson(mContext, ServerUrl.acceptFriend(), new ReceiveTrustVo(requestId, acceptId), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                showToast("接受成功");
                isChange = true;
                getData();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, BaseBean.class);
    }

    /**
     * 拒绝
     */
    private void refuse() {
        PostCall.putJson(mContext, ServerUrl.declineFriend(), new ReceiveTrustVo(requestId, acceptId), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                showToast("拒绝成功");
                isChange = true;
                getData();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, BaseBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivAvatar:
                StartActivityUtils.startVcardNoQRcode(mContext, isReceive ? data.getRequest_id() : data.getAccept_id());
                break;
            case R.id.btn1:
                String content = ((Button) view).getText().toString();
                if ("接受".equals(content)) {
                    accept();
                } else if ("交谈".equals(content)) {
                    StartActivityUtils.startChat(mContext, data.getHx_username(), nickName);
                }
                break;
            case R.id.btn2:
                refuse();
                break;
        }
    }

    @Override
    public void finish() {
        if (isChange)
            setResult(RESULT_OK);
        super.finish();
    }
}
