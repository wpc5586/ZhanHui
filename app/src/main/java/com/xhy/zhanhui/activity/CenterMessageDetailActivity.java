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
import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.listener.OnRecyclerItemClickListener;
import com.aaron.aaronlibrary.listener.OnRecyclerItemLongClickListener;
import com.aaron.aaronlibrary.transformations.RoundedCornersTransformation;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.aaron.aaronlibrary.utils.TimeUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.CenterBean;
import com.xhy.zhanhui.http.domain.CenterMessageBean;
import com.xhy.zhanhui.http.domain.CenterMsgDetailBean;
import com.xhy.zhanhui.http.domain.ExhibitionNewsBean;
import com.xhy.zhanhui.widget.ExhibitionTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息详情页面
 * Created by Aaron on 15/12/2017.
 */

public class CenterMessageDetailActivity extends ZhanHuiActivity {

    private CenterMessageBean.Obj messageData;
    private ImageView ivAvatar;
    private TextView tvName, tvContent, tvTime;
    private RecyclerView recyclerView;
    private CenterAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_center_message_detail;
    }

    @Override
    protected void findView() {
        super.findView();
        recyclerView = findViewById(R.id.recycler);
        ivAvatar = findViewById(R.id.ivThum);
        tvName = findViewById(R.id.tvName);
        tvContent = findViewById(R.id.tvContent);
        tvTime = findViewById(R.id.tvTime);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("通知详情");
        if (getIntent().hasExtra("messageData"))
            messageData = (CenterMessageBean.Obj) getIntent().getSerializableExtra("messageData");
        if (messageData == null)
            return;
        initData();
        setIsRead();
        getData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ImageUtils.loadImageRoundedCorners(mContext, messageData.getSender_icon(), ivAvatar, RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
        tvName.setText(messageData.getSender_name());
        tvContent.setText(messageData.getContent());
        tvTime.setText(TimeUtils.getTimestampString(Long.valueOf(messageData.getPush_time())));
    }

    /**
     * 后台设置已读
     */
    public void setIsRead() {
        PostCall.put(mContext, ServerUrl.messagesIsRead(messageData.getMessage_id()), ServerUrl.getUserIdBody(), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {

            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, BaseBean.class);
    }

    /**
     * 获取页面数据
     */
    private void getData() {
        PostCall.get(mContext, ServerUrl.messagesDetail(messageData.getMessage_id()), new BaseMap(), new PostCall.PostResponse<CenterMsgDetailBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, CenterMsgDetailBean bean) {
                List<CenterMsgDetailBean.Obj.Item> dataBean = bean.getData().getAttached();
                if (dataBean == null)
                    return;
                setRecyclerView();
                int size = dataBean.size() / 3;
                if (size == 0 && dataBean.size() > 0)
                    size = dataBean.size();
                else if (dataBean.size() % 3 != 0)
                    size += 1;
                for (int j = 0; j < size; j++) {
                    List<CenterMsgDetailBean.Obj.Item> tempDatas = new ArrayList<>();
                    int pos = 3 * j;
                    for (int k = pos; k < pos + 3; k++) {
                        if (k < dataBean.size()) {
                            tempDatas.add(dataBean.get(k));
                        }
                    }
                    adapter.addData(tempDatas);
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, CenterMsgDetailBean.class);
    }

    /**
     * 设置RecyclerView
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
        } else
            adapter.notifyDataSetChanged();
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
                    CenterMsgDetailBean.Obj.Item tempData = (CenterMsgDetailBean.Obj.Item) ((List) data).get(i);
                    holder.gridTextViews[i].setVisibility(View.VISIBLE);
                    holder.gridTextViews[i].setText(tempData.getDocument_name());
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
                    CenterMsgDetailBean.Obj.Item item = (CenterMsgDetailBean.Obj.Item) v.getTag();
                    CenterBean.Obj.Item itemData = new CenterBean().new Obj().new Item();
                    itemData.setDocument_id(item.getDocument_id());
                    itemData.setDocument_icon(item.getDocument_icon());
                    itemData.setDocument_name(item.getDocument_name());
                    StartActivityUtils.startCenterDetail(mContext, itemData);
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
}
