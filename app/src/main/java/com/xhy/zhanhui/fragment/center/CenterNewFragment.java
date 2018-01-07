package com.xhy.zhanhui.fragment.center;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.fragment.CenterFragment;
import com.xhy.zhanhui.http.domain.CenterBean;
import com.xhy.zhanhui.http.domain.ExhibitionNewsBean;
import com.xhy.zhanhui.widget.ExhibitionTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 资料中心-新收到的Fragment
 * Created by Aaron on 14/12/2017.
 */

public class CenterNewFragment extends ZhanHuiFragment {

    private String module = CenterFragment.MODULES[1];
    private String filter = CenterFragment.FILTERS[0];
    private String group = CenterFragment.GROUPS[0];
    private String order = CenterFragment.ORDERS[0];

    private boolean isInit1 = false, isInit2 = false; // 判断是否已初始化Spanner，初始化之前Spanner事件无效
    private AppCompatSpinner spinner1, spinner2;
    private RecyclerView recyclerView;
    private CenterAdapter adapter;
    private TextView tvSpanner1, tvSpanner2;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_center_collection;
    }

    @Override
    protected void findViews(View view) {
        recyclerView = view.findViewById(R.id.recycler);
        spinner1 = view.findViewById(R.id.spinner1);
        spinner2 = view.findViewById(R.id.spinner2);
        findViewAndSetListener(R.id.rlSpanner1);
        findViewAndSetListener(R.id.rlSpanner2);
        tvSpanner1 = view.findViewById(R.id.tvSpanner1);
        tvSpanner2 = view.findViewById(R.id.tvSpanner2);
    }

    @Override
    protected void init() {
        super.init();
        initSpanner();
        getData(false);
    }

    private void initSpanner() {
        final String[] titles1 = getResources().getStringArray(R.array.spanner1);
        final String[] titles2 = getResources().getStringArray(R.array.spanner2);
        spinner1.setOnItemSelectedListener(new AppCompatSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInit1) {
                    tvSpanner1.setText(titles1[position]);
                    filter = CenterFragment.FILTERS[position];
                    getData(true);
                } else
                    isInit1 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isInit2) {
                    tvSpanner2.setText(titles2[position]);
                    group = CenterFragment.GROUPS[position];
                    getData(true);
                } else
                    isInit2 = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 获取页面数据
     */
    private void getData(final boolean isShowDialog) {
        PostCall.get(mContext, ServerUrl.center(getUserId(), module, filter, group, order), new BaseMap(), new PostCall.PostResponse<CenterBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, CenterBean bean) {
                if (!isShowDialog)
                    initSpanner();
                List<CenterBean.Obj> dataBean = bean.getData();
                if (dataBean == null)
                    return;
                setRecyclerView();
                for (int i = 0; i < dataBean.size(); i++) {
                    CenterBean.Obj obj = dataBean.get(i);
                    adapter.addData(obj.getGroup_name());
                    List<CenterBean.Obj.Item> items = obj.getGroup_items();
                    int size = items.size() / 3;
                    if (size == 0 && items.size() > 0)
                        size = items.size();
                    else if (items.size() % 3 != 0)
                        size += 1;
                    for (int j = 0; j < size; j++) {
                        List<CenterBean.Obj.Item> tempDatas = new ArrayList<>();
                        int pos = 3 * j;
                        for (int k = pos; k < pos + 3; k++) {
                            if (k < items.size()) {
                                tempDatas.add(items.get(k));
                            }
                        }
                        adapter.addData(tempDatas);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, isShowDialog, CenterBean.class);
    }

    /**
     * 设置资料、动态
     */
    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (adapter == null) {
            adapter = new CenterAdapter(mContext);
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
        } else {
            // 如果是重新加载数据，清空adapter
            adapter.clearData();
        }
    }

    public class CenterAdapter extends RecyclerView.Adapter<CenterHolder> implements View.OnClickListener, View.OnLongClickListener {

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

        public CenterAdapter(Context context) {
            this.context = context;
        }

        public void addData(Object data) {
            datas.add(data);
            notifyDataSetChanged();
        }

        public void clearData() {
            datas.clear();
            notifyDataSetChanged();
        }

        @Override
        public CenterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final CenterHolder holder = new CenterHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_exhibition_grid, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(CenterHolder holder, int position) {
            Object data = getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            if (data instanceof String) {
                holder.titleView.setVisibility(View.VISIBLE);
                holder.contentGrid.setVisibility(View.GONE);
                holder.contentList.setVisibility(View.GONE);
                holder.titleView.setTag(data);
                holder.titleView.setArrowVisibility(View.GONE);
                if (((String) data).contains("divide")) {
                    data = ((String) data).replace("divide", "");
                    holder.titleView.setDivideVisibility(View.VISIBLE);
                } else
                    holder.titleView.setDivideVisibility(View.GONE);
                if (((String) data).contains("hallId")) {
                    String[] temp = ((String) data).split("hallId");
                    data = temp[0];
                    holder.titleView.setDivideVisibility(View.VISIBLE);
                    holder.titleView.setOnClickListener(this);
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
                    CenterBean.Obj.Item tempData = (CenterBean.Obj.Item) ((List) data).get(i);
                    holder.gridTextViews[i].setVisibility(View.VISIBLE);
                    holder.gridTextViews[i].setText(tempData.getCompany_name());
                    ImageView imageView = holder.gridImageViews[i];
                    int imageW = (AppInfo.getScreenWidthOrHeight(mContext, true) - MathUtils.dip2px(mContext, 18)) / 3;
                    imageView.getLayoutParams().height = (int) (imageW * 1.345f);
                    ImageUtils.loadImage(mContext, tempData.getDocument_icon(), imageView);
                    View parent = (View) imageView.getParent();
                    parent.setTag(tempData);
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

        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    CenterHolder holder = (CenterHolder) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, holder);
                    break;
                case R.id.rl1:
                case R.id.rl2:
                case R.id.rl3:
                    CenterBean.Obj.Item item = (CenterBean.Obj.Item) v.getTag();
                    StartActivityUtils.startCenterDetail(mContext, item);
                    break;

                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    CenterHolder holder = (CenterHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class CenterHolder extends BaseViewHolder {
        RelativeLayout parent;
        ExhibitionTitleView titleView;
        LinearLayout contentGrid;
        RelativeLayout contentList;
        ImageView imageView1, imageView2, imageView3, listImage;
        TextView textView1, textView2, textView3, listTitle, browse;
        ImageView[] gridImageViews;
        TextView[] gridTextViews;

        public CenterHolder(View itemView) {
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rlSpanner1:
                spinner1.performClick();
                break;
            case R.id.rlSpanner2:
                spinner2.performClick();
                break;
        }
    }
}
