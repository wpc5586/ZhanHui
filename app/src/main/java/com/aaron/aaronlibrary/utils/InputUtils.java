package com.aaron.aaronlibrary.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 输入工具
 * 
 * @author wangpc
 */
public class InputUtils {

    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示
     * @param mContext 上下文
     */
    public static void toggleSoftInput(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    
    /**
     * 强制显示、隐藏键盘
     * @param mContext 上下文
     * @param view 接受软键盘输入的视图
     * @param isShow 是否是显示
     */
    public static void hideOrShowSoftInput(Context mContext, View view, boolean isShow) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isShow)
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        else
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    
    /**
     * 强制显示、隐藏键盘
     * @param mContext 上下文
     * @return 若返回true，则表示输入法打开
     */
    public static boolean inputIsActive(Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }
}
