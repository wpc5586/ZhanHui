package com.xhy.zhanhui.activity;

import android.view.View;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.domain.OrderDetailBean;

/**
 * 预约发起页面
 * Created by Aaron on 27/12/2017.
 */

public class OrderSponsorActivity extends ZhanHuiActivity{

    private String reservationId;
    private OrderDetailBean.Obj data;
    private TextView tvCompany, tvExhibition, tvBooth, tvTime, tvRemark;

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
        tvRemark = findViewById(R.id.tvRemark);
        findAndSetClickListener(R.id.btnTrust);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("发起预约");
        reservationId = getStringExtra("reservationId");
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        PostCall.get(mContext, ServerUrl.reservationDetail(reservationId), new BaseMap(), new PostCall.PostResponse<OrderDetailBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, OrderDetailBean bean) {
                data = bean.getData();
                tvCompany.setText(data.getCompany_name());
                tvExhibition.setText(data.getEvent_name());
                tvBooth.setText(data.getBooth_no());
                tvTime.setText(data.getCreate_time());
                tvRemark.setText(data.getReservation_note());
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, OrderDetailBean.class);
    }

    /**
     * 发起预约
     */
    private void apply() {
        BaseMap params = new BaseMap();
        params.put("user_id", getUserId());
//        params.put("company_id", "");
//        params.put("user_id", getUserId());
//        params.put("user_id", getUserId());
//        params.put("user_id", getUserId());
        PostCall.postJson(mContext, ServerUrl.applyReservation(), params, new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                showToast("取消成功");
                setResult(RESULT_OK);
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
        switch(view.getId()) {
            case R.id.btnTrust:
                apply();
                break;
        }
    }
}
