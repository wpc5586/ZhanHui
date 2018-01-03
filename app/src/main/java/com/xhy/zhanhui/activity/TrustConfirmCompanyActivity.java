package com.xhy.zhanhui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.TimeUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.domain.CenterBean;
import com.xhy.zhanhui.http.domain.TrustCompanyBean;
import com.xhy.zhanhui.http.vo.TrustVo;

/**
 * 信任确认页面-企业页面
 * Created by Aaron on 15/12/2017.
 */

public class TrustConfirmCompanyActivity extends ZhanHuiActivity {

    private TrustCompanyBean bean;
    private ImageView ivThum;
    private TextView tvName;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_trust_confirm_company;
    }

    @Override
    protected void findView() {
        super.findView();
        ivThum = findViewById(R.id.ivAvatar);
        tvName = findViewById(R.id.tvName);
        findAndSetClickListener(R.id.btnCommit);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("确认申请");
        if (getIntent().hasExtra("bean"))
            bean = (TrustCompanyBean) getIntent().getSerializableExtra("bean");
        ImageUtils.loadImage(mContext, bean.getData().getImage_url(), ivThum);
        tvName.setText(bean.getData().getCompany_name());
    }

    /**
     * 提交信任申请
     */
    private void commit() {
        TrustCompanyBean.Obj.User user = null;
        if (bean.getData().getCompany_users() != null && bean.getData().getCompany_users().size() > 0)
            user = bean.getData().getCompany_users().get(0);
        else {
            showToast("申请接收方为空");
            return;
        }
        PostCall.postJson(mContext, ServerUrl.requestFrien(), new TrustVo(getUserId(), user.getUser_id(), "3"), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                showToast("申请成功");
                finish();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, BaseBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnCommit:
                commit();
                break;
        }
    }
}
