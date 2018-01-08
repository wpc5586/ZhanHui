package com.aaron.aaronlibrary.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * ActionbarView
 * Created by Aaron on 2016/6/6.
 */
public class ActionbarView extends RelativeLayout {

    public static final String TAG = "ActionbarView";

    public static final int ACTIONBAR_MODE_DEFAULT = 0; // 默认actionbar模式，标题
    public static final int ACTIONBAR_MODE_SEARCH = 1; // 搜索
    
    private static final int BUTTON_TEXT_SIZE = 16;

    private int defaultId = 100;

    private int mode = ACTIONBAR_MODE_DEFAULT;

    private Context mContext;

    private View divider;

    private ImageView backButton;

    private TextView titleView;

    private EditText searchView;

    private Button searchBtn;

    private int button_width;

    private int actionbarH;

    private List<Button> buttons;

    public ActionbarView(Context context) {
        super(context);
        init(context);
    }

    public ActionbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActionbarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context mContext) {
        this.mContext = mContext;
        button_width = MathUtils.dip2px(mContext, 60);
        inflate(mContext, R.layout.actionbar, this);
        actionbarH = MathUtils.dip2px(mContext, mContext.getResources().getDimensionPixelSize(R.dimen.actionbar_height));
        titleView = findViewById(R.id.actionbar_title);
        backButton = findViewById(R.id.actionbar_back);
        searchView = findViewById(R.id.actionbar_search);
        divider = findViewById(R.id.actionbar_divider);
        buttons = new ArrayList<>();
        setListener();
    }

    private void setListener() {
        if (mContext instanceof Activity)
            backButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity) mContext).finish();
                }
            });
    }

    public void setMode(int mode) {
        this.mode = mode;
        switch (mode) {
            case ACTIONBAR_MODE_DEFAULT:
                titleView.setVisibility(VISIBLE);
                searchView.setVisibility(GONE);
                break;
            case ACTIONBAR_MODE_SEARCH:
                titleView.setVisibility(GONE);
                searchView.setVisibility(VISIBLE);
                searchBtn = addRightTextButton("搜索", null);
                LayoutParams params = (LayoutParams) searchView.getLayoutParams();
                params.addRule(RelativeLayout.LEFT_OF, searchBtn.getId());
                break;
        }
    }

    public void setSearchBtnListener(final OnSearchBtnListener onSearchBtnListener) {
        if (searchBtn != null)
            searchBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSearchBtnListener != null && searchView != null)
                        onSearchBtnListener.search(searchView.getText().toString());
                }
            });
    }

    public interface OnSearchBtnListener {
        public void search(String content);
    }

    public void setSearchEditorActionListener(TextView.OnEditorActionListener onEditorActionListener) {
        if (searchView != null)
            searchView.setOnEditorActionListener(onEditorActionListener);
    }

    /**
     * 设置内容在返回键的右侧
     * @param layoutId
     */
    public void setLayout(int layoutId) {
        removeTitle();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.RIGHT_OF, backButton.getId());
        addView(View.inflate(getContext(), layoutId, null), params);
    }

    /**
     * 设置标题内容
     * @param title 内容
     */
    public void setTitle(String title) {
        titleView.setText(title);
    }

    /**
     * 设置标题字体大小
     * @param size 字体大小(px)
     */
    public void setTitleSize(float size) {
        titleView.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 设置标题颜色
     * @param color 颜色
     */
    public void setTitleColor(int color) {
        titleView.setTextColor(color);
    }

    /**
     * 移除title控件
     */
    public void removeTitle() {
        removeView(titleView);
    }

    /**
     * 获取TitleView
     * @return titleView
     */
    public TextView getTitleView() {
        return titleView;
    }

    /**
     * 获取下划线View
     * @return titleView
     */
    public View getDividerView() {
        return divider;
    }

    /**
     * 给Actionbar添加右侧的文字按钮（从最右方开始添加）
     * @param content 文字内容
     * @param onClickListener 点击事件
     */
    public Button addRightTextButton(String content, OnClickListener onClickListener) {
        Button button = getButton(content);
        button.setOnClickListener(onClickListener);
        LayoutParams params = new LayoutParams(actionbarH, actionbarH);
        button.setLayoutParams(params);
        if (buttons.size() > 1)
            params.addRule(RelativeLayout.LEFT_OF, buttons.get(buttons.size() - 2).getId());
        else
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(button);
        if (content != null && content.length() <= 2)
            button.getLayoutParams().width = button_width;
        return button;
    }

    /**
     * 给Actionbar添加右侧的图标按钮（从最右方开始添加）
     * @param resourceId 图片资源id
     * @param onClickListener 点击事件
     */
    public ImageView addRightButton(int resourceId, OnClickListener onClickListener) {
        ImageView button = getButton(resourceId);
        button.setOnClickListener(onClickListener);
        LayoutParams params = new LayoutParams(actionbarH, actionbarH);
        button.setLayoutParams(params);
        if (buttons.size() > 1)
            params.addRule(RelativeLayout.LEFT_OF, buttons.get(buttons.size() - 2).getId());
        else
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addView(button);
        button.getLayoutParams().width = button_width;
        button.setPadding(MathUtils.dip2px(mContext, 10), MathUtils.dip2px(mContext, 15),
                MathUtils.dip2px(mContext, 10), MathUtils.dip2px(mContext, 15));
        return button;
    }

    public void setOnBackListener(OnClickListener onClickListener) {
        backButton.setOnClickListener(onClickListener);
    }

    public void setOnBackIcon(int drawableId) {
        backButton.setImageResource(drawableId);
    }

    public ImageView getBackButton() {
        return backButton;
    }

    public EditText getSearchView() {
        return searchView;
    }

    public Button getSearchButton() {
        return searchBtn;
    }

    public int getRightButtonCount() {
        return buttons.size();
    }

    /**
     * 删除右侧的按钮
     * @param index 角标
     * @return true:删除成功 false: 删除失败
     */
    @SuppressLint("NewApi")
    public boolean removeRightButton(int index) {
        if (index >= getRightButtonCount())
            return false;
        removeView(buttons.get(index));
        buttons.remove(index);
        if (getRightButtonCount() > index)
            ((LayoutParams) buttons.get(index).getLayoutParams()).removeRule(RelativeLayout.LEFT_OF);
        else
            return true;
        if (index == 0)
            ((LayoutParams) buttons.get(0).getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        else if (index < getRightButtonCount())
            ((LayoutParams) buttons.get(index).getLayoutParams()).addRule(RelativeLayout.LEFT_OF, buttons.get(index - 1).getId());
        return true;
    }

    private Button getButton(String content) {
        Button button = new Button(mContext);
        button.setId(defaultId++);
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, BUTTON_TEXT_SIZE);
        button.setText(content);
        button.setTextColor(mContext.getResources().getColor(R.color.theme_black));
        button.setBackgroundResource(R.drawable.selector_actionbar_button);
        buttons.add(button);
        return button;
    }

    private ImageView getButton(int resourceId) {
        ImageView button = new ImageView(mContext);
        button.setId(defaultId++);
        button.setImageResource(resourceId);
        button.setBackgroundResource(R.drawable.selector_actionbar_button);
        return button;
    }
    
    public List<Button> getRightButtons() {
        return buttons;
    }
}
