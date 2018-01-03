package com.xhy.zhanhui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.domain.BaseViewHolder;
import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.listener.OnRecyclerItemClickListener;
import com.aaron.aaronlibrary.listener.OnRecyclerItemLongClickListener;
import com.aaron.aaronlibrary.manager.MyLinearLayoutManager;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.ExhibitionProductInfoBean;
import com.xhy.zhanhui.widget.MyConvenientBanner;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * 产品详情页面
 * Created by Aaron on 10/12/2017.
 */

public class ProductDetailActivity extends ZhanHuiActivity implements OnItemClickListener {

    private String productId;
    private MyConvenientBanner convenientBanner;
    private ImageView ivThum;
    private TextView tvName, tvId, tvAttentionNum, tvProductNum, tvIntro, tvAttention;
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private RecyclerView recyclerParam, recyclerView;
    private ProductDetailAdapter adapter;
    private ProductParamAdapter adapterParam;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_product_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contentIsBelow = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {
        super.findView();
        convenientBanner = findViewById(R.id.convenientBanner);
        jzVideoPlayerStandard = findViewById(R.id.videoPlayer);
        ivThum = findViewById(R.id.ivThum);
        tvName = findViewById(R.id.tvName);
        tvId = findViewById(R.id.tvId);
        tvAttentionNum = findViewById(R.id.tvAttentionNum);
        tvProductNum = findViewById(R.id.tvProductNum);
        tvIntro = findViewById(R.id.tvIntro);
        recyclerParam = findViewById(R.id.recyclerParam);
        recyclerView = findViewById(R.id.recycler);
        tvAttention = findViewById(R.id.tv2);
        findAndSetClickListener(R.id.rl2);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("");
        setActionbarBackground(R.color.transparent);
        setActionbarDividerVisibility(false);
        productId = getIntent().getStringExtra("product_id");
        initBannerHeight();
        getData();
    }

    /**
     * 初始化banner的高度
     */
    private void initBannerHeight() {
        convenientBanner.getLayoutParams().height = (int) (AppInfo.getScreenWidthOrHeight(mContext, true) / 1.7617f);
    }

