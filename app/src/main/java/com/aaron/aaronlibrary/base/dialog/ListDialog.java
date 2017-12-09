package com.aaron.aaronlibrary.base.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 发布选择
 * @author wangpc
 *
 */
public class ListDialog extends Dialog implements OnClickListener {
    
    private View view;
    private Activity mContext;
    
    private RelativeLayout rl_content;
    
    private RelativeLayout rl_parent;
    
    private LinearLayout ll_buttons;
    
    private View menu_bg;
    
    private ArrayList<String> btnNames;
    
    private ExecutorService executorService;
    
    private int contentHeight;
    
    private OnListDialogButtonClickListener buttonClickListener;
    
    private ListButtonClickListener listButtonClickListener;
    
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
    public ListDialog(final Activity context, int theme) {
        super(context,theme);
        view = getLayoutInflater().from(getContext()).inflate(com.xhy.zhanhui.R.layout.layout_dialog_list, null);
        super.setContentView(view);
        mContext = context;
        findView();
        init();
    }

    public void findView() {
        rl_parent = (RelativeLayout) findViewById(com.xhy.zhanhui.R.id.ac_dialog_parent);
        rl_content = (RelativeLayout) findViewById(com.xhy.zhanhui.R.id.ac_dialog_list_rl);
        ll_buttons = (LinearLayout) findViewById(com.xhy.zhanhui.R.id.ac_dialog_list_ll_btn);
        findAndSetClickListener(com.xhy.zhanhui.R.id.ac_dialog_list_btn_cancel);
        menu_bg = findAndSetClickListener(com.xhy.zhanhui.R.id.ac_dialog_list_bg);
    }

    /**
     * <p>方法描述: 初始化</p>
     */
    public void init() {
        executorService = Executors.newFixedThreadPool(1);
        rl_parent.setBackgroundColor(mContext.getResources().getColor(com.xhy.zhanhui.R.color.blank));
        btnNames = new ArrayList<String>();
        listButtonClickListener = new ListButtonClickListener();
    }
    
    @Override
    public void show() {
        super.show();
        setMenuOutside(true);
    }
    
    public ArrayList<String> getBtnNames() {
        return btnNames;
    }

    public void setBtnNames(ArrayList<String> btnNames) {
        this.btnNames = btnNames;
        setButtons();
    }

    public void setButtonClickListener(
            OnListDialogButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
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
     * <p>方法描述: 设置按钮</p>
     */
    @SuppressLint("NewApi")
    private void setButtons() {
        int size = btnNames.size();
        if (size > 0) {
            setBtnName(0);
            LayoutParams params = (LayoutParams) ll_buttons.getChildAt(0).getLayoutParams();
            Button btn_model = (Button) ll_buttons.getChildAt(0);
            if (size > 1)
                btn_model.setBackgroundResource(com.xhy.zhanhui.R.drawable.list_button_first_bg);
            btn_model.setOnClickListener(listButtonClickListener);
            btn_model.setTag(0);
            for (int i = 1; i < size; i++) {
                Button button = new Button(mContext);
                button.setLayoutParams(params);
                button.setText(btnNames.get(i));
                if (i == size - 1)
                    button.setBackgroundResource(com.xhy.zhanhui.R.drawable.list_button_last_bg);
                else
                    button.setBackgroundResource(com.xhy.zhanhui.R.drawable.list_button_noradius_bg);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, btn_model.getTextSize());
                button.setTextColor(mContext.getResources().getColor(com.xhy.zhanhui.R.color.common_title));
                button.setOnClickListener(listButtonClickListener);
                button.setTag(i);
                ll_buttons.addView(button);
            }
        }
        int btnHeight = getDimen(com.xhy.zhanhui.R.dimen.ac_dialog_list_btn_h)
                + getDimen(com.xhy.zhanhui.R.dimen.ac_dialog_list_btn_margin_l_r);
        contentHeight = btnHeight * (size + 1) + getDimen(com.xhy.zhanhui.R.dimen.ac_dialog_list_btn_margin_l_r);
    }

    /**
     * <p>方法描述: 设置按钮文字</p>
     */
    private void setBtnName(int position) {
        Button button = (Button) ll_buttons.getChildAt(position);
        button.setVisibility(View.VISIBLE);
        button.setText(btnNames.get(position));
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
    
    public interface OnListDialogButtonClickListener {

        /**
         * <p>方法描述: 确认</p>
         */
        void onListDialogButtonClick(int position, String text);
    }
}
