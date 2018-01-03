package com.xhy.zhanhui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.easeui.DemoHelper;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.OrderDetailBean;
import com.xhy.zhanhui.http.vo.TrustVo;

/**
 * 预约详情页面
 * Created by Aaron on 27/12/2017.
 */

public class OrderDetailActivity extends ZhanHuiActivity{

    private String reservationId;
    private boolean isFriend; // 用于判断是否已经是朋友关系
    private OrderDetailBean.Obj data;
    private TextView tvCompany, tvExhibition, tvBooth, tvTime, tvRemark;
    private Button btnTrust, btnCancel;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void findView() {
        super.findView();
        tvCompany = findViewById(R.id.tvCompany);
        tvExhibition = findViewById(R.id.tvExhibition);
        tvBooth = findViewById(R.id.tvBooth);
        tvTime = findViewById(R.id.tvTime);
        tvRemark = findViewById(R.id.tvRemark);
        btnTrust = findAndSetClickListener(R.id.btnTrust);
        btnCancel = findAndSetClickListener(R.id.btnCancel);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("预约详情");
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
                switch (data.getState()) {
                    case "1":
                        break;
                    case "2":
                        btnCancel.setEnabled(false);
                        btnCancel.setText("已同意");
                        break;
                    case "3":
                        btnCancel.setEnabled(false);
                        btnCancel.setText("已拒绝");
                        break;
                    case "4":
                        btnCancel.setEnabled(false);
                        btnCancel.setText("已完成");
                        break;
                    case "5":
                        btnCancel.setEnabled(false);
                        btnCancel.setText("已取消");
                        break;
                }
                if (DemoHelper.getInstance().getContactList().get(data.getHx_username()) != null) {
                    isFriend = true;
                    btnTrust.setText("交谈");
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, OrderDetailBean.class);
    }

    /**
     * 提交信任申请
     */
    private void trust() {
        PostCall.postJson(mContext, ServerUrl.requestFrien(), new TrustVo(getUserId(), data.getHx_username(), "3"), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                showToast("申请成功");
                btnTrust.setEnabled(false);
                btnTrust.setText("已申请信任并交换名片");
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, BaseBean.class);
    }

    /**
     * 取消预约
     */
    private void cancel() {
        PostCall.put(mContext, ServerUrl.cancelReservation(data.getReservation_id()), "state=2", new PostCall.PostResponse<BaseBean>() {
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
                if (isFriend)
                    StartActivityUtils.startChat(mContext, data.getHx_username());
                else
                    trust();
                break;
            case R.id.btnCancel:
                cancel();
                break;
        }
    }
}
