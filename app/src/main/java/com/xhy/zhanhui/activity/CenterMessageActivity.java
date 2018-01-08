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
import com.aaron.aaronlibrary.utils.TimeUtils;
import com.aaron.aaronlibrary.widget.listview.SwipeItemLayout;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.CenterMessageBean;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 资料中心消息列表页面
 * Created by Aaron on 15/12/2017.
 */

public class CenterMessageActivity extends ZhanHuiActivity implements SwipeItemLayout.OnScrollOffsetListener {

    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private CenterMessageAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_center_message;
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
        setActionbarTitle("通知");
        initPull();
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        PostCall.get(mContext, ServerUrl.messages(), new BaseMap(), new PostCall.PostResponse<CenterMessageBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, CenterMessageBean bean) {
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
        }, new String[]{}, true, CenterMessageBean.class);
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
     * 设置资料、动态
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addOnItemTouchListener(new SwipeItemLayout.OnSwipeItemTouchListener(mContext));
        if (adapter == null) {
            adapter = new CenterMessageAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                    StartActivityUtils.startCenterMessageDetail(mContext, (CenterMessageBean.Obj) ((CenterMessageHolder) holder).data);
                }
            });
            adapter.setOnItemLongClickListener(new OnRecyclerItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, BaseViewHolder holder) {}
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

    public class CenterMessageAdapter extends RecyclerView.Adapter<CenterMessageHolder> implements View.OnClickListener, View.OnLongClickListener {

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

        public CenterMessageAdapter(Context context) {
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
        public CenterMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final CenterMessageHolder holder = new CenterMessageHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_center_message, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(CenterMessageHolder holder, int position) {
            CenterMessageBean.Obj data = (CenterMessageBean.Obj) getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            ImageUtils.loadImageRoundedCorners(mContext, data.getSender_icon(), holder.ivAvatar, RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
            holder.tvName.setText(data.getSender_name());
            holder.tvContent.setText(data.getContent());
            holder.tvTime.setText(TimeUtils.getTimestampString(Long.valueOf(data.getPush_time())));
            holder.btnDelete.setOnClickListener(this);
            holder.btnDelete.setTag(holder);
            holder.itemLayout.setOnScrollOffsetListener(CenterMessageActivity.this);
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
        private void delete(final CenterMessageHolder holder) {
            String messageId = ((CenterMessageBean.Obj) holder.data).getMessage_id();
            PostCall.delete(mContext, ServerUrl.deleteMessages(messageId), "", new PostCall.PostResponse<BaseBean>() {
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
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    CenterMessageHolder holder = (CenterMessageHolder) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, holder);
                    break;
                case R.id.delete:
                    CenterMessageHolder holder1 = (CenterMessageHolder) v.getTag();
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
                    CenterMessageHolder holder = (CenterMessageHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class CenterMessageHolder extends BaseViewHolder {
        SwipeItemLayout itemLayout;
        RelativeLayout parent;
        ImageView ivAvatar;
        TextView tvName, tvContent, tvTime;
        Button btnDelete;

        public CenterMessageHolder(View itemView) {
            super(itemView);
            itemLayout = itemView.findViewById(R.id.swipeItem);
            parent = itemView.findViewById(R.id.parent);
            ivAvatar = itemView.findViewById(R.id.ivThum);
            tvName = itemView.findViewById(R.id.tvName);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
            btnDelete = itemView.findViewById(R.id.delete);
        }
    }
}
