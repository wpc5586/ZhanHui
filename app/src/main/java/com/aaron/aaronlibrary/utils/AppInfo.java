package com.aaron.aaronlibrary.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * app相关信息
 * @author wangpc
 *
 */
public class AppInfo {
    
    /**
     * <p>app是否是debug模式</p>
     */
    public static boolean isDebug = true;
    
    public static int statusBarHeight;
    
    public static int getStatusBarHeight(Context mContext) {
        if (statusBarHeight == 0) {
            Rect frame = new Rect();
            ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        return statusBarHeight;
    }
    
    /**
     * <p>app是否是初次启动</p>
     */
    public static boolean isFirstStartup(Context context){
        SharedPreferences settings = context.getSharedPreferences("setting", 0);
        boolean FirstStartup = settings.getBoolean("FirstStartup",true);
        
        if (FirstStartup) {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("FirstStartup",false);
            editor.commit();
            return true;
        } else {
            return false;
        }
    }
    /**
     * <p>方法描述：获取免责界面checkbox是否勾选状态</p>
     */
    
    public static boolean getIsChecked(Context context, CheckBox checkbox){
        SharedPreferences settings = context.getSharedPreferences("setting", 0);
        boolean isChecked = settings.getBoolean("isChecked",false);
        return isChecked;
    }
    /**
     * <p>方法描述：存储免责界面checkbox当前状态</p>
     */
    public static void putShowGuide(Context context, boolean isChecked){
        SharedPreferences settings = context.getSharedPreferences("setting", 0);
        SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("isChecked",isChecked);
            editor.commit();
    }
    
    /**
     * <p> 方法描述: 确认SD卡的可用空间大小是否符合要求 </p>
     * @return true:符合要求  <br> false:不符合要求
     * */
    @SuppressWarnings("deprecation")
    public static boolean checkMemory(long size) {
        if (checkSDAccess()) {// SD卡存在
            File path = Environment.getExternalStorageDirectory();
            StatFs statfs = new StatFs(path.getPath());
            // 获取SDCard上每个block的SIZE
            long blocSize = statfs.getBlockSize();
            // 获取可供程序使用的Block的数量
            long availaBlock = statfs.getAvailableBlocks();
            // 计算 SDCard 剩余大小MB (availaBlock * blocSize / 1024 / 1024)
            long available = availaBlock * blocSize / 1024 / 1024;
            
            if (available > size) {// 可用空间符合要求
                return true;
            } else {// 可用空间不符合要求
                return false;
            }
        }
        return false;
    }
    
    /**
     * <p>方法描述: 确认SD卡是否可以访问 </p>
     * @return true:可以正常访问  <br> false:无法访问
     */
    public static boolean checkSDAccess() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) ? true : false;
    }
    
    /**
     * <p>方法描述: 获取屏幕高宽度</p>
     * @param context 上下文
     * @param isWidth 是否获得宽度
     */
    public static int getScreenWidthOrHeight(Context context, boolean isWidth) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return isWidth ? metrics.widthPixels : metrics.heightPixels;
    }

    /**
     * <p>方法描述: 获取屏幕上方通知栏高度</p>
     */
    public static int getStatusBarHeight() {  
        return Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }
    
    /**
     * <p>方法描述: 获取版本号</p>
     * @param context 上下文
     * @param isName 是Name
     */
    public static String getVersionNameOrCode(Context context, boolean isName) throws NameNotFoundException {
        // 获取packagemanager的实例  
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息  
        PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        return isName ? packInfo.versionName : String.valueOf(packInfo.versionCode);
    }

    /**
     * <p>方法描述: 图片转换（Drawable转Bitmap）</p>
     */
    public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap
                    .createBitmap(
                            drawable.getIntrinsicWidth(),
                            drawable.getIntrinsicHeight(),
                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                    : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }

    /**
     * <p>方法描述: 打开系统相机</p>
     */
    public static void openCamera(Context context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(context.getFilesDir().getAbsolutePath(), Constants.IMAGE_TEMP)));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), Constants.IMAGE_TEMP)));
        System.out.println("~~~ " + Environment.getExternalStorageDirectory());
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_MY_USERIMAGE_CAMERA);
    }

    /**
     * <p>方法描述: 打开系统相册</p>
     */
    public static void openPhoto(Context context) {
//        Intent intent = new Intent(Intent.ACTION_PICK, null);
//        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/jpeg");
//        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_PHOTO);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", true);
        ((Activity) context).startActivityForResult(intent,
                Constants.REQUEST_PHOTO);
    }

    /**
     * <p>方法描述: 开始图片裁剪</p>
     */
    public static void openPhotoZoom(Context context) {
        File temp = new File(Environment.getExternalStorageDirectory() + "/" + Constants.IMAGE_TEMP);
//        File temp = new File(context.getFilesDir().getAbsolutePath()  + Constants.IMAGE_TEMP);
        Uri data = Uri.fromFile(temp);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/jpeg");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 480);
        intent.putExtra("outputY", 480);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_MY_USERIMAGE_CUT);
    }

    /**
     * <p>方法描述: 开始图片裁剪</p>
     */
    public static void openPhotoZoom(Context context, Bitmap bitmap) {
//        File temp = new File(Environment.getExternalStorageDirectory() + "/" + Constants.IMAGE_TEMP);
//        File temp = new File(context.getFilesDir().getAbsolutePath()  + Constants.IMAGE_TEMP);
//        Uri data = Uri.fromFile(temp);
        Uri data = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null,null));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(data, "image/jpeg");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 480);
        intent.putExtra("outputY", 480);
        intent.putExtra("return-data", true);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_MY_USERIMAGE_CUT);
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     * 
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidthOrHeight(activity, true);
        int height = getScreenWidthOrHeight(activity, false);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     * 
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidthOrHeight(activity, true);
        int height = getScreenWidthOrHeight(activity, false);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }

    public static Bitmap convertDrawable2BitmapByCanvas(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap createBitmapByColor(int color, int width, int height) {
        if (width < 0)
            width = 1;

        if (height < 0)
            height = 1;

        int[] pix = new int[width * height];

        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                int index = y * width + x;
                pix[index] = color;

            }

        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bm.setPixels(pix, 0, width, 0, 0, width, height);
        return bm;
    }

    public static boolean isNeedHttps() {
//        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
        return true;
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground(Context mContext) {
        // Returns a list of application processes that are running on the
        // device
        ActivityManager activityManager = (ActivityManager) mContext.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = mContext.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }
}
