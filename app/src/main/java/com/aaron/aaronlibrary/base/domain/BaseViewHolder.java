package com.aaron.aaronlibrary.base.domain;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Aaron on 2016/8/17.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {

    public Object data;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }
}
