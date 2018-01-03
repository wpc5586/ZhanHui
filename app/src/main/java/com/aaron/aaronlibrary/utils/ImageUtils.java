package com.aaron.aaronlibrary.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.aaron.aaronlibrary.base.app.CrashApplication;
import com.aaron.aaronlibrary.base.utils.DataPath;
import com.aaron.aaronlibrary.base.utils.Logger;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.transformations.CropCircleTransformation;
import com.aaron.aaronlibrary.transformations.RoundedCornersAndBlurTransformation;
import com.aaron.aaronlibrary.transformations.RoundedCornersTransformation;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 图片下载工具
 * 
 * @author wangpc
 */
public class ImageUtils {

    public static final String TAG = "------ImageUtils------";
    
    // 根据图片size和image的max/min size加载圆角图片
    public static void loadImageRoundedCornersEx(Context mContext, //
                                                 String path,//
                                                 ImageView target,//
                                                 RoundedCornersTransformation.CornerType cornerType,//
                                                 int radius, //
                                                 int imgW, int imgH,//
                                                 float maxW, float maxH,//
                                                 float minW, float minH) {
        setImageViewSize(target, imgW, imgH, maxW, maxH, minW, minH);
        loadImageRoundedCorners(mContext, path, target, cornerType, radius);
    }

    // 根据图片size和image的max/min size加载圆角&模糊图片
    public static void loadImageRoundedCornersAndBlueEx(Context mContext, //
                                                        String path,//
                                                        ImageView target,//
                                                        RoundedCornersTransformation.CornerType cornerType,//
                                                        int radius, //
                                                        int imgW, int imgH,//
                                                        float maxW, float maxH,//
                                                        float minW, float minH) {
        setImageViewSize(target, imgW, imgH, maxW, maxH, minW, minH);
        loadImageRoundedCornersAndBlur(mContext, path, target, cornerType, radius);
    }

    // 加载圆角图片
    @SuppressWarnings("unchecked")
    public static void loadImageRoundedCorners(Context mContext, String path, ImageView target,
                                               RoundedCornersTransformation.CornerType cornerType, int radius) {

        if (TextUtils.isEmpty(path)) {
            return;
        }

        try {
            Glide.with(mContext)//
                   .load(path)//
                   .crossFade(500)//
                   .diskCacheStrategy(DiskCacheStrategy.SOURCE)//
                   .bitmapTransform(new RoundedCornersTransformation(mContext, radius, 0, cornerType, target))//
                   .into(target);//
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.e(TAG, "loadImageRoundedCorners " + path);

        target.setVisibility(View.VISIBLE);
    }

    // 加载圆角图片（从本地drawable）
    @SuppressWarnings("unchecked")
    public static void  loadImageRoundedCorners(Context mContext, int id, ImageView target,
                                                RoundedCornersTransformation.CornerType cornerType, int radius) {
        if (id == 0)
            return;
        try {
            Glide.with(mContext)//
                    .load(id)
                    .crossFade(0)//
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)//
                    .bitmapTransform(new RoundedCornersTransformation(mContext, radius, 0, cornerType, target))//
                    .into(target);//
        } catch (Exception e) {
            e.printStackTrace();
        }

        Logger.e(TAG, "loadImageRoundedCorners " + id);

        target.setVisibility(View.VISIBLE);
    }

    // 加载圆形图片
    @SuppressWarnings("unchecked")
    public static void loadImageCircle(Context mContext, String path, ImageView target, boolean... isAvatar) {
        boolean avatar = isAvatar.length > 0 && isAvatar[0];

        if (avatar) {
            try {
                Glide.with(mContext)//
                .load(com.xhy.zhanhui.R.mipmap.common_avatar)//
                .crossFade(0)//
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)//
                .bitmapTransform(new CropCircleTransformation(mContext))//
                .into(target);//
            } catch (Exception e) {
                e.printStackTrace();
            }
            // target.setImageResource(R.mipmap.common_avatar);
        }

        if (TextUtils.isEmpty(path)) {
            return;
        }

