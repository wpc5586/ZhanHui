package com.xhy.zhanhui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.domain.BaseViewHolder;
import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.listener.OnRecyclerItemClickListener;
import com.aaron.aaronlibrary.listener.OnRecyclerItemLongClickListener;
import com.aaron.aaronlibrary.transformations.RoundedCornersTransformation;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.aaron.aaronlibrary.widget.listview.SwipeItemLayout;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.BusinessOfflineBean;
import com.xhy.zhanhui.http.domain.BusinessOnlineBean;
import com.xhy.zhanhui.http.domain.TargetCompanyBean;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 商圈推荐企业页面
 * Created by Aaron on 20/12/2017.
 */

public class BusinessTargetCompanyActivity extends ZhanHuiActivity implements SwipeItemLayout.OnScrollOffsetListener {

    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private BusinessAttentionAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_business_attention_company;
    }

    @Override
    protected void findView() {
        ptrFrameLayout = findViewById(R.id.ptrFrameLayout);
        recyclerView = findViewById(R.id.recycler);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("推荐企业");
        initPull();
        getData();
    }

    /**
     * 设置上拉下拉
     */
    private void initPull() {
        PublicMethod.setPullView(mContext, ptrFrameLayout, PtrFrameLayout.Mode.REFRESH, new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(final PtrFrameLayout frame) {
//                page++;
//                getData();
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {
                adapter = null;
                getData();
            }
        });
    }

    /**
     * 获取推荐数据
     */
    private void getData() {
        // 如果是重新加载数据，清空adapter
        if (adapter != null)
            adapter.clearData();
        PostCall.get(mContext, ServerUrl.targetCompanies(), new BaseMap(), new PostCall.PostResponse<TargetCompanyBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, TargetCompanyBean bean) {
                if (ptrFrameLayout.isRefreshing())
                    ptrFrameLayout.refreshComplete();
                List<TargetCompanyBean.Obj> dataBean = bean.getData();
                if (dataBean == null)
                    return;
                setRecyclerView();
                int size = bean.getData().size();
                for (int i = 0; i < size; i++) {
                    adapter.addData(bean.getData().get(i));
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, TargetCompanyBean.class);
    }

    /**
     * 设置RecyclerView
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(mContext));
        if (adapter == null) {
            adapter = new BusinessAttentionAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                    String companyId = ((TargetCompanyBean.Obj) holder.data).getCompany_id();
                    StartActivityUtils.startCompanyDetail(mContext, companyId);
                }
            });
        } else
            adapter.notifyDataSetChanged();
    }

    /**
     * Item滑动监听
     * @param offset
     */
    @Override
    public void scroll(int offset) {
        if (offset == 0)
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
        else
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.NONE);
    }

    public class BusinessAttentionAdapter extends RecyclerView.Adapter<BusinessCompanyHolder> implements View.OnClickListener, View.OnLongClickListener {

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

        public BusinessAttentionAdapter(Context context) {
            this.context = context;
        }

        public void addData(Object data) {
            datas.add(data);
            notifyDataSetChanged();
        }

        public void removeData(Object data, int position) {
            datas.remove(data);
            notifyItemRemoved(position);
        }

        public void clearData() {
            datas.clear();
        }

        @Override
        public BusinessCompanyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final BusinessCompanyHolder holder = new BusinessCompanyHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_business_company_attention, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(BusinessCompanyHolder holder, int position) {
            Object data = getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            ImageUtils.loadImageRoundedCorners(mContext, ((TargetCompanyBean.Obj) data).getImage_url(), holder.ivAvatar, RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
            holder.tvName.setText(((TargetCompanyBean.Obj) data).getCompany_name());
            holder.btnTrust.setOnClickListener(this);
            holder.btnTrust.setTag(holder);
            holder.btnDelete.setOnClickListener(this);
            holder.btnDelete.setTag(holder);
            holder.itemLayout.setOnScrollOffsetListener(BusinessTargetCompanyActivity.this);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public Object getItem(int position) {
            return datas.get(position);
        }

        /**
         * 删除推荐企业
         * @param holder
         */
        private void delete(final BusinessCompanyHolder holder) {
            String companyId = ((TargetCompanyBean.Obj) holder.data).getCompany_id();
            PostCall.delete(mContext, ServerUrl.deleteTargetCompanies(companyId), "", new PostCall.PostResponse<BaseBean>() {
                @Override
                public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                    removeData(holder.data, holder.getAdapterPosition());
                }

                @Override
                public void onFailure(int statusCode, byte[] responseBody) {

                }
            }, new String[]{}, false, BaseBean.class);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    BusinessCompanyHolder holder = (BusinessCompanyHolder) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, holder);
                    break;
                case R.id.btnTrust:
                    BusinessCompanyHolder holder1 = (BusinessCompanyHolder) v.getTag();
                    String companyId = "";
                    companyId = ((TargetCompanyBean.Obj) holder1.data).getCompany_id();
//                        TrustCompanyBean bean = new TrustCompanyBean();
//                        TrustCompanyBean.Obj obj = bean.new Obj();
//                        List<TrustCompanyBean.Obj.User> users = new ArrayList<>();
//                        TrustCompanyBean.Obj.User user = obj.new User();
//                        BusinessOnlineBean.Obj.User holderUser = ((BusinessOnlineBean.Obj) holder1.data).getCompany_users().get(0);
//                        user.setUser_id(holderUser.getUser_id());
//                        user.setHx_username(holderUser.getHx_username());
//                        user.setIcon(holderUser.getIcon());
//                        user.setNickname(holderUser.getNickname());
//                        user.setV_title(holderUser.getV_title());
//                        users.add(user);
//                        obj.setCompany_users(users);
//                        obj.setCompany_id(((BusinessOnlineBean.Obj) holder1.data).getCompany_id());
//                        obj.setCompany_name(((BusinessOnlineBean.Obj) holder1.data).getCompany_name());
//                        bean.setData(obj);
                    StartActivityUtils.startTrustCompanyDetail(mContext, companyId, BusinessCompanyDetailActivity.TYPE_TARGET);
                    break;
                case R.id.delete:
                    BusinessCompanyHolder holder2 = (BusinessCompanyHolder) v.getTag();
                    holder2.itemLayout.close();
                    delete(holder2);
                    break;

                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    BusinessCompanyHolder holder = (BusinessCompanyHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                case R.id.btnTrust:
                    Object data = v.getTag();
                    String companyId = "";
                    if (data instanceof BusinessOnlineBean.Obj) {
                        companyId = ((BusinessOnlineBean.Obj) data).getCompany_id();
                    } else if (data instanceof BusinessOfflineBean.Obj) {
                        companyId = ((BusinessOfflineBean.Obj) data).getCompany_id();
                    }
                    // TODO
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class BusinessCompanyHolder extends BaseViewHolder {
        SwipeItemLayout itemLayout;
        Button btnDelete;
        RelativeLayout parent;
        ImageView ivAvatar;
        TextView tvName, tvContent, tvDegree;
        Button btnTrust;

        public BusinessCompanyHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.swipeItem);
            btnDelete = itemView.findViewById(R.id.delete);
            parent = itemView.findViewById(R.id.parent);
            ivAvatar = itemView.findViewById(R.id.ivThum);
            tvName = itemView.findViewById(R.id.tvName);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvDegree = itemView.findViewById(R.id.tvTime);
            btnTrust = itemView.findViewById(R.id.btnTrust);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rlSpanner1:
                break;
        }
    }
}
