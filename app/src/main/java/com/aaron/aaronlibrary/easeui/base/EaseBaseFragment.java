package com.aaron.aaronlibrary.easeui.base;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.aaron.aaronlibrary.easeui.widget.EaseTitleBar;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiFragment;

/**
 * 环信UI基类
 * Created by Aaron on 2017/11/7.
 */

public abstract class EaseBaseFragment extends ZhanHuiFragment{

    protected EaseTitleBar titleBar;
    protected InputMethodManager inputMethodManager;

    public void showTitleBar(){
        if(titleBar != null){
            titleBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideTitleBar(){
        if(titleBar != null){
            titleBar.setVisibility(View.GONE);
        }
    }

    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected abstract void initView();

    protected abstract void setUpView();

    @Override
    protected void findViews(View view) {
        super.findViews(view);
        //noinspection ConstantConditions
        titleBar = (EaseTitleBar) view.findViewById(R.id.title_bar);
    }

    @Override
    protected void init() {
        super.init();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        initView();
        setUpView();
    }
}
