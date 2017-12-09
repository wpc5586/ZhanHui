package com.aaron.aaronlibrary.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "search_history_info")
public class SearchHistoryInfo {

    @DatabaseField(columnName = "search_id", generatedId = true)
    private long searchId;

    /**
     * 搜索内容
     */
    @DatabaseField(columnName = "content")
    private String content;


    public SearchHistoryInfo() {
    }

    public SearchHistoryInfo(String content) {
        this.content = content;
    }

    public SearchHistoryInfo(long searchId, String content) {
        this.searchId = searchId;
        this.content = content;
    }

    public long getSearchId() {
        return searchId;
    }

    public void setSearchId(long searchId) {
        this.searchId = searchId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
