package com.xhy.zhanhui.fragment.businesscircle;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.BusinessAttentionUserActivity;
import com.xhy.zhanhui.activity.BusinessTargetUserActivity;
import com.xhy.zhanhui.activity.BusinessUserDetailActivity;
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.BusinessTrustBean;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 商圈-企业Fragment
 * Created by Aaron on 14/12/2017.
 */

public class BusinessUserFragment extends ZhanHuiFragment {

    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private BusinessUserAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_business_user;
    }

    @Override
    protected void findViews(View view) {
        ptrFrameLayout = findViewById(R.id.ptrFrameLayout);
        recyclerView = view.findViewById(R.id.recycler);
        findViewAndSetListener(R.id.rlTarget);
        findViewAndSetListener(R.id.rlAttention);
    }

    @Override
    protected void init() {
        super.init();
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
     * 获取信任企业数据
     */
    private void getData() {
        // 如果是重新加载数据，清空adapter
        if (adapter != null)
            adapter.clearData();
        PostCall.get(mContext, ServerUrl.trustCustomers(), new BaseMap(), new PostCall.PostResponse<BusinessTrustBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BusinessTrustBean bean) {
                if (ptrFrameLayout.isRefreshing())
                    ptrFrameLayout.refreshComplete();
                List<BusinessTrustBean.Obj> dataBean = bean.getData();
                if (dataBean == null)
                    return;
                setRecyclerView();
                int size = bean.getData().size();
                showNoDataBg(size);
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
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
        linearLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
//        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (adapter == null) {
            adapter = new BusinessUserAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                    String toUserId = "";
                    if (holder.data instanceof BusinessTrustBean.Obj) {
                        toUserId = ((BusinessTrustBean.Obj) holder.data).getUser_id();
                    }
                    StartActivityUtils.startTrustUserDetail(mContext, toUserId, BusinessUserDetailActivity.TYPE_TRUST);
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

    public class BusinessUserAdapter extends RecyclerView.Adapter<BusinessUserHolder> implements View.OnClickListener, View.OnLongClickListener {

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

        public BusinessUserAdapter(Context context) {
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
        public BusinessUserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final BusinessUserHolder holder = new BusinessUserHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_business_company, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(BusinessUserHolder holder, int position) {
            Object data = getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            ImageUtils.loadImage(mContext, ((BusinessTrustBean.Obj) data).getIcon(), holder.ivAvatar);
            holder.tvName.setText(((BusinessTrustBean.Obj) data).getUser_name());
            if (position == getItemCount() - 1)
                holder.divider.setVisibility(View.GONE);
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
                    BusinessUserHolder holder = (BusinessUserHolder) v.getTag();
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
                    BusinessUserHolder holder = (BusinessUserHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class BusinessUserHolder extends BaseViewHolder {
        RelativeLayout parent;
        ImageView ivAvatar;
        TextView tvName;
        View divider;

        public BusinessUserHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            ivAvatar = itemView.findViewById(R.id.ivThum);
            tvName = itemView.findViewById(R.id.tvName);
            divider = itemView.findViewById(R.id.divider);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rlTarget:
                startMyActivity(BusinessTargetUserActivity.class);
                break;
            case R.id.rlAttention:
                startMyActivity(BusinessAttentionUserActivity.class);
                break;
        }
    }

    public void refresh() {
        getData();
    }
}
