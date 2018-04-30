package com.xhy.zhanhui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.dialog.TrustDialog;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.DemandDetailBean;
import com.xhy.zhanhui.http.domain.DemandResultBean;
import com.xhy.zhanhui.http.domain.IBusinessCompanyBean;
import com.xhy.zhanhui.http.domain.TrustCompanyBean;
import com.xhy.zhanhui.http.vo.DeleteIBusinessResultVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 智能商务-需求详情页面
 * Created by Administrator on 2018/1/25.
 */

public class IntelligentBusinessDetailActivity extends ZhanHuiActivity{

    private DemandDetailBean.Obj data;
    private List<DemandResultBean.Obj> resultData;
    private String demandId;
    private TextView tvName, tvBrand, tvClass, tvNum, tvDemand, tvTime, tvStateTitle, tvStateContent;
    private ImageView image1, image2, image3, ivState;
    private ImageView[] imageViews;
    private RelativeLayout rlBottom;
    private RecyclerView recyclerView;
    private IBusinessDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        contentIsBelow = false;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_intelligent_business_detail;
    }

    @Override
    protected void findView() {
        tvName = findViewById(R.id.tvName);
        tvBrand = findViewById(R.id.tvBrand);
        tvClass = findViewById(R.id.tvClass);
        tvNum = findViewById(R.id.tvNum);
        tvDemand = findViewById(R.id.tvDemand);
        tvTime = findViewById(R.id.tvTime);
        tvStateTitle = findViewById(R.id.tvStateTitle);
        tvStateContent = findViewById(R.id.tvStateContent);
        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);
        ivState = findViewById(R.id.ivState);
        imageViews = new ImageView[]{image1, image2, image3};
        recyclerView = findViewById(R.id.recycler);
        rlBottom = findViewById(R.id.rlBottom);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarTitle("需求匹配");
        setActionbarBackground(R.color.transparent);
        setActionbarDividerVisibility(false);
        setStatusBarVisibility(false);
        getActionbarView().getBackButton().setImageResource(R.mipmap.common_back_white1);
        setActionbarTitleColor(R.color.white);
        demandId = getStringExtra("demandId");
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        PostCall.get(mContext, ServerUrl.demandsDetail(demandId), new BaseMap(), new PostCall.PostResponse<DemandDetailBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, DemandDetailBean bean) {
                data = bean.getData();
                if (data != null) {
                    tvName.setText(data.getDemand_title());
                    tvBrand.setText(data.getDemand_brand());
                    tvClass.setText(data.getCategory_name());
                    tvNum.setText(data.getDemand_nums());
                    tvDemand.setText(data.getDemand_notes());
                    tvTime.setText(data.getPost_time());
                    if (data.getImage_url() != null && data.getImage_url().size() > 0) {
                        for (int i = 0; i < data.getImage_url().size(); i++) {
                            ImageUtils.loadImageRoundedCorners(mContext, data.getImage_url().get(i).getImage_url(), imageViews[i], RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 5));
                        }
                    }
                    if ("0".equals(data.getMatching_nums())) {
                        tvStateTitle.setText("星云智能商务大脑");
                        tvStateContent.setText("正在为您精准匹配中");
                        ivState.setImageResource(R.mipmap.icon_demand_detail_state1);
                    } else if ("0".equals(data.getMatching_accept_nums())) {
                        tvStateTitle.setText("已为您智能匹配" + data.getMatching_nums() + "家满足需求企业");
                        tvStateContent.setText("请耐心等待企业接受");
                        ivState.setImageResource(R.mipmap.icon_demand_detail_state2);
                    } else {
                        tvStateTitle.setText("");
                        tvStateContent.setText("");
                        getResultData();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, DemandDetailBean.class);
    }

    /**
     * 获取匹配结果数据
     */
    private void getResultData() {
        PostCall.get(mContext, ServerUrl.demandsCompanyList(demandId), new BaseMap(), new PostCall.PostResponse<DemandResultBean>() {
            @Override
            public void onSuccess(int statusCode, byte[] responseBody, DemandResultBean bean) {
                resultData = bean.getData();
                if (resultData != null && resultData.size() > 0) {
                    rlBottom.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    setRecyclerView();
                    for (int i = 0; i < resultData.size(); i++) {
                        adapter.addData(resultData.get(i));
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, byte[] responseBody) {

            }
        }, new String[]{}, true, DemandResultBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

        }
    }

    /**
     * 设置
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        if (adapter == null) {
            adapter = new IBusinessDetailAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                }
            });
            adapter.setOnItemLongClickListener(new OnRecyclerItemLongClickListener() {
                @Override
                public void onItemLongClick(View view, final BaseViewHolder holder) {
                }
            });
        } else
            adapter.notifyDataSetChanged();
    }

    public class IBusinessDetailAdapter extends RecyclerView.Adapter<IBusinessDetailHolder> implements View.OnClickListener, View.OnLongClickListener {

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

        public IBusinessDetailAdapter(Context context) {
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
        public IBusinessDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final IBusinessDetailHolder holder = new IBusinessDetailHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_demand_result, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(IBusinessDetailHolder holder, int position) {
            DemandResultBean.Obj data = (DemandResultBean.Obj) getItem(position);
            holder.data = data;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            boolean isTrust = false;
            switch (data.getUser_handle_state()) {
                case "1":
                    holder.tvTitle.setText("最新");
                    break;
                case "2":
                    holder.tvTitle.setText("已浏览");
                    break;
                case "3":
                    holder.tvTitle.setText("信任");
                    isTrust = true;
                    break;
            }
            holder.tvNum.setText(String.valueOf(data.getMatchings().size()));
            for (int i = 0; i < data.getMatchings().size(); i++) {
                holder.llContent.addView(getContentView(data.getMatchings().get(i), isTrust));
            }
        }

        private RelativeLayout getContentView(DemandResultBean.Obj.Matching data, boolean isTrust) {
            RelativeLayout relativeLayout = (RelativeLayout) View.inflate(mContext, R.layout.item_demand_result_company, null);
            TextView tvName = relativeLayout.findViewById(R.id.tvName);
            TextView tvDegree = relativeLayout.findViewById(R.id.tvDegree);
            TextView tvState = relativeLayout.findViewById(R.id.tvState);
            Button button = relativeLayout.findViewById(R.id.button);
            if (isTrust) {
                tvState.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            } else {
                tvState.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                button.setTag(data);
                button.setOnClickListener(this);
            }
            ImageView imageView = relativeLayout.findViewById(R.id.ivImage);
            ImageUtils.loadImageRoundedCorners(mContext, data.getCompany_icon(), imageView, RoundedCornersTransformation.CornerType.ALL, MathUtils.dip2px(mContext, 4));
            tvName.setText(data.getCompany_name());
            tvDegree.setText(data.getMatched_degree());
            relativeLayout.setTag(data.getMatching_id());
            relativeLayout.setOnClickListener(this);
            relativeLayout.setOnLongClickListener(this);
            return relativeLayout;
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
                    IBusinessDetailHolder holder = (IBusinessDetailHolder) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, holder);
                    break;
                case R.id.content:
                    String matchingId = (String) v.getTag();
                    StartActivityUtils.startIBusinessCompanyDetail(mContext, matchingId, IBusinessCompanyDetailActivity.TYPE_DEFAULT);
                    break;
                case R.id.button:
                    trust((DemandResultBean.Obj.Matching) v.getTag());
                    break;
                default:
                    break;
            }
        }

        private void trust(DemandResultBean.Obj.Matching data) {
            if (!isVcardIdZero()) {
                TrustDialog dialog = new TrustDialog((Activity) mContext, R.style.listDialog);
                for (int i = 0; i < data.getCompany_users().size(); i++) {
                    DemandResultBean.Obj.Matching.User user = data.getCompany_users().get(i);
                    // TODO
                    dialog.addData(user.getUser_id(), user.getHx_username(), "", user.getNickname(), user.getV_title());
                }
                dialog.show();
//                TrustCompanyBean bean = new TrustCompanyBean();
//                TrustCompanyBean.Obj obj = bean.new Obj();
//                List<TrustCompanyBean.Obj.User> users = new ArrayList<>();
//                TrustCompanyBean.Obj.User user = obj.new User();
//                user.setUser_id(data.getCompany_users().get(0).getUser_id());
//                user.setHx_username(data.getCompany_users().get(0).getHx_username());
//                users.add(user);
//                obj.setCompany_users(users);
//                obj.setImage_url(data.getCompany_icon());
////                obj.setCompany_id(data.getCompany_id());
//                obj.setCompany_name(data.getCompany_name());
//                bean.setData(obj);
//                StartActivityUtils.startTrustCompany(mContext, bean);
            }
        }

        @Override
        public boolean onLongClick(final View v) {
            switch (v.getId()) {
                case R.id.parent:
                    IBusinessDetailHolder holder = (IBusinessDetailHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                case R.id.content:
                    final String matchingId = (String) v.getTag();
                    showAlertDialog("", "是否删除该条匹配结果？", "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete(v, matchingId);
                        }
                    }, "取消", null, true);
                    break;
                default:
                    break;
            }
            return false;
        }

        /**
         * 删除匹配结果企业
         */
        private void delete(final View view, String matchingId) {
            PostCall.putJson(mContext, ServerUrl.deleteDemandsResult(matchingId), new DeleteIBusinessResultVo("poster"), new PostCall.PostResponse<BaseBean>() {

                @Override
                public void onSuccess(int i, byte[] bytes, BaseBean baseBean) {
                    ((ViewGroup) view.getParent()).removeView(view);
                }

                @Override
                public void onFailure(int i, byte[] bytes) {}
            }, new String[]{}, false, BaseBean.class);
        }
    }

    static class IBusinessDetailHolder extends BaseViewHolder {
        RelativeLayout parent;
        TextView tvTitle, tvNum;
        LinearLayout llContent;

        public IBusinessDetailHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvNum = itemView.findViewById(R.id.tvNum);
            llContent = itemView.findViewById(R.id.llContent);
        }
    }
}
