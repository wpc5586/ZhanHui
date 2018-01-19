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
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.listener.OnRecyclerItemClickListener;
import com.aaron.aaronlibrary.listener.OnRecyclerItemLongClickListener;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.CenterBean;
import com.xhy.zhanhui.http.domain.TrustUserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 商圈客戶详情页面
 * Created by Aaron on 15/12/2017.
 */

public class BusinessUserDetailActivity extends ZhanHuiActivity {

    public static final int TYPE_TARGET = 0;
    public static final int TYPE_TRUST = 1;
    public static final int TYPE_FOCUS = 2;
    private int type = TYPE_TARGET;

    private String toUserId;
    private TrustUserBean bean;
    private ImageView ivAvatar, ivCard; // 企业图片
    private TextView tvName, tvIntro, tvDe1, tvDe2, tvDe3, tvDe4, tvRelation;
    private Button btnTrust;
    private RecyclerView recyclerDocument, recyclerProduct;
    private TrustCompanyAdapter adapterDocument, adapterProduct;
    private LinearLayout llUser1, llUser2, llUser3, llTrend;
    private LinearLayout[] llUsers;
    private RelativeLayout rlRelation1, rlRelation2;
    private View divider1;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_trust_user_detail;
    }

    @Override
    protected void findView() {
        super.findView();
        ivAvatar = findAndSetClickListener(R.id.ivAvatar);
        ivCard = findViewById(R.id.ivCard);
        tvName = findViewById(R.id.tvName);
        tvIntro = findViewById(R.id.tvIntro);
        tvDe1 = findViewById(R.id.tvProductDe);
        tvDe2 = findViewById(R.id.tvDemandDe);
        tvDe3 = findViewById(R.id.tvCompanyDe);
        tvDe4 = findViewById(R.id.tvDe);
        recyclerDocument = findViewById(R.id.recycler);
        recyclerProduct = findViewById(R.id.recycler1);
        llUser1 = findViewById(R.id.llUser1);
        llUser2 = findViewById(R.id.llUser2);
        llUser3 = findViewById(R.id.llUser3);
        llUsers = new LinearLayout[]{llUser1, llUser2, llUser3};
        llTrend = findViewById(R.id.llTrend);
        btnTrust = findAndSetClickListener(R.id.btnTrust);
        tvRelation = findViewById(R.id.tvRelation);
        rlRelation1 = findViewById(R.id.rlRelation1);
        rlRelation2 = findViewById(R.id.rlRelation2);
        divider1 = findViewById(R.id.divider1);
    }

    @Override
    protected void init() {
        super.init();
        if (getIntent().hasExtra("toUserId"))
            toUserId = getIntent().getStringExtra("toUserId");
        if (getIntent().hasExtra("type"))
            type = getIntent().getIntExtra("type", TYPE_TARGET);
        String title = getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            setActionbarTitle(title);
            tvRelation.setVisibility(View.GONE);
            rlRelation1.setVisibility(View.GONE);
            rlRelation2.setVisibility(View.GONE);
            divider1.setVisibility(View.GONE);
        } else
            setActionbarTitle("客户详情");
        if (type == TYPE_TRUST) {
            ivCard.setVisibility(View.VISIBLE);
        }
        getData();
    }

    /**
     * 获取页面数据
     */
    private void getData() {
        String url = "";
        switch (type) {
            case TYPE_TARGET:
                url = ServerUrl.targetCustomers(toUserId);
                llTrend.setVisibility(View.GONE);
                break;
            case TYPE_TRUST:
                url = ServerUrl.trustCustomers(toUserId);
                llTrend.setVisibility(View.GONE);
                break;
            case TYPE_FOCUS:
                url = ServerUrl.focusCustomers(toUserId);
                llTrend.setVisibility(View.GONE);
                break;
        }
        PostCall.get(mContext, url, new BaseMap(), new PostCall.PostResponse<TrustUserBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, TrustUserBean bean) {
                BusinessUserDetailActivity.this.bean = bean;
                TrustUserBean.Obj data = bean.getData();
                if (data == null)
                    return;
                ImageUtils.loadImageCircle(mContext, data.getIcon(), ivAvatar);
                tvName.setText(data.getNickname());
                tvIntro.setText(data.getV_title());
                tvDe1.setText(data.getRecommend_index());
                tvDe2.setText(data.getAttention_degree());
                if (data.getHx_username().equals(getHxUserId()))
                    btnTrust.setVisibility(View.GONE);
                if (type == TYPE_TRUST)
                    btnTrust.setText("交谈");
                setRecyclerView();
//                if (data.getDocuments() != null)
//                    adapterDocument.setDataDocument(data.getDocuments());
//                if (data.getProducts() != null)
//                    adapterProduct.setDataProduct(data.getProducts());
//                for (int i = 0; i < data.getCompany_users().size(); i++) {
//                    TrustUserBean.Obj.User user = data.getCompany_users().get(i);
//                    LinearLayout linearLayout = llUsers[i];
//                    linearLayout.setVisibility(View.VISIBLE);
//                    ImageView imageView = (ImageView) linearLayout.getChildAt(0);
//                    TextView tvName = (TextView) linearLayout.getChildAt(1);
////                    TextView tvIntro = (TextView) linearLayout.getChildAt(2);
//                    ImageUtils.loadImageCircle(mContext, user.getIcon(), imageView);
//                    tvName.setText(user.getNickname());
//                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, TrustUserBean.class);
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
//                CenterBean.Obj.Item item = new CenterBean().new Obj().new Item();
//                item.setDocument_id(((TrustUserBean.Obj.Document) holder.data).getDocument_id());
//                item.setDocument_name(((TrustUserBean.Obj.Document) holder.data).getDocument_name());
//                item.setDocument_icon(((TrustUserBean.Obj.Document) holder.data).getD_image_url());
//                StartActivityUtils.startCenterDetail(mContext, item);
            }
        });
        adapterProduct = new TrustCompanyAdapter(mContext);
        adapterProduct.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder) {
//                StartActivityUtils.startProductDetail(mContext, ((TrustUserBean.Obj.Product) holder.data).getProduct_id());
            }
        });
        recyclerDocument.setAdapter(adapterDocument);
        recyclerProduct.setAdapter(adapterProduct);
    }

    /**
     * 信任并交换名片
     */
    private void trust() {
        if (!isVcardIdZero()) {
            if (bean != null)
                StartActivityUtils.startTrustUser(mContext, bean.getData());
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivAvatar:
                if (ivCard.getVisibility() == View.VISIBLE)
                    StartActivityUtils.startVcard(mContext, bean.getData().getUser_id());
                break;
            case R.id.btnTrust:
                String content = ((Button) view).getText().toString();
                if ("交谈".equals(content))
                    StartActivityUtils.startChat(mContext, bean.getData().getHx_username());
                else
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

//        public void setDataDocument(List<TrustUserBean.Obj.Document> data) {
//            datas.clear();
//            datas.addAll(data);
//            notifyDataSetChanged();
//        }
//
//        public void setDataProduct(List<TrustUserBean.Obj.Product> data) {
//            datas.clear();
//            datas.addAll(data);
//            notifyDataSetChanged();
//        }

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
//            if (data instanceof TrustUserBean.Obj.Product) {
//                holder.textView.setText(((TrustUserBean.Obj.Product) data).getProduct_name());
//                ImageUtils.loadImage(mContext, ((TrustUserBean.Obj.Product) data).getP_image_url(), holder.imageView);
//            } else if (data instanceof TrustUserBean.Obj.Document) {
//                holder.textView.setText(((TrustUserBean.Obj.Document) data).getDocument_name());
//                ImageUtils.loadImage(mContext, ((TrustUserBean.Obj.Document) data).getD_image_url(), holder.imageView);
//            }
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
