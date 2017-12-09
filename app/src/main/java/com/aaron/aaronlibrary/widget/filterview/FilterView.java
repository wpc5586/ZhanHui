package com.aaron.aaronlibrary.widget.filterview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.aaron.aaronlibrary.widget.wheelcity.WheelScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选控件（从已有的列表中，选中多个数据）
 * Created by wpc on 2017/7/19.
 */

public class FilterView extends View {

    private List<String> items = new ArrayList<>();
    private List<String> selectItenms = new ArrayList<>();

    private FilterScroller scroller;

    private boolean isScrollingPerformed; // 是否正在滑动
    private int scrollingOffset;


    public FilterView(Context context) {
        super(context);
        init(context);
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        scroller = new FilterScroller(context, onScrollingListener);
    }

    // Scrolling listener
    FilterScroller.OnScrollingListener onScrollingListener = new FilterScroller.OnScrollingListener() {
        public void onStarted() {
            isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }

        public void onScroll(int distance) {
            doScroll(distance);

            int height = getHeight();
            if (scrollingOffset > height) {
                scrollingOffset = height;
                scroller.stopScrolling();
            } else if (scrollingOffset < -height) {
                scrollingOffset = -height;
                scroller.stopScrolling();
            }
        }

        public void onFinished() {
            if (isScrollingPerformed) {
                notifyScrollingListenersAboutEnd();
                isScrollingPerformed = false;
            }

            scrollingOffset = 0;
            invalidate();
        }

        public void onJustify() {
            if (Math.abs(scrollingOffset) > WheelScroller.MIN_DELTA_FOR_SCROLLING) {
                scroller.scroll(scrollingOffset, 0);
            }
        }
    };

    /**
     * Notifies listeners about starting scrolling
     */
    protected void notifyScrollingListenersAboutStart() {
//        for (OnWheelScrollListener listener : scrollingListeners) {
//            listener.onScrollingStarted(this);
//        }
    }

    /**
     * Notifies listeners about ending scrolling
     */
    protected void notifyScrollingListenersAboutEnd() {
//        for (OnWheelScrollListener listener : scrollingListeners) {
//            listener.onScrollingFinished(this);
//        }
    }

    /**
     * Scrolls the wheel
     *
     * @param delta the scrolling value
     */
    private void doScroll(int delta) {
        scrollingOffset += delta;
        invalidate();

        // update offset
        if (scrollingOffset > getHeight()) {
            scrollingOffset = scrollingOffset % getHeight() + getHeight();
        }
    }
}
