package com.xhy.zhanhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.aaron.aaronlibrary.widget.ActionbarView;
import com.aaron.aaronlibrary.widget.listview.SwipeItemLayout;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.OrderBean;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 首页-我的预约列表页面
 * Created by Aaron on 15/12/2017.
 */

public class OrderActivity extends ZhanHuiActivity implements SwipeItemLayout.OnScrollOffsetListener {

    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private ReceiveTrustAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_order;
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
        setActionbarTitle("收到的邀请");
        setActionbarMode(ActionbarView.ACTIONBAR_MODE_SEARCH);
        getActionbarView().setSearchEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                    return true;
                }
                return false;
            }
        });
        getActionbarView().getSearchButton().setVisibility(View.INVISIBLE);
        initPull();
        getData(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            adapter = null;
            getData(true);
        }
    }

    /**
     * 搜索预约
     */
    private void search() {

    }

    /**
     * 获取数据
     */
    private void getData(boolean isShowDialog) {
        PostCall.get(mContext, ServerUrl.reservations(), new BaseMap(), new PostCall.PostResponse<OrderBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, OrderBean bean) {
                if (ptrFrameLayout.isRefreshing())
                    ptrFrameLayout.refreshComplete();
                setRecyclerView();
                for (int i = 0; i < bean.getData().size(); i++) {
                    adapter.addData(bean.getData().get(i));
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, isShowDialog, OrderBean.class);
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

    /**
     * 设置资料、动态
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(mContext));
        if (adapter == null) {
            adapter = new ReceiveTrustAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                    StartActivityUtils.startTrustDetail(mContext, ((OrderBean.Obj) holder.data).getReservation_id());
                }
            });
            adapter.setOnItemLongClickListener(new OnRecyclerItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, BaseViewHolder holder) {}
            });
        } else
            adapter.notifyDataSetChanged();
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
                    inflate(R.layout.item_order, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ReceiveTrustHolder holder, int position) {
            OrderBean.Obj data = (OrderBean.Obj) getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            ImageUtils.loadImageRoundedCorners(mContext, data.getImage_url(), holder.ivAvatar, RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
            holder.tvName.setText(data.getCompany_name());
            holder.tvTime.setText("预约时间：" + data.getReservation_time());
//            holder.itemLayout.setEnabled(false);
//            holder.btnDelete.setOnClickListener(this);
//            holder.btnDelete.setTag(holder);
            switch (data.getState()) {
                case "1":
                    holder.tvState.setText("等待对方响应");
                    holder.tvState.setTextColor(getColorFromResuource(R.color.order_state1));
                    holder.ivState.setVisibility(View.VISIBLE);
                    holder.ivState.setImageResource(R.mipmap.icon_order_state1);
                    holder.tvName.setTextColor(getColorFromResuource(R.color.order_title));
                    holder.tvTime.setTextColor(getColorFromResuource(R.color.order_text));
                    holder.tvStateText.setTextColor(getColorFromResuource(R.color.order_text));
                    break;
                case "2":
                    holder.tvState.setText("已接收");
                    holder.tvState.setTextColor(getColorFromResuource(R.color.order_state2));
                    holder.ivState.setVisibility(View.VISIBLE);
                    holder.ivState.setImageResource(R.mipmap.icon_order_state2);
                    holder.tvName.setTextColor(getColorFromResuource(R.color.order_title));
                    holder.tvTime.setTextColor(getColorFromResuource(R.color.order_text));
                    holder.tvStateText.setTextColor(getColorFromResuource(R.color.order_text));
                    break;
                case "3":
                    holder.tvState.setText("被拒绝");
                    holder.tvState.setTextColor(getColorFromResuource(R.color.order_state3));
                    holder.ivState.setVisibility(View.VISIBLE);
                    holder.ivState.setImageResource(R.mipmap.icon_order_state3);
                    holder.tvName.setTextColor(getColorFromResuource(R.color.order_title));
                    holder.tvTime.setTextColor(getColorFromResuource(R.color.order_text));
                    holder.tvStateText.setTextColor(getColorFromResuource(R.color.order_text));
                    break;
                case "4":
                    holder.tvState.setText("已完成");
                    holder.tvState.setTextColor(getColorFromResuource(R.color.order_state4));
                    holder.ivState.setVisibility(View.VISIBLE);
                    holder.ivState.setImageResource(R.mipmap.icon_order_state4);
                    holder.tvName.setTextColor(getColorFromResuource(R.color.order_title_done));
                    holder.tvTime.setTextColor(getColorFromResuource(R.color.order_text_donw));
                    holder.tvStateText.setTextColor(getColorFromResuource(R.color.order_text_donw));
                    break;
                case "5":
                    holder.tvState.setText("已取消");
                    holder.tvState.setTextColor(getColorFromResuource(R.color.order_state4));
                    holder.ivState.setVisibility(View.GONE);
                    holder.tvName.setTextColor(getColorFromResuource(R.color.order_title_done));
                    holder.tvTime.setTextColor(getColorFromResuource(R.color.order_text_donw));
                    holder.tvStateText.setTextColor(getColorFromResuource(R.color.order_text_donw));
                    break;
            }
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        public Object getItem(int position) {
            return datas.get(position);
        }

        /**
         * 删除消息
         * @param holder
         */
        private void delete(final ReceiveTrustHolder holder) {
//            PostCall.deleteJson(mContext, ServerUrl.deleteAcceptRecord(), new DeleteTrustVo((((OrderBean.Obj) holder.data).getRequest_id()), getUserId()), new PostCall.PostResponse<BaseBean>() {
//                @Override
//                public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
//                    removeData(holder.data, holder.getAdapterPosition());
//                }
//
//                @Override
//                public void onFailure(int statusCode, byte[] responseBody) {
//
//                }
//            }, new String[]{}, false, BaseBean.class);
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
        ImageView ivAvatar, ivState;
        TextView tvName, tvTime, tvStateText, tvState;
//        Button btnDelete;

        public ReceiveTrustHolder(View itemView) {
            super(itemView);
//            itemLayout = itemView.findViewById(R.id.swipeItem);
            parent = itemView.findViewById(R.id.parent);
            ivAvatar = itemView.findViewById(R.id.ivThum);
            tvName = itemView.findViewById(R.id.tvName);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvStateText = itemView.findViewById(R.id.tvStateText);
            tvState = itemView.findViewById(R.id.tvState);
            ivState = itemView.findViewById(R.id.ivState);
//            btnDelete = itemView.findViewById(R.id.delete);
        }
    }
}
