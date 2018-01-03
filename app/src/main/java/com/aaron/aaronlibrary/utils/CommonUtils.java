package com.aaron.aaronlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用工具类
 * 
 * @author guoym
 * 
 */
public class CommonUtils {

    /**
     * <p>
     * 方法描述: 从drawable中获取Bitmap
     * </p>
     * 
     * @param id
     *            资源文件
     * @param inSampleSize
     *            缩放大小
     */
    public static Bitmap getBitmapFromDrawable(Context mContext, int id, int inSampleSize) {
        InputStream is = mContext.getResources().openRawResource(id);
        Options options = new Options();
        options.inTempStorage = new byte[100 * 1024];
        options.inPurgeable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = inSampleSize;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(is, null, options);
    }
//
//    /**
//     * 方法描述: 跳转相册选择
//     *
//     * @param mContext
//     *            上下文
//     * @param type
//     *            类型（example:AlbumActivity.TYPE_PIC）
//     * @param num
//     *            可选择数量
//     * @param onAlbumNextListener
//     *            相册选择下一步回调接口
//     * @param isClean
//     *            是否清理相册已选择的图片缓存
//     * @param entry
//     */
//    public static void startAlbum(Context mContext, int type, int num, OnAlbumNextListener onAlbumNextListener,
//            boolean isClean, int... entry) {
//        if (isClean)
//            Bimp.tempSelectBitmap.clear();
//        AlbumActivity.onAlbumNextListener = onAlbumNextListener;
//        Intent intent = new Intent(mContext, AlbumActivity.class);
//        intent.putExtra(Constants.INTENT_ALL_TO_ALBUM_TYPE, type);
//        intent.putExtra(Constants.INTENT_ALL_TO_ALBUM_NUM, num);
//        if (entry != null && entry.length > 0) {
//            intent.putExtra(Constants.INTENT_ALL_TO_ALBUM_ENTRY, entry[0]);
//        }
//        ((Activity) mContext).startActivityForResult(intent, Constants.REQUEST_ALL_TO_ALBUM);
//        ((Activity) mContext).overridePendingTransition(com.aaron.aaronlibrary.R.anim.slide_in_from_bottom, com.aaron.aaronlibrary.R.anim.slide_out_to_bottom);
//    }

    public static List<String> getImageColorsFromPath(List<String> paths) {
        List<String> colors = new ArrayList<String>();
        for (int i = 0; i < paths.size(); i++) {
            Bitmap bitmap = BitmapFactory.decodeFile(paths.get(i), getBitmapOption(16));
            int R = 0, G = 0, B = 0;
            int pixelColor;
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelColor = bitmap.getPixel(x, y);
                    // A += Color.alpha(pixelColor);
                    R += Color.red(pixelColor);
                    G += Color.green(pixelColor);
                    B += Color.blue(pixelColor);
                }
            }
            int total = width * height;
            String color = "";
            color += Integer.toHexString(R / total) + Integer.toHexString(G / total) + Integer.toHexString(B / total);
            colors.add(color);
        }
        return colors;
    }

    public static List<String> getImageColors(List<Bitmap> bitmaps) {
        List<String> colors = new ArrayList<String>();
        for (int i = 0; i < bitmaps.size(); i++) {
            Bitmap bitmap = bitmaps.get(i);
            int R = 0, G = 0, B = 0;
            int pixelColor;
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelColor = bitmap.getPixel(x, y);
                    // A += Color.alpha(pixelColor);
                    R += Color.red(pixelColor);
                    G += Color.green(pixelColor);
                    B += Color.blue(pixelColor);
                }
            }
            int total = width * height;
            String color = "";
            color += Integer.toHexString(R / total) + Integer.toHexString(G / total) + Integer.toHexString(B / total);
            colors.add(color);
        }
        return colors;
    }

    private static Options getBitmapOption(int inSampleSize) {
        System.gc();
        Options options = new Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }

    public static String getWebMapUrl(String la, String lo) {
        return "http://restapi.amap.com/v3/staticmap?location=" + lo + "," + la
                + "&zoom=16&size=700*700&key=5b349675532bf0ef4726bac763ae82eb&scale=1";
    }

    public static String getWebMapUrl(String la, String lo, int width, int height) {
        return "http://restapi.amap.com/v3/staticmap?location=" + lo + "," + la + "&zoom=14&size=" + width + "*"
                + height + "&key=63b387c3cf5ddd7f15abf1cd011b02ea&scale=1";
    }

    /**
     * 
     * 动态改变View的高宽
     * 
     * @param v
     * @param w单位DP
     * @param h单位DP
     */
    public static void setViewSize(Context context, View view, float width, float height) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (width == LayoutParams.MATCH_PARENT) {
            lp.width = LayoutParams.MATCH_PARENT;
        } else if (width == LayoutParams.WRAP_CONTENT) {
            lp.width = LayoutParams.WRAP_CONTENT;
        } else {
            lp.width = MathUtils.dip2px(context, width);
        }
        if (height == LayoutParams.MATCH_PARENT) {
            lp.height = LayoutParams.MATCH_PARENT;
        } else if (height == LayoutParams.WRAP_CONTENT) {
            lp.height = LayoutParams.WRAP_CONTENT;
        } else {
            lp.height = MathUtils.dip2px(context, height);
        }
        view.setLayoutParams(lp);

    }

    /**
     * 
     * 动态改变View的高宽为包裹内容
     * 
     * @param v
     */
    public static void setViewSizeW(Context context, View view) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        lp.width = LayoutParams.WRAP_CONTENT;
        lp.height = LayoutParams.WRAP_CONTENT;
        view.setLayoutParams(lp);

    }

    /**
     * 
     * 动态改变View的高宽
     * 
     * @param v
     * @param w单位
     *            放大倍数
     * @param h单位
     *            放大倍数
     */
    public static void setViewSizeZoom(Context context, View view, float width, float height) {
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        setViewSizeW(context, view);
        lp.width = (int) (view.getMeasuredWidth() * width);
        lp.height = (int) (view.getMeasuredHeight() * height);
        view.setLayoutParams(lp);

    }

    public static List<String> getImageColors(File file) {
        List<String> colors = new ArrayList<String>();
        String path = file.getAbsolutePath();
        Bitmap bitmap = BitmapFactory.decodeFile(path, getBitmapOption(16));
        if (bitmap == null) {
            colors.add("FFFFFF");
            return colors;
        }
        int R = 0, G = 0, B = 0;
        int pixelColor;
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixelColor = bitmap.getPixel(x, y);
                // A += Color.alpha(pixelColor);
                R += Color.red(pixelColor);
                G += Color.green(pixelColor);
                B += Color.blue(pixelColor);
            }
        }
        int total = width * height;
        String color = "";
        color += ((R / total) < 10 ? "0" + Integer.toHexString(R / total) : Integer.toHexString(R / total))
                + ((G / total) < 10 ? "0" + Integer.toHexString(G / total) : Integer.toHexString(G / total))
                + ((B / total) < 10 ? "0" + Integer.toHexString(B / total) : Integer.toHexString(B / total));
        colors.add(color);
        bitmap.recycle();
        return colors;
    }

}
