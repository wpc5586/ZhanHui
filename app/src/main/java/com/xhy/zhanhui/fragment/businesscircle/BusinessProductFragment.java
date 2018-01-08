package com.xhy.zhanhui.fragment.businesscircle;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
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
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.ProductBean;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 商圈-关注产品Fragment
 * Created by Aaron on 14/12/2017.
 */

public class BusinessProductFragment extends ZhanHuiFragment {

    private PtrClassicFrameLayout ptrFrameLayout;
    private RecyclerView recyclerView;
    private BusinessProductAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_business_product;
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
        PostCall.get(mContext, ServerUrl.favoritesProduct(), new BaseMap(), new PostCall.PostResponse<ProductBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ProductBean bean) {
                if (ptrFrameLayout.isRefreshing())
                    ptrFrameLayout.refreshComplete();
                List<ProductBean.Obj> dataBean = bean.getData();
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
        }, new String[]{}, false, ProductBean.class);
    }


    /**
     * 设置RecyclerView
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        GridLayoutManager manager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(manager);
        if (adapter == null) {
            adapter = new BusinessProductAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                    String productId = ((ProductBean.Obj) holder.data).getProduct_id();
                    StartActivityUtils.startProductDetail(mContext, productId);
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

    public class BusinessProductAdapter extends RecyclerView.Adapter<BusinessProductHolder> implements View.OnClickListener, View.OnLongClickListener {

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

        public BusinessProductAdapter(Context context) {
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
        public BusinessProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final BusinessProductHolder holder = new BusinessProductHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_product, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(BusinessProductHolder holder, int position) {
            Object data = getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            ImageUtils.loadImage(mContext, ((ProductBean.Obj) data).getImage_url(), holder.ivAvatar);
            holder.tvName.setText(((ProductBean.Obj) data).getProduct_name());
            holder.tvCompany.setText(((ProductBean.Obj) data).getCompany_name());
            int paddingT = MathUtils.dip2px(mContext, 10);
            int paddingL = MathUtils.dip2px(mContext, 5);
            holder.parent.setPadding(paddingL, paddingT, paddingL, 0);
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
                    BusinessProductHolder holder = (BusinessProductHolder) v.getTag();
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
                    BusinessProductHolder holder = (BusinessProductHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class BusinessProductHolder extends BaseViewHolder {
        RelativeLayout parent;
        ImageView ivAvatar;
        TextView tvName;
        TextView tvCompany;

        public BusinessProductHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            ivAvatar = itemView.findViewById(R.id.image);
            tvName = itemView.findViewById(R.id.title);
            tvCompany = itemView.findViewById(R.id.company);
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
