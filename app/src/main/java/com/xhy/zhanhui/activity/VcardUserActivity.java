package com.xhy.zhanhui.activity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.aaronlibrary.easeui.Constant;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.Constants;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.domain.VcardBean;

import java.util.Timer;
import java.util.TimerTask;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * 个人电子名片页面
 * Created by Aaron on 22/12/2017.
 */

public class VcardUserActivity extends ZhanHuiActivity{

    private String userId;
    private TextView tvVcard, tvName, tvIntro, tvCompany, tvCompanyEn, tvPhone, tvEmail, tvUrl, tvAddress;
    private ImageView ivCode;
    private String vcardNo, timestp, type;
    private Timer timer = new Timer();
    private TimerTask task;
    private final int TIMER_DURATION = 30 * 1000;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_vcard_company;
    }

    @Override
    protected void findView() {
        super.findView();
        tvVcard = findViewById(R.id.tvVcard);
        tvName = findViewById(R.id.tvName);
        tvIntro = findViewById(R.id.tvIntro);
        tvCompany = findViewById(R.id.tvCompany);
        tvCompanyEn = findViewById(R.id.tvCompanyEn);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);
        tvUrl = findViewById(R.id.tvUrl);
        tvAddress = findViewById(R.id.tvAddress);
        ivCode = findViewById(R.id.ivCode);
        findAndSetClickListener(R.id.btnClose);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("");
        setActionbarVisibility(false);
        userId = getStringExtra("userId");
        boolean isShow = true; // 是否显示二维码
        if (getIntent().hasExtra("isShow"))
            isShow = getIntent().getBooleanExtra("isShow", true);
        if (!isShow) {
            ivCode.setVisibility(View.GONE);
            findViewById(R.id.tvInfo).setVisibility(View.GONE);
            findViewById(R.id.divide).setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(userId))
            userId = getUserId();
        getActionbarView().setOnBackIcon(R.mipmap.icon_close);
        if (!Constants.VERSION_IS_USER) {
            tvCompany.setVisibility(View.GONE);
            tvName.setHint("未填写公司名称信息");
            tvIntro.setHint("未填写公司英文名称信息");
        }
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        PostCall.get(mContext, ServerUrl.vcard(userId), new BaseMap(), new PostCall.PostResponse<VcardBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, VcardBean bean) {
                VcardBean.Obj data = bean.getData();
                tvVcard.setText(data.getVcard_no());
                if (!TextUtils.isEmpty(data.getV_name())) {
                    tvName.setText(data.getV_name());
                    tvIntro.setText(data.getV_title());
                    tvCompany.setText(data.getV_company());
                    tvPhone.setText(data.getV_mobile());
                    tvEmail.setText(data.getV_email());
                    tvUrl.setText(data.getV_website());
                    tvAddress.setText(data.getV_address());
                } else {
                    tvName.setText(data.getCompany_name());
                    tvIntro.setText(data.getCompany_name_en());
                    tvPhone.setText(data.getCompany_tel());
                    tvEmail.setText(data.getCompany_email());
                    tvUrl.setText(data.getCompany_website());
                    tvAddress.setText(data.getCompany_adress());
                }
                setCode(data.getVcard_no(), data.getTimestp(), "online");
                autoRefreshQRCode();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, VcardBean.class);
    }

    /**
     * 生成二维码
     * @param vcardNo id
     * @param timestp 时间戳
     * @param type 类型
     */
    private void setCode(final String vcardNo, final String timestp, final String type) {
        this.vcardNo = vcardNo;
        this.timestp = timestp;
        this.type = type;
        try {
            //生成二维码图片，第一个参数是二维码的内容，第二个参数是正方形图片的边长，单位是像素
            final int length = (int) (AppInfo.getScreenWidthOrHeight(mContext, true) * 0.5f);
            new Thread(){
                @Override
                public void run() {
                    final Bitmap qrcodeBitmap = QRCodeEncoder.syncEncodeQRCode(vcardNo + "#" + timestp + "#" + type, length);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ivCode.setImageBitmap(qrcodeBitmap);
                        }
                    });
                }
            }.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 每30秒自动刷新二维码
     */
    private void autoRefreshQRCode() {
        task = new TimerTask() {
            @Override
            public void run() {
                timestp = String.valueOf(Integer.parseInt(timestp) + 30);
                setCode(vcardNo, timestp, "online");
            }
        };
        timer.schedule(task, 0, TIMER_DURATION);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (task != null) {
            timer = new Timer();
            getData();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (task != null)
            timer.cancel();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnClose:
                finish();
                break;
        }
    }
}
