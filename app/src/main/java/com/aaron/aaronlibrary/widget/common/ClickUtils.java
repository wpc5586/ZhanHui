package com.aaron.aaronlibrary.widget.common;

import android.view.MotionEvent;

public class ClickUtils {

    /**
     * 计算触摸事件是否触发了点击
     * @return true:点击 false：没有点击
     */
    public static boolean computeTouchIsClick(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            break;
        case MotionEvent.ACTION_UP:
            return true;

        default:
            break;
        }
        return false;
    }
}
