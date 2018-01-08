package com.xhy.zhanhui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
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
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.aaron.aaronlibrary.widget.listview.SwipeItemLayout;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.SendTrustBean;
import com.xhy.zhanhui.http.vo.DeleteTrustVo;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 商务社交-发出的邀请列表页面
 * Created by Aaron on 15/12/2017.
 */

public class SendTrustActivity extends ZhanHuiActivity implements SwipeItemLayout.OnScrollOffsetListener {

    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private ReceiveTrustAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_send_trust;
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
        setActionbarTitle("我发起申请");
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
     * 获取数据
     */
    private void getData(boolean isShowDialog) {
        PostCall.get(mContext, ServerUrl.requestInvitations(), new BaseMap(), new PostCall.PostResponse<SendTrustBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, SendTrustBean bean) {
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
        }, new String[]{}, isShowDialog, SendTrustBean.class);
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
                    StartActivityUtils.startTrustDetail(mContext, getUserId(), ((SendTrustBean.Obj) holder.data).getAccept_id(), false);
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
                    inflate(R.layout.item_receive_trust, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ReceiveTrustHolder holder, int position) {
            SendTrustBean.Obj data = (SendTrustBean.Obj) getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            ImageUtils.loadImageCircle(mContext, data.getIcon(), holder.ivAvatar);
            holder.tvName.setText(data.getNickname());
//            holder.btnAccept.setOnClickListener(this);
            holder.btnAccept.setTag(holder);
            holder.btnAccept.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
            holder.btnAccept.getLayoutParams().width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            holder.itemLayout.setOnScrollOffsetListener(SendTrustActivity.this);
            holder.btnDelete.setOnClickListener(this);
            holder.btnDelete.setTag(holder);
            switch (data.getState()) {
                case "1":
                    holder.btnAccept.setBackgroundColor(getColorFromResuource(R.color.transparent));
                    holder.btnAccept.setTextColor(getColorFromResuource(R.color.theme_black));
                    holder.btnAccept.setText("等待对方同意");
                    holder.btnAccept.getLayoutParams().height = MathUtils.dip2px(mContext, 25);
                    holder.btnAccept.setEnabled(false);
                    holder.ivDot2.setImageResource(R.mipmap.icon_dot_orange);
                    holder.ivDot3.setImageResource(R.mipmap.icon_dot_gray);
                    break;
                case "2":
                    setBtnAcceptSuccess(holder, "已同意");
                    holder.ivDot2.setImageResource(R.mipmap.icon_dot_green);
                    holder.ivDot3.setImageResource(R.mipmap.icon_dot_green);
                    break;
                case "3":
                    setBtnAcceptSuccess(holder, "已拒绝");
                    holder.ivDot2.setImageResource(R.mipmap.icon_dot_red);
                    holder.ivDot3.setImageResource(R.mipmap.icon_dot_red);
                    break;
                case "4":
                    setBtnAcceptSuccess(holder, "已解除");
                    holder.ivDot2.setImageResource(R.mipmap.icon_dot_red);
                    holder.ivDot3.setImageResource(R.mipmap.icon_dot_red);
                    break;
            }
        }

        /**
         * 设置按钮已同意状态
         * @param holder
         * @param text 内容
         */
        private void setBtnAcceptSuccess(ReceiveTrustHolder holder, String text) {
            holder.btnAccept.setBackgroundColor(getColorFromResuource(R.color.transparent));
            holder.btnAccept.setTextColor(getColorFromResuource(R.color.theme_black));
            holder.btnAccept.setText(text);
            holder.btnAccept.getLayoutParams().height = MathUtils.dip2px(mContext, 25);
            holder.btnAccept.setEnabled(false);
        }

        /**
         * 删除消息
         * @param holder
         */
        private void delete(final ReceiveTrustHolder holder) {
            PostCall.deleteJson(mContext, ServerUrl.deleteRequestRecord(), new DeleteTrustVo(getUserId(), (((SendTrustBean.Obj) holder.data).getAccept_id())), new PostCall.PostResponse<BaseBean>() {
                @Override
                public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                    removeData(holder.data, holder.getAdapterPosition());
                    showNoDataBg(getItemCount());
                }

                @Override
                public void onFailure(int statusCode, byte[] responseBody) {

                }
            }, new String[]{}, false, BaseBean.class);
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
                case R.id.delete:
                    ReceiveTrustHolder holder1 = (ReceiveTrustHolder) v.getTag();
                    holder1.itemLayout.close();
                    delete(holder1);
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
        SwipeItemLayout itemLayout;
        RelativeLayout parent;
        ImageView ivAvatar, ivDot1, ivDot2, ivDot3;
        TextView tvName;
        Button btnAccept;
        Button btnDelete;

        public ReceiveTrustHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.swipeItem);
            parent = itemView.findViewById(R.id.parent);
            ivAvatar = itemView.findViewById(R.id.ivThum);
            tvName = itemView.findViewById(R.id.tvName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            ivDot1 = itemView.findViewById(R.id.ivDot1);
            ivDot2 = itemView.findViewById(R.id.ivDot2);
            ivDot3 = itemView.findViewById(R.id.ivDot3);
            btnDelete = itemView.findViewById(R.id.delete);
        }
    }
}
