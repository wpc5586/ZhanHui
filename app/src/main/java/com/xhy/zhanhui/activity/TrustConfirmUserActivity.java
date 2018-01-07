package com.xhy.zhanhui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.easeui.utils.EaseCommonUtils;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.hyphenate.EMCallBack;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.domain.OfflineScanFriendBean;
import com.xhy.zhanhui.http.domain.TrustCompanyBean;
import com.xhy.zhanhui.http.domain.TrustUserBean;
import com.xhy.zhanhui.http.vo.OfflineTrustVo;
import com.xhy.zhanhui.http.vo.TrustVo;
import com.xhy.zhanhui.preferences.TrustSharedPreferences;

/**
 * 信任确认页面-用户页面
 * Created by Aaron on 15/12/2017.
 */

public class TrustConfirmUserActivity extends ZhanHuiActivity {

    private String vcardNo, timestp;
    private TrustUserBean.Obj trustData;
    private OfflineScanFriendBean.Obj offlineData;
    private boolean isOffline; // 是否是线下扫描二维码跳转
    private ImageView ivAvatar;
    private TextView tvName, tvIntro, tvCompany;
    private EditText editText;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_trust_confirm_user;
    }

    @Override
    protected void findView() {
        super.findView();
        ivAvatar = findViewById(R.id.ivAvatar);
        tvName = findViewById(R.id.tvName);
        tvIntro = findViewById(R.id.tvIntro);
        tvCompany = findViewById(R.id.tvCompany);
        editText = findViewById(R.id.etMessage);
        findAndSetClickListener(R.id.btnCommit);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("资料详情");
        vcardNo = getStringExtra("vcard_no");
        timestp = getStringExtra("timestp");
        if (getIntent().hasExtra("trustData"))
            trustData = (TrustUserBean.Obj) getIntent().getSerializableExtra("trustData");
        if (trustData == null) {
            isOffline = true;
            getOfflineData();
            tvIntro.setVisibility(View.GONE);
        } else {
            ImageUtils.loadImageCircle(mContext, trustData.getIcon(), ivAvatar);
            tvName.setText(trustData.getNickname());
            tvIntro.setText(trustData.getV_title());
        }
    }

    /**
     * 获取线下朋友信息
     */
    private void getOfflineData() {
        PostCall.get(mContext, ServerUrl.getOfflineScanFriendInfo(vcardNo, timestp), new BaseMap(), new PostCall.PostResponse<OfflineScanFriendBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, OfflineScanFriendBean bean) {
                offlineData = bean.getData();
                if (offlineData != null) {
                    ImageUtils.loadImageCircle(mContext, offlineData.getAvatar(), ivAvatar);
                    tvName.setText(offlineData.getName());
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {
                finish();
            }
        }, new String[]{}, true, OfflineScanFriendBean.class);
    }

    /**
     * 提交信任申请
     */
    private void commit() {
        showProgressDialog("申请中");
        EaseCommonUtils.addFriend(trustData.getHx_username(), editText.getText().toString(), new EMCallBack() {
            @Override
            public void onSuccess() {
                PostCall.postJson(mContext, ServerUrl.requestFrien(), new TrustVo(getUserId(), trustData.getUser_id(), "3"), new PostCall.PostResponse<BaseBean>() {
                    @Override
                    public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                        dismissProgressDialog();
                        showToast("申请成功");
                        setResult(RESULT_OK);
                        finish();
                        TrustSharedPreferences.getInstance().setTrustData("向" + trustData.getNickname() + "发送了信任申请");
                    }
                    @Override
                    public void onFailure(int statusCode, byte[] responseBody) {
                        dismissProgressDialog();
                    }
                }, new String[]{}, false, BaseBean.class);
            }

            @Override
            public void onError(int code, String error) {
                showToast(error);
                dismissProgressDialog();
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    /**
     * 提交线下信任申请
     */
    private void offlineCommit() {
        showProgressDialog("申请中");
        EaseCommonUtils.addFriend(offlineData.getHx_username(), editText.getText().toString(), new EMCallBack() {
            @Override
            public void onSuccess() {
                PostCall.postJson(mContext, ServerUrl.offlineScanToAddFriend(), new OfflineTrustVo(getUserId(), offlineData.getUser_id(), getHxUserId(), offlineData.getHx_username()), new PostCall.PostResponse<BaseBean>() {
                    @Override
                    public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                        dismissProgressDialog();
                        showToast("申请成功");
                        setResult(RESULT_OK);
                        finish();
                        TrustSharedPreferences.getInstance().setTrustData("向" + offlineData.getNickname() + "发送了信任申请");
                    }

                    @Override
                    public void onFailure(int statusCode, byte[] responseBody) {
                        dismissProgressDialog();
                    }
                }, new String[]{}, false, BaseBean.class);
            }

            @Override
            public void onError(int code, String error) {
                showToast(error);
                dismissProgressDialog();
            }

            @Override
            public void onProgress(int progress, String status) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnCommit:
                if (!isVcardIdZero()) {
                    if (isOffline)
                        offlineCommit();
                    else
                        commit();
                }
                break;
        }
    }
}
