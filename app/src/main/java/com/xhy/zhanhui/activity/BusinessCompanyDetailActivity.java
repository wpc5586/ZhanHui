package com.xhy.zhanhui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.domain.BaseViewHolder;
import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.listener.OnRecyclerItemClickListener;
import com.aaron.aaronlibrary.listener.OnRecyclerItemLongClickListener;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.aaron.aaronlibrary.utils.TimeUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.fragment.center.CenterAllFragment;
import com.xhy.zhanhui.http.domain.CenterBean;
import com.xhy.zhanhui.http.domain.ExhibitionNewsBean;
import com.xhy.zhanhui.http.domain.TrustCompanyBean;
import com.xhy.zhanhui.widget.ExhibitionTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 商圈企业详情页面
 * Created by Aaron on 15/12/2017.
 */

public class BusinessCompanyDetailActivity extends ZhanHuiActivity {

    public static final int TYPE_TARGET = 0;
    public static final int TYPE_TRUST = 1;
    public static final int TYPE_FOCUS = 2;
    private int type = TYPE_TARGET;

    private String companyId;
    private TrustCompanyBean bean;
    private ImageView ivAvatar; // 企业图片
    private TextView tvName, tvIntro, tvProductDe, tvDemandDe, tvCompanyDe, tvDe;
    private RecyclerView recyclerDocument, recyclerProduct;
    private TrustCompanyAdapter adapterDocument, adapterProduct;
    private LinearLayout llUser1, llUser2, llUser3, llTrend;
    private LinearLayout[] llUsers;
    private Button btnTrust;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_trust_company_detail;
    }

    @Override
    protected void findView() {
        super.findView();
        ivAvatar = findViewById(R.id.ivAvatar);
        tvName = findViewById(R.id.tvName);
        tvIntro = findViewById(R.id.tvIntro);
        tvProductDe = findViewById(R.id.tvProductDe);
        tvDemandDe = findViewById(R.id.tvDemandDe);
        tvCompanyDe = findViewById(R.id.tvCompanyDe);
        tvDe = findViewById(R.id.tvDe);
        recyclerDocument = findViewById(R.id.recycler);
        recyclerProduct = findViewById(R.id.recycler1);
        llUser1 = findViewById(R.id.llUser1);
        llUser2 = findViewById(R.id.llUser2);
        llUser3 = findViewById(R.id.llUser3);
        llUsers = new LinearLayout[]{llUser1, llUser2, llUser3};
        llTrend = findViewById(R.id.llTrend);
        btnTrust = findAndSetClickListener(R.id.btnTrust);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("企业详情");
        if (getIntent().hasExtra("company_id"))
            companyId = getIntent().getStringExtra("company_id");
        if (getIntent().hasExtra("type"))
            type = getIntent().getIntExtra("type", TYPE_TARGET);
        getData();
    }

    /**
     * 获取页面数据
     */
    private void getData() {
        String url = "";
        switch (type) {
            case TYPE_TARGET:
                url = ServerUrl.targetCompanies(companyId);
                llTrend.setVisibility(View.GONE);
                break;
            case TYPE_TRUST:
                url = ServerUrl.trustCompanies(companyId);
                break;
            case TYPE_FOCUS:
                url = ServerUrl.focusCompanies(companyId);
                break;
        }
        PostCall.get(mContext, url, new BaseMap(), new PostCall.PostResponse<TrustCompanyBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, TrustCompanyBean bean) {
                BusinessCompanyDetailActivity.this.bean = bean;
                TrustCompanyBean.Obj data = bean.getData();
                if (data == null)
                    return;
                ImageUtils.loadImageCircle(mContext, data.getImage_url(), ivAvatar);
                tvName.setText(data.getCompany_name());
                tvIntro.setText(data.getCompany_name_en());
                if (!TextUtils.isEmpty(data.getAttention_degree()))
                    tvDe.setText(data.getAttention_degree() + "%");
                if (!TextUtils.isEmpty(data.getRecommend_index()))
                    tvDe.setText(data.getRecommend_index() + "%");
                if (data.getCompany_users() != null && data.getCompany_users().size() > 0 && data.getCompany_users().get(0).getHx_username().equals(getHxUserId()))
                    btnTrust.setVisibility(View.GONE);
                setRecyclerView();
                if (data.getDocuments() != null)
                    adapterDocument.setDataDocument(data.getDocuments());
                if (data.getProducts() != null)
                    adapterProduct.setDataProduct(data.getProducts());
                for (int i = 0; i < data.getCompany_users().size(); i++) {
                    TrustCompanyBean.Obj.User user = data.getCompany_users().get(i);
                    LinearLayout linearLayout = llUsers[i];
                    linearLayout.setVisibility(View.VISIBLE);
                    ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                    TextView tvName = (TextView) linearLayout.getChildAt(1);
//                    TextView tvIntro = (TextView) linearLayout.getChildAt(2);
                    ImageUtils.loadImageCircle(mContext, user.getIcon(), imageView);
                    tvName.setText(user.getNickname());
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, TrustCompanyBean.class);
    }

    /**
     * 设置RecyclerView
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(mContext);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext);
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerDocument.setLayoutManager(layoutManager1);
        recyclerProduct.setLayoutManager(layoutManager2);
        adapterDocument = new TrustCompanyAdapter(mContext);
        adapterDocument.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder) {
                CenterBean.Obj.Item item = new CenterBean().new Obj().new Item();
                item.setDocument_id(((TrustCompanyBean.Obj.Document) holder.data).getDocument_id());
                item.setDocument_name(((TrustCompanyBean.Obj.Document) holder.data).getDocument_name());
                item.setDocument_icon(((TrustCompanyBean.Obj.Document) holder.data).getD_image_url());
                StartActivityUtils.startCenterDetail(mContext, item);
            }
        });
        adapterProduct = new TrustCompanyAdapter(mContext);
        adapterProduct.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder) {
                StartActivityUtils.startProductDetail(mContext, ((TrustCompanyBean.Obj.Product) holder.data).getProduct_id());
            }
        });
        recyclerDocument.setAdapter(adapterDocument);
        recyclerProduct.setAdapter(adapterProduct);
    }

    /**
     * 信任并交换名片
     */
    private void trust() {
        if (bean != null)
            StartActivityUtils.startTrustCompany(mContext, bean);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnTrust:
                trust();
                break;
        }
    }

    public class TrustCompanyAdapter extends RecyclerView.Adapter<TrustCompanyHolder> implements View.OnClickListener, View.OnLongClickListener {

        private final Context context;

        private List<Object> datas = new ArrayList<>();

        private OnRecyclerItemClickListener onItemClickListener;

        private OnRecyclerItemLongClickListener onItemLongClickListener;

        public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void setOnItemLongClickListener(OnRecyclerItemLongClickListener onItemLongClickListener) {
            this.onItemLongClickListener = onItemLongClickListener;
        }

        public TrustCompanyAdapter(Context context) {
            this.context = context;
        }

        public void addData(Object data) {
            datas.add(data);
            notifyDataSetChanged();
        }

        public void setData(List<Object> data) {
            datas.clear();
            datas.addAll(data);
            notifyDataSetChanged();
        }

        public void setDataDocument(List<TrustCompanyBean.Obj.Document> data) {
            datas.clear();
            datas.addAll(data);
            notifyDataSetChanged();
        }

        public void setDataProduct(List<TrustCompanyBean.Obj.Product> data) {
            datas.clear();
            datas.addAll(data);
            notifyDataSetChanged();
        }

        public void clearData() {
            datas.clear();
        }

        @Override
        public TrustCompanyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final TrustCompanyHolder holder = new TrustCompanyHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_trust_company_detail, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(TrustCompanyHolder holder, int position) {
            Object data = getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            if (data instanceof TrustCompanyBean.Obj.Product) {
                holder.textView.setText(((TrustCompanyBean.Obj.Product) data).getProduct_name());
                ImageUtils.loadImage(mContext, ((TrustCompanyBean.Obj.Product) data).getP_image_url(), holder.imageView);
            } else if (data instanceof TrustCompanyBean.Obj.Document) {
                holder.textView.setText(((TrustCompanyBean.Obj.Document) data).getDocument_name());
                ImageUtils.loadImage(mContext, ((TrustCompanyBean.Obj.Document) data).getD_image_url(), holder.imageView);
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    TrustCompanyHolder holder = (TrustCompanyHolder) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, holder);
                    break;
                case R.id.rl1:
                case R.id.rl2:
                case R.id.rl3:
                    CenterBean.Obj.Item item = (CenterBean.Obj.Item) v.getTag();
                    StartActivityUtils.startCenterDetail(mContext, item);
                    break;

                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    TrustCompanyHolder holder = (TrustCompanyHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class TrustCompanyHolder extends BaseViewHolder {
        RelativeLayout parent;
        ImageView imageView;
        TextView textView;

        public TrustCompanyHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
