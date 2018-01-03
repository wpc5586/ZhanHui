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
import com.xhy.zhanhui.http.domain.BusinessTrustBean;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 商圈关注客户页面
 * Created by Aaron on 20/12/2017.
 */

public class BusinessAttentionUserActivity extends ZhanHuiActivity {

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
        setActionbarTitle("关注客户");
        initPull();
        getOnlineData();
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
                getOnlineData();
            }
        });
    }

    /**
     * 获取线上关注数据
     */
    private void getOnlineData() {
        // 如果是重新加载数据，清空adapter
        if (adapter != null)
            adapter.clearData();
        if (!ptrFrameLayout.isRefreshing())
            showProgressDialog("加载中");
        PostCall.get(mContext, ServerUrl.onlineFocusCustomers(), new BaseMap(), new PostCall.PostResponse<BusinessTrustBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BusinessTrustBean bean) {
                List<BusinessTrustBean.Obj> dataBean = bean.getData();
                if (dataBean == null)
                    return;
                setRecyclerView();
                int size = bean.getData().size();
                for (int i = 0; i < size; i++) {
                    adapter.addData(bean.getData().get(i));
                }
                getOfflineData();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, BusinessTrustBean.class);
    }

    /**
     * 获取线下关注数据
     */
    private void getOfflineData() {
        PostCall.get(mContext, ServerUrl.offlineFocusCustomers(), new BaseMap(), new PostCall.PostResponse<BusinessTrustBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BusinessTrustBean bean) {
                if (ptrFrameLayout.isRefreshing())
                    ptrFrameLayout.refreshComplete();
                else
                    dismissProgressDialog();
                List<BusinessTrustBean.Obj> dataBean = bean.getData();
                if (dataBean == null)
                    return;
                int size = bean.getData().size();
                for (int i = 0; i < size; i++) {
                    adapter.addData(bean.getData().get(i));
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, BusinessTrustBean.class);
    }


    /**
     * 设置RecyclerView
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (adapter == null) {
            adapter = new BusinessAttentionAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                    String toUserId = "";
                    if (holder.data instanceof BusinessTrustBean.Obj) {
                        toUserId = ((BusinessTrustBean.Obj) holder.data).getUser_id();
                    } else if (holder.data instanceof BusinessTrustBean.Obj) {
                        toUserId = ((BusinessTrustBean.Obj) holder.data).getUser_id();
                    }
//                    StartActivityUtils.startTrustCompanyDetail(mContext, companyId, BusinessCompanyDetailActivity.TYPE_FOCUS);
                    StartActivityUtils.startVcardNoQRcode(mContext, toUserId);
                }
            });
            adapter.setOnItemLongClickListener(new OnRecyclerItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, BaseViewHolder holder) {
                }
            });
        } else
            adapter.notifyDataSetChanged();
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

        public BusinessAttentionAdapter(Context context){
            this.context = context;
        }

        public void addData(Object data) {
            datas.add(data);
            notifyDataSetChanged();
        }

        public void clearData() {
            datas.clear();
        }

        @Override
        public BusinessCompanyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final BusinessCompanyHolder holder = new BusinessCompanyHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_business_user_attention, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(BusinessCompanyHolder holder, int position) {
            Object data = getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            if (data instanceof BusinessTrustBean.Obj) {
                ImageUtils.loadImageCircle(mContext, ((BusinessTrustBean.Obj) data).getIcon(), holder.ivAvatar);
                holder.tvName.setText(((BusinessTrustBean.Obj) data).getUser_name());
//                holder.tvContent.setText(data.getContent());
//                holder.tvDegree.setText(data.getContent());
            }
//            else if (data instanceof BusinessTrustBean.Obj) {
//                ImageUtils.loadImageRoundedCorners(mContext, ((BusinessTrustBean.Obj) data).getIcon(), holder.ivAvatar, RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
//                holder.tvName.setText(((BusinessTrustBean.Obj) data).getCompany_name());
////                holder.tvContent.setText(data.getContent());
////                holder.tvDegree.setText(((BusinessTrustBean.Obj) data).getAttention_degree());
//            }
            holder.btnTrust.setOnClickListener(this);
            holder.btnTrust.setTag(holder);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public Object getItem(int position) { return datas.get(position); }

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
                    String toUserId = "";
                    if (holder1.data instanceof BusinessTrustBean.Obj) {
                        toUserId = ((BusinessTrustBean.Obj) holder1.data).getUser_id();
//                        TrustCompanyBean bean = new TrustCompanyBean();
//                        TrustCompanyBean.Obj obj = bean.new Obj();
//                        List<TrustCompanyBean.Obj.User> users = new ArrayList<>();
//                        TrustCompanyBean.Obj.User user = obj.new User();
//                        BusinessTrustBean.Obj.User holderUser = ((BusinessTrustBean.Obj) holder1.data).getCompany_users().get(0);
//                        user.setUser_id(holderUser.getUser_id());
//                        user.setHx_username(holderUser.getHx_username());
//                        user.setIcon(holderUser.getIcon());
//                        user.setNickname(holderUser.getNickname());
//                        user.setV_title(holderUser.getV_title());
//                        users.add(user);
//                        obj.setCompany_users(users);
//                        obj.setCompany_id(((BusinessTrustBean.Obj) holder1.data).getCompany_id());
//                        obj.setCompany_name(((BusinessTrustBean.Obj) holder1.data).getCompany_name());
//                        bean.setData(obj);
                    } else if (holder1.data instanceof BusinessTrustBean.Obj) {
                        toUserId = ((BusinessTrustBean.Obj) holder1.data).getUser_id();
//                        TrustCompanyBean bean = new TrustCompanyBean();
//                        TrustCompanyBean.Obj obj = bean.new Obj();
//                        List<TrustCompanyBean.Obj.User> users = new ArrayList<>();
//                        TrustCompanyBean.Obj.User user = obj.new User();
//                        BusinessTrustBean.Obj.User holderUser = ((BusinessTrustBean.Obj) holder1.data).getCompany_users().get(0);
//                        user.setUser_id(holderUser.getUser_id());
//                        user.setHx_username(holderUser.getHx_username());
//                        user.setIcon(holderUser.getIcon());
//                        user.setNickname(holderUser.getNickname());
//                        user.setV_title(holderUser.getV_title());
//                        users.add(user);
//                        obj.setCompany_users(users);
//                        obj.setCompany_id(((BusinessTrustBean.Obj) holder1.data).getCompany_id());
//                        obj.setCompany_name(((BusinessTrustBean.Obj) holder1.data).getCompany_name());
//                        bean.setData(obj);
                    }
                    StartActivityUtils.startTrustUserDetail(mContext, toUserId, BusinessUserDetailActivity.TYPE_FOCUS);
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
                default:
                    break;
            }
            return false;
        }
    }

    static class BusinessCompanyHolder extends BaseViewHolder {
        RelativeLayout parent;
        ImageView ivAvatar;
        TextView tvName, tvContent, tvDegree;
        Button btnTrust;

        public BusinessCompanyHolder(View itemView) {
            super(itemView);
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
