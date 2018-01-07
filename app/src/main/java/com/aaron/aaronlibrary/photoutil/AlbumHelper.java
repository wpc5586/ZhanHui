package com.aaron.aaronlibrary.photoutil;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.Video;
import android.text.TextUtils;
import android.util.Log;

import com.aaron.aaronlibrary.base.app.CrashApplication;
import com.aaron.aaronlibrary.base.utils.Logger;
import com.aaron.aaronlibrary.utils.TimeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class AlbumHelper {
    
    public static final int TYPE_PIC = 0;
    public static final int TYPE_PICANDVIDEO = 1;
    public static final int TYPE_VIDEO = 2;
    
    final String TAG = getClass().getSimpleName();
    Context context;
    ContentResolver cr;

    HashMap<String, String> thumbnailList = new HashMap<String, String>();

    HashMap<String, String> thumbnailVideoList = new HashMap<String, String>();

    List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();

    private static AlbumHelper instance;

    private static int totalImageNum;

    private static String photoLastID = "";
    
    private static int totalVideoNum;
    
    private static String videoLastID = "";

    public static OnAlbumChangeListener albumChangeListener;

    private AlbumHelper() {
    }

    public static AlbumHelper getHelper() {
        if (instance == null) {
            instance = new AlbumHelper();
        }
        return instance;
    }

    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            cr = context.getContentResolver();
        }
    }

    public static void onListChanged(ArrayList<ImageItem> dataList, List<ImageBucket> imageList, String dirName) {
        if (albumChangeListener != null) {
            albumChangeListener.onListChanged(dataList, imageList, dirName);
        }
    }

    public void getThumbnail() {
        String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
        Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getThumbnailColumnData(cursor);
    }

    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            @SuppressWarnings("unused")
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cur.getColumnIndex(Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);

                // Do something with the values.
                // Log.i(TAG, _id + " image_id:" + image_id + " path:"
                // + image_path + "---");
                // HashMap<String, String> hash = new HashMap<String, String>();
                // hash.put("image_id", image_id + "");
                // hash.put("path", image_path);
                // thumbnailList.add(hash);
                thumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    public void getVideoThumbnail() {
        String[] projection = { Video.VideoColumns._ID, Video.Thumbnails.VIDEO_ID, Video.Thumbnails.DATA };
        Cursor cursor = cr.query(Video.Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getVideoThumbnailColumnData(cursor);
    }

    private void getVideoThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            @SuppressWarnings("unused")
            int _id;
            int image_id;
            String image_path;
            int _idColumn = cur.getColumnIndex(Video.Thumbnails._ID);
            int image_idColumn = cur.getColumnIndex(Video.Thumbnails.VIDEO_ID);
            int dataColumn = cur.getColumnIndex(Video.Thumbnails.DATA);

            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);

                // Do something with the values.
                // Log.i(TAG, _id + " image_id:" + image_id + " path:"
                // + image_path + "---");
                // HashMap<String, String> hash = new HashMap<String, String>();
                // hash.put("image_id", image_id + "");
                // hash.put("path", image_path);
                // thumbnailList.add(hash);
                thumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    void getAlbum() {
        String[] projection = { Albums._ID, Albums.ALBUM, Albums.ALBUM_ART, Albums.ALBUM_KEY, Albums.ARTIST,
                Albums.NUMBER_OF_SONGS };
        Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null, null, null);
        getAlbumColumnData(cursor);

    }

    private void getAlbumColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int _id;
            String album;
            String albumArt;
            String albumKey;
            String artist;
            int numOfSongs;

            int _idColumn = cur.getColumnIndex(Albums._ID);
            int albumColumn = cur.getColumnIndex(Albums.ALBUM);
            int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
            int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
            int artistColumn = cur.getColumnIndex(Albums.ARTIST);
            int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);

            do {
                // Get the field values
                _id = cur.getInt(_idColumn);
                album = cur.getString(albumColumn);
                albumArt = cur.getString(albumArtColumn);
                albumKey = cur.getString(albumKeyColumn);
                artist = cur.getString(artistColumn);
                numOfSongs = cur.getInt(numOfSongsColumn);

                // Do something with the values.
                Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt + "albumKey: " + albumKey + " artist: "
                        + artist + " numOfSongs: " + numOfSongs + "---");
                HashMap<String, String> hash = new HashMap<String, String>();
                hash.put("_id", _id + "");
                hash.put("album", album);
                hash.put("albumArt", albumArt);
                hash.put("albumKey", albumKey);
                hash.put("artist", artist);
                hash.put("numOfSongs", numOfSongs + "");
                albumList.add(hash);

            } while (cur.moveToNext());

        }
    }

    boolean hasBuildImagesBucketList = false;

    @SuppressLint("InlinedApi")
    void buildImagesBucketList(int type) {
        long startTime = System.currentTimeMillis();

        // getThumbnail();

        if (type == TYPE_PICANDVIDEO || type == TYPE_PIC) {

            String columns[] = new String[] { Media._ID, Media.BUCKET_ID, Media.PICASA_ID, Media.DATA,
                    Media.DISPLAY_NAME, Media.TITLE, Media.SIZE, Media.BUCKET_DISPLAY_NAME, Media.DATE_ADDED,
                    Media.WIDTH, Media.HEIGHT };

            final Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
            if (cur.moveToFirst()) {
                int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
                int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
                int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
                int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
                int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
                int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
                int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
                int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
                int totalNum = cur.getCount();
                int added = cur.getColumnIndexOrThrow(Media.DATE_ADDED); // 测试
                int widthIndex = 0;
                int heightIndex = 0;
                widthIndex = cur.getColumnIndexOrThrow(Media.WIDTH);
                heightIndex = cur.getColumnIndexOrThrow(Media.HEIGHT);
                totalImageNum = totalNum;

                do {
                    String _id = cur.getString(photoIDIndex);
                    String name = cur.getString(photoNameIndex);
                    String path = cur.getString(photoPathIndex);
                    String title = cur.getString(photoTitleIndex);
                    String size = cur.getString(photoSizeIndex);
                    String bucketName = cur.getString(bucketDisplayNameIndex);
                    String bucketId = cur.getString(bucketIdIndex);
                    String picasaId = cur.getString(picasaIdIndex);
                    String time = cur.getString(added);// 时间
                    int width = 0;
                    int height = 0;
                    if (widthIndex != 0)
                        width = cur.getInt(widthIndex);
                    if (heightIndex != 0)
                        height = cur.getInt(heightIndex);

                    System.out.println(TAG + _id + ", bucketId: " + bucketId + ", picasaId: " + picasaId + " name:" + name
                            + " path:" + path + " title: " + title + " size: " + size + " bucket: " + bucketName
                            + "---" + "time" + TimeUtils.timeToyyyyMMddHHmmss(Long.parseLong(time))
                            + ", width：" + width + ", height:" + height );

                    ImageBucket bucket = bucketList.get(bucketId);
                    if (bucket == null) {
                        bucket = new ImageBucket();
                        bucketList.put(bucketId, bucket);
                        bucket.imageList = new ArrayList<ImageItem>();
                        bucket.bucketName = bucketName;
                    }
                    bucket.count++;
                    ImageItem imageItem = new ImageItem();
                    imageItem.imageId = _id;
                    imageItem.imagePath = path;
                    String thumbPath = thumbnailList.get(_id);
                    File thumbFile = new File(TextUtils.isEmpty(thumbPath) ? "" : thumbPath);
                    imageItem.thumbnailPath = thumbFile == null || !thumbFile.exists() ? "" : thumbPath;
                    imageItem.setAddTime(Integer.parseInt(time));
                    imageItem.setWidth(width);
                    imageItem.setHeight(height);
                    if (new File(imageItem.imagePath).exists() && Integer.parseInt(TextUtils.isEmpty(size) ? "0" : size) > 0) // 判断图片文件如果存在，在加入相册list中
                        bucket.imageList.add(imageItem);

                } while (cur.moveToNext());
            }

            Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet().iterator();
            while (itr.hasNext()) {
                Entry<String, ImageBucket> entry = (Entry<String, ImageBucket>) itr.next();
                ImageBucket bucket = entry.getValue();
                Logger.d(TAG, entry.getKey() + ", " + bucket.bucketName + ", " + bucket.count + " ---------- ");
                for (int i = 0; i < bucket.imageList.size(); ++i) {
                    ImageItem image = bucket.imageList.get(i);
                    Logger.d(TAG, "----- " + image.imageId + ", " + image.imagePath + ", " + image.thumbnailPath);
                }
            }
            // hasBuildImagesBucketList = true;
            long endTime = System.currentTimeMillis();
            Logger.d(TAG, "use time: " + (endTime - startTime) + " ms");
        }
        if (type == TYPE_PICANDVIDEO || type == TYPE_VIDEO) {
            // 获取视频
             String[] projection = { Video.Media.DATA,
             Video.Media._ID, Video.Media.BUCKET_ID, Video.Media.BUCKET_DISPLAY_NAME,
             Video.Media.DATE_ADDED, Video.Media.WIDTH, Video.Media.HEIGHT,
             Video.Media.DURATION, Video.Media.SIZE};
            // String whereClause = MediaStore.Video.Media.DATA + " = '" +
            // Videopath + "'";
            Cursor cursor = cr.query(Video.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            options.inDither = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            String _id = "";
            String videoPath = "";
            totalVideoNum = cursor.getCount();
            if (cursor == null || totalVideoNum == 0) {
            } else if (cursor.moveToFirst()) {
                int _idColumn = cursor.getColumnIndex(Video.Media._ID);
                int _dataColumn = cursor.getColumnIndex(Video.Media.DATA);
                int bucketIdIndex = cursor.getColumnIndexOrThrow(Video.Media.BUCKET_ID);
                int bucketDisplayNameIndex = cursor.getColumnIndexOrThrow(Video.Media.BUCKET_DISPLAY_NAME);
                int added = cursor.getColumnIndexOrThrow(Media.DATE_ADDED); // 测试
                int widthIndex = 0;
                int heightIndex = 0;
                widthIndex = cursor.getColumnIndexOrThrow(Video.Media.WIDTH);
                heightIndex = cursor.getColumnIndexOrThrow(Video.Media.HEIGHT);
                do {
                    _id = cursor.getString(_idColumn);
                    videoPath = cursor.getString(_dataColumn);
                    String bucketName = cursor.getString(bucketDisplayNameIndex);
                    String bucketId = "";
                    String time = cursor.getString(added);// 时间
                    int width = cursor.getInt(widthIndex);
                    int height = cursor.getInt(heightIndex);
                    try {
                        bucketId = cursor.getString(bucketIdIndex);
                    } catch (Exception e) {
                        bucketId = "";
                    }
                    long duration = cursor.getInt(cursor.getColumnIndexOrThrow(Video.Media.DURATION));
                    // if (duration > 15000) {
                    // continue;
                    // }
                    int size = cursor.getInt(cursor.getColumnIndexOrThrow(Video.Media.SIZE));
                    if (size > 1024 * 1024 * 10) {
                        continue;
                    }
                    bucketIdIndex++;
                    ImageBucket bucket = bucketList.get(bucketId);
                    if (bucket == null) {
                        bucket = new ImageBucket();
                        bucketList.put(bucketId, bucket);
                        bucket.imageList = new ArrayList<ImageItem>();
                        bucket.bucketName = bucketName;
                    }
                    bucket.count++;
                    ImageItem imageItem = new ImageItem();
                    imageItem.imageId = _id;
                    imageItem.imagePath = videoPath;
                    imageItem.setAddTime(Integer.parseInt(time));
                    imageItem.setPic(false);
                    imageItem.setVideoSize(size / 1024.0f / 1024.0f);
                    imageItem.setWidth(width);
                    imageItem.setHeight(height);
                    imageItem.setVideoDuration(duration);
                    String thumPath = thumbnailVideoList.get(_id);
                    if (!TextUtils.isEmpty(thumPath)) {
                        imageItem.setThumbnailPath(thumPath);
                    } else {
//                        Bitmap bitmap = MediaStore.Video.Thumbnails.getThumbnail(cr, Long.parseLong(TextUtils.isEmpty(_id) ? "0" : _id),
//                                Images.Thumbnails.MICRO_KIND, options);
//                        // imageItem.setBitmap(bitmap);
//                        File f = new File(DataPath.DATA_PATH_CACHE, _id + ".png");
//                        if (f.exists()) {
//                            imageItem.setBitmapPath(f.getAbsolutePath());
//                        } else if (bitmap != null) {
//                            imageItem.setBitmapPath(EaseImageUtils.saveBitmapToCache(bitmap, _id + ".png").getAbsolutePath());
//                        }
//                        if (bitmap != null)
//                            bitmap.recycle();
                    }
                    if (new File(imageItem.imagePath).exists() && duration > 0 && size > 0) // 如果文件存在并且时间和大小均大于0，加入相册list中
                        bucket.imageList.add(imageItem);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    @SuppressLint("SdCardPath")
    public static String saveBitmap(Bitmap bm) {
        File destDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/atwill/");
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/atwill/",
                System.currentTimeMillis() + ".png");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            return f.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断相册是否有数据变化
     *
     * @return true 是 false 否
     */
    public boolean bucketIsUpdate() {
        boolean isUpdate = false;
        String columns[] = new String[] { Media._ID };
        if (cr == null)
        	cr = CrashApplication.APP.getContentResolver();
        Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
        String columnsVideo[] = new String[] { Video.Media._ID };
        Cursor curVideo = cr.query(Video.Media.EXTERNAL_CONTENT_URI, columnsVideo, null, null, null);
        int nowImageNum = cur.getCount();
        int nowVideoNum = curVideo.getCount();
        if (cur.moveToLast() && curVideo.moveToLast()) {
            int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
            String _id = cur.getString(photoIDIndex);
            int videoIDIndex = curVideo.getColumnIndexOrThrow(Video.Media._ID);
            String _videoId = curVideo.getString(videoIDIndex);
            // 判断相册总数是否发生变化，或者最后一张照片变化了，就重新加载相册数据
            if ((TextUtils.isEmpty(photoLastID) && TextUtils.isEmpty(videoLastID))
                    || (!photoLastID.equals(_id) || !videoLastID.equals(_videoId))
                    || (nowImageNum != totalImageNum || nowVideoNum != totalVideoNum)) {
                hasBuildImagesBucketList = false;
                isUpdate = true;
            } else {
                isUpdate = false;
                hasBuildImagesBucketList = true;
            }
            photoLastID = _id;
            videoLastID = _videoId;
        } else {
            hasBuildImagesBucketList = false;
            isUpdate = true;
        }
        return isUpdate;
    }

    /**
     * 总相册数组
     */
    private List<ImageBucket> tmpList = new ArrayList<ImageBucket>();

    /**
     * 目前相册数组
     */
    private List<ImageBucket> nowList = new ArrayList<ImageBucket>();

    /**
     * 目前相册类型
     */
    private int nowType = -1;

    @SuppressWarnings("unchecked")
    public synchronized List<ImageBucket> getImagesBucketList(boolean refresh, int type) {
        if (bucketIsUpdate()) {
            cleanList();
            getThumbnail();
            getVideoThumbnail();
            if (refresh || (!refresh && !hasBuildImagesBucketList)) {
                buildImagesBucketList(type);
            }

//            List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
            Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet().iterator();

            while (itr.hasNext()) {
                Entry<String, ImageBucket> entry = (Entry<String, ImageBucket>) itr.next();
                tmpList.add(entry.getValue());
            }
            nowType = type;
        } else {
            nowList = new ArrayList<ImageBucket>();
            if (type == nowType && nowList.size() != 0) {
                return nowList;
            }
            for (ImageBucket imageBucket : tmpList) {
                ImageBucket bucket = new ImageBucket();
                bucket.bucketName = imageBucket.bucketName;
                bucket.imageList = (List<ImageItem>) ((ArrayList<ImageItem>) imageBucket.imageList).clone();
                bucket.count = bucket.imageList != null ? bucket.imageList.size() : 0;
                nowList.add(bucket);
            }
            switch (type) {
            case TYPE_PICANDVIDEO:
                
                break;
            case TYPE_PIC:
                for (ImageBucket imageBucket : nowList) {
                    List<ImageItem> tempItems = new ArrayList<ImageItem>();
                    tempItems.addAll(imageBucket.imageList);
                    for (ImageItem imageItem : imageBucket.imageList) {
                        if (!imageItem.isPic())
                            tempItems.remove(imageItem);
                    }
                    imageBucket.imageList.clear();
                    imageBucket.imageList.addAll(tempItems);
                }
                break;
            case TYPE_VIDEO:
                for (ImageBucket imageBucket : nowList) {
                    List<ImageItem> tempItems = new ArrayList<ImageItem>();
                    tempItems.addAll(imageBucket.imageList);
                    for (ImageItem imageItem : imageBucket.imageList) {
                        if (imageItem.isPic())
                            tempItems.remove(imageItem);
                    }
                    imageBucket.imageList.clear();
                    imageBucket.imageList.addAll(tempItems);
                }
                break;

            default:
                break;
            }
            nowType = type;
            return nowList;
        }

        return tmpList;
    }

    String getOriginalImagePath(String image_id) {
        String path = null;
        Log.i(TAG, "---(^o^)----" + image_id);
        String[] projection = { Media._ID, Media.DATA };
        Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection, Media._ID + "=" + image_id, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(Media.DATA));

        }
        return path;
    }
    
    public HashMap<String, String> getThumbnailList() {
        return thumbnailList;
    }

    public void cleanCache() {
        totalImageNum = 0;
        totalVideoNum = 0;
        videoLastID = "";
        photoLastID = "";
        cleanList();
    }
    
    public void cleanList() {
        tmpList.clear();
        thumbnailList.clear();
        thumbnailVideoList.clear();
        albumList.clear();
        bucketList.clear();
    }

    public interface OnAlbumChangeListener {
        public void onListChanged(ArrayList<ImageItem> dataList, List<ImageBucket> imageList, String dirName);
    }
}