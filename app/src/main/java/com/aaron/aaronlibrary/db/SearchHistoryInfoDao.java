package com.aaron.aaronlibrary.db;

import com.aaron.aaronlibrary.db.bean.SearchHistoryInfo;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;

public class SearchHistoryInfoDao {

    private Dao<SearchHistoryInfo, Long> dao;

    static private SearchHistoryInfoDao _dao;

    static public SearchHistoryInfoDao getSingleInstance() {
        if (_dao == null)
            _dao = new SearchHistoryInfoDao();
        return _dao;
    }

    @SuppressWarnings("unchecked")
    public SearchHistoryInfoDao() {
        try {
            dao = DBHelper.getHelper().getDao(SearchHistoryInfo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isExist(String content) {
        return getByContent(content) != null ? true : false;
    }

    public long countOf() {
        try {
            return dao.countOf();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<SearchHistoryInfo> queryForAll() {
        try {
            return dao.queryBuilder().orderBy("search_id", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void add(Long searchId, String content) {
        SearchHistoryInfo searchInfo = new SearchHistoryInfo(searchId, content);
        SearchHistoryInfoDao.getSingleInstance().updateOrCreate(searchInfo);
    }

    public void updateOrCreate(SearchHistoryInfo searchInfo) {
        try {
            if (getByContent(searchInfo.getContent()) != null) {
                update(searchInfo);
            } else {
                add(searchInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(SearchHistoryInfo searchInfo) {
        try {
            dao.create(searchInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(SearchHistoryInfo searchInfo) {
        try {
            dao.update(searchInfo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(SearchHistoryInfo searchInfo) {
        try {
            dao.delete(searchInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteAll(List<SearchHistoryInfo> searchInfos) {
        try {
            dao.delete(searchInfos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SearchHistoryInfo getById(long id) {
        try {
            return dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SearchHistoryInfo getByContent(String content) {
        try {
            return dao.queryForFirst(dao.queryBuilder().where().eq("content", content).prepare());
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
