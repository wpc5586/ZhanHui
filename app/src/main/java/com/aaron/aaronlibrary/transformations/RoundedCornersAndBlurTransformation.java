package com.aaron.aaronlibrary.transformations;

/**
 * Copyright (C) 2015 Wasabeef
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.widget.ImageView;

import com.aaron.aaronlibrary.transformations.RoundedCornersTransformation.CornerType;
import com.aaron.aaronlibrary.utils.FastBlur;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public class RoundedCornersAndBlurTransformation implements Transformation<Bitmap> {

    private BitmapPool mBitmapPool;

    private float bitmapRadius;
    private float imageViewRadius;

    private float bitmapDiameter;

    private int mMargin;
    private CornerType mCornerType;
    ImageView imageView;

    public RoundedCornersAndBlurTransformation(Context context, int radius, int margin) {
        this(context, radius, margin, CornerType.ALL, null);
    }

    public RoundedCornersAndBlurTransformation(BitmapPool pool, int radius, int margin) {
        this(pool, radius, margin, CornerType.ALL);
    }

    public RoundedCornersAndBlurTransformation(Context context, int radius, int margin, CornerType cornerType,
                                               ImageView imageView) {
        this(Glide.get(context).getBitmapPool(), radius, margin, cornerType);
        this.imageView = imageView;
    }

    public RoundedCornersAndBlurTransformation(BitmapPool pool, int radius, int margin, CornerType cornerType) {
        mBitmapPool = pool;
        bitmapRadius = radius;
        imageViewRadius = radius;

        bitmapDiameter = bitmapRadius * 2;

        mMargin = margin;
        mCornerType = cornerType;
    }

    @Override
    public Resource<Bitmap> transform(Resource<Bitmap> resource, int outWidth, int outHeight) {
        Bitmap source = resource.get();

        int width = source.getWidth();
        int height = source.getHeight();

        try {
            int oWidth = imageView.getLayoutParams().width;
            int oHeight = imageView.getLayoutParams().height;

            if (oWidth >= oHeight) {
                bitmapRadius = width * imageViewRadius / (float) oWidth;
            } else {
                bitmapRadius = height * imageViewRadius / (float) oHeight;
            }

            bitmapDiameter = bitmapRadius * 2;

        } catch (Exception e) {
            e.printStackTrace();
        }

        bitmapRadius = height * imageViewRadius / outHeight;
        bitmapDiameter = bitmapRadius * 2;

        Bitmap bitmap = mBitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        drawRoundRect(canvas, paint, width, height);
        bitmap = FastBlur.doBlur(bitmap, 20, true);
        return BitmapResource.obtain(bitmap, mBitmapPool);
    }

    private void drawRoundRect(Canvas canvas, Paint paint, float width, float height) {
        float right = width - mMargin;
        float bottom = height - mMargin;

        switch (mCornerType) {
        case ALL:
            canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), bitmapRadius, bitmapRadius, paint);
            break;
        case TOP_LEFT:
            drawTopLeftRoundRect(canvas, paint, right, bottom);
            break;
        case TOP_RIGHT:
            drawTopRightRoundRect(canvas, paint, right, bottom);
            break;
        case BOTTOM_LEFT:
            drawBottomLeftRoundRect(canvas, paint, right, bottom);
            break;
        case BOTTOM_RIGHT:
            drawBottomRightRoundRect(canvas, paint, right, bottom);
            break;
        case TOP:
            drawTopRoundRect(canvas, paint, right, bottom);
            break;
        case BOTTOM:
            drawBottomRoundRect(canvas, paint, right, bottom);
            break;
        case LEFT:
            drawLeftRoundRect(canvas, paint, right, bottom);
            break;
        case RIGHT:
            drawRightRoundRect(canvas, paint, right, bottom);
            break;
        case OTHER_TOP_LEFT:
            drawOtherTopLeftRoundRect(canvas, paint, right, bottom);
            break;
        case OTHER_TOP_RIGHT:
            drawOtherTopRightRoundRect(canvas, paint, right, bottom);
            break;
        case OTHER_BOTTOM_LEFT:
            drawOtherBottomLeftRoundRect(canvas, paint, right, bottom);
            break;
        case OTHER_BOTTOM_RIGHT:
            drawOtherBottomRightRoundRect(canvas, paint, right, bottom);
            break;
        case DIAGONAL_FROM_TOP_LEFT:
            drawDiagonalFromTopLeftRoundRect(canvas, paint, right, bottom);
            break;
        case DIAGONAL_FROM_TOP_RIGHT:
            drawDiagonalFromTopRightRoundRect(canvas, paint, right, bottom);
            break;
        default:
            canvas.drawRoundRect(new RectF(mMargin, mMargin, right, bottom), bitmapRadius, bitmapRadius, paint);
            break;
        }
    }

    private void drawTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + bitmapDiameter, mMargin + bitmapDiameter),
                bitmapRadius, bitmapRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin + bitmapRadius, mMargin + bitmapRadius, bottom), paint);
        canvas.drawRect(new RectF(mMargin + bitmapRadius, mMargin, right, bottom), paint);
    }

    private void drawTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - bitmapDiameter, mMargin, right, mMargin + bitmapDiameter), bitmapRadius,
                bitmapRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right - bitmapRadius, bottom), paint);
        canvas.drawRect(new RectF(right - bitmapRadius, mMargin + bitmapRadius, right, bottom), paint);
    }

    private void drawBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, bottom - bitmapDiameter, mMargin + bitmapDiameter, bottom),
                bitmapRadius, bitmapRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin, mMargin + bitmapDiameter, bottom - bitmapRadius), paint);
        canvas.drawRect(new RectF(mMargin + bitmapRadius, mMargin, right, bottom), paint);
    }

    private void drawBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - bitmapDiameter, bottom - bitmapDiameter, right, bottom), bitmapRadius,
                bitmapRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right - bitmapRadius, bottom), paint);
        canvas.drawRect(new RectF(right - bitmapRadius, mMargin, right, bottom - bitmapRadius), paint);
    }

    private void drawTopRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + bitmapDiameter), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRect(new RectF(mMargin, mMargin + bitmapRadius, right, bottom), paint);
    }

    private void drawBottomRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, bottom - bitmapDiameter, right, bottom), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right, bottom - bitmapRadius), paint);
    }

    private void drawLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + bitmapDiameter, bottom), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRect(new RectF(mMargin + bitmapRadius, mMargin, right, bottom), paint);
    }

    private void drawRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - bitmapDiameter, mMargin, right, bottom), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right - bitmapRadius, bottom), paint);
    }

    private void drawOtherTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, bottom - bitmapDiameter, right, bottom), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRoundRect(new RectF(right - bitmapDiameter, mMargin, right, bottom), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right - bitmapRadius, bottom - bitmapRadius), paint);
    }

    private void drawOtherTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + bitmapDiameter, bottom), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRoundRect(new RectF(mMargin, bottom - bitmapDiameter, right, bottom), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRect(new RectF(mMargin + bitmapRadius, mMargin, right, bottom - bitmapRadius), paint);
    }

    private void drawOtherBottomLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + bitmapDiameter), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRoundRect(new RectF(right - bitmapDiameter, mMargin, right, bottom), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRect(new RectF(mMargin, mMargin + bitmapRadius, right - bitmapRadius, bottom), paint);
    }

    private void drawOtherBottomRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, right, mMargin + bitmapDiameter), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + bitmapDiameter, bottom), bitmapRadius, bitmapRadius,
                paint);
        canvas.drawRect(new RectF(mMargin + bitmapRadius, mMargin + bitmapRadius, right, bottom), paint);
    }

    private void drawDiagonalFromTopLeftRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(mMargin, mMargin, mMargin + bitmapDiameter, mMargin + bitmapDiameter),
                bitmapRadius, bitmapRadius, paint);
        canvas.drawRoundRect(new RectF(right - bitmapDiameter, bottom - bitmapDiameter, right, bottom), bitmapRadius,
                bitmapRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin + bitmapRadius, right - bitmapDiameter, bottom), paint);
        canvas.drawRect(new RectF(mMargin + bitmapDiameter, mMargin, right, bottom - bitmapRadius), paint);
    }

    private void drawDiagonalFromTopRightRoundRect(Canvas canvas, Paint paint, float right, float bottom) {
        canvas.drawRoundRect(new RectF(right - bitmapDiameter, mMargin, right, mMargin + bitmapDiameter), bitmapRadius,
                bitmapRadius, paint);
        canvas.drawRoundRect(new RectF(mMargin, bottom - bitmapDiameter, mMargin + bitmapDiameter, bottom),
                bitmapRadius, bitmapRadius, paint);
        canvas.drawRect(new RectF(mMargin, mMargin, right - bitmapRadius, bottom - bitmapRadius), paint);
        canvas.drawRect(new RectF(mMargin + bitmapRadius, mMargin + bitmapRadius, right, bottom), paint);
    }

    @Override
    public String getId() {
        return "RoundedTransformation(radius=" + bitmapRadius + ", margin=" + mMargin + ", diameter=" + bitmapDiameter
                + ", cornerType=" + mCornerType.name() + "picW=" + imageView.getLayoutParams().width + "picH"
                + imageView.getLayoutParams().height + ")";
    }
}
