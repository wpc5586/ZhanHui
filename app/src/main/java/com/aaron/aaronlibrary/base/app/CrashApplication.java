package com.aaron.aaronlibrary.base.app;

import android.app.Application;

import com.aaron.aaronlibrary.db.PreferenceManager;
import com.aaron.aaronlibrary.utils.Constants;
import com.mob.MobApplication;

/**
 * <p>类名称: CrashApplication</p>
 * <p>类描述: 应用基类</p>
 * <p>所属模块: domain</p>
 * <p>创建时间: 15-5-12  9:18 </p> 
 * <p>作者: 王鹏程 </p>
 * <p>版本: 1.0 </p>
 */
public class CrashApplication extends MobApplication {
    
    public static Application APP;
    
    @Override
    public void onCreate() {
        APP = this;
        super.onCreate();
        // 初始化PreferenceManager
        PreferenceManager.init(APP);
        //应用出现异常后自动重新启动
        if (!Constants.DEBUGABLE) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
        }
    }
}
