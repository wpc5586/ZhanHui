package com.xhy.zhanhui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.TimeUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.vo.OrderSponsorVo;

/**
 * 预约发起页面
 * Created by Aaron on 27/12/2017.
 */

public class OrderSponsorActivity extends ZhanHuiActivity {

    private String eventId, eventName, boothNo, companyId, companyName;
    private TextView tvCompany, tvExhibition, tvBooth, tvTime;
    private EditText etRemark;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_order_sponsor;
    }

    @Override
    protected void findView() {
        super.findView();
        tvCompany = findViewById(R.id.tvCompany);
        tvExhibition = findViewById(R.id.tvExhibition);
        tvBooth = findViewById(R.id.tvBooth);
        tvTime = findViewById(R.id.tvTime);
        etRemark = findViewById(R.id.etRemark);
        findAndSetClickListener(R.id.btnTrust);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("发起预约");
        eventId = getStringExtra("eventId");
        eventName = getStringExtra("eventName");
        boothNo = getStringExtra("boothNo");
        companyId = getStringExtra("companyId");
        companyName = getStringExtra("companyName");
        initData();
    }

    /**
     * 获取数据
     */
    private void initData() {
        tvCompany.setText(companyName);
        tvExhibition.setText(eventName);
        tvBooth.setText(boothNo);
        tvTime.setText(TimeUtils.timeToyyyyMMddHHmmssSLASH(System.currentTimeMillis()));
    }

    /**
     * 发起预约
     */
    private void apply() {
        PostCall.postJson(mContext, ServerUrl.applyReservation(), new OrderSponsorVo(getUserId(), companyId, eventId, tvTime.getText().toString(), etRemark.getText().toString()), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                showToast("预约成功");
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
            case R.id.btnTrust:
                apply();
                break;
        }
    }
}
