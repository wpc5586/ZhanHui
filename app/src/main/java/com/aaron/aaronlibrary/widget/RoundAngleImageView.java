package com.aaron.aaronlibrary.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.aaron.aaronlibrary.utils.MathUtils;
import com.xhy.zhanhui.R;

/**
 * 圆角控件
 * 需要在Manifest设置activity关闭硬件加速 
 * android:hardwareAccelerated="false"
 * @author wangpc
 *
 */
public class RoundAngleImageView extends ImageView {
    private Paint paint;
    private int roundWidth = 5;
    private int roundHeight = 5;
    private Paint paint2;
    /**
     * 用于记录此view在第几个
     */
    private int position;
    int style;
    
    /**
     * 是否是老版本处理圆角方式
     */
    private boolean isOld;

    public static final int ALL = 0;
    public static final int TOP = 1;
    public static final int BOTTOM = 2;
    public static final int LEFTTOP = 3;
    public static final int RIGHTTOP = 4;
    public static final int LEFTBOTTOM = 5;
    public static final int RIGHTBOTTOM = 6;
    public static final int NOT = 7;
    public static final int LEFT = 8;
    public static final int RIGHT = 9;

    public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundAngleImageView(Context context) {
        super(context);
        init(context, null);
    }

    @SuppressLint("Recycle")
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
           TypedArray a = context.obtainStyledAttributes(attrs,
                  R.styleable.RoundAngleImageView);
           // roundWidth =
           // a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundWidth1,
           // roundWidth);
           // roundHeight =
           // a.getDimensionPixelSize(R.styleable.RoundAngleImageView_roundHeight,
           // roundHeight);
           style = a.getInt(R.styleable.RoundAngleImageView_roundStyle, 0);
           // } else {
        }
        float density = context.getResources().getDisplayMetrics().density;
        roundWidth = (int) (roundWidth * density);
        roundHeight = (int) (roundHeight * density);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        
        paint2 = new Paint();
        paint2.setXfermode(null);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (!isOld) {
           switch (style) {
           case ALL:
               drawAll(canvas);
               break;
           case TOP:
               drawTop(canvas);
               break;
           case BOTTOM:
               drawBottom(canvas);
               break;
           case LEFTTOP:
               drawLeftTop(canvas);
               break;
           case RIGHTTOP:
               drawRightTop(canvas);
               break;
           case RIGHTBOTTOM:
               drawRightBottom(canvas);
               break;
           case LEFTBOTTOM:
               drawLeftBottom(canvas);
               break;
           case NOT:
               break;
           case LEFT:
               drawLeft(canvas);
               break;
           case RIGHT:
               drawRight(canvas);
               break;
           }
           // 防锯齿
           canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG| Paint.ANTI_ALIAS_FLAG));
        }
        super.onDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        if (isOld) {
            int width = getWidth() != 0 ? getWidth() :
            MathUtils.dip2px(getContext(), 100);
            int height = getHeight() != 0 ? getHeight() :
            MathUtils.dip2px(getContext(), 100);
            Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
            Canvas canvas2 = new Canvas(bitmap);
            super.draw(canvas2);
            Log.i("", "----------- " + roundHeight + "  " + roundWidth);
            
            switch (style) {
            case ALL:
                drawLiftUp(canvas2);
                drawRightUp(canvas2);
                drawLiftDown(canvas2);
                drawRightDown(canvas2);
                break;
            case TOP:
                drawLiftUp(canvas2);
                drawRightUp(canvas2);
                break;
            case BOTTOM:
                drawLiftDown(canvas2);
                drawRightDown(canvas2);
                break;
            case LEFTTOP:
                drawLiftUp(canvas2);
                break;
            case RIGHTTOP:
                drawRightUp(canvas2);
                break;
            case RIGHTBOTTOM:
                drawRightDown(canvas2);
                break;
            case LEFTBOTTOM:
                drawLiftDown(canvas2);
                break;
            case NOT:
                break;
            case LEFT:
                drawLiftDown(canvas2);
                drawLiftUp(canvas2);
                break;
            case RIGHT:
                drawRightDown(canvas2);
                drawRightUp(canvas2);
                break;
            }
            canvas.drawBitmap(bitmap, 0, 0, paint2);
            bitmap.recycle();
        } else {
            super.draw(canvas);
        }
    }

    private void drawAll(Canvas canvas) {
        Path clipPath = new Path();
        // RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        // 左上角
        clipPath.moveTo(0, roundHeight);
        clipPath.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -180,
               90);
        // 右上角
        clipPath.lineTo(getWidth() - roundWidth, 0);
        clipPath.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(),
               0 + roundHeight * 2), -90, 90);
        // 右下角
        clipPath.lineTo(getWidth(), getHeight() - roundHeight);
        clipPath.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight()
               - roundHeight * 2, getWidth(), getHeight()), 0, 90);
        // 左下角
        clipPath.lineTo(roundWidth, getHeight());
        clipPath.arcTo(new RectF(0, getHeight() - roundHeight * 2,
               0 + roundWidth * 2, getHeight()), 90, 90);
        clipPath.lineTo(0, roundHeight);
        clipPath.close();
        canvas.clipPath(clipPath);
    }

    private void drawTop(Canvas canvas) {
        Path clipPath = new Path();
        // 左上角
        clipPath.moveTo(0, roundHeight);
        clipPath.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -180,
               90);
        // 右上角
        clipPath.lineTo(getWidth() - roundWidth, 0);
        clipPath.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(),
               0 + roundHeight * 2), -90, 90);
        // 右下角
        clipPath.lineTo(getWidth(), getHeight());
        // 左下角
        clipPath.lineTo(0, getHeight());
        clipPath.lineTo(0, 0);
        clipPath.close();
        canvas.clipPath(clipPath);
    }

    private void drawBottom(Canvas canvas) {
        Path clipPath = new Path();
        // RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        // 左上角
        clipPath.moveTo(0, 0);
        // 右上角
        clipPath.lineTo(getWidth(), 0);
        // 右下角
        clipPath.lineTo(getWidth(), getHeight() - roundHeight);
        clipPath.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight()
               - roundHeight * 2, getWidth(), getHeight()), 0, 90);
        // 左下角
        clipPath.lineTo(roundWidth, getHeight());
        clipPath.arcTo(new RectF(0, getHeight() - roundHeight * 2,
               0 + roundWidth * 2, getHeight()), 90, 90);
        clipPath.lineTo(0, 0);
        clipPath.close();
        canvas.clipPath(clipPath);
    }

    private void drawLeftTop(Canvas canvas) {
        Path clipPath = new Path();
        // RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        // 左上角
        clipPath.moveTo(0, roundHeight);
        clipPath.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -180,
               90);
        // 右上角
        clipPath.lineTo(getWidth(), 0);
        // 右下角
        clipPath.lineTo(getWidth(), getHeight());
        // 左下角
        clipPath.lineTo(0, getHeight());
        clipPath.lineTo(0, getHeight() - roundHeight);
        clipPath.close();
        canvas.clipPath(clipPath);
    }

    private void drawRightTop(Canvas canvas) {
        Path clipPath = new Path();
        // RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        // 左上角
        clipPath.moveTo(0, 0);
        // 右上角
        clipPath.lineTo(getWidth() - roundWidth, 0);
        clipPath.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(),
               0 + roundHeight * 2), -90, 90);
        // 右下角
        clipPath.lineTo(getWidth(), getHeight());
        // 左下角
        clipPath.lineTo(0, getHeight());
        clipPath.lineTo(0, 0);
        clipPath.close();
        canvas.clipPath(clipPath);
    }

    private void drawLeftBottom(Canvas canvas) {
        Path clipPath = new Path();
        // RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        // 左上角
        clipPath.moveTo(0, 0);
        // 右上角
        clipPath.lineTo(getWidth(), 0);
        // 右下角
        clipPath.lineTo(getWidth(), getHeight());
        // 左下角
        clipPath.lineTo(roundWidth, getHeight());
        clipPath.arcTo(new RectF(0, getHeight() - roundHeight * 2,
               0 + roundWidth * 2, getHeight()), 90, 90);
        clipPath.lineTo(0, getHeight() - roundHeight);
        clipPath.lineTo(0, 0);
        clipPath.close();
        canvas.clipPath(clipPath);
    }

    private void drawRightBottom(Canvas canvas) {
        Path clipPath = new Path();
        // RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        // 左上角
        clipPath.moveTo(0, 0);
        // 右上角
        clipPath.lineTo(getWidth(), 0);
        // 右下角
        clipPath.lineTo(getWidth(), getHeight() - roundHeight);
        clipPath.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight()
               - roundHeight * 2, getWidth(), getHeight()), 0, 90);
        // 左下角
        clipPath.lineTo(0, getHeight());
        clipPath.lineTo(0, 0);
        clipPath.close();
        canvas.clipPath(clipPath);
    }

    private void drawLeft(Canvas canvas) {
        Path clipPath = new Path();
        // RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        // 左上角
        clipPath.moveTo(0, roundHeight);
        clipPath.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -180,
               90);
        // 右上角
        clipPath.lineTo(getWidth(), 0);
        // 右下角
        clipPath.lineTo(getWidth(), getHeight());
        // 左下角
        clipPath.lineTo(roundWidth, getHeight());
        clipPath.arcTo(new RectF(0, getHeight() - roundHeight * 2,
               0 + roundWidth * 2, getHeight()), 90, 90);
        clipPath.lineTo(0, getHeight() - roundHeight);
        clipPath.close();
        canvas.clipPath(clipPath);
    }

    private void drawRight(Canvas canvas) {
        Path clipPath = new Path();
        // RectF rect = new RectF(0, 0, this.getWidth(), this.getHeight());
        // 左上角
        clipPath.moveTo(0, 0);
        // 右上角
        clipPath.moveTo(getWidth() - roundWidth, 0);
        clipPath.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(),
               0 + roundHeight * 2), -90, 90);
        // 右下角
        clipPath.lineTo(getWidth(), getHeight() - roundHeight);
        clipPath.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight()
               - roundHeight * 2, getWidth(), getHeight()), 0, 90);
        // 左下角
        clipPath.lineTo(0, getHeight());
        clipPath.lineTo(0, 0);
        clipPath.close();
        canvas.clipPath(clipPath);
    }

    private void drawLiftUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, roundHeight);
        path.lineTo(0, 0);
        path.lineTo(roundWidth, 0);
        path.arcTo(new RectF(0, 0, roundWidth * 2, roundHeight * 2), -90, -90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawLiftDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(0, getHeight() - roundHeight);
        path.lineTo(0, getHeight());
        path.lineTo(roundWidth, getHeight());
        path.arcTo(new RectF(0, getHeight() - roundHeight * 2,
               0 + roundWidth * 2, getHeight()), 90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightDown(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth() - roundWidth, getHeight());
        path.lineTo(getWidth(), getHeight());
        path.lineTo(getWidth(), getHeight() - roundHeight);
        path.arcTo(new RectF(getWidth() - roundWidth * 2, getHeight()
               - roundHeight * 2, getWidth(), getHeight()), 0, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawRightUp(Canvas canvas) {
        Path path = new Path();
        path.moveTo(getWidth(), roundHeight);
        path.lineTo(getWidth(), 0);
        path.lineTo(getWidth() - roundWidth, 0);
        path.arcTo(new RectF(getWidth() - roundWidth * 2, 0, getWidth(),
               0 + roundHeight * 2), -90, 90);
        path.close();
        canvas.drawPath(path, paint);
    }

    public RoundAngleImageView setStyle(int style) {
        this.style = style;
        refreshDrawableState();
        return this;
    }
    
    public RoundAngleImageView setIsOld(boolean isOld) {
        this.isOld = isOld;
        return this;
    }
    
    public boolean isOld() {
        return isOld;
    }
}