    /**
     * 设置页面
     *
     * @param list 图片数据
     */
    private void setBanner(List<ExhibitionProductInfoBean.Obj.Image1> list) {
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, list)   //设置需要切换的View
                .setPointViewVisible(true)    //设置指示器是否可见
//                .setPageIndicator(new int[]{R.drawable.ic_launcher, R.drawable.ic_launcher})   //设置指示器圆点
                .startTurning(5000);     //设置自动切换（同时设置了切换时间间隔）
//                .stopTurning();    //关闭自动切换
        convenientBanner.setManualPageable(true);  //设置手动影响（设置了该项无法手动切换）
        convenientBanner.setPageIndicatorAlign(MyConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT) //设置指示器位置（左、中、右）
                .setOnItemClickListener(this); //设置点击监听事件
    }

    /**
     * Banner点击事件
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {

    }

    /**
     * 首页BannerItemView
     * Created by wpc on 2016/11/17 0017.
     */
    public static class NetworkImageHolderView implements Holder<ExhibitionProductInfoBean.Obj.Image1> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, ExhibitionProductInfoBean.Obj.Image1 data) {
            String url = data.getImage_url1();
            if (!TextUtils.isEmpty(url) && !url.contains("http"))
                url = ServerUrl.getService() + url;
            ImageUtils.loadImage(context, url, imageView, false);
        }
    }

    private void getData() {
        PostCall.get(mContext, ServerUrl.products(productId), new BaseMap(), new PostCall.PostResponse<ExhibitionProductInfoBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionProductInfoBean bean) {
                ExhibitionProductInfoBean.Obj data = bean.getData();
                setBanner(bean.getData().getImages1());
                if (data.getVideos() != null && data.getVideos().size() > 0) {
                    jzVideoPlayerStandard.setUp(data.getVideos().get(0).getVideo_url(), JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");
                    jzVideoPlayerStandard.setVisibility(View.VISIBLE);
                    PublicMethod.setVideoPlayer(jzVideoPlayerStandard);
                }
                setRecyclerView();
                adapter.setData(data.getImages2());
                adapterParam.setData(data.getParams());
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, ExhibitionProductInfoBean.class);
    }

    /**
     * 设置资料、动态
     */
    private void setRecyclerView() {
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
        linearLayoutManager.setScrollEnabled(false);
        recyclerParam.setLayoutManager(linearLayoutManager);
        MyLinearLayoutManager linearLayoutManager1 = new MyLinearLayoutManager(mContext);
        linearLayoutManager1.setScrollEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager1);
        if (adapter == null) {
            adapter = new ProductDetailAdapter(mContext);
            adapterParam = new ProductParamAdapter(mContext);
            recyclerView.setAdapter(adapter);
            recyclerParam.setAdapter(adapterParam);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                }
            });
            adapter.setOnItemLongClickListener(new OnRecyclerItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, BaseViewHolder holder) {
                }
            });
        } else {
            adapter.notifyDataSetChanged();
            adapterParam.notifyDataSetChanged();
        }
    }

    public class ProductDetailAdapter extends RecyclerView.Adapter<ProductDetailHolder> implements View.OnClickListener, View.OnLongClickListener {

        private final Context context;

        private List<ExhibitionProductInfoBean.Obj.Image2> datas = new ArrayList<>();

        private OnRecyclerItemClickListener onItemClickListener;

        private OnRecyclerItemLongClickListener onItemLongClickListener;

        public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void setOnItemLongClickListener(OnRecyclerItemLongClickListener onItemLongClickListener) {
            this.onItemLongClickListener = onItemLongClickListener;
        }

        public ProductDetailAdapter(Context context) {
            this.context = context;
        }

        public void setData(List<ExhibitionProductInfoBean.Obj.Image2> data) {
            this.datas.clear();
            this.datas.addAll(data);
            notifyDataSetChanged();
        }

        public void addData(ExhibitionProductInfoBean.Obj.Image2 data) {
            datas.add(data);
            notifyDataSetChanged();
        }

        @Override
        public ProductDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ProductDetailHolder holder = new ProductDetailHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_product_detail, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ProductDetailHolder holder, int position) {
            ExhibitionProductInfoBean.Obj.Image2 data = getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            ImageUtils.loadImage(mContext, data.getImage_url2(), holder.imageView);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public ExhibitionProductInfoBean.Obj.Image2 getItem(int position) {
            return datas.get(position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    ProductDetailHolder holder = (ProductDetailHolder) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, holder);
                    break;
                case R.id.rl1:
                case R.id.rl2:
                case R.id.rl3:
                    String companyId = (String) v.getTag();
                    StartActivityUtils.startCompanyDetail(mContext, companyId);
                    break;

                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    ProductDetailHolder holder = (ProductDetailHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class ProductDetailHolder extends BaseViewHolder {
        RelativeLayout parent;
        ImageView imageView;

        public ProductDetailHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            imageView = itemView.findViewById(R.id.image);
        }
    }

    public class ProductParamAdapter extends RecyclerView.Adapter<ProductParamHolder> implements View.OnClickListener, View.OnLongClickListener {

        private final Context context;

        private List<ExhibitionProductInfoBean.Obj.Param> datas = new ArrayList<>();

        private OnRecyclerItemClickListener onItemClickListener;

        private OnRecyclerItemLongClickListener onItemLongClickListener;

        public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void setOnItemLongClickListener(OnRecyclerItemLongClickListener onItemLongClickListener) {
            this.onItemLongClickListener = onItemLongClickListener;
        }

        public ProductParamAdapter(Context context) {
            this.context = context;
        }

        public void setData(List<ExhibitionProductInfoBean.Obj.Param> data) {
            this.datas.clear();
            this.datas.addAll(data);
            notifyDataSetChanged();
        }

        public void addData(ExhibitionProductInfoBean.Obj.Param data) {
            datas.add(data);
            notifyDataSetChanged();
        }

        @Override
        public ProductParamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ProductParamHolder holder = new ProductParamHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_product_param, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ProductParamHolder holder, int position) {
            ExhibitionProductInfoBean.Obj.Param data = getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            holder.textView.setText(data.getProduct_param());
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public ExhibitionProductInfoBean.Obj.Param getItem(int position) {
            return datas.get(position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    ProductDetailHolder holder = (ProductDetailHolder) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, holder);
                    break;

                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    ProductDetailHolder holder = (ProductDetailHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class ProductParamHolder extends BaseViewHolder {
        RelativeLayout parent;
        TextView textView;

        public ProductParamHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    /**
     * 添加关注
     */
    private void attention() {
        PostCall.get(mContext, ServerUrl.attentionProduct(productId), new BaseMap(), new PostCall.PostResponse<ExhibitionProductInfoBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionProductInfoBean bean) {
                tvAttention.setText("已关注");
                showToast("已关注");
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, ExhibitionProductInfoBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch(view.getId()) {
            case R.id.rl2: // 添加关注
                attention();
                break;
        }
    }
}
