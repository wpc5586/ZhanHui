package com.xhy.zhanhui.activity;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.domain.BaseViewHolder;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.listener.OnRecyclerItemClickListener;
import com.aaron.aaronlibrary.listener.OnRecyclerItemLongClickListener;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.ExhibitionNewsBean;
import com.xhy.zhanhui.http.domain.ExhibitionProductListBean;
import com.xhy.zhanhui.widget.ExhibitionTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 产品列表页面
 * Created by Aaron on 2017/12/14.
 */

public class ProductListActivity extends ZhanHuiActivity {

    /**
     * 行业Id
     */
    private String industry2Id;
    /**
     * 行业名称
     */
    private String industry2Name;
    private RecyclerView recyclerView;
    private ExhibitionDetailAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_company_list;
    }

    @Override
    protected void findView() {
        super.findView();
        recyclerView = findViewById(R.id.recycler);
    }

    @Override
    protected void init() {
        super.init();
        if (getIntent().hasExtra("industry2_id"))
            industry2Id = getIntent().getStringExtra("industry2_id");
        if (getIntent().hasExtra("industry2_name"))
            industry2Name = getIntent().getStringExtra("industry2_name");
        setActionbarTitle(industry2Name);
        getData();
    }

    private void getData() {
        PostCall.get(mContext, ServerUrl.industry(industry2Id), new BaseMap(), new PostCall.PostResponse<ExhibitionProductListBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionProductListBean bean) {
                List<ExhibitionProductListBean.Obj> dataBean = bean.getData();
                setRecyclerView();
                int size = dataBean.size() / 3;
                if (size == 0 && dataBean.size() > 0)
                    size = dataBean.size();
                else if(dataBean.size() % 3 != 0)
                    size += 1;
                for (int i = 0; i < size; i++) {
                    List<ExhibitionProductListBean.Obj> tempDatas = new ArrayList<>();
                    int pos = 3 * i;
                    for (int j = pos; j < pos + 3; j++) {
                        if (j < dataBean.size()) {
                            tempDatas.add(dataBean.get(j));
                        }
                    }
                    adapter.addData(tempDatas);
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, ExhibitionProductListBean.class);
    }

    /**
     * 设置资料、动态
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (adapter == null) {
            adapter = new ExhibitionDetailAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
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

    public class ExhibitionDetailAdapter extends RecyclerView.Adapter<ExhibitionDetailHolder> implements View.OnClickListener, View.OnLongClickListener {

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

        public ExhibitionDetailAdapter(Context context){
            this.context = context;
        }

        public void addData(Object data) {
            datas.add(data);
            notifyDataSetChanged();
        }

        @Override
        public ExhibitionDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final ExhibitionDetailHolder holder = new ExhibitionDetailHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_exhibition_grid, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(ExhibitionDetailHolder holder, int position) {
            Object data = getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            if (data instanceof String) {
                holder.titleView.setVisibility(View.VISIBLE);
                holder.contentGrid.setVisibility(View.GONE);
                holder.contentList.setVisibility(View.GONE);
                if (((String) data).contains("divide")) {
                    data = ((String) data).replace("divide", "");
                    holder.titleView.setDivideVisibility(View.VISIBLE);
                } else
                    holder.titleView.setDivideVisibility(View.GONE);
                holder.titleView.setText((String) data);
            } else if (data instanceof List) {
                holder.titleView.setVisibility(View.GONE);
                holder.contentGrid.setVisibility(View.VISIBLE);
                holder.contentList.setVisibility(View.GONE);
                holder.resetList();
                int size = ((List) data).size();
                for (int i = 0; i < size; i++) {
                    ExhibitionProductListBean.Obj tempData = (ExhibitionProductListBean.Obj) ((List) data).get(i);
                    holder.gridTextViews[i].setVisibility(View.VISIBLE);
                    holder.gridTextViews[i].setText(tempData.getProduct_name());
                    ImageView imageView = holder.gridImageViews[i];
                    imageView.getLayoutParams().height = (AppInfo.getScreenWidthOrHeight(mContext, true) - MathUtils.dip2px(mContext, 18)) / 3;
                    ImageUtils.loadImage(mContext, tempData.getImage_url(), imageView);
                    View parent = (View) imageView.getParent();
                    parent.setTag(tempData.getProduct_id());
                    parent.setOnClickListener(this);
                }
            } else if (data instanceof ExhibitionNewsBean.Obj) {
                holder.titleView.setVisibility(View.GONE);
                holder.contentGrid.setVisibility(View.GONE);
                holder.contentList.setVisibility(View.VISIBLE);
                holder.listTitle.setText(((ExhibitionNewsBean.Obj) data).getNews_title());
                holder.browse.setText(((ExhibitionNewsBean.Obj) data).getBrowse());
                ImageUtils.loadImage(mContext, ((ExhibitionNewsBean.Obj) data).getImage_url(), holder.listImage);
            }
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
                    ExhibitionDetailHolder holder = (ExhibitionDetailHolder) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, holder);
                    break;
                case R.id.rl1:
                case R.id.rl2:
                case R.id.rl3:
                    String productId = (String) v.getTag();
                    StartActivityUtils.startProductDetail(mContext, productId);
                    break;

                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    ExhibitionDetailHolder holder = (ExhibitionDetailHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class ExhibitionDetailHolder extends BaseViewHolder {
        RelativeLayout parent;
        ExhibitionTitleView titleView;
        LinearLayout contentGrid;
        RelativeLayout contentList;
        ImageView imageView1, imageView2, imageView3, listImage;
        TextView textView1, textView2, textView3, listTitle, browse;
        ImageView[] gridImageViews;
        TextView[] gridTextViews;

        public ExhibitionDetailHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            titleView = itemView.findViewById(R.id.titleView);
            imageView1 = itemView.findViewById(R.id.image1);
            imageView2 = itemView.findViewById(R.id.image2);
            imageView3 = itemView.findViewById(R.id.image3);
            gridImageViews = new ImageView[]{imageView1, imageView2, imageView3};
            textView1 = itemView.findViewById(R.id.title1);
            textView2 = itemView.findViewById(R.id.title2);
            textView3 = itemView.findViewById(R.id.title3);
            gridTextViews = new TextView[]{textView1, textView2, textView3};
            contentGrid = itemView.findViewById(R.id.contentGrid);
            contentList = itemView.findViewById(R.id.contentList);
            listImage = itemView.findViewById(R.id.listImage);
            listTitle = itemView.findViewById(R.id.listTitle);
            browse = itemView.findViewById(R.id.browse);
        }

        public void resetList() {
            for (int i = 0; i < gridImageViews.length; i++) {
                gridImageViews[i].setVisibility(View.GONE);
                gridTextViews[i].setVisibility(View.GONE);
            }
        }
    }
}
