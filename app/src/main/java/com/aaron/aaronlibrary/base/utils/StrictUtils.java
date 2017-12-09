package com.aaron.aaronlibrary.base.utils;

import android.os.StrictMode;

import com.aaron.aaronlibrary.utils.Constants;

/**
 * 严苛模式工具类
 * @author wangpc
 *
 */
public class StrictUtils {

    /**
     * 开启线程严苛模式
     */
    public static void startThread() {
        if (Constants.DEBUGABLE) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
            .detectDiskReads()
            .detectDiskWrites()
            .detectNetwork()
            .penaltyLog()
            .build());
        }
    }

    /**
     * 开启线程严苛模式
     */
    public static void startVm() {
        if (Constants.DEBUGABLE) {
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());
        }
    }
}
