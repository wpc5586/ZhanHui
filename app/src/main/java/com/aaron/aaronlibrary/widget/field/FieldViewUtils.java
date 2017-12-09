package com.aaron.aaronlibrary.widget.field;

import android.support.v4.view.ViewPager;
import android.view.animation.AccelerateInterpolator;

import com.aaron.aaronlibrary.widget.field.view.FixedSpeedScroller;

import java.lang.reflect.Field;

/**
 * 反射改变view的工具类
 * @author wangpc
 *
 */
public class FieldViewUtils {

    /**
     * 设置ViewPager滑动速度
     */
    public static void setViewPagerSpeed(ViewPager viewPager) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(300);
        } catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
}
