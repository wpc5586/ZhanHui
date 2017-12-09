package com.aaron.aaronlibrary.db.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "download_info")
public class DownloadInfo {

//    @DatabaseField(columnName = "download_id", id = true)
//    private long downloadId;

    /**
     * 下载地址
     */
    @DatabaseField(columnName = "path", id = true)
    private String path;

    /**
     * 下载起始位置
     */
    @DatabaseField(columnName = "start_position")
    private int startPosition;

    /**
     * 存储路径
     */
    @DatabaseField(columnName = "save_path")
    private String savePath;

    /**
     * 存储文件名
     */
    @DatabaseField(columnName = "file_name")
    private String fileName;

    /**
     * 当前已经下载的长度
     */
    @DatabaseField(columnName = "download_bytes")
    private int downloadBytes;

    /**
     * 总长度
     */
    @DatabaseField(columnName = "total_bytes")
    private int totalBytes;

    /**
     * 当前下载状态
     * 1正在下载
     * 2下载完成
     */
    @DatabaseField(columnName = "status")
    private int status;

    public DownloadInfo() {
    }

    public DownloadInfo(/*long downloadId, */String path) {
//        this.downloadId = downloadId;
        this.path = path;
    }

//    public long getDownloadId() {
//        return downloadId;
//    }
//
//    public void setDownloadId(long downloadId) {
//        this.downloadId = downloadId;
//    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDownloadBytes() {
        return downloadBytes;
    }

    public void setDownloadBytes(int downloadBytes) {
        this.downloadBytes = downloadBytes;
    }

    public int getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(int totalBytes) {
        this.totalBytes = totalBytes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
