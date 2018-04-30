package com.xhy.zhanhui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.domain.BaseViewHolder;
import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.listener.OnRecyclerItemClickListener;
import com.aaron.aaronlibrary.listener.OnRecyclerItemLongClickListener;
import com.aaron.aaronlibrary.transformations.RoundedCornersTransformation;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.CenterBean;
import com.xhy.zhanhui.http.domain.IBusinessUserBean;
import com.xhy.zhanhui.http.vo.IBusinessHandleVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 智能商务-客户需求详情页面
 * Created by Aaron on 15/12/2017.
 */

public class IBusinessUserDetailActivity extends ZhanHuiActivity {

    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_TRUST = 1;
    public static final int TYPE_HANDLE = 2; // 已处理
    private int type = TYPE_DEFAULT;

    private String matchingId;
    private IBusinessUserBean bean;
    private ImageView ivAvatar, ivCard, image1, image2, image3;
    private ImageView[] imageViews;
    private TextView tvName, tvIntro, tvDe1, tvDe2, tvDe3, tvDe4, tvRelation, tvMessageText, tv1, tv2, tv3, tv4, tv5, tv7;
    private View divide;
    private EditText editText;
    private Button btnAgree, btnRefuse;
    private RecyclerView recyclerDocument, recyclerProduct;
    private TrustCompanyAdapter adapterDocument, adapterProduct;
    private RelativeLayout rlRelation1, rlRelation2;
    private View divider1;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_ibusiness_user_detail;
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
        btnAgree = findAndSetClickListener(R.id.btnAgree);
        divide = findViewById(R.id.divide);
        tvMessageText = findViewById(R.id.tvMessageText);
        btnRefuse = findAndSetClickListener(R.id.btnRefuse);
        tvRelation = findViewById(R.id.tvRelation);
        rlRelation1 = findViewById(R.id.rlRelation1);
        rlRelation2 = findViewById(R.id.rlRelation2);
        divider1 = findViewById(R.id.divider1);
        editText = findViewById(R.id.et);
        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);
        tv3 = findViewById(R.id.tv3);
        tv4 = findViewById(R.id.tv4);
        tv5 = findViewById(R.id.tv5);
        tv7 = findViewById(R.id.tv7);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        imageViews = new ImageView[]{image1, image2, image3};
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
            setActionbarTitle("客户需求");
        if (type == TYPE_TRUST) {
            ivCard.setVisibility(View.VISIBLE);
        } else if (type == TYPE_DEFAULT) {
            divide.setVisibility(View.VISIBLE);
            btnRefuse.setVisibility(View.VISIBLE);
            tvMessageText.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
        } else {
            btnAgree.setVisibility(View.GONE);
        }
        getData();
    }

    /**
     * 获取页面数据
     */
    private void getData() {
        PostCall.get(mContext, ServerUrl.demandsDetailUser(matchingId), new BaseMap(), new PostCall.PostResponse<IBusinessUserBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, IBusinessUserBean bean) {
                IBusinessUserDetailActivity.this.bean = bean;
                IBusinessUserBean.Obj data = bean.getData();
                if (data == null)
                    return;
                ImageUtils.loadImageCircle(mContext, data.getUser().getIcon(), ivAvatar);
                tvName.setText(data.getUser().getNickname());
                tvIntro.setText(data.getUser().getV_title());
                tvDe2.setText(data.getRelation().getMatched_degree());
                tvDe4.setText(data.getRelation().getAttention_degree());
                if (data.getUser().getUser_id().equals(getUserId()))
                    btnAgree.setVisibility(View.GONE);
                if (type == TYPE_TRUST)
                    btnAgree.setText("交谈");
                tv1.setText(data.getDemand().getDemand_title());
                tv2.setText(data.getDemand().getDemand_brand());
                tv3.setText(data.getDemand().getCategory_name());
                tv4.setText(data.getDemand().getDemand_nums());
                tv5.setText(data.getDemand().getDemand_notes());
                tv7.setText(data.getDemand().getPost_time());
                if (data.getDemand_images() != null && data.getDemand_images().size() > 0) {
                    for (int i = 0; i < data.getDemand_images().size(); i++) {
                        ImageUtils.loadImageRoundedCorners(mContext, data.getDemand_images().get(i), imageViews[i], RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, IBusinessUserBean.class);
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
//                item.setDocument_id(((IBusinessUserBean.Obj.Document) holder.data).getDocument_id());
//                item.setDocument_name(((IBusinessUserBean.Obj.Document) holder.data).getDocument_name());
//                item.setDocument_icon(((IBusinessUserBean.Obj.Document) holder.data).getD_image_url());
//                StartActivityUtils.startCenterDetail(mContext, item);
            }
        });
        adapterProduct = new TrustCompanyAdapter(mContext);
        adapterProduct.setOnItemClickListener(new OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, BaseViewHolder holder) {
//                StartActivityUtils.startProductDetail(mContext, ((IBusinessUserBean.Obj.Product) holder.data).getProduct_id());
            }
        });
        recyclerDocument.setAdapter(adapterDocument);
        recyclerProduct.setAdapter(adapterProduct);
    }

    /**
     * 处理匹配结果
     * @param state 1：接受 2：拒绝 3：删除
     */
    private void handle(final String state) {
        PostCall.putJson(mContext, ServerUrl.demandsHandle(matchingId), new IBusinessHandleVo("receiver", editText.getText().toString(), Integer.parseInt(state)), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                switch (state) {
                    case "1":
                        showToast("接受成功");
                        break;
                    case "2":
                        showToast("拒绝成功");
                        break;
                    case "3":
                        showToast("删除成功");
                        break;
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, BaseBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ivAvatar:
                if (ivCard.getVisibility() == View.VISIBLE)
                    StartActivityUtils.startVcard(mContext, bean.getData().getUser().getUser_id());
                break;
            case R.id.btnAgree:
                String content = ((Button) view).getText().toString();
                if ("交谈".equals(content))
                    StartActivityUtils.startChat(mContext, bean.getData().getUser().getHx_username());
                else
                    handle("1");
                break;
            case R.id.btnRefuse:
                handle("2");
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

//        public void setDataDocument(List<IBusinessUserBean.Obj.Document> data) {
//            datas.clear();
//            datas.addAll(data);
//            notifyDataSetChanged();
//        }
//
//        public void setDataProduct(List<IBusinessUserBean.Obj.Product> data) {
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
//            if (data instanceof IBusinessUserBean.Obj.Product) {
//                holder.textView.setText(((IBusinessUserBean.Obj.Product) data).getProduct_name());
//                ImageUtils.loadImage(mContext, ((IBusinessUserBean.Obj.Product) data).getP_image_url(), holder.imageView);
//            } else if (data instanceof IBusinessUserBean.Obj.Document) {
//                holder.textView.setText(((IBusinessUserBean.Obj.Document) data).getDocument_name());
//                ImageUtils.loadImage(mContext, ((IBusinessUserBean.Obj.Document) data).getD_image_url(), holder.imageView);
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