        try {
            Glide.with(mContext)//
            .load(path)//
            .crossFade(0)//
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)//
            .bitmapTransform(new CropCircleTransformation(mContext))//
            .into(target);//
        } catch (Exception e) {
            e.printStackTrace();
        }

        target.setVisibility(View.VISIBLE);
    }

    // 加载圆角+模糊图片
    @SuppressWarnings("unchecked")
    public static void loadImageRoundedCornersAndBlur(Context mContext, String path, ImageView target,
                                                      RoundedCornersTransformation.CornerType cornerType, int radius) {

        if (TextUtils.isEmpty(path)) {
            return;
        }

        try {
            Glide.with(mContext)//
                    .load(path)//
                    .crossFade(0)//
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//
                    .bitmapTransform(new RoundedCornersAndBlurTransformation(mContext, radius, 0, cornerType, target))//
                    .into(target);//
        } catch (Exception e) {
            e.printStackTrace();
        }

        target.setVisibility(View.VISIBLE);
    }

    /**
     * 加载图片
     * @param mContext
     * @param path
     * @param target
     * @param isAvatar [0]：是否是加载头像  [1]：是否是加载本地
     */
    public static void loadImage(Context mContext, String path, ImageView target, boolean... isAvatar) {
        boolean avatar = isAvatar.length > 0 && isAvatar[0];
        boolean isLocal = isAvatar.length > 1 && isAvatar[1];
        if (TextUtils.isEmpty(path)) {
            if (avatar) {
                target.setImageResource(com.xhy.zhanhui.R.mipmap.common_avatar);
            }
            return;
        }
        String headPath = path;
        if (!isLocal && headPath.charAt(0) != 'h') {
            headPath = ServerUrl.SERVICE + path;
        }
//        target.setTag(null);
        if (avatar)
            target.setImageResource(com.xhy.zhanhui.R.mipmap.common_avatar);
        try {
            Glide.with(mContext).load(headPath).crossFade(800).into(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        target.setVisibility(View.VISIBLE);
    }

    public static void loadLocalImage(Context mContext, String path, ImageView target, boolean... isAvatar) {
        boolean avatar = isAvatar.length > 0 && isAvatar[0];
        if (TextUtils.isEmpty(path)) {
            if (avatar) {
                target.setImageResource(com.xhy.zhanhui.R.mipmap.common_avatar);
            }
            return;
        }

        File f = new File(path);
        if (!f.exists())
            return;

        // target.setTag(null);
        if (avatar)
            target.setImageResource(com.xhy.zhanhui.R.mipmap.common_avatar);
        try {
            Glide.with(mContext).load(f).crossFade(0).into(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
        target.setVisibility(View.VISIBLE);
    }

    public static void loadImage(Context mContext, String path, ImageView target, ImageView target1) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        target.setTag(null);
        target1.setTag(null);

        try {
            DrawableTypeRequest<String> r = Glide.with(mContext).load(path);
            r.crossFade(0);
            r.into(target);
            r.into(target1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageWithSize(Context mContext, String path, ImageView target, int width, int height) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        target.setTag(null);
        try {
            DrawableTypeRequest<String> r = Glide.with(mContext).load(path);
            r.crossFade(0);
            r.override(width, height).into(target);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageWithSize(final Context mContext, final String path, String smallPath, final ImageView target, final int width, final int height) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        target.setTag(null);
        try {
            DrawableTypeRequest<String> r = Glide.with(mContext).load(smallPath);
            r.crossFade(0);
            if (width != 0 && height != 0) {
                r.override(width, height);
            }
            r.into(new GlideDrawableImageViewTarget(target) {

                @SuppressLint("NewApi")
                @Override
                public void onResourceReady(final GlideDrawable arg0, GlideAnimation<? super GlideDrawable> arg1) {
                    super.onResourceReady(arg0, arg1);
//                    target.setBackground(null);
                    DrawableTypeRequest<String> r2 = Glide.with(mContext).load(path);
                    r2.crossFade(800);
                    if (width != 0 && height != 0) {
                        r2.override(width, height);
                    }
                    r2.placeholder(arg0).into(target);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImage(final Context mContext, final String path, String smallPath, final ImageView target) {
        if (TextUtils.isEmpty(smallPath)) {
            return;
        }
        target.setTag(null);
        try {
            Glide.with(mContext).load(smallPath).crossFade(0).into(new GlideDrawableImageViewTarget(target) {

                @Override
                public void onResourceReady(GlideDrawable arg0, GlideAnimation<? super GlideDrawable> arg1) {
                    super.onResourceReady(arg0, arg1);
                    loadImage(mContext, path, target);
                }
            });
            target.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageInitColor(final Context mContext, final String color, final String path,
                                          String smallPath, final ImageView target, final ImageView mapTarget, boolean... isLoadAll) {
        boolean loadAll = isLoadAll.length > 0 && isLoadAll[0];
        if (TextUtils.isEmpty(smallPath)) {
            return;
        }
        // int red = 255;
        // int green = 255;
        // int blue = 255;
        if ((!TextUtils.isEmpty(color)) && color.length() == 6) {
            // red = Integer.valueOf(color.subSequence(0, 2).toString(), 16);
            // green = Integer.valueOf(color.subSequence(2, 4).toString(), 16);
            // blue = Integer.valueOf(color.subSequence(4, 6).toString(), 16);
            target.setBackgroundColor(Color.parseColor("#" + color));
            mapTarget.setBackgroundColor(Color.parseColor("#" + color));
            mapTarget.setImageBitmap(null);
            target.setImageBitmap(null);
        }
        // target.setTag(smallPath);
        try {
            DrawableTypeRequest<String> r = Glide.with(mContext).load(path);
            if (loadAll) {
                r.diskCacheStrategy(DiskCacheStrategy.ALL);

            }
            Logger.e("color", "color" + color);
            r.into(mapTarget);
            r.crossFade(800).into(new GlideDrawableImageViewTarget(target) {

                @SuppressLint("NewApi")
                @Override
                public void onResourceReady(GlideDrawable arg0, GlideAnimation<? super GlideDrawable> arg1) {
                    super.onResourceReady(arg0, arg1);
                    target.setBackground(null);
                    mapTarget.setBackground(null);
                    // loadImage(mContext, path, target,mapTarget);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        target.setVisibility(View.VISIBLE);
        mapTarget.setVisibility(View.VISIBLE);
    }

    public static void loadImageInitColor(final Context mContext, final String color, final String path,
                                          String smallPath, final ImageView target, boolean... isLoadAll) {
        boolean loadAll = isLoadAll.length > 0 && isLoadAll[0];
        if (TextUtils.isEmpty(smallPath)) {
            return;
        }
        if ((!TextUtils.isEmpty(color)) && color.length() == 6) {
            target.setBackgroundColor(Color.parseColor("#" + color));
            target.setImageBitmap(null);
        }
        // target.setTag(smallPath);
        try {
            DrawableTypeRequest<String> r = Glide.with(mContext).load(smallPath);
            if (loadAll) {
                r.diskCacheStrategy(DiskCacheStrategy.ALL);
            }
            Logger.e("color", "color" + color);
            r.crossFade(0).into(new GlideDrawableImageViewTarget(target) {

                @SuppressLint("NewApi")
                @Override
                public void onResourceReady(final GlideDrawable arg0, GlideAnimation<? super GlideDrawable> arg1) {
                    super.onResourceReady(arg0, arg1);
//                    target.setBackground(null);
                    try {
                        Glide.with(mContext).load(path).placeholder(arg0).crossFade(800).into(target);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            target.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageInitColor(final Context mContext, final String color, final String path,
                                          String smallPath, final ImageView target, final int width, final int height, boolean... isLoadAll) {
        boolean loadAll = isLoadAll.length > 0 && isLoadAll[0];
        if (TextUtils.isEmpty(smallPath)) {
            return;
        }
        if ((!TextUtils.isEmpty(color)) && color.length() == 6) {
            target.setBackgroundColor(Color.parseColor("#" + color));
            target.setImageBitmap(null);
        }
        // target.setTag(smallPath);
        try {
            DrawableTypeRequest<String> r = Glide.with(mContext).load(smallPath);
            if (loadAll) {
                r.diskCacheStrategy(DiskCacheStrategy.ALL);
            }
            Logger.e("color", "color" + color);
            r.crossFade(0).override(width, height).into(new GlideDrawableImageViewTarget(target) {

                @SuppressLint("NewApi")
                @Override
                public void onResourceReady(final GlideDrawable arg0, GlideAnimation<? super GlideDrawable> arg1) {
                    super.onResourceReady(arg0, arg1);
//                    target.setBackground(null);
                    try {
                        Glide.with(mContext).load(path).placeholder(arg0).crossFade(800).override(width, height).into(target);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            target.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageInitColor(final Context mContext, final String color, final String path,
                                          final ImageView target, boolean... isLoadAll) {
        boolean loadAll = isLoadAll.length > 0 && isLoadAll[0];
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if ((!TextUtils.isEmpty(color)) && color.length() == 6) {
            target.setBackgroundColor(Color.parseColor("#" + color));
            target.setImageBitmap(null);
        }
        // target.setTag(smallPath);
        try {
            DrawableTypeRequest<String> r = Glide.with(mContext).load(path);
            if (loadAll) {
                r.diskCacheStrategy(DiskCacheStrategy.ALL);
            }
            Logger.e("color", "color" + color);
            r.crossFade(0).into(target);
            target.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadImageInitColor(final Context mContext, final String color, final String path,
                                          final ImageView target, int width, int height, boolean... isLoadAll) {
        boolean loadAll = isLoadAll.length > 0 && isLoadAll[0];
        if (TextUtils.isEmpty(path)) {
            return;
        }
        if ((!TextUtils.isEmpty(color)) && color.length() == 6) {
            target.setBackgroundColor(Color.parseColor("#" + color));
            target.setImageBitmap(null);
        }
        // target.setTag(smallPath);
        try {
            DrawableTypeRequest<String> r = Glide.with(mContext).load(path);
            if (loadAll) {
                r.diskCacheStrategy(DiskCacheStrategy.ALL);
            }
            Logger.e("color", "color" + color);
            r.crossFade(0).override(width, height).into(target);
            target.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static Bitmap getBitmap(final Context mContext, final OnGlideDownLoadBitmapListener onGlideDownLoadBitmapListener,
                                   final String path, int width, int height) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        try {
            DrawableTypeRequest<String> r = Glide.with(mContext).load(path);
//          try {
          if (width == 0 || height == 0) {
              r.asBitmap().centerCrop().into(new SimpleTarget() {

                  @Override
                  public void onResourceReady(Object bitmap, GlideAnimation arg1) {
                      if (onGlideDownLoadBitmapListener != null) {
                          onGlideDownLoadBitmapListener.onDownloadFinish((Bitmap) bitmap);
                      }
                  }
              });
          } else {
              r.asBitmap().centerCrop().into(new SimpleTarget(width, height) {
                  
                  @Override
                  public void onResourceReady(Object bitmap, GlideAnimation arg1) {
                      if (onGlideDownLoadBitmapListener != null) {
                          onGlideDownLoadBitmapListener.onDownloadFinish((Bitmap) bitmap);
                      }
                  }
              });
          }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public interface OnGlideDownLoadBitmapListener {
        public void onDownloadFinish(Bitmap bitmap);
    }

    public static void loadImageWithListener(Context mContext, String path, ImageView target, final Callback callBack) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        try {
            Glide.with(mContext).load(path).crossFade(0).into(new GlideDrawableImageViewTarget(target) {

                @Override
                public void onResourceReady(GlideDrawable drawable, GlideAnimation<? super GlideDrawable> arg1) {
                    super.onResourceReady(drawable, arg1);
                    int w = drawable.getIntrinsicWidth();
                    int h = drawable.getIntrinsicHeight();
                    Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                            : Config.RGB_565;
                    Bitmap bitmap = Bitmap.createBitmap(w, h, config);
                    // 注意，下面三行代码要用到，否在在View或者surfaceview里的canvas.drawBitmap会看不到图
                    Canvas canvas = new Canvas(bitmap);
                    drawable.setBounds(0, 0, w, h);
                    drawable.draw(canvas);
                    if (callBack != null) {
                        callBack.onResourceReady(bitmap, drawable);
                        if (bitmap != null)
                            bitmap.recycle();
                    }
                    bitmap.recycle();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        target.setVisibility(View.VISIBLE);
    }

    public static void loadImageWithListener(Context mContext, String path, ImageView target) {
        if (TextUtils.isEmpty(path)) {
            return;
        }
        try {
            Glide.with(mContext).load(path).crossFade(0).into(new GlideDrawableImageViewTarget(target) {

                @Override
                public void onResourceReady(GlideDrawable drawable, GlideAnimation<? super GlideDrawable> arg1) {
                    super.onResourceReady(drawable, arg1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        target.setVisibility(View.VISIBLE);
    }

    public interface Callback {
        public void onResourceReady(Bitmap bitmap, GlideDrawable drawable);
    }

    /**
     * 保存图片到本地
     * 
     * @param bitmap 图片对象
     * @param picName 图片文件名
     */
    public static String saveBitmap(Bitmap bitmap) {
        File file = new File(DataPath.getDirectory(DataPath.DATA_PATH));
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(DataPath.getDirectory(DataPath.DATA_PATH_PIC));
        if (!file1.exists()) {
            file1.mkdirs();
        }
        File f = new File(DataPath.getDirectory(DataPath.DATA_PATH_PIC),
                System.currentTimeMillis() + ".png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(CrashApplication.APP, "保存成功", Toast.LENGTH_SHORT).show();
            return f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            Toast.makeText(CrashApplication.APP, "保存失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(CrashApplication.APP, "保存失败", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return "";
    }

    // 根据图片size计算imageview's size
    public static int[] calcImageViewSize(float imgW, float imgH, float maxW, float maxH, float minW, float minH) {

        float f = 0.0f;
        float viewW, viewH;
        int[] res = new int[2];

        if (imgW >= imgH) {
            if (imgW >= maxW) {
                viewW = maxW;
                f = maxW / imgW;
                viewH = imgH * f;
            } else {
                viewW = imgW;
                viewH = imgH;
            }
        } else {
            if (imgH >= maxH) {
                viewH = maxH;
                f = maxH / imgH;
                viewW = imgW * f;
            } else {
                viewW = imgW;
                viewH = imgH;
            }
        }

        if (viewW >= viewH) {
            if (viewW < minW) {
                f = minW / viewW;
                viewW *= f;
                viewH *= f;
            }
        } else {
            if (viewH < minH) {
                f = minH / viewH;
                viewW *= f;
                viewH *= f;
            }
        }

        res[0] = (int) viewW;
        res[1] = (int) viewH;
        return res;
    }

    // 根据图片size和ImageView max/min size设置ImageWiew width/height
    public static void setImageViewSize(ImageView iv, int imgW, int imgH, float maxW, float maxH, float minW, float minH) {
        int[] ivSize = calcImageViewSize(imgW, imgH, maxW, maxH, minW, minH);

        // Logger.e("wwwwwwwwwwwwwwwwwwwwwww", " ivSize width=" + ivSize[0]//
        // + " height=" + ivSize[1]//
        // );

        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        lp.width = ivSize[0];
        lp.height = ivSize[1];
        iv.setLayoutParams(lp);
    }

    // 根据原图size计算缩略图size
    public static int[] calcThumbImageSize(final float max, float imgW, float imgH) {
        float i = -1f;
        int[] res = new int[2];

        if (imgW > imgH && imgW > max) {
            i = imgW;
        } else if (imgH > imgW && imgH > max) {
            i = imgH;
        }

        if (i > 0f) {
            float f = max / i;

            res[0] = (int) (imgW * f);
            res[1] = (int) (imgH * f);
        } else {
            res[0] = (int) imgW;
            res[1] = (int) imgH;
        }

        return res;
    }
    
    /** 
     *  处理图片  
     * @param bm 所要转换的bitmap 
     * @param newWidth 新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap 
     */ 
     public static Bitmap zoomImg(Bitmap bm, int newWidth , int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片   www.2cto.com
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
    
    public static File image2jpg(File file) {
        File newFile = null;
        // 文件转成byte
        byte[] buffer = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bb = new byte[1024];
            int n;
            while ((n = fis.read(bb)) != -1) {
                baos.write(bb, 0, n);
            }
            fis.close();
            baos.close();
            buffer = baos.toByteArray();
            // byte转成PNG再转成File
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);
            newFile = saveBitmap(bitmap, "new_" + file.getName());
        } catch (Exception e1) {
            e1.printStackTrace();
            return file;
        }
        return newFile;
    }

    // 第二 1)：图片按比例大小压缩方法（根据路径获取图片并压缩）：
    public static Bitmap getImageNoCompress(String srcPath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = true;// 开始读入图片，此时把options.inJustDecodeBounds 设回true了

        BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bitmap为null

        newOpts.inJustDecodeBounds = false;

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        float myH = AppInfo.getScreenWidthOrHeight(CrashApplication.APP, true);
        float myW = AppInfo.getScreenWidthOrHeight(CrashApplication.APP, false);

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放

        if (w > h && w > myW) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / myW);
        } else if (w < h && h > myH) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / myH);
        }

        if (be <= 0)
            be = 1;

        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        return BitmapFactory.decodeFile(srcPath, newOpts);
    }

    // 第一：我们先看下质量压缩方法：
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    // 第二 1)：图片按比例大小压缩方法（根据路径获取图片并压缩）：
    public static Bitmap getimage(String srcPath) {

        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = true;// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bitmap为null

        newOpts.inJustDecodeBounds = false;

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float myH = 800f;// 这里设置高度为800f
        float myW = 480f;// 这里设置宽度为480f

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放

        if (w > h && w > myW) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / myW);
        } else if (w < h && h > myH) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / myH);
        }

        if (be <= 0)
            be = 1;

        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    // 第二 2)：图片按比例大小压缩方法（根据路径获取图片并压缩）：
    public static Bitmap getImage(String srcPath, int width) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = true;// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bitmap为null

        newOpts.inJustDecodeBounds = false;

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放

        be = (int) (newOpts.outWidth / width);

        if (be <= 0)
            be = 1;

        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 根据图片宽高进行判定然后压缩图片的宽高（不压缩质量）
     * 
     * @param file
     * @return
     */
    public static File getImage(File file) {
        String srcPath = file.getAbsolutePath();
        BitmapFactory.Options newOpts = new BitmapFactory.Options();

        newOpts.inJustDecodeBounds = true;// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bitmap为null

        newOpts.inJustDecodeBounds = false;

        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        // 设置宽高的缩小大小终值
        float targetSize = 512f;

        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放

        if (w > h && h > targetSize) {// 如果宽度大的话根据高度固定大小缩放
            be = (int) (newOpts.outWidth / targetSize);
        } else if (w < h && w > targetSize) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / targetSize);
        }

        if (be <= 0)
            be = 1;

        if (be == 1) {
            return file;
        }
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return saveBitmap(bitmap, System.currentTimeMillis() + ".jpg");// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 判断图片是否过大（大于1M）
     */
    public static boolean imageIsTooBig(File file) {
        try {
            return new FileInputStream(file).available() > 1024 * 1024;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static File getComressFilePath(String srcPath, int width) {
        String fileName = srcPath.substring(srcPath.lastIndexOf("/") + 1);
        Bitmap b = getImage(srcPath, width);
        File f = saveBitmap(b, fileName);
        return f;
    }

    // 第三：图片按比例大小压缩方法（根据Bitmap图片压缩）：
    public Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        // 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;// 这里设置高度为800f
        float ww = 480f;// 这里设置宽度为480f
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放
        if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
    }

    public static File saveBitmap(Bitmap bm, String filename) {
        // File f = new File(EaseConstant.APP.getCacheDir().toString(), filename);
        File f = new File(DataPath.getDirectory(DataPath.DATA_PATH_PIC) + filename);

        if (f.exists()) {
            f.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    public static File saveBitmapToCache(Bitmap bm, String filename) {
        File f = new File(DataPath.DATA_PATH_CACHE, filename);

        if (f.exists()) {
            return f;
        }

        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    public static File saveBitmapAbsolutePath(Bitmap bm, String filename) {
        File f = new File(filename);
        if (bm == null)
            return f;

        if (f.exists()) {
            f.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return f;
    }

    @SuppressLint("SimpleDateFormat")
    public static File savePicture(Bitmap bm) {
        File fileFolder = new File(DataPath.getDirectory(DataPath.DATA_PATH_CAMERA));
        if (!fileFolder.exists()) {
            fileFolder.mkdir();
        }

        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String filename = format.format(date) + ".jpg";

        File jpgFile = new File(fileFolder.getPath(), filename);

        if (jpgFile.exists()) {
            jpgFile.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(jpgFile);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jpgFile;
    }

    static public File getVideoThumbnail(String filePath, String tofile) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.GINGERBREAD_MR1)
            return new File(filePath);
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = null;
        retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }

        File f = saveBitmapAbsolutePath(bitmap, tofile);
        if (bitmap != null)
            bitmap.recycle();
        return f;

    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static boolean fileExists(String filename) {
        File file = new File(filename);
        return file.exists();
    }
    
    public static boolean imageIsTooLong(Context context, int width, int height) {
        return width > AppInfo.getScreenWidthOrHeight(context, true) * 3
                || height > AppInfo.getScreenWidthOrHeight(context, false) * 3
                || (width != 0 && height != 0 && (width > height ? width / height : height / width) > 3);
    }
}
