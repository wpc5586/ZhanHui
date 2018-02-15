package com.xhy.zhanhui.activity;

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
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.DemandDetailBean;
import com.xhy.zhanhui.http.domain.DemandResultBean;
import com.xhy.zhanhui.http.domain.IBusinessListBean;

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
        getResultData();
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
                    setRecyclerView();
                    for (int i = 0; i < resultData.size(); i++) {
                        adapter.addData(resultData.get(i));
                    }
                } else {

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
                    StartActivityUtils.startDemandResultDetail(mContext, ((DemandResultBean.Obj.Matching) holder.data).getMatching_id());
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
            switch (data.getUser_handle_state()) {
                case "1":
                    holder.tvTitle.setText("最新");
                    break;
                case "2":
                    holder.tvTitle.setText("已浏览");
                    break;
                case "3":
                    holder.tvTitle.setText("信任");
                    break;
            }
            holder.tvNum.setText(String.valueOf(data.getMatchings().size()));
        }

        private RelativeLayout getContentView(DemandResultBean.Obj.Matching data) {
            RelativeLayout relativeLayout = (RelativeLayout) View.inflate(mContext, R.layout.item_demand_result_company, null);
            TextView tvName = relativeLayout.findViewById(R.id.tvName);
            TextView tvDegree = relativeLayout.findViewById(R.id.tvDegree);
            Button button = relativeLayout.findViewById(R.id.button);+
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
                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    IBusinessDetailHolder holder = (IBusinessDetailHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
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
