package com.aaron.aaronlibrary.base.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aaron.aaronlibrary.base.activity.BaseActivity;
import com.aaron.aaronlibrary.base.utils.Logger;
import com.aaron.aaronlibrary.utils.ToastUtil;

/**
 * BaseFragment
 * @author wangpc
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = BaseFragment.class.getSimpleName();
    private boolean isPrepared;

    /**
     * 第一次onResume中的调用onUserVisible避免操作与onFirstUserVisible操作重复
     */
    private boolean isFirstResume = true;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;

    protected Context mContext;
    protected View view;

    protected abstract int getContentLayoutId();
    protected abstract void findViews(View view);
    protected abstract void init();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstResume) {
            isFirstResume = false;
            return;
        }
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
            if (isFirstInvisible) {
                isFirstInvisible = false;
                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    public synchronized void initPrepare() {
        if (isPrepared) {
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    /**
     * 第一次fragment可见（进行初始化工作）
     */
    public void onFirstUserVisible() {
        Logger.d(TAG, "onFirstUserVisible");
    }

    /**
     * fragment可见（切换回来或者onResume）
     */
    public void onUserVisible() {
        Logger.d(TAG, "onUserVisible");

    }

    /**
     * 第一次fragment不可见（不建议在此处理事件）
     */
    public void onFirstUserInvisible() {
        Logger.d(TAG, "onFirstUserInvisible");

    }

    /**
     * fragment不可见（切换掉或者onPause）
     */
    public void onUserInvisible() {
        Logger.d(TAG, "onUserInvisible");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.mContext = getActivity();
        if (view == null)
            view = inflater.inflate(getContentLayoutId(), container, false);
        findViews(view);
        init();
        return view;
    }

    public void showToast(final String content){
        if (getActivity() != null && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).runOnUiThread(new Runnable() {
            public void run() {
                ToastUtil.show(mContext, content);
            }
        });
    }

    public void showLongToast(final String content){
        if (getActivity() != null && getActivity() instanceof BaseActivity)
            ((BaseActivity) getActivity()).runOnUiThread(new Runnable() {
                public void run() {
                    ToastUtil.showLong(mContext, content);
                }
            });
    }

    @Override
    public void onClick(View v) {

    }

    protected void startMyActivity(Class cls) {
        startActivity(new Intent(mContext, cls));
    }
}
