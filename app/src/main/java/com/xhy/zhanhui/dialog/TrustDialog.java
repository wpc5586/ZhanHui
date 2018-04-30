package com.xhy.zhanhui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.domain.BaseViewHolder;
import com.aaron.aaronlibrary.listener.OnRecyclerItemClickListener;
import com.aaron.aaronlibrary.listener.OnRecyclerItemLongClickListener;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.IBusinessCompanyDetailActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.TrustUserBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 添加企业信任选择对话框
 * @author wangpc
 *
 */
public class TrustDialog extends Dialog implements OnClickListener {
    
    private View view;
    private Activity mContext;

    private List<String> ids = new ArrayList<>();
    private List<String> hxIds = new ArrayList<>();
    private List<String> avatars = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> intros = new ArrayList<>();
    
    private RelativeLayout rl_content;
    
    private RelativeLayout rl_parent;
    
    private RecyclerView recyclerView;
    private TrustAdapter adapter;
    
    private View menu_bg;
    
    private ExecutorService executorService;
    
    private int contentHeight;
    
    private OnListDialogButtonClickListener buttonClickListener;

    private final int ANIMATION_TIME = 300;
    
    private Runnable finishRunnable = new Runnable() {
        
        @Override
        public void run() {
            try {
                Thread.sleep(ANIMATION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dismiss();
        }
    };
    
    @SuppressWarnings("static-access")
    public TrustDialog(final Activity context, int theme) {
        super(context,theme);
        view = getLayoutInflater().from(getContext()).inflate(R.layout.layout_dialog_trust, null);
        super.setContentView(view);
        mContext = context;
        findView();
        init();
    }

    public void findView() {
        rl_parent = findViewById(com.xhy.zhanhui.R.id.ac_dialog_parent);
        rl_content = findViewById(com.xhy.zhanhui.R.id.ac_dialog_list_rl);
        recyclerView = findViewById(R.id.recycler);
        findAndSetClickListener(com.xhy.zhanhui.R.id.ac_dialog_list_btn_cancel);
        menu_bg = findAndSetClickListener(com.xhy.zhanhui.R.id.ac_dialog_list_bg);
    }

    /**
     * <p>方法描述: 初始化</p>
     */
    public void init() {
        executorService = Executors.newFixedThreadPool(1);
        rl_parent.setBackgroundColor(mContext.getResources().getColor(com.xhy.zhanhui.R.color.transparent));
    }

    public void addData(String userId, String hxUserId, String avatar, String name, String intro) {
        ids.add(userId);
        hxIds.add(hxUserId);
        avatars.add(avatar);
        names.add(name);
        intros.add(intro);
        setRecyclerView();
    }
    
    @Override
    public void show() {
        super.show();
        setMenuOutside(true);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == com.xhy.zhanhui.R.id.ac_dialog_list_btn_cancel || id == com.xhy.zhanhui.R.id.ac_dialog_list_bg) {
            back();
        }
    }
    
    @Override
    public void dismiss() {
        super.dismiss();
        finishRunnable = null;
    }
    
    /**
     * <p>方法描述: 退出</p>
     */
    public void back() {
        setMenuOutside(false);
        executorService.execute(finishRunnable);
    }

    /**
     * <p>方法描述: 设置Menu的其他区域是否变为不透明</p>
     * @param visible true:显示  false:隐藏
     */
    private void setMenuOutside(boolean visible) {
        if (visible) {
            menu_bg.setVisibility(View.VISIBLE);
            rl_content.setVisibility(View.VISIBLE);
            startBgAnimation(0.0f, 1.0f);
            startMenuAnimation(contentHeight, 0);
        } else {
            startBgAnimation(1.0f, 0.0f);
            menu_bg.setVisibility(View.GONE);
            startMenuAnimation(0, contentHeight);
            rl_content.setVisibility(View.GONE);
        }
    }
    
    /**
     * <p>方法描述: 开始菜单的其他区域背景动画</p>
     */
    private void startBgAnimation(float fromAlpha, float toAlpha) {
        Animation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(ANIMATION_TIME);
        menu_bg.startAnimation(alphaAnimation);
    }
    
    /**
     * <p>方法描述: 开始菜单动画</p>
     */
    private void startMenuAnimation(float fromY, float toY) {
        TranslateAnimation animation = new TranslateAnimation(0, 0, fromY, toY);
        animation.setDuration(ANIMATION_TIME);
        rl_content.startAnimation(animation);
    }
    
    private View findAndSetClickListener(int id) {
        View view = findViewById(id);
        view.setOnClickListener(this);
        return view;
    }
    
    private int getDimen(int id) {
        return mContext.getResources().getDimensionPixelOffset(id);
    }
    
    private class ListButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (buttonClickListener != null) {
                buttonClickListener.onListDialogButtonClick((Integer) view.getTag(), ((Button) view).getText().toString());
            }
            back();
        }
    }

    /**
     * 设置RecyclerView
     */
    private void setRecyclerView() {
//        MyLinearLayoutManager linearLayoutManager = new MyLinearLayoutManager(mContext);
//        linearLayoutManager.setScrollEnabled(false);
//        recyclerView.setLayoutManager(linearLayoutManager);
        if (adapter == null) {
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(manager);
            adapter = new TrustAdapter(mContext);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerItemClickListener() {
                @Override
                public void onItemClick(View view, BaseViewHolder holder) {
                    int pos = holder.getAdapterPosition();
                    TrustUserBean.Obj userData = new TrustUserBean().new Obj();
                    userData.setHx_username(hxIds.get(pos));
                    userData.setIcon(avatars.get(pos));
                    userData.setNickname(names.get(pos));
                    userData.setUser_id(ids.get(pos));
                    userData.setV_title(intros.get(pos));
                    StartActivityUtils.startTrustUser(mContext, userData);
                    dismiss();
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
    
    public interface OnListDialogButtonClickListener {

        /**
         * <p>方法描述: 确认</p>
         */
        void onListDialogButtonClick(int position, String text);
    }

    public class TrustAdapter extends RecyclerView.Adapter<TrustHolder> implements View.OnClickListener, View.OnLongClickListener {

        private final Context context;

        private OnRecyclerItemClickListener onItemClickListener;

        private OnRecyclerItemLongClickListener onItemLongClickListener;

        public void setOnItemClickListener(OnRecyclerItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        public void setOnItemLongClickListener(OnRecyclerItemLongClickListener onItemLongClickListener) {
            this.onItemLongClickListener = onItemLongClickListener;
        }

        public TrustAdapter(Context context) {
            this.context = context;
        }

        @Override
        public TrustHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final TrustHolder holder = new TrustHolder(LayoutInflater.from(context).
                    inflate(R.layout.item_trust_dialog, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(TrustHolder holder, int position) {
            String userId = getItem(position);
            holder.data = userId;
            holder.parent.setTag(holder);
            holder.parent.setOnClickListener(this);
            holder.parent.setOnLongClickListener(this);
            ImageUtils.loadImageCircle(mContext, avatars.get(position), holder.ivAvatar);
            holder.tvName.setText(names.get(position));
            holder.tvIntro.setText(intros.get(position));
        }

        @Override
        public int getItemCount() {
            return avatars.size();
        }

        public String getItem(int position) {
            return ids.get(position);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.parent:
                    TrustHolder holder = (TrustHolder) v.getTag();
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, holder);
                    break;
                case R.id.content:
                    String matchingId = (String) v.getTag();
                    StartActivityUtils.startIBusinessCompanyDetail(mContext, matchingId, IBusinessCompanyDetailActivity.TYPE_DEFAULT);
                    break;
                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(final View v) {
            switch (v.getId()) {
                case R.id.parent:
                    TrustHolder holder = (TrustHolder) v.getTag();
                    if (onItemLongClickListener != null)
                        onItemLongClickListener.onItemLongClick(v, holder);
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    static class TrustHolder extends BaseViewHolder {
        RelativeLayout parent;
        TextView tvName, tvIntro;
        ImageView ivAvatar;

        public TrustHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
            tvName = itemView.findViewById(R.id.tvName);
            tvIntro = itemView.findViewById(R.id.tvIntro);
        }
    }
}
