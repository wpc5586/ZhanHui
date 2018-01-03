package com.aaron.aaronlibrary.manager;

import android.app.Activity;
import android.content.Context;

import com.aaron.aaronlibrary.base.dialog.ListDialog;
import com.aaron.aaronlibrary.base.dialog.ListDialog.OnListDialogButtonClickListener;
import com.xhy.zhanhui.R;

import java.util.ArrayList;

/**
 * <p>类名称: DialogManager</p>
 * <p>类描述: 对话框页面管理</p>
 * @author wangpc
 */
public class DialogManager{

    public static void showListDialog(Context context, ArrayList<String> list, OnListDialogButtonClickListener buttonClickListener) {
        ListDialog dialog = new ListDialog((Activity) context, R.style.listDialog);
        dialog.setBtnNames(list);
        dialog.setButtonClickListener(buttonClickListener);
        dialog.show();
    }
}
