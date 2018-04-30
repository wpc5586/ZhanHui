package com.xhy.zhanhui.fragment.ibusiness;

import android.content.Context;
import android.content.DialogInterface;
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
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.IBusinessUserDetailActivity;
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.IBusinessReceiveBean;
import com.xhy.zhanhui.http.vo.IBusinessHandleVo;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 智能匹配-收到的需求-最新的Fragment
 * Created by Aaron on 14/12/2017.
 */

public class IBusinessNewestFragment extends ZhanHuiFragment{

    private String state = "0";  //company_handle_state=0 为最新的需求匹配结果(未同意的) company_handle_state=1 为已同意的需求匹配结
    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private IBusinessReceiveAdapter adapter;

    public IBusinessNewestFragment setState(String state) {
        this.state = state;
        return this;
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_ibusiness_receive;
    }

    @Override
    protected void findViews(View view) {
        ptrFrameLayout = findViewById(R.id.ptrFrameLayout);
        recyclerView = view.findViewById(R.id.recycler);
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
        PostCall.get(mContext, ServerUrl.demandsList(state), new BaseMap(), new PostCall.PostResponse<IBusinessReceiveBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, IBusinessReceiveBean bean) {
                if (ptrFrameLayout.isRefreshing())
                    ptrFrameLayout.refreshComplete();
                List<IBusinessReceiveBean.Obj> dataBean = bean.getData();
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
        }, new String[]{}, true, IBusinessReceiveBean.class);
    }


    /**
     * 设置RecyclerView
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        if (adapter == null) {
            adapter = new IBusinessReceiveAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                    int type = IBusinessUserDetailActivity.TYPE_DEFAULT;
                    if ("2".equals(((IBusinessReceiveBean.Obj) holder.data).getIs_friend()))
                        type = IBusinessUserDetailActivity.TYPE_TRUST;
                    else if ("1".equals(((IBusinessReceiveBean.Obj) holder.data).getCompany_handle_state()))
                        type = IBusinessUserDetailActivity.TYPE_HANDLE;
                    String matchingId = ((IBusinessReceiveBean.Obj) holder.data).getMatching_id();
                    StartActivityUtils.startDemandUserDetail(mContext, matchingId, type);
                }
            });
            adapter.setOnItemLongClickListener(new OnRecyclerItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, final BaseViewHolder holder) {
                    showAlertDialog("", "是否删除该条匹配结果？", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handle(((IBusinessReceiveBean.Obj) holder.data).getMatching_id(), "3", holder.getAdapterPosition());
                        }
                    }, "取消", null, true);
                }
            });
        } else
            adapter.notifyDataSetChanged();
    }

    public class IBusinessReceiveAdapter extends RecyclerView.Adapter<IBusinessReceiveHolder> implements View.OnClickListener, View.OnLongClickListener {

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

        public IBusinessReceiveAdapter(Context context) {
            this.context = context;
        }

        public void addData(Object data) {
            datas.add(data);
            notifyDataSetChanged();
        }

        public void removeData(int position) {
            datas.remove(position);
            notifyItemRemoved(position);
        }

        public void clearData() {
            datas.clear();
        }

        @Override
        public IBusinessReceiveHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final IBusinessReceiveHolder holder = new IBusinessReceiveHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_ibusiness_receive, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(IBusinessReceiveHolder holder, int position) {
            Object data = getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            ImageUtils.loadImage(mContext, ((IBusinessReceiveBean.Obj) data).getUser_icon(), holder.ivAvatar);
            holder.tvName.setText(((IBusinessReceiveBean.Obj) data).getNickname());
            holder.tvDemand.setText(((IBusinessReceiveBean.Obj) data).getDemand_title());
            if ("0".equals(IBusinessNewestFragment.this.state)) {
                holder.btnAgree.setTag(holder);
                holder.btnRefuse.setTag(holder);
                holder.btnAgree.setOnClickListener(this);
                holder.btnRefuse.setOnClickListener(this);
            } else {
                holder.btnAgree.setVisibility(View.INVISIBLE);
                holder.btnRefuse.setVisibility(View.INVISIBLE);
                holder.tvState.setVisibility(View.VISIBLE);
                switch (((IBusinessReceiveBean.Obj) data).getIs_friend()) {
                    case "0":
                        holder.tvState.setText("客户已收到贵公司的需求同意");
                        holder.tvState.setTextColor(getColorFromResuource(R.color.ibusiness_receiver1));
                        break;
                    case "1":
                        holder.tvState.setText("客户向您发送了信任申请");
                        holder.tvState.setTextColor(getColorFromResuource(R.color.ibusiness_receiver2));
                        break;
                    case "2":
                        holder.tvState.setText("客户已成为贵公司信任客户");
                        holder.tvState.setTextColor(getColorFromResuource(R.color.ibusiness_receiver2));
                        break;
                }
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
                    IBusinessReceiveHolder holder = (IBusinessReceiveHolder) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, holder);
                    break;
                case R.id.btnAgree:
                    IBusinessReceiveHolder holder1 = (IBusinessReceiveHolder) v.getTag();
                    handle(((IBusinessReceiveBean.Obj) holder1.data).getMatching_id(), "1", holder1.getAdapterPosition());
                    break;
                case R.id.btnRefuse:
                    IBusinessReceiveHolder holder2 = (IBusinessReceiveHolder) v.getTag();
                    handle(((IBusinessReceiveBean.Obj) holder2.data).getMatching_id(), "2", holder2.getAdapterPosition());
                    break;

                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    IBusinessReceiveHolder holder = (IBusinessReceiveHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    /**
     * 处理匹配结果
     * @param state 1：接受 2：拒绝 3：删除
     */
    private void handle(String matchingId, final String state, final int position) {
        PostCall.putJson(mContext, ServerUrl.demandsHandle(matchingId), new IBusinessHandleVo("receiver", "", Integer.parseInt(state)), new PostCall.PostResponse<IBusinessReceiveBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, IBusinessReceiveBean bean) {
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
                adapter.removeData(position);

            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, IBusinessReceiveBean.class);
    }

    static class IBusinessReceiveHolder extends BaseViewHolder {
        RelativeLayout parent;
        ImageView ivAvatar;
        TextView tvName;
        TextView tvDemand, tvState;
        Button btnAgree, btnRefuse;

        public IBusinessReceiveHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvDemand = itemView.findViewById(R.id.tvDemand);
            tvState = itemView.findViewById(R.id.tvState);
            btnAgree = itemView.findViewById(R.id.btnAgree);
            btnRefuse = itemView.findViewById(R.id.btnRefuse);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        getData();
    }
}
