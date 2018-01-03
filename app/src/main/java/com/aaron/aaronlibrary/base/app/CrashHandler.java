package com.aaron.aaronlibrary.base.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import com.aaron.aaronlibrary.base.utils.DataPath;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>类名称: CrashHandler</p>
 * <p>类描述: UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.</p>
 * <p>所属模块: utils</p>
 * <p>创建时间: 15-3-12  11:20 </p> 
 * <p>作者: 王鹏程 </p>
 * <p>版本: 1.0 </p>
 */
public class CrashHandler implements UncaughtExceptionHandler {

    /**
     * <p>变量：系统默认的UncaughtException处理类</p>
     */
    private UncaughtExceptionHandler mDefaultHandler;

    /**
     * <p>变量：CrashHandler实例</p>
     */
    private static CrashHandler INSTANCE = new CrashHandler();

    /**
     * <p>变量：程序的Context对象</p>
     */
    private CrashApplication application;

    /**
     * <p>变量：崩溃后的Log内容</p>
     */
    private String content;

    /**
     * <p>变量：用来存储设备信息和异常信息</p>
     */
    private Map<String, String> info = new HashMap<String, String>();

    /**
     * <p>变量：用于格式化日期,作为日志文件名的一部分</p>
     */
    private SimpleDateFormat format;

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {

    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /**
     * <p>方法描述: 初始化</p>
     * @param application 应用基类
     */
    @SuppressLint("SimpleDateFormat")
    public void init(CrashApplication application) {
        this.application = application;
        format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
    }

    /**
     * <p>方法描述: 当UncaughtException发生时会转入该重写的方法来处理 </p>
     * @param thread 线程
     * @param ex 异常信息
     */
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义的没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(2000);// 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//             退出程序
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
            // 重启程序 TODO
//            Intent intent = new Intent(application.getApplicationContext(), GuideActivity.class);
//            PendingIntent restartIntent = PendingIntent.getActivity(
//                    application.getApplicationContext(), 0, intent,
//                    Intent.FLAG_ACTIVITY_NEW_TASK);
//            AlarmManager mgr = (AlarmManager)application.getSystemService(Context.ALARM_SERVICE);
//            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
//                    restartIntent); // 1秒钟后重启应用
//            HermesActivity.popAllActivity();
        }
    }

    /**
     * <p>方法描述: 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.</p>
     * @param ex 异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    public boolean handleException(Throwable ex) {
        if (ex == null)
            return false;
        new Thread() {
            public void run() {
                Looper.prepare();
//                Toast.makeText(application, application.getString(R.string.real_camera), Toast.LENGTH_SHORT).show();
                
                Looper.loop();
            }
        }.start();
        // 收集设备参数信息
        collectDeviceInfo(application);
        // 保存日志文件
        saveCrashInfo2File(ex);
        return true;
    }
  
    /**
     * <p>方法描述: 收集设备参数信息 </p>
     * @param context 上下文对象
     */
    public void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();// 获得包管理器
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
            if (pi != null) {
                String versionName = pi.versionName == null ? "null"
                        : pi.versionName;
                String versionCode = pi.versionCode + "";
                info.put("versionName", versionName);
                info.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();// 反射机制
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                info.put(field.getName(), field.get("").toString());
//                LogUtil.d(TAG, field.getName() + ":" + field.get(""));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    
    /** 
     * <p>方法描述: 保存日志 </p>  
     */  
    private String saveCrashInfo2File(Throwable ex) {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : info.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            content += key + "=" + value + "\r\n";
            sb.append(key + "=" + value + "\r\n");
        }
        StringWriter writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();// 记得关闭
        String result = writer.toString();
        sb.append(result);
        content += result;
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                connectNetwork(content);
            }
        }).start();
        // 保存文件
        long timetamp = System.currentTimeMillis();
        String time = format.format(new Date());
        String fileName = "crash-" + time + "-" + timetamp + ".log";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            try {
                File dir = new File(DataPath.getDirectory(DataPath.DATA_PATH_CRASH));
//                LogUtil.e("CrashHandler", dir.toString());
                if (!dir.exists())
                    dir.mkdir();
                FileOutputStream fos = new FileOutputStream(new File(dir,
                         fileName));
                fos.write(sb.toString().getBytes());
                fos.close();
                return fileName;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * <p>方法描述: 网络请求</p>
     * @param requestString 请求的字段内容
     */
    private String connectNetwork(String requestString) {
        String urlPath = "http://59.44.43.234:18126/cgi-bin/logrecord"; // 外网地址
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(3000); // 超时
            connection.setRequestMethod("POST"); // 请求方式
            connection.setDoInput(true); // 可读写
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",  
                    "application/x-www-form-urlencoded; charset=utf-8");
            byte[] sendData = postData("HermesLog", requestString).getBytes("UTF-8");
            connection.setRequestProperty("Content-Length", sendData.length + "");
            
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(sendData);
            
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = "";
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                str += (temp + "\n");
            }
//            LogUtil.d(application.getClass().getSimpleName(), str);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * <p>方法描述: 拼接请求字段</p>
     * @param logname log文件名
     * @param content log内容
     * @return 请求字段
     */
    private String postData(String logname, String content) {
        return "log_name=" + logname + "&log=" + content;
    }
}