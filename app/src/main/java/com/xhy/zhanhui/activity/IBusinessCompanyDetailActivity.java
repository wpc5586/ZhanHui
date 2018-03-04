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
import com.xhy.zhanhui.http.domain.ExhibitionCompanyInfoBean;
import com.xhy.zhanhui.http.domain.IBusinessCompanyBean;
import com.xhy.zhanhui.http.domain.TrustCompanyBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 智能商务-企业详情页面
 * Created by Aaron on 15/12/2017.
 */

public class IBusinessCompanyDetailActivity extends ZhanHuiActivity {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_TRUST = 1;
    private int type = TYPE_DEFAULT;

    private String matchingId;
    private IBusinessCompanyBean bean;
    private ImageView ivAvatar, ivCard; // 企业图片
    private TextView tvName, tvIntro, tvMessage, tvDe1, tvDe2, tvDe3, tvDe4, tvRelation;
    private Button btnTrust;
    private RecyclerView recyclerDocument, recyclerProduct;
    private TrustCompanyAdapter adapterDocument, adapterProduct;
    private LinearLayout llUser1, llUser2, llUser3, llTrend;
    private LinearLayout[] llUsers;
    private RelativeLayout rlRelation1, rlRelation2;
    private View divider1;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_ibusiness_company_detail;
    }

    @Override
    protected void findView() {
        super.findView();
        ivAvatar = findAndSetClickListener(R.id.ivAvatar);
        ivCard = findViewById(R.id.ivCard);
        tvName = findViewById(R.id.tvName);
        tvIntro = findViewById(R.id.tvIntro);
        tvMessage = findViewById(R.id.tvMessage);
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
        setActionbarBackgroundResource(R.mipmap.ibusiness_actionbar_bg);
        setActionbarTitleColor(R.color.white);
        getActionbarView().getBackButton().setImageResource(R.mipmap.common_back_white1);
        matchingId = getStringExtra("matchingId");
        if (getIntent().hasExtra("type"))
            type = getIntent().getIntExtra("type", TYPE_DEFAULT);
        String title = getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            setActionbarTitle(title);
            tvRelation.setVisibility(View.GONE);
            rlRelation1.setVisibility(View.GONE);
            rlRelation2.setVisibility(View.GONE);
            divider1.setVisibility(View.GONE);
        } else
            setActionbarTitle("企业详情");
        if (type == TYPE_TRUST) {
            ivCard.setVisibility(View.VISIBLE);
        }
        getData();
    }

    /**
     * 获取页面数据
     */
    private void getData() {
        PostCall.get(mContext, ServerUrl.demandsResult(matchingId), new BaseMap(), new PostCall.PostResponse<IBusinessCompanyBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, IBusinessCompanyBean bean) {
                IBusinessCompanyDetailActivity.this.bean = bean;
                IBusinessCompanyBean.Obj data = bean.getData();
                if (data == null)
                    return;
                ImageUtils.loadImageCircle(mContext, data.getCompany().getCompany_icon(), ivAvatar);
                tvName.setText(data.getCompany().getCompany_name());
                tvIntro.setText(data.getCompany().getCompany_name_en());
                tvMessage.setText(data.getCompany().getCompany_message());
                tvDe2.setText(data.getRelation().getMatched_degree());
                tvDe4.setText(data.getRelation().getAttention_degree());
                if (data.getCompany().getCompany_id().equals(getHxUserId()))
                    btnTrust.setVisibility(View.GONE);
                if (type == TYPE_TRUST)
                    btnTrust.setText("交谈");
//                setRecyclerView();
//                if (data.getDocuments() != null)
//                    adapterDocument.setDataDocument(data.getDocuments());
//                if (data.getProducts() != null)
//                    adapterProduct.setDataProduct(data.getProducts());
                for (int i = 0; i < data.getCompany_users().size(); i++) {
                    IBusinessCompanyBean.Obj.User user = data.getCompany_users().get(i);
                    LinearLayout linearLayout = llUsers[i];
                    linearLayout.setVisibility(View.VISIBLE);
                    ImageView imageView = (ImageView) linearLayout.getChildAt(0);
                    TextView tvName = (TextView) linearLayout.getChildAt(1);
                    TextView tvIntro = (TextView) linearLayout.getChildAt(2);
                    ImageUtils.loadImageCircle(mContext, user.getIcon(), imageView);
                    tvName.setText(user.getNickname());
                    tvIntro.setText(user.getV_title());
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, IBusinessCompanyBean.class);
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
//                item.setDocument_id(((IBusinessCompanyBean.Obj.Document) holder.data).getDocument_id());
//                item.setDocument_name(((IBusinessCompanyBean.Obj.Document) holder.data).getDocument_name());
//                item.setDocument_icon(((IBusinessCompanyBean.Obj.Document) holder.data).getD_image_url());
//                StartActivityUtils.startCenterDetail(mContext, item);
            }
        });
        adapterProduct = new TrustCompanyAdapter(mContext);
        adapterProduct.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder) {
//                StartActivityUtils.startProductDetail(mContext, ((IBusinessCompanyBean.Obj.Product) holder.data).getProduct_id());
            }
        });
        recyclerDocument.setAdapter(adapterDocument);
        recyclerProduct.setAdapter(adapterProduct);
    }

    /**
     * 信任并交换名片
     */
    private void trust() {
        IBusinessCompanyBean.Obj data = bean.getData();
        if (data != null && data.getCompany() != null && data.getCompany_users().size() > 0) {
            if (!isVcardIdZero()) {
                TrustCompanyBean bean = new TrustCompanyBean();
                TrustCompanyBean.Obj obj = bean.new Obj();
                obj.setCompany_name(data.getCompany().getCompany_name());
                obj.setImage_url(data.getCompany().getCompany_icon());
                List<TrustCompanyBean.Obj.User> users = new ArrayList<>();
                TrustCompanyBean.Obj.User user = obj.new User();
                IBusinessCompanyBean.Obj.User userInfo = data.getCompany_users().get(0);
                user.setHx_username(userInfo.getHx_username());
                user.setIcon(userInfo.getIcon());
                user.setNickname(userInfo.getNickname());
                user.setUser_id(userInfo.getUser_id());
                user.setV_title(userInfo.getV_title());
                users.add(user);
                obj.setCompany_users(users);
                bean.setData(obj);
                StartActivityUtils.startTrustCompany(mContext, bean);
            }
        } else
            showToast("该企业没有用户信息，无法申请信任");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivAvatar:
                if (ivCard.getVisibility() == View.VISIBLE)
                    StartActivityUtils.startVcard(mContext, bean.getData().getCompany().getCompany_id());
                break;
            case R.id.btnTrust:
                String content = ((Button) view).getText().toString();
                if ("交谈".equals(content))
                    StartActivityUtils.startChat(mContext, bean.getData().getCompany().getCompany_id());
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

//        public void setDataDocument(List<IBusinessCompanyBean.Obj.Document> data) {
//            datas.clear();
//            datas.addAll(data);
//            notifyDataSetChanged();
//        }
//
//        public void setDataProduct(List<IBusinessCompanyBean.Obj.Product> data) {
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
//            if (data instanceof IBusinessCompanyBean.Obj.Product) {
//                holder.textView.setText(((IBusinessCompanyBean.Obj.Product) data).getProduct_name());
//                ImageUtils.loadImage(mContext, ((IBusinessCompanyBean.Obj.Product) data).getP_image_url(), holder.imageView);
//            } else if (data instanceof IBusinessCompanyBean.Obj.Document) {
//                holder.textView.setText(((IBusinessCompanyBean.Obj.Document) data).getDocument_name());
//                ImageUtils.loadImage(mContext, ((IBusinessCompanyBean.Obj.Document) data).getD_image_url(), holder.imageView);
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
