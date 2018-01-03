package com.aaron.aaronlibrary.photoutil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

import com.aaron.aaronlibrary.utils.SystemUtils;

import java.io.File;
import java.io.Serializable;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class ImageItem implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7368913492697140623L;
    public String imageId;
    public String thumbnailPath;
    public String imagePath; // 文件地址（图片、视频）
    private long videoDuration; // 视频时间
//    private String bitmapPath; // 视频预览图地址
    private int width; // 图片宽度
    private int height; // 图片高度
    private float videoSize; // 视频大小（单位：M）
    private boolean isPic = true; // 是否是图片
    public boolean isSelected = false;
    private int addTime; // 图片创建时间

    public static ImageItem getImageItem4Pic(String filename) {
        ImageItem imageItem = new ImageItem();
        imageItem.setPic(true);
        imageItem.setImagePath(filename);

        return imageItem;
    }

    public static ImageItem getImageItem4Video(String videoFilename, String thumbFilename, int videoDuration) {
        ImageItem imageItem = new ImageItem();
        imageItem.setPic(false);
        imageItem.setImagePath(videoFilename);
        imageItem.setVideoDuration(videoDuration);
        imageItem.setBitmapPath(thumbFilename);

        return imageItem;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
    
    /**
     * 判断是否图片过大(高宽有超过4096像素的)
     */
    public boolean isTooBig() {
        int limit = 4096;
        return height >= limit || width >= limit;
    }

    @SuppressLint("SimpleDateFormat")
    public String getVideoDuration() {
        Date date = new Date(videoDuration);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return sdf.format(date);
    }

    public int getAddTime() {
        return addTime;
    }

    public void setAddTime(int addTime) {
        this.addTime = addTime;
    }

    public int getVideoDurationInt() {
        return (int) videoDuration;
    }

    public void setVideoDuration(long videoDuration) {
        this.videoDuration = videoDuration;
    }

    public String getBitmapPath() {
        return thumbnailPath;
    }

    public void setBitmapPath(String bitmapPath) {
        this.thumbnailPath = bitmapPath;
    }

    public boolean isPic() {
        return isPic;
    }

    public void setPic(boolean isPic) {
        this.isPic = isPic;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public float getVideoSize() {
        return videoSize;
    }
    
    public String getVideoSizeString() {
        return new java.text.DecimalFormat("#0.00").format(videoSize);
    }

    public void setVideoSize(float videoSize) {
        this.videoSize = videoSize;
    }
    
    /**
     * 获取文件对象
     * @param mContext 上下文
     * @return File
     */
    public File getFile(Context mContext) {
        return SystemUtils.getPicByPath(mContext, getImagePath());
    }

    /**
     * 获取视频的预览图Bitmap对象
     * @return Bitmap
     */
    public Bitmap getBitmap() {
        return BitmapFactory.decodeFile(thumbnailPath);
    }
    
    /**
     * 获取图片的Bitmap对象
     * @param inSampleSize 压缩比例
     * @return Bitmap
     */
    public Bitmap getImageBitmap(int inSampleSize) {
        return BitmapFactory.decodeFile(imagePath, getBitmapOption(inSampleSize));
    }
    
    private Options getBitmapOption(int inSampleSize){
        System.gc();
        Options options = new Options();
        options.inPurgeable = true;
        options.inSampleSize = inSampleSize;
        return options;
    }
}
