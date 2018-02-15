package com.xhy.zhanhui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.IBusinessListBean;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 智能商务-需求列表页面
 * Created by Aaron on 2018/2/6.
 */

public class IntelligentBusinessListActivity extends ZhanHuiActivity {

    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private ReceiveTrustAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_intelligent_business_list;
    }

    @Override
    protected void findView() {
        super.findView();
        ptrFrameLayout = findViewById(R.id.ptrFrameLayout);
        recyclerView = findViewById(R.id.recycler);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("我的需求");
        initPull();
        getData(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            adapter = null;
            getData(false);
        }
    }

    /**
     * 获取数据
     */
    private void getData(boolean isShowDialog) {
        PostCall.get(mContext, ServerUrl.demandsList(), new BaseMap(), new PostCall.PostResponse<IBusinessListBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, IBusinessListBean bean) {
                if (ptrFrameLayout.isRefreshing())
                    ptrFrameLayout.refreshComplete();
                setRecyclerView();
                showNoDataBg(bean.getData().size());
                for (int i = 0; i < bean.getData().size(); i++) {
                    adapter.addData(bean.getData().get(i));
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, isShowDialog, IBusinessListBean.class);
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
                getData(false);
            }
        });
    }

    /**
     * 设置
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (adapter == null) {
            adapter = new ReceiveTrustAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                    StartActivityUtils.startDemandDetail(mContext, ((IBusinessListBean.Obj) holder.data).getDemand_id());
                }
            });
            adapter.setOnItemLongClickListener(new OnRecyclerItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, final BaseViewHolder holder) {
                    showAlertDialog("", "是否删除该需求？", "取消", null, "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete(holder.getAdapterPosition());
                        }
                    }, true);
                }
            });
        } else
            adapter.notifyDataSetChanged();
    }

    /**
     * 删除需求
     *
     * @param index 角标
     */
    private void delete(final int index) {
        final IBusinessListBean.Obj data = (IBusinessListBean.Obj) adapter.getItem(index);
        PostCall.delete(mContext, ServerUrl.demandsDetail(data.getDemand_id()), "0", new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                adapter.removeData(data, index);
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, BaseBean.class);
    }

    public class ReceiveTrustAdapter extends RecyclerView.Adapter<ReceiveTrustHolder> implements View.OnClickListener, View.OnLongClickListener {

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

        public ReceiveTrustAdapter(Context context) {
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

        @Override
        public ReceiveTrustHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ReceiveTrustHolder holder = new ReceiveTrustHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_demand, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ReceiveTrustHolder holder, int position) {
            IBusinessListBean.Obj data = (IBusinessListBean.Obj) getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            holder.tvName.setText(data.getDemand_title());
            holder.tvTime.setText(data.getPost_time());
//            holder.itemLayout.setEnabled(false);
//            holder.btnDelete.setOnClickListener(this);
//            holder.btnDelete.setTag(holder);
            if (Integer.parseInt(data.getMatching_nums()) == 0) {
                holder.tvState.setText("匹配中");
                holder.ivState.setImageResource(R.mipmap.icon_demand_state1);
                ImageUtils.loadImageRoundedCorners(mContext, R.mipmap.item_demand_bg1, holder.ivBg, RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
            } else if (Integer.parseInt(data.getMatching_success_nums()) == 0) {
                holder.tvState.setText("匹配" + data.getMatching_nums() + "个结果");
                holder.ivState.setImageResource(R.mipmap.icon_demand_state3);
                ImageUtils.loadImageRoundedCorners(mContext, R.mipmap.item_demand_bg3, holder.ivBg, RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
            } else {
                holder.tvState.setText("已解决");
                holder.ivState.setImageResource(R.mipmap.icon_demand_state2);
                ImageUtils.loadImageRoundedCorners(mContext, R.mipmap.item_demand_bg2, holder.ivBg, RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
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
                    ReceiveTrustHolder holder = (ReceiveTrustHolder) v.getTag();
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
                    ReceiveTrustHolder holder = (ReceiveTrustHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class ReceiveTrustHolder extends BaseViewHolder {
        //        SwipeItemLayout itemLayout;
        RelativeLayout parent;
        ImageView ivBg, ivState;
        TextView tvName, tvTime, tvStateText, tvState;
//        Button btnDelete;

        public ReceiveTrustHolder(View itemView) {
            super(itemView);
//            itemLayout = itemView.findViewById(R.id.swipeItem);
            parent = itemView.findViewById(R.id.parent);
            ivBg = itemView.findViewById(R.id.ivBg);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvState = itemView.findViewById(R.id.tvState);
            ivState = itemView.findViewById(R.id.ivState);
//            btnDelete = itemView.findViewById(R.id.delete);
        }
    }
}
