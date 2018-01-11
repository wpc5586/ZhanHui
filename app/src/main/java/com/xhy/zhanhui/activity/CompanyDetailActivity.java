package com.xhy.zhanhui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.domain.BaseViewHolder;
import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.listener.OnRecyclerItemClickListener;
import com.aaron.aaronlibrary.listener.OnRecyclerItemLongClickListener;
import com.aaron.aaronlibrary.manager.MyLinearLayoutManager;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.Constants;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.aaron.aaronlibrary.web.X5WebView;
import com.aaron.aaronlibrary.widget.verticalscrolltext.Sentence;
import com.aaron.aaronlibrary.widget.verticalscrolltext.VerticalScrollTextView;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.ExhibitionCompanyInfoBean;
import com.xhy.zhanhui.http.domain.ExhibitionDataBean;
import com.xhy.zhanhui.http.domain.ExhibitionProductBean;
import com.xhy.zhanhui.http.domain.TrustCompanyBean;
import com.xhy.zhanhui.widget.ExhibitionTitleView;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * 企业详情页面
 * Created by Aaron on 10/12/2017.
 */

public class CompanyDetailActivity extends ZhanHuiActivity {

    private String companyId;
    private ExhibitionCompanyInfoBean.Obj data;
    private ScrollView scrollView;
    private RelativeLayout rlUp, rlBottom;
    private ImageView ivThum;
    private TextView tvName, tvId, tvAttentionNum, tvProductNum, tvIntro, tvAttention;
    private VerticalScrollTextView scrollTextView;
    private JZVideoPlayerStandard jzVideoPlayerStandard;
    private RecyclerView recyclerView;
    private ExhibitionDetailAdapter adapter;
    private List<ExhibitionDataBean.Obj> dataBean; // 资料数据
    private LinearLayout llContent;
    private X5WebView webView;
    private Button btnOrder;
    private View btnDivider;

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
        rlBottom = findViewById(R.id.rlBottom);
        jzVideoPlayerStandard = findViewById(R.id.videoPlayer);
        scrollTextView = findViewById(R.id.scrollTextView);
        ivThum = findViewById(R.id.ivThum);
        tvName = findViewById(R.id.tvName);
        tvId = findViewById(R.id.tvId);
        tvAttentionNum = findViewById(R.id.tvAttentionNum);
        tvProductNum = findViewById(R.id.tvProductNum);
        tvIntro = findViewById(R.id.tvIntro);
        llContent = findViewById(R.id.llContent);
        tvAttention = findViewById(R.id.tv2);
        findAndSetClickListener(R.id.rl2);
        btnDivider = findViewById(R.id.centerView);
        btnOrder = findAndSetClickListener(R.id.btnOrder);
        findAndSetClickListener(R.id.btnTrust);
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
                    int height = MathUtils.dip2px(mContext, 50);
                    float ratio = scrollY / (float) height;
                    if (scrollY == 0) {
                        setActionbarBackground(R.color.transparent);
                        setStatusBackground(R.color.transparent);
                        setActionbarTitleColor(R.color.white);
                    } else {
                        setActionbarBackground(R.color.white);
                        setStatusBackground(R.color.theme);
                        setActionbarTitleColor(R.color.theme_black);
                        if (ratio <= 1 && ratio >= 0) {
                            getActionbarView().getBackground().setAlpha((int) (ratio * 255));
                        }
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
        if (!Constants.VERSION_IS_USER) {
            btnOrder.setVisibility(View.GONE);
            btnDivider.setVisibility(View.GONE);
        }
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
        BaseMap params = new BaseMap();
        params.put("user_id", getUserId());
        PostCall.get(mContext, ServerUrl.company(companyId), params, new PostCall.PostResponse<ExhibitionCompanyInfoBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionCompanyInfoBean bean) {
                data = bean.getData();
                ImageUtils.loadImage(mContext, data.getImage_url(), ivThum);
                tvName.setText(data.getCompany_name());
                tvId.setText(data.getBooth_no());
                tvAttentionNum.setText(data.getFocus());
                tvProductNum.setText(data.getPdt_nums());
                tvIntro.setText(data.getCompany_introduction());
                if (bean.getData().getCompany_users() != null && bean.getData().getCompany_users().size() > 0 && bean.getData().getCompany_users().get(0).getHx_username().equals(getHxUserId())) {
                    rlBottom.setVisibility(View.GONE);
                }
                if (Constants.VERSION_IS_USER) {
                    if ("1".equals(data.getIs_online_focus()))
                        tvAttention.setText("已关注");
                    else
                        tvAttention.setText("关注");
                }
//                tvIntroduction.setText(getIndent(data.getCompany_introduction()));
                if (!TextUtils.isEmpty(data.getVideo_url())) {
                    JZUtils.clearSavedProgress(mContext, "");
                    jzVideoPlayerStandard.setUp(data.getVideo_url(), JZVideoPlayerStandard.SCREEN_WINDOW_LIST, "");
                    jzVideoPlayerStandard.setVisibility(View.VISIBLE);
                    PublicMethod.setVideoPlayer(jzVideoPlayerStandard);
                    createVideoThumbnail(data.getVideo_url(), jzVideoPlayerStandard.getLayoutParams().width, jzVideoPlayerStandard.getLayoutParams().height, jzVideoPlayerStandard.thumbImageView);
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, ExhibitionCompanyInfoBean.class);
        PostCall.get(mContext, ServerUrl.companyDocument(companyId), new BaseMap(), new PostCall.PostResponse<ExhibitionDataBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, ExhibitionDataBean bean) {
                dataBean = bean.getData();
                // 设置WebView
                setData();
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, ExhibitionDataBean.class);
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
        webView.loadUrl(ServerUrl.companyIntroduction(companyId));
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        recyclerView = new RecyclerView(mContext);
        relativeLayout.addView(recyclerView);
        llContent.addView(relativeLayout);
        relativeLayout.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        relativeLayout.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        recyclerView.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

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
                        StartActivityUtils.startCenterDetail(mContext, ((ExhibitionDataBean.Obj) data).getDocument_id(), ((ExhibitionDataBean.Obj) data).getImage_url(), ((ExhibitionDataBean.Obj) data).getDocument_name());
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

    /**
     * 信任并交换名片
     */
    private void trust() {
        if (data != null && data.getCompany_users() != null && data.getCompany_users().size() > 0) {
            if (!isVcardIdZero()) {
                TrustCompanyBean bean = new TrustCompanyBean();
                TrustCompanyBean.Obj obj = bean.new Obj();
                obj.setCompany_name(data.getCompany_name());
                obj.setImage_url(data.getImage_url());
                List<TrustCompanyBean.Obj.User> users = new ArrayList<>();
                TrustCompanyBean.Obj.User user = obj.new User();
                ExhibitionCompanyInfoBean.Obj.User userInfo = data.getCompany_users().get(0);
                user.setHx_username(userInfo.getHx_username());
                user.setIcon(userInfo.getIcon());
                user.setNickname(userInfo.getNickname());
                user.setUser_id(userInfo.getUser_id());
                user.setV_title(userInfo.getV_title());
                users.add(user);
                obj.setCompany_users(users);
                bean.setData(obj);
                StartActivityUtils.startTrustCompany(mContext, bean);
            }
        } else
            showToast("该企业没有用户信息，无法申请信任");
    }

    /**
     * 添加关注
     */
    private void attention() {
        PostCall.post(mContext, ServerUrl.attentionCompany(companyId), new BaseMap(), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                tvAttention.setText("已关注");
                showToast("已关注");
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, BaseBean.class);
    }

    /**
     * 取消关注
     */
    private void cancelAttention() {
        PostCall.delete(mContext, ServerUrl.cancelCompany(companyId), "", new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, BaseBean bean) {
                tvAttention.setText("关注");
                showToast("已取消关注");
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, false, BaseBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnOrder:
                if (!isVcardIdZero() && data != null)
                    StartActivityUtils.startSponsorOrder(mContext, data.getEvent_id(), data.getEvent_name(), data.getBooth_no(), data.getCompany_id(), data.getCompany_name());
                break;
            case R.id.btnTrust:
                trust();
                break;
            case R.id.rl2: // 添加关注
                if (data == null || !Constants.VERSION_IS_USER)
                    return;
                if ("关注".equals(tvAttention.getText().toString()))
                    attention();
                else
                    cancelAttention();
                break;
        }
    }
}
