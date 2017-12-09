package com.aaron.aaronlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;

/**
 * 系统工具（如拨打电话，调用导航等功能）
 * 
 * @author wangpc
 */
public class SystemUtils {
    
    public static void toNavi(Context mContext) {
        
    }
    
    /**
     * 拨打电话
     * @param mContext 上下文
     * @param number 电话号码
     */
    public static void toCall(Context mContext, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        mContext.startActivity(intent);
    }

    /**
     * 根据图库图片uri获得图片文件
     * @param selectedImage 选择的图片uri
     * @return File 图片文件
     */
    public static File getPicByUri(Context mContext, Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(mContext, "不能找到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return null;
            }
            return new File(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(mContext, "不能找到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return null;

            }
            return new File(file.getAbsolutePath());
        }
    }

    /**
     * 根据图库图片path获得图片文件
     * @param filePath 选择的图片path
     * @return File 图片文件
     */
    public static File getPicByPath(Context mContext, String filePath) {
        File f = new File(filePath);
        if (!f.exists()) {
            return null;
        }
        Uri selectedImage = Uri.fromFile(f);
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = mContext.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(mContext, "不能找到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return null;
            }
            return new File(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(mContext, "不能找到图片", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return null;

            }
            return new File(file.getAbsolutePath());
        }
    }
}
