package com.xhy.zhanhui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.transformations.RoundedCornersTransformation;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.domain.DemandDetailBean;

/**
 * 智能商务-需求详情页面
 * Created by Administrator on 2018/1/25.
 */

public class IntelligentBusinessDetailActivity extends ZhanHuiActivity{

    private DemandDetailBean.Obj data;
    private String demandId;
    private TextView tvName, tvBrand, tvClass, tvNum, tvDemand, tvTime, tvStateTitle, tvStateContent;
    private ImageView image1, image2, image3, ivState;
    private ImageView[] imageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contentIsBelow = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_intelligent_business_release;
    }

    @Override
    protected void findView() {
        tvName = findViewById(R.id.tvName);
        tvBrand = findViewById(R.id.tvBrand);
        tvClass = findViewById(R.id.tvClass);
        tvNum = findViewById(R.id.tvNum);
        tvDemand = findViewById(R.id.tvDemand);
        tvTime = findViewById(R.id.tvTime);
        tvStateTitle = findViewById(R.id.tvStateTitle);
        tvStateContent = findViewById(R.id.tvStateContent);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        ivState = findViewById(R.id.ivState);
        imageViews = new ImageView[]{image1, image2, image3};
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("需求匹配");
        setActionbarBackground(R.color.transparent);
        setActionbarDividerVisibility(false);
        setStatusBarVisibility(false);
        getActionbarView().getBackButton().setImageResource(R.mipmap.common_back_white1);
        setActionbarTitleColor(R.color.white);
        demandId = getStringExtra("demandId");
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        PostCall.get(mContext, ServerUrl.demandsDetail(demandId), new BaseMap(), new PostCall.PostResponse<DemandDetailBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, DemandDetailBean bean) {
                data = bean.getData();
                if (data != null) {
                    tvName.setText(data.getDemand_title());
                    tvBrand.setText(data.getDemand_brand());
                    tvClass.setText(data.getCategory_name());
                    tvNum.setText(data.getDemand_nums());
                    tvDemand.setText(data.getDemand_notes());
                    tvTime.setText(data.getPost_time());
                    if (data.getImage_url() != null && data.getImage_url().size() > 0) {
                        for (int i = 0; i < data.getImage_url().size(); i++) {
                            ImageUtils.loadImageRoundedCorners(mContext, data.getImage_url().get(i).getImage_url(), imageViews[i], RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, DemandDetailBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

        }
    }
}
