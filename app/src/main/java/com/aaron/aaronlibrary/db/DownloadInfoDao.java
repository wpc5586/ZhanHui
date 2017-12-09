package com.aaron.aaronlibrary.db;

import com.aaron.aaronlibrary.db.bean.DownloadInfo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

public class DownloadInfoDao {

    private Dao<DownloadInfo, Long> dao;

    // static private DownloadInfoDao _dao;

    static public DownloadInfoDao getSingleInstance() {
        // if (_dao == null)
        // _dao = new DownloadInfoDao();
        //
        // return _dao;
        return new DownloadInfoDao();
    }

    @SuppressWarnings("unchecked")
    public DownloadInfoDao() {
        try {
            dao = DBHelper.getHelper().getDao(DownloadInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isExist(String url) {
        return getByPath(url) != null ? true : false;
    }

    public long countOf() {
        try {
            return dao.countOf();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void add(/* Long downloadId, */String path) {
        DownloadInfo downloadInfo = new DownloadInfo(/* downloadId, */path);
        DownloadInfoDao.getSingleInstance().updateOrCreate(downloadInfo);
    }

    public void updateOrCreate(DownloadInfo downloadInfo) {
        try {
            if (getByPath(downloadInfo.getPath()) != null) {
                update(downloadInfo);
            } else {
                add(downloadInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(DownloadInfo downloadInfo) {
        try {
            dao.create(downloadInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(DownloadInfo downloadInfo) {
        try {
            dao.update(downloadInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(DownloadInfo downloadInfo) {
        try {
            dao.delete(downloadInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DownloadInfo getById(long id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DownloadInfo getByPath(String path) {
        try {
            return dao.queryForFirst(dao.queryBuilder().where().eq("path", path).prepare());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clean() {
        try {
            dao.delete(dao.deleteBuilder().prepare());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
