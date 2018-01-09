package com.xhy.zhanhui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.TimeUtils;
import com.bigkoo.pickerview.TimePickerView;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.vo.OrderSponsorVo;

import java.util.Calendar;
import java.util.Date;

/**
 * 预约发起页面
 * Created by Aaron on 27/12/2017.
 */

public class OrderSponsorActivity extends ZhanHuiActivity {

    private String eventId, eventName, boothNo, companyId, companyName;
    private TextView tvCompany, tvExhibition, tvBooth, tvTime;
    private EditText etRemark;
    private Date date;

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
        findAndSetClickListener(R.id.rlTime);
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
//        tvTime.setText(TimeUtils.timeToyyyyMMddHHmmssSLASH(System.currentTimeMillis()));
        tvTime.setHint("点击选择时间");
    }

    /**
     * 发起预约
     */
    private void apply() {
        if (date == null) {
            showToast("请先选择预约时间");
            return;
        }
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

    /**
     * 显示时间选择对话框
     */
    private void showTimeDialog() {
        TimePickerView pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                OrderSponsorActivity.this.date = date;
                tvTime.setText(TimeUtils.timeToyyyyMMddHHmmssSLASH(date.getTime()));
            }
        })
                .setContentSize(16)
                .build();
        Calendar calendar = Calendar.getInstance();
        if (date != null)
            calendar.setTime(date);
        pvTime.setDate(calendar);//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnTrust:
                apply();
                break;
            case R.id.rlTime:
                showTimeDialog();
                break;
        }
    }
}
