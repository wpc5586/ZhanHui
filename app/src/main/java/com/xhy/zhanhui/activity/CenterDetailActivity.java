package com.xhy.zhanhui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.CenterBean;
import com.xhy.zhanhui.http.domain.CenterDetailBean;

/**
 * 资料详情页面
 * Created by Aaron on 15/12/2017.
 */

public class CenterDetailActivity extends ZhanHuiActivity {

    /**
     * 是否已经收藏
     */
    private boolean isCollect = false;
    private CenterBean.Obj.Item item;
    private CenterDetailBean bean;
    private ImageView ivThum;
    private TextView tvName, tvIntro, tvCompany, tvTime, tvSize, tvIntroduction;
    private Button btnAttention;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_center_detail;
    }

    @Override
    protected void findView() {
        super.findView();
        ivThum = findViewById(R.id.ivThum);
        tvName = findViewById(R.id.tvName);
        tvIntro = findViewById(R.id.tvIntro);
        tvCompany = findViewById(R.id.tvCompany);
        tvTime = findViewById(R.id.tvTime);
        tvSize = findViewById(R.id.tvSize);
        tvIntroduction = findViewById(R.id.tvIntroduction);
        findAndSetClickListener(R.id.btnRead);
        btnAttention = findAndSetClickListener(R.id.btnAttention);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("资料详情");
        if (getIntent().hasExtra("item"))
            item = (CenterBean.Obj.Item) getIntent().getSerializableExtra("item");
        if (item != null) {
            setActionbarTitle(item.getDocument_name());
            ImageUtils.loadImage(mContext, item.getDocument_icon(), ivThum);
            tvName.setText(item.getDocument_name());
            tvCompany.setText("公司：");
            if (!TextUtils.isEmpty(item.getIs_collected())) {
                if ("1".equals(item.getIs_collected()))
                    isCollect = true;
                else
                    isCollect = false;
            } else if (getIntent().hasExtra("is_collect"))
                isCollect = getIntent().getBooleanExtra("is_collect", false);
//            if (!TextUtils.isEmpty(item.getFavorite_time()))
//                tvTime.setText("收藏时间：" + TimeUtils.getTimestampString(Long.valueOf(item.getFavorite_time())));
//            else
//                tvTime.setText("上传时间：" + TimeUtils.getTimestampString(Long.valueOf(item.getPush_time())));
        }
        setButtonStatus();
        getData();
    }

    private void setButtonStatus() {
        btnAttention.setText(isCollect ? "取消收藏" : "收藏");
    }

    /**
     * 获取数据
     */
    private void getData() {
        PostCall.get(mContext, ServerUrl.getDocument(item.getDocument_id()), new BaseMap(), new PostCall.PostResponse<CenterDetailBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, CenterDetailBean bean) {
                CenterDetailActivity.this.bean = bean;
                CenterDetailBean.Obj data = bean.getData();
                if (data == null)
                    return;
                setActionbarTitle(data.getDocument_name());
                ImageUtils.loadImage(mContext, data.getImage_url(), ivThum);
                tvName.setText(data.getDocument_name());
                tvCompany.setText(("1".equals(data.getFlag()) ? "公司：" : "展会：") + data.getBelongs_name());
                tvTime.setText("上传时间：" + data.getDocument_time());
                float size = Integer.parseInt(data.getFile_size()) / 1024 / 1024f;
                tvSize.setText("文件大小：" + String.format("%.2f", size) + "MB");
                if (!TextUtils.isEmpty(data.getIs_fav())) {
                    if ("1".equals(data.getIs_fav()))
                        isCollect = true;
                    else
                        isCollect = false;
                }
                setButtonStatus();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, CenterDetailBean.class);
    }

    /**
     * 收藏
     */
    private void collect() {
        BaseMap params = new BaseMap();
        params.put("user_id", getUserId());
        PostCall.post(mContext, ServerUrl.collectDocument(item.getDocument_id()), new BaseMap(), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                isCollect = true;
                setButtonStatus();
                MainActivity.getInstance().refreshCenterCollect();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, BaseBean.class);
    }

    /**
     * 取消收藏
     */
    private void cancelCollect() {
        PostCall.delete(mContext, ServerUrl.cancelCollectDocument(item.getDocument_id()), ServerUrl.getUserIdBody(), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                isCollect = false;
                setButtonStatus();
                MainActivity.getInstance().refreshCenterCollect();
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
            case R.id.btnRead:
                StartActivityUtils.startPdfUrl(mContext, bean.getData().getFile_url(), bean.getData().getDocument_name());
                break;
            case R.id.btnAttention:
                if (!isCollect)
                    collect();
                else
                    cancelCollect();
                break;
        }
    }
}
