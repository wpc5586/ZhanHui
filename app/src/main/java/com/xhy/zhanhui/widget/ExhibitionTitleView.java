package com.xhy.zhanhui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xhy.zhanhui.R;

/**
 * 栏目标题控件
 * Created by Aaron on 10/12/2017.
 */

public class ExhibitionTitleView extends RelativeLayout {

    private Context mContext;
    private TextView textView;
    private View divide;
    private ImageView ivArrow;

    public ExhibitionTitleView(Context context) {
        super(context);
        init(context, null);
    }

    public ExhibitionTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExhibitionTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.mContext = context;
        inflate(mContext, R.layout.item_exhibition_title, this);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.ExhibitionTitleView);
            String title = a.getString(R.styleable.ExhibitionTitleView_text);
            textView = findViewById(R.id.title);
            textView.setText(title);
            divide = findViewById(R.id.divide);
            ivArrow = findViewById(R.id.ivArrow);
        }
    }

    public void setText(String text) {
        textView.setText(text);
    }

    public void setArrowVisibility(int visibility) {
        ivArrow.setVisibility(visibility);
    }

    public void setDivideVisibility(int visibility) {
        divide.setVisibility(visibility);
    }
}
