/**
 * 
 */
package com.aaron.aaronlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.text.Spanned;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastUtil {

	public static void show(Context context, String info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

	public static void show(Context context, int info) {
		Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
	}

    public static void showLong(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }

    public static void showLong(Context context, int info) {
        Toast.makeText(context, info, Toast.LENGTH_LONG).show();
    }
    
    public static void  SetToast(Context context, int imageId, Spanned fromHtml, int duration) {
        Toast toast;
        LayoutInflater inflater = null;
        if (context instanceof Activity)
            inflater = ((Activity) context).getLayoutInflater();
        else
            inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(com.xhy.zhanhui.R.layout.toast_account_set,
          (ViewGroup) ((Activity) context).findViewById(com.xhy.zhanhui.R.id.llToast));
        ImageView image = (ImageView) layout
          .findViewById(com.xhy.zhanhui.R.id.tvImageToast);
        image.setBackgroundResource(imageId);
        TextView text = (TextView) layout.findViewById(com.xhy.zhanhui.R.id.tvTitleToast);
        text.setText(fromHtml);
        toast= new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
    public static void SetToast(Context context , int imageId , String content , int duration){
        Toast toast;
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        View layout = inflater.inflate(com.xhy.zhanhui.R.layout.toast_account_set,
          (ViewGroup) ((Activity) context).findViewById(com.xhy.zhanhui.R.id.llToast));
        ImageView image = (ImageView) layout
          .findViewById(com.xhy.zhanhui.R.id.tvImageToast);
        image.setBackgroundResource(imageId);
        TextView text = (TextView) layout.findViewById(com.xhy.zhanhui.R.id.tvTitleToast);
        text.setText(content);
        toast= new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }
    /**
     * 哭脸吐司
     * @param context 上下文
     * @param content 内容
     */
    public static void setErrorToast(Context context, String content){
       SetToast(context , com.xhy.zhanhui.R.mipmap.common_face_sad ,content , 1000);
    }
    /**
     * 笑脸吐司
     * @param context 上下文
     * @param content 文字
     */
    public static void setOKToast(Context context, String content){
        SetToast(context , com.xhy.zhanhui.R.mipmap.common_face_ok ,content , 1000);
    }
    /**
     * 哭脸吐司
     * @param context 上下文
     * @param content 内容
     */
    public static void setErrorToast(Context context, Spanned content){
       SetToast(context , com.xhy.zhanhui.R.mipmap.common_face_sad ,content , 1000);
    }
    /**
     * 笑脸吐司
     * @param context 上下文
     * @param content 文字
     */
    public static void setOKToast(Context context, Spanned content){
        SetToast(context , com.xhy.zhanhui.R.mipmap.common_face_ok ,content , 1000);
    }
}
