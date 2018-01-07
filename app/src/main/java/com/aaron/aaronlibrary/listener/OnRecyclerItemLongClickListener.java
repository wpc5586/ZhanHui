package com.aaron.aaronlibrary.listener;

import android.view.View;

import com.aaron.aaronlibrary.base.domain.BaseViewHolder;


/**
 * RecyclerViewItem长点击事件
 * Created by wangpc on 2016/8/17.
 */
public interface OnRecyclerItemLongClickListener {
    void onItemLongClick(View view, BaseViewHolder holder);
}
