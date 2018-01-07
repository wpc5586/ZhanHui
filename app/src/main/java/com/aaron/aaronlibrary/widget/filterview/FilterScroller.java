package com.aaron.aaronlibrary.widget.filterview;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 筛选控件滑动控制器
 * Created by wpc on 2017/7/20.
 */

public class FilterScroller {

    /**
     * 滑动监听接口
     */
    public interface OnScrollingListener {
        /**
         * 滑动中回调
         * @param distance 滑动距离
         */
        void onScroll(int distance);

        /**
         * 滑动开始回调
         */
        void onStarted();

        /**
         * 滑动完成回调
         */
        void onFinished();

        /**
         * 滑动结束时的校正
         */
        void onJustify();
    }

    /**
     * 滑动持续时间
     **/
    private static final int SCROLLING_DURATION = 400;

    /**
     * 滚动的最小增量
     **/
    public static final int MIN_DELTA_FOR_SCROLLING = 1;

    // 消息常量
    private final int MESSAGE_SCROLL = 0; // 滚动
    private final int MESSAGE_JUSTIFY = 1; // 对齐

    private Context context;

    // 监听
    private OnScrollingListener listener;
    private GestureDetector gestureDetector;
    private Scroller scroller;
    private int lastScrollY;
    private float lastTouchedY;
    private boolean isScrollingPerformed;

    public FilterScroller(Context context, OnScrollingListener listener) {
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false); // 不允许长按

        scroller = new Scroller(context);

        this.listener = listener;
        this.context = context;
    }


    /**
     * 设置interpolator
     * @param interpolator interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        scroller.forceFinished(true);
        scroller = new Scroller(context, interpolator);
    }

    /**
     * 滑动
     * @param distance t滑动距离
     * @param time 滑动持续时间
     */
    public void scroll(int distance, int time) {
        scroller.forceFinished(true);

        lastScrollY = 0;

        scroller.startScroll(0, 0, 0, distance, time != 0 ? time : SCROLLING_DURATION);
        setNextMessage(MESSAGE_SCROLL);

        startScrolling();
    }

    /**
     * 停止滑动
     */
    public void stopScrolling() {
        scroller.forceFinished(true);
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastTouchedY = event.getY();
                scroller.forceFinished(true);
                clearMessages();
                break;

            case MotionEvent.ACTION_MOVE:
                int distanceY = (int)(event.getY() - lastTouchedY);
                if (distanceY != 0) {
                    startScrolling();
                    listener.onScroll(distanceY);
                    lastTouchedY = event.getY();
                }
                break;
        }

        if (!gestureDetector.onTouchEvent(event) && event.getAction() == MotionEvent.ACTION_UP) {
            justify();
        }

        return true;
    }

    /**
     * 清空消息队列，加入消息到队列中
     *
     * @param message 设置消息
     */
    private void setNextMessage(int message) {
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }

    /**
     * 清理消息队列
     */
    private void clearMessages() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }

    /**
     * 动画Handler
     */
    private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            scroller.computeScrollOffset();
            int currY = scroller.getCurrY();
            int delta = lastScrollY - currY;
            lastScrollY = currY;
            if (delta != 0) {
//                listener.onScroll(delta);
            }

            // 滑到最终Y坐标时，手动结束滑动
            if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                currY = scroller.getFinalY();
                scroller.forceFinished(true);
            }
            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == MESSAGE_SCROLL) {
                justify();
            } else {
                finishScrolling();
            }
        }
    };

    /**
     * 对齐
     */
    private void justify() {
        listener.onJustify();
        setNextMessage(MESSAGE_JUSTIFY);
    }

    /**
     * 开始滑动
     */
    private void startScrolling() {
        if (!isScrollingPerformed) {
            isScrollingPerformed = true;
            listener.onStarted();
        }
    }

    /**
     * 结束滑动
     */
    void finishScrolling() {
        if (isScrollingPerformed) {
            listener.onFinished();
            isScrollingPerformed = false;
        }
    }

    /**
     * 手势监听
     */
    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            lastScrollY = 0;
            final int maxY = 0x7FFFFFFF;
            final int minY = -maxY;
            scroller.fling(0, lastScrollY, 0, (int) -velocityY, 0, 0, minY, maxY);
            setNextMessage(MESSAGE_SCROLL);
            return true;
        }
    };
}
