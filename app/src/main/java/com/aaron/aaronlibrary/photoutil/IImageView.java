package com.aaron.aaronlibrary.photoutil;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 用于相册页面记录加载视频预览图的ImageView
 * @author wangpc
 *
 */
public class IImageView extends ImageView {
    
    private Bitmap bitmap;
    
    private Object ttag;

    public IImageView(Context context) {
        super(context);
    }

    public IImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Object getTtag() {
        return ttag;
    }

    public void setTtag(Object ttag) {
        this.ttag = ttag;
    }
}