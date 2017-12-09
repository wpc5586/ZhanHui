package com.aaron.aaronlibrary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.aaron.aaronlibrary.base.app.CrashApplication;
import com.aaron.aaronlibrary.db.bean.DownloadInfo;
import com.aaron.aaronlibrary.db.bean.SearchHistoryInfo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DBHelper extends OrmLiteSqliteOpenHelper {
    private static final String DB_NAME = "sys.db";

    private static Map<String, DBHelper> instance_map;

    public static OnDbNameListener onDbNameListener;

    @SuppressWarnings("rawtypes")
    private Map<String, Dao> daos = new HashMap<String, Dao>();

    public DBHelper(Context context, String username) {
        super(context, username + "_" + (onDbNameListener == null ? DB_NAME : onDbNameListener.getDbName()), null,
                (onDbNameListener == null ? 1 : onDbNameListener.getDbVersion()));
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, DownloadInfo.class);//下载信息
            TableUtils.createTable(connectionSource, SearchHistoryInfo.class);//搜索历史
            if (onDbNameListener != null)
                onDbNameListener.onCreate(connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, DownloadInfo.class, true);
            TableUtils.dropTable(connectionSource, SearchHistoryInfo.class, true);//搜索历史
            if (onDbNameListener != null)
                onDbNameListener.onUpgrade(connectionSource);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static synchronized DBHelper getHelper() {
        if (instance_map == null) {
            instance_map = new HashMap<String, DBHelper>();
        }

        String username = PreferenceManager.getInstance().getCurrentUsername();
        DBHelper h = instance_map.get(username);
        if (h == null) {
            h = new DBHelper(CrashApplication.APP, username);
            instance_map.put(username, h);
        }
        return h;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }

        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }

        return dao;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            @SuppressWarnings("unused")
            Dao dao = daos.get(key);
            dao = null;
        }
    }

    public interface OnDbNameListener {
        String getDbName();
        int getDbVersion();
        void onCreate(ConnectionSource connectionSource);
        void onUpgrade(ConnectionSource connectionSource);
    }
}
