package com.xhy.zhanhui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.easeui.utils.EaseCommonUtils;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.hyphenate.EMCallBack;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.domain.TrustCompanyBean;
import com.xhy.zhanhui.http.vo.TrustVo;
import com.xhy.zhanhui.preferences.TrustSharedPreferences;

/**
 * 信任确认页面-企业页面
 * Created by Aaron on 15/12/2017.
 */

public class TrustConfirmCompanyActivity extends ZhanHuiActivity {

    private TrustCompanyBean bean;
    private ImageView ivThum;
    private TextView tvName;
    private EditText editText;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_trust_confirm_company;
    }

    @Override
    protected void findView() {
        super.findView();
        ivThum = findViewById(R.id.ivAvatar);
        tvName = findViewById(R.id.tvName);
        editText = findViewById(R.id.etMessage);
        findAndSetClickListener(R.id.btnCommit);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("确认申请");
        if (getIntent().hasExtra("bean"))
            bean = (TrustCompanyBean) getIntent().getSerializableExtra("bean");
        if (bean != null && bean.getData() != null) {
            ImageUtils.loadImage(mContext, bean.getData().getImage_url(), ivThum);
            tvName.setText(bean.getData().getCompany_name());
        }
    }

    /**
     * 提交信任申请
     */
    private void commit() {
        TrustCompanyBean.Obj.User user;
        if (bean.getData().getCompany_users() != null && bean.getData().getCompany_users().size() > 0)
            user = bean.getData().getCompany_users().get(0);
        else {
            showToast("申请接收方为空");
            return;
        }
        showProgressDialog("申请中");
        final TrustCompanyBean.Obj.User finalUser = user;
        EaseCommonUtils.addFriend(finalUser.getHx_username(), editText.getText().toString(), new EMCallBack() {
            @Override
            public void onSuccess() {
                PostCall.postJson(mContext, ServerUrl.requestFrien(), new TrustVo(getUserId(), finalUser.getUser_id(), "3"), new PostCall.PostResponse<BaseBean>() {
                    @Override
                    public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                        dismissProgressDialog();
                        showToast("申请成功");
                        finish();
                        TrustSharedPreferences.getInstance().setTrustData("向" + TrustConfirmCompanyActivity.this.bean.getData().getCompany_name() + "发送了信任申请");
                    }

                    @Override
                    public void onFailure(int statusCode, byte[] responseBody) {
                        dismissProgressDialog();
                    }
                }, new String[]{}, false, BaseBean.class);
            }

            @Override
            public void onError(int code, String error) {

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
                if (bean != null && bean.getData() != null && !isVcardIdZero())
                    commit();
                break;
        }
    }
}
