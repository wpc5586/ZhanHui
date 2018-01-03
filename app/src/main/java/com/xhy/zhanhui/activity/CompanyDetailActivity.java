package com.xhy.zhanhui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.domain.BaseViewHolder;
import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.listener.OnRecyclerItemClickListener;
import com.aaron.aaronlibrary.listener.OnRecyclerItemLongClickListener;
import com.aaron.aaronlibrary.manager.MyLinearLayoutManager;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.aaron.aaronlibrary.widget.verticalscrolltext.Sentence;
import com.aaron.aaronlibrary.widget.verticalscrolltext.VerticalScrollTextView;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.ExhibitionCompanyInfoBean;
import com.xhy.zhanhui.http.domain.ExhibitionDataBean;
import com.xhy.zhanhui.http.domain.ExhibitionProductBean;
import com.xhy.zhanhui.widget.ExhibitionTitleView;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * 企业详情页面
 * Created by Aaron on 10/12/2017.
 */

public class CompanyDetailActivity extends ZhanHuiActivity {

    private String companyId;
    private ScrollView scrollView;
    private RelativeLayout rlUp;
    private ImageView ivThum;
    private TextView tvName, tvId, tvAttentionNum, tvProductNum, tvIntro, tvIntroduction;
    private VerticalScrollTextView scrollTextView;
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private RecyclerView recyclerView;
    private ExhibitionDetailAdapter adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_company_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            contentIsBelow = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void findView() {
        super.findView();
        scrollView = findViewById(R.id.scrollView);
        rlUp = findViewById(R.id.rlUp);
        jzVideoPlayerStandard = findViewById(R.id.videoPlayer);
        scrollTextView = findViewById(R.id.scrollTextView);
        ivThum = findViewById(R.id.ivThum);
        tvName = findViewById(R.id.tvName);
        tvId = findViewById(R.id.tvId);
        tvAttentionNum = findViewById(R.id.tvAttentionNum);
        tvProductNum = findViewById(R.id.tvProductNum);
        tvIntro = findViewById(R.id.tvIntro);
        tvIntroduction = findViewById(R.id.tvIntroduction);
        recyclerView = findViewById(R.id.recycler);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("企业之窗");
        setActionbarBackground(R.color.transparent);
        setActionbarTitleColor(R.color.white);
        setActionbarDividerVisibility(false);
        int paddingTop = getDimen(R.dimen.actionbar_height) + AppInfo.getStatusBarHeight();
        companyId = getIntent().getStringExtra("company_id");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rlUp.setPadding(0, paddingTop, 0, 0);
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == 0) {
                        setActionbarBackground(R.color.transparent);
                        setStatusBackground(R.color.transparent);
                    } else {
                        setActionbarBackground(R.color.theme);
                        setStatusBackground(R.color.theme);
                    }
                }
            });
        } else {
            rlUp.setPadding(0, 0, 0, 0);
            setActionbarBackground(R.color.white);
            setActionbarTitleColor(R.color.black);
        }
        getData();
        setScrollTextView();
    }

    private void setScrollTextView() {
        List<Sentence> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Sentence(i, "企业通知" + i));
        }
        scrollTextView.setList(list);
        scrollTextView.updateUI();
    }

    private void getData() {
        PostCall.get(mContext, ServerUrl.company(companyId), new BaseMap(), new PostCall.PostResponse<ExhibitionCompanyInfoBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionCompanyInfoBean bean) {
                ExhibitionCompanyInfoBean.Obj data = bean.getData();
                ImageUtils.loadImage(mContext, data.getImage_url(), ivThum);
                tvName.setText(data.getCompany_name());
                tvId.setText(data.getBooth_no());
                tvAttentionNum.setText(data.getFocus());
                tvProductNum.setText(data.getPdt_nums());
                tvIntro.setText(data.getCompany_introduction());
                // 隐藏前两个字完美解决缩进问题
                SpannableStringBuilder span = new SpannableStringBuilder("缩进" + data.getCompany_introduction());
                span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                        Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                tvIntroduction.setText(span);
                jzVideoPlayerStandard.setUp(data.getVideo_url(), JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");
                jzVideoPlayerStandard.setVisibility(View.VISIBLE);
                PublicMethod.setVideoPlayer(jzVideoPlayerStandard);
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, ExhibitionCompanyInfoBean.class);
        PostCall.get(mContext, ServerUrl.companyDocument(companyId), new BaseMap(), new PostCall.PostResponse<ExhibitionDataBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionDataBean bean) {
                List<ExhibitionDataBean.Obj> dataBean = bean.getData();
                setRecyclerView();
                adapter.addData("资料");
                int size = dataBean.size() / 3;
                if (size == 0 && dataBean.size() > 0)
                    size = dataBean.size();
                else if(dataBean.size() % 3 != 0)
                    size += 1;
                for (int i = 0; i < size; i++) {
                    List<ExhibitionDataBean.Obj> tempDatas = new ArrayList<>();
                    int pos = 3 * i;
                    for (int j = pos; j < pos + 3; j++) {
                        if (j < dataBean.size()) {
                            tempDatas.add(dataBean.get(j));
                        }
                    }
                    adapter.addData(tempDatas);
                }
                getProductData();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, ExhibitionDataBean.class);
    }

    /**
     * 获取产品数据
     */
    private void getProductData() {
        PostCall.get(mContext, ServerUrl.companyProduct(companyId), new BaseMap(), new PostCall.PostResponse<ExhibitionProductBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionProductBean bean) {
                if (adapter == null)
                    setRecyclerView();
                List<ExhibitionProductBean.Obj> dataBean = bean.getData();
                adapter.addData("产品");
                int size = dataBean.size() / 3;
                if (size == 0 && dataBean.size() > 0)
                    size = dataBean.size();
                else if(dataBean.size() % 3 != 0)
                    size += 1;
                for (int i = 0; i < size; i++) {
                    List<ExhibitionProductBean.Obj> tempDatas = new ArrayList<>();
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
        }, new String[]{}, false, ExhibitionProductBean.class);
    }

    /**
     * 设置资料、动态
     */
    private void setRecyclerView() {
        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
        linearLayoutManager.setScrollEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
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
                if (size == 0)
                    return;
                if (((List) data).get(0) instanceof ExhibitionDataBean.Obj) {
                    for (int i = 0; i < size; i++) {
                        ExhibitionDataBean.Obj tempData = (ExhibitionDataBean.Obj) ((List) data).get(i);
                        holder.gridTextViews[i].setVisibility(View.VISIBLE);
                        holder.gridTextViews[i].setText(tempData.getDocument_name());
                        ImageView imageView = holder.gridImageViews[i];
                        int imageW = (AppInfo.getScreenWidthOrHeight(mContext, true) - MathUtils.dip2px(mContext, 18)) / 3;
                        imageView.getLayoutParams().height = (int) (imageW * 1.345f);
                        ImageUtils.loadImage(mContext, tempData.getImage_url(), imageView);
                        View parent = (View) imageView.getParent();
                        parent.setTag(tempData);
                        parent.setOnClickListener(this);
                    }
                } else if (((List) data).get(0) instanceof ExhibitionProductBean.Obj) {
                    for (int i = 0; i < size; i++) {
                        ExhibitionProductBean.Obj tempData = (ExhibitionProductBean.Obj) ((List) data).get(i);
                        holder.gridTextViews[i].setVisibility(View.VISIBLE);
                        holder.gridTextViews[i].setText(tempData.getProduct_name() + "：" + tempData.getProduct_introduction());
                        ImageView imageView = holder.gridImageViews[i];
                        imageView.getLayoutParams().height = (AppInfo.getScreenWidthOrHeight(mContext, true) - MathUtils.dip2px(mContext, 18)) / 3;
                        ImageUtils.loadImage(mContext, tempData.getImage_url(), imageView);
                        View parent = (View) imageView.getParent();
                        parent.setTag(tempData);
                        parent.setOnClickListener(this);
                    }
                }
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
                    Object data = v.getTag();
                    if (data instanceof ExhibitionDataBean.Obj) {
                        StartActivityUtils.startCenterDetail(mContext, ((ExhibitionDataBean.Obj) data).getDocument_id(),
                                ((ExhibitionDataBean.Obj) data).getImage_url(), ((ExhibitionDataBean.Obj) data).getDocument_name());
                    } else if (data instanceof ExhibitionProductBean.Obj) {
                        String productId = ((ExhibitionProductBean.Obj) data).getProduct_id();
                        StartActivityUtils.startProductDetail(mContext, productId);
                    }
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

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }
}
