package com.aaron.aaronlibrary.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.aaron.aaronlibrary.base.utils.DataPath;
import com.aaron.aaronlibrary.db.DownloadInfoDao;
import com.aaron.aaronlibrary.db.bean.DownloadInfo;
import com.aaron.aaronlibrary.utils.download.FinalHttp;
import com.aaron.aaronlibrary.utils.download.http.AjaxCallBack;
import com.aaron.aaronlibrary.utils.download.http.HttpHandler;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class DownloadUtils {

    private static final String serviceString = Context.DOWNLOAD_SERVICE;

    public static final Uri CONTENT_URI = Uri.parse("content://downloads/my_downloads");

    public DownloadUtils() {

    }

    /***************************************** DownloadManager涓嬭浇 **********************************************/


    /**
     * DownloadManager下载文件（根据服务器地址取本地的保存文件名）
     * 
     * @param mContext
     *            上下文
     * @param url
     *            下载路径
     * @param downloadListener
     *            下载监听
     * @return DownloadPro 下载信息
     */
    public static DownloadPro downloadFile(Context mContext, String url, final OnDownloadListener downloadListener) {
        return downloadFile(mContext, url, getServerFileName(url), downloadListener);
    }

    /**
     * DownloadManager下载文件
     * 
     * @param mContext
     *            上下文
     * @param url
     *            下载路径
     * @param fileName
     *            本地文件名
     * @param downloadListener
     *            下载监听
     * @param isCache 是否保存在缓存中  [0]: 1为不缓存  2为下载视频文件
     * @return DownloadPro 下载信息
     */
    @SuppressWarnings("unused")
    public static DownloadPro downloadFile(final Context mContext, String url, String fileName,
                                           final OnDownloadListener downloadListener, int... isCache) {
        if (!(mContext instanceof Activity))
            throw new IllegalArgumentException("Context must be instanceof Activity");
        boolean cache = true;
        boolean isPic = true;
        if (isCache.length > 0) {
            switch (isCache[0]) {
            case 0:
                
                break;
            case 1:
                cache = false;
                break;
            case 2:
                cache = false;
                isPic = false;
                break;

            default:
                break;
            }
        }
        final DownloadManager downloadManager = (DownloadManager) mContext.getSystemService(serviceString);
        Uri uri = Uri.parse(url);
        Request request = new Request(uri);
        final File file = new File(DataPath.getDirectory(cache ? DataPath.DATA_PATH_DOWNLOAD
                : (isPic ? DataPath.DATA_PATH_PIC : DataPath.DATA_PATH_VIDEO)) + fileName);
        request.setDestinationUri(Uri.fromFile(file));
        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
        request.setVisibleInDownloadsUi(false);
        final DownloadPro downloadPro = new DownloadUtils().new DownloadPro(downloadManager, request);
        if (file.exists()) {
            downloadPro.setFile(file);
            if (downloadListener != null) {
                if (file != null)
                    downloadListener.onFinished(file, true);
                else
                    downloadListener.onError("file is null");
            }
            return downloadPro;
        }
        final long reference = downloadManager.enqueue(request);
        ContentObserver contentObserver = new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange) {
                long[] bytesAndStatus = getBytesAndStatus(downloadManager, reference);
                if (downloadPro.getTotalSize() == 0)
                    downloadPro.setTotalSize(bytesAndStatus[1]);
                int percent = (int) (bytesAndStatus[0] / (float) bytesAndStatus[1] * 100);
                if (downloadListener != null && downloadPro.getCurrentPercent() < percent
                        && downloadPro.getFile() == null) {
                    downloadPro.setCurrentPercent(percent);
                    downloadListener.onProgress(bytesAndStatus[0], bytesAndStatus[1], percent, (int) bytesAndStatus[2]);
                }
            }
        };
        downloadPro.setContentObserver(contentObserver);
        mContext.getContentResolver().registerContentObserver(CONTENT_URI, true, contentObserver);
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                long completeDownloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (completeDownloadId == reference) {
                    downloadPro.setFile(file);
                    if (downloadPro.getCurrentPercent() == 100 && downloadPro.getFile() != null) {
                        if (downloadListener != null) {
                            if (downloadPro.getCurrentPercent() < 100) {
                                downloadPro.setCurrentPercent(100);
                                // 下载完成标记state == 2
                                downloadListener.onProgress(downloadPro.getTotalSize(), downloadPro.getTotalSize(),
                                        100, 2);
                            }
                            if (file != null)
                                downloadListener.onFinished(file, false);
                            else
                                downloadListener.onError("file is null");
                        }
                    }
                }
            }
        };
        mContext.registerReceiver(broadcastReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        downloadPro.setBroadcastReceiver(broadcastReceiver);
        return downloadPro;
    }

    public static void unRegister(Context mContext, DownloadPro downloadPro) {
        if (!(mContext instanceof Activity))
            throw new IllegalArgumentException("Context must be instanceof Activity");
        if (downloadPro == null)
            return;
        if (downloadPro.getContentObserver() != null) {
            mContext.getContentResolver().unregisterContentObserver(downloadPro.getContentObserver());
        }
        if (downloadPro.getBroadcastReceiver() != null) {
            mContext.unregisterReceiver(downloadPro.getBroadcastReceiver());
        }
    }

    /**
     * 移除任务
     *
     * @param reference
     */
    public void remove(DownloadManager downloadManager, long... reference) {
        downloadManager.remove(reference);
    }

    public class DownloadPro {

        private DownloadManager downloadManager;

        private Request request;

        private ContentObserver contentObserver;

        private BroadcastReceiver broadcastReceiver;

        private File file;

        private long totalSize;

        /**
         * 当前下载进度
         */
        private int currentPercent = -1;

        /**
         * 数据更新间隔(ms)
         */
        private int loadFrequency = 20;

        public DownloadPro(DownloadManager downloadManager, Request request) {
            this.downloadManager = downloadManager;
            this.request = request;
        }

        public DownloadPro(DownloadManager downloadManager, Request request, ContentObserver contentObserver,
                           BroadcastReceiver broadcastReceiver) {
            this.downloadManager = downloadManager;
            this.request = request;
            this.contentObserver = contentObserver;
            this.broadcastReceiver = broadcastReceiver;
        }

        public long getTotalSize() {
            return totalSize;
        }

        public void setTotalSize(long totalSize) {
            this.totalSize = totalSize;
        }

        public int getLoadFrequency() {
            return loadFrequency;
        }

        public void setLoadFrequency(int loadFrequency) {
            this.loadFrequency = loadFrequency;
        }

        public int getCurrentPercent() {
            return currentPercent;
        }

        public void setCurrentPercent(int currentPercent) {
            this.currentPercent = currentPercent;
        }

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public boolean deleteFile() {
            if (file != null && file.exists()) {
                return file.delete();
            }
            return false;
        }

        public DownloadManager getDownloadManager() {
            return downloadManager;
        }

        public void setDownloadManager(DownloadManager downloadManager) {
            this.downloadManager = downloadManager;
        }

        public Request getRequest() {
            return request;
        }

        public void setRequest(Request request) {
            this.request = request;
        }

        public ContentObserver getContentObserver() {
            return contentObserver;
        }

        public void setContentObserver(ContentObserver contentObserver) {
            this.contentObserver = contentObserver;
        }

        public BroadcastReceiver getBroadcastReceiver() {
            return broadcastReceiver;
        }

        public void setBroadcastReceiver(BroadcastReceiver broadcastReceiver) {
            this.broadcastReceiver = broadcastReceiver;
        }
    }

    public interface OnDownloadListener {
        public void onProgress(long bytesWritten, long totalSize, int percent, int state);

        public void onFinished(File file, boolean isExist);

        public void onError(String text);
    }

    /**
     * 获取已下载字节和状态
     *
     * @param downloadId
     * @return
     */
    private static long[] getBytesAndStatus(DownloadManager downloadManager, long downloadId) {
        long[] bytesAndStatus = new long[] { -1, -1, 0 };
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                bytesAndStatus[0] = c.getLong(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                bytesAndStatus[1] = c.getLong(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                bytesAndStatus[2] = c.getLong(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return bytesAndStatus;
    }

    public static String getServerFileName(String path) {
        String fileName = path.substring(path.lastIndexOf('/') + 1);
        return fileName;
    }

    /*****************************************断点续传下载**********************************************/

    public static Set<String> urlList = new HashSet<>();

    public static Map<String, HttpHandler<File>> tasks = new HashMap<String, HttpHandler<File>>();

    /**
     * 下载文件（支持断点续传）
     *
     * @param context
     *            上下文�
     * @param url
     *            下载路径
     * @return true：下载完成 false：正在下载
     */
    public static boolean downloadFileWithListener(Context context, final String url,
                                                   final DownloadUtils.OnDownloadListener downloadListener) {
    	if (TextUtils.isEmpty(url))
    		throw new IllegalArgumentException("download file url is null");
        if (fileIsDownloaded(url)) {
            if (downloadListener != null)
                downloadListener.onFinished(new File(getFileSavePath(url)), true);
            return true;
        }
        boolean isHas = pathIsHasInList(url);
        DownloadInfo info = DownloadInfoDao.getSingleInstance().getByPath(url);
        if ((info == null || info.getTotalBytes() == 0 || info.getDownloadBytes() == 0
                || info.getDownloadBytes() < info.getTotalBytes()) && !isHas) {
            // 当进入if后，后台方才下载完毕
            if (info != null && info.getTotalBytes() != 0
                    && info.getDownloadBytes() == info.getTotalBytes()
                    && downloadListener != null) {
                downloadListener.onFinished(new File(getFileSavePath(url)), false);
            }
            urlList.add(url);
            if (info == null)
                saveDownloading(url, getFileSavePath(url), getServerFileName(url), 0, 0, 1);
            final List<PercentPro> pros = new ArrayList<PercentPro>();
            pros.add(new DownloadUtils().new PercentPro((int) System.currentTimeMillis(), 0, 0, 0));
            FinalHttp finalHttp = new FinalHttp();
            SSLSocketFactory factory = getSSLSocketFactory();
            if (factory != null)
                finalHttp.configSSLSocketFactory(factory);
            HttpHandler<File> handler = finalHttp.download(url, getFileSavePath(url), true, new AjaxCallBack<File>() {

                @Override
                public void onLoading(long count, long current) {
                    final int percent = (int) (current / (float) count * 100);
                    final int curPercent = getDownloadPercent(url);
                    updateDownloading((int) current, url, (int) count);
                    if (percent > curPercent) {
                        pros.add(new DownloadUtils().new PercentPro((int) System.currentTimeMillis(), percent, current, count));
                        if (pros.size() == 2) {
                            updateProgress(url, pros, downloadListener);
                        }
                    }
                }

                @Override
                public void onSuccess(File t) {}

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    super.onFailure(t, errorNo, strMsg);
                    if (downloadListener != null) {
                        downloadListener.onError(errorNo + strMsg);
                    }
                    deletePath(url);
                }
            });
            tasks.put(url, handler);
        }
        return false;
    }

    /**
     * 获取SSLSocketFactory
     * @return SSLSocketFactory
     */
    private static SSLSocketFactory getSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            sslSocketFactory = new MySSLSocketFactory(trustStore);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    /**
     * 判断文件是否下载完成
     *
     * @param url 下载路径
     * @return true：文件已下载完成  false：文件尚未下载完成
     */
    private static boolean fileIsDownloaded(String url) {
        File f = new File(getFileSavePath(url));
        DownloadInfo downloadInfo = DownloadInfoDao.getSingleInstance().getByPath(url);
        if (!f.exists()) {
            if (downloadInfo != null && !pathIsHasInList(url))
                DownloadInfoDao.getSingleInstance().delete(downloadInfo);
            return false;
        }
        int s = (int) f.length();
        if (downloadInfo != null && downloadInfo.getTotalBytes() != 0 && downloadInfo.getTotalBytes() == s)
            return true;
        else if (downloadInfo != null && downloadInfo.getTotalBytes() != 0 && downloadInfo.getTotalBytes() < s) {
            f.delete();
            DownloadInfoDao.getSingleInstance().delete(downloadInfo);
            deletePath(url);
        }
        return false;
    }

    /**
     * 判断数据库是否存在下载信息
     * @param path 下载路径
     * @return
     */
    public static boolean pathIsHasInDataBase(String path) {
        return DownloadInfoDao.getSingleInstance().isExist(path);
    }

    /**
     * 判断是否正在下载中
     *
     * @param path
     *            下载路径
     * @return
     */
    public synchronized static boolean pathIsHasInList(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        return urlList.contains(path);
    }

    /**
     * 删除任务列表
     *
     * @param path
     *            下载路径
     */
    public static void deletePath(String path) {
        urlList.remove(path);
    }

    /**
     * 清除任务
     */
    public static void clear() {
        urlList.clear();
    }

    /**
     * 停止所有下载
     */
    public static void stopAllTask() {
        Iterator<Entry<String, HttpHandler<File>>> itr = tasks.entrySet().iterator();
        while (itr.hasNext()) {
            Entry<String, HttpHandler<File>> entry = (Entry<String, HttpHandler<File>>) itr.next();
            ((HttpHandler<File>) entry.getValue()).stop();
        }
    }

    /**
     * 获取本地保存路径
     *
     * @param url
     *            下载路径
     * @return 返回本地的保存路径
     */
    public static String getFileSavePath(String url) {
        return DataPath.getDirectory(DataPath.DATA_PATH_DOWNLOAD) + getServerFileName(url);
    }

    /**
     * 保存到本地数据库
     * @param url 下载路径
     * @param savePath 保存的本地路径
     * @param fileName 文件名
     * @param downloadBytes 文件已下载字节数
     * @param totalBytes 文件总大小字节数
     * @param status 状态（1：下载中，2：下载完成）
     */
    private static void saveDownloading(String url, String savePath, String fileName, int downloadBytes, int totalBytes,
                                        int status) {
        DownloadInfo downloadInfo = new DownloadInfo();
        downloadInfo.setPath(url);
        downloadInfo.setSavePath(savePath);
        downloadInfo.setFileName(fileName);
        downloadInfo.setDownloadBytes(downloadBytes);
        downloadInfo.setTotalBytes(totalBytes);
        downloadInfo.setStatus(status);
        DownloadInfoDao.getSingleInstance().add(downloadInfo);
    }

    /**
     * 更新下载数据
     * @param curSize 当前已下载字节
     * @param url 下载路径
     */
    private static void updateDownloading(int curSize, String url, int count) {
        DownloadInfo downloadInfo = DownloadInfoDao.getSingleInstance().getByPath(url);
        if (downloadInfo == null)
        	return;
        downloadInfo.setDownloadBytes(curSize);
        downloadInfo.setTotalBytes(count);
        DownloadInfoDao.getSingleInstance().update(downloadInfo);
    }

    /**
     * 完全下载
     * @param url 下载路径
     */
    private static void finishDownloading(String url) {
        DownloadUtils.deletePath(url);
        DownloadInfo downloadInfo = DownloadInfoDao.getSingleInstance().getByPath(url);
        if (downloadInfo == null)
        	return;
        downloadInfo.setStatus(2);// 下载完成状态为2
        DownloadInfoDao.getSingleInstance().update(downloadInfo);
    }

    /**
     * 获得保存的已下载字节数
     * @param url 下载路径
     * @return int 当前已下载字节
     */
    private static int getDownloadPercent(String url) {
        DownloadInfo downloadInfo = DownloadInfoDao.getSingleInstance().getByPath(url);
        if (downloadInfo == null || downloadInfo.getTotalBytes() == 0)
            return 0;
        int percent = (int) (downloadInfo.getDownloadBytes() / (float) downloadInfo.getTotalBytes() * 100);
        return percent;
    }

    /**
     * 均匀更新下载进度
     */
    private static void updateProgress(final String url, final List<PercentPro> pros, final DownloadUtils.OnDownloadListener downloadListener) {
        final int curPercent = pros.get(0).getPercent();
        final int percent = pros.get(1).getPercent();
        final long current = pros.get(1).getCurrentBytes();
        final long count = pros.get(1).getCount();
        final int curTime = pros.get(0).getTime();
        final int time = pros.get(1).getTime();
        ValueAnimator animator = ObjectAnimator.ofFloat(curPercent, percent);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                float tempPercent = (float) animator.getAnimatedValue();
                if (downloadListener != null) {
                    downloadListener.onProgress(current, count, (int) tempPercent, 1);
                }
                if (tempPercent == percent) {
                    if (pros.size() > 0)
                        pros.remove(0);
                    if (pros.size() > 1) {
                        updateProgress(url, pros, downloadListener);
                    }
                }
                if (tempPercent == 100) {
                    finishDownloading(url);
                    if (downloadListener != null) {
                        downloadListener.onFinished(new File(getFileSavePath(url)), false);
                    }
                }
            }
        });
        animator.setDuration(time - curTime);
        animator.start();
    }
    
    public class PercentPro {
        private int time;
        private int percent;
        private long currentBytes;
        private long count;
        
        public PercentPro(int time, int percent, long currentBytes, long count) {
            this.time = time;
            this.percent = percent;
            this.currentBytes = currentBytes;
            this.count = count;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getPercent() {
            return percent;
        }

        public void setPercent(int percent) {
            this.percent = percent;
        }

        public long getCurrentBytes() {
            return currentBytes;
        }

        public void setCurrentBytes(long currentBytes) {
            this.currentBytes = currentBytes;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }
    }

    private static class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
