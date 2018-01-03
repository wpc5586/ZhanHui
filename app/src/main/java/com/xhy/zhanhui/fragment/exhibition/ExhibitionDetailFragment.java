package com.xhy.zhanhui.fragment.exhibition;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.aaron.aaronlibrary.web.X5WebView;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiFragment;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.ExhibitionDataBean;
import com.xhy.zhanhui.http.domain.ExhibitionMainBean;
import com.xhy.zhanhui.http.domain.ExhibitionNewsBean;
import com.xhy.zhanhui.http.domain.NoticeBean;
import com.xhy.zhanhui.widget.ExhibitionTitleView;
import com.xhy.zhanhui.widget.MyConvenientBanner;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 展会-展会Fragment
 * Created by Aaron on 09/12/2017.
 */

public class ExhibitionDetailFragment extends ZhanHuiFragment implements OnItemClickListener {

    private PtrClassicFrameLayout ptrFrameLayout;
    private MyConvenientBanner convenientBanner;
    private LinearLayout llContent;
    private X5WebView webView;
    private ImageView ivThum;
    private TextView tvName, tvAttentionNum, tvTime, tvAddress;
    private RecyclerView recyclerView;
    private ExhibitionDetailAdapter adapter;
    private List<ExhibitionDataBean.Obj> dataBean;
    private ExhibitionDataBean exhibitionDataBean;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_exhibition_detail;
    }

    @Override
    protected void findViews(View view) {
        convenientBanner = view.findViewById(R.id.convenientBanner);
        ptrFrameLayout = view.findViewById(R.id.ptrFrameLayout);
        recyclerView = view.findViewById(R.id.recycler);
        llContent = view.findViewById(R.id.llContent);
        ivThum = view.findViewById(R.id.ivThum);
        tvName = view.findViewById(R.id.tvName);
        tvAttentionNum = view.findViewById(R.id.tvAttentionNum);
        tvTime = view.findViewById(R.id.tvTime);
        tvAddress = view.findViewById(R.id.tvAddress);
    }

    @Override
    public void init() {
        super.init();
        initPull();
        initBannerHeight();
        initBanner();
        getData();
        convenientBanner.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state != ViewPager.SCROLL_STATE_IDLE)
                    ptrFrameLayout.setMode(PtrFrameLayout.Mode.NONE);
                else
                    ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
            }
        });
    }

    /**
     * 数据加载后加载页面数据并初始化webview
     */
    private void setData() {
        if (webView != null)
            return;
        webView = new X5WebView(mContext);
        llContent.addView(webView);
        PublicMethod.setWebView(mContext, webView, null);
        webView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        webView.loadUrl(ServerUrl.exposH5());
        recyclerView = new RecyclerView(mContext);
        llContent.addView(recyclerView);
        recyclerView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

        dataBean = exhibitionDataBean.getData();
        setRecyclerView();
        adapter.addData("展会资料");
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
        getNewsData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (exhibitionDataBean != null && recyclerView == null) {
            setData();
        }
    }

    /**
     * 获取页面数据
     */
    private void getData() {
        PostCall.get(mContext, ServerUrl.expos(), new BaseMap(), new PostCall.PostResponse<ExhibitionMainBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionMainBean bean) {
                if (ptrFrameLayout.isRefreshing())
                    ptrFrameLayout.refreshComplete();
                setData(bean);
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, ExhibitionMainBean.class);
        PostCall.get(mContext, ServerUrl.exposDocuments(), new BaseMap(), new PostCall.PostResponse<ExhibitionDataBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionDataBean bean) {
                exhibitionDataBean = bean;
                if (getUserVisibleHint())
                    setData();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, ExhibitionDataBean.class);
    }

    /**
     * 获取新闻数据
     */
    private void getNewsData() {
        PostCall.get(mContext, ServerUrl.exposNews(), new BaseMap(), new PostCall.PostResponse<ExhibitionNewsBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionNewsBean bean) {
                if (adapter == null)
                    setRecyclerView();
                adapter.addData("展会新闻divide");
                int size = bean.getData().size();
                for (int i = 0; i < size; i++) {
                    adapter.addData(bean.getData().get(i));
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, ExhibitionNewsBean.class);
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

    private void setData(ExhibitionMainBean bean) {
        ImageUtils.loadImage(mContext, bean.getData().getImage_url(), ivThum);
        tvName.setText(bean.getData().getEvent_name());
        tvAddress.setText(bean.getData().getEvent_address());
        tvTime.setText(bean.getData().getEvent_start_time() + "-" + bean.getData().getEvent_end_time());
        tvAttentionNum.setText(bean.getData().getFocus());
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
     * 初始化banner的高度
     */
    private void initBannerHeight() {
        convenientBanner.getLayoutParams().height = (int) (AppInfo.getScreenWidthOrHeight(mContext, true) / 1.7617f);
    }

    /**
     * 获取Banner数据
     */
    private void initBanner() {
        PostCall.get(mContext, ServerUrl.exposAds(), new BaseMap(), new PostCall.PostResponse<NoticeBean>() {

            @Override
            public void onSuccess(int i, byte[] bytes, NoticeBean noticeBean) {
                setBanner(noticeBean.getData());
            }

            @Override
            public void onFailure(int i, byte[] bytes) {}
        }, new String[]{}, false, NoticeBean.class);
    }

    /**
     * 设置页面
     * @param list 图片数据
     */
    private void setBanner(List<NoticeBean.Obj> list) {
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, list)   //设置需要切换的View
                .setPointViewVisible(true)    //设置指示器是否可见
//                .setPageIndicator(new int[]{R.drawable.ic_launcher, R.drawable.ic_launcher})   //设置指示器圆点
                .startTurning(5000);     //设置自动切换（同时设置了切换时间间隔）
//                .stopTurning();    //关闭自动切换
        convenientBanner.setManualPageable(true);  //设置手动影响（设置了该项无法手动切换）
        convenientBanner.setPageIndicatorAlign(MyConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT) //设置指示器位置（左、中、右）
                .setOnItemClickListener(this); //设置点击监听事件
    }

    /**
     * Banner点击事件
     * @param position
     */
    @Override
    public void onItemClick(int position) {

    }

    /**
     * 首页BannerItemView
     * Created by wpc on 2016/11/17 0017.
     */
    public static class NetworkImageHolderView implements Holder<NoticeBean.Obj> {
        private ImageView imageView;
        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context,int position, NoticeBean.Obj data) {
//        imageView.setImageResource(R.drawable.ic_action_refresh_dark);
            String url = /*ServerUrl.getService() + */data.getImage_url();
            if (!TextUtils.isEmpty(url) && !url.contains("http"))
                url = ServerUrl.getService() + url;
//        Logger.d("Notice url--------", url);
            ImageUtils.loadImage(context, url, imageView, false);
        }
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
            } else if (data instanceof ExhibitionNewsBean.Obj) {
                holder.titleView.setVisibility(View.GONE);
                holder.contentGrid.setVisibility(View.GONE);
                holder.contentList.setVisibility(View.VISIBLE);
                holder.listTitle.setText(((ExhibitionNewsBean.Obj) data).getNews_title());
                holder.browse.setText(((ExhibitionNewsBean.Obj) data).getBrowse());
                ImageUtils.loadImage(mContext, ((ExhibitionNewsBean.Obj) data).getImage_url(), holder.listImage);
                holder.contentList.setTag(ServerUrl.exposNewsUrl(((ExhibitionNewsBean.Obj) data).getNews_id()));
                holder.contentList.setOnClickListener(this);
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
                case R.id.contentList:
                    String url = (String) v.getTag();
                    StartActivityUtils.startWebUrl(mContext, url, "展会新闻");
                    break;
                case R.id.rl1:
                case R.id.rl2:
                case R.id.rl3:
                    ExhibitionDataBean.Obj data = (ExhibitionDataBean.Obj) v.getTag();
//                    String pdfUrl = data.getFile_url();
//                    String title = data.getDocument_name();
//                    StartActivityUtils.startPdfUrl(mContext, pdfUrl, title);
                    StartActivityUtils.startCenterDetail(mContext, data.getDocument_id(), data.getImage_url(), data.getDocument_name());
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
