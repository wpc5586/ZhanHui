package com.aaron.aaronlibrary.base.adapter;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.aaronlibrary.photoutil.ImageBucket;
import com.aaron.aaronlibrary.photoutil.ImageItem;
import com.aaron.aaronlibrary.photoutil.Res;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.aaron.aaronlibrary.widget.RoundAngleImageView;

import java.util.List;

/**
 * 这个是显示所有包含图片的文件夹的适配器
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日 下午11:49:44
 */
public class FolderAdapter extends BaseAdapter {

    private Activity mContext;
    private DisplayMetrics dm;
    public List<ImageBucket> imageList;
    private String firstPath;
//    BitmapCache cache;
    final String TAG = getClass().getSimpleName();

    public FolderAdapter(Activity c) {
//        cache = new BitmapCache();
        init(c);
    }

    public FolderAdapter(Activity c, List<ImageBucket> imageList) {
//        cache = new BitmapCache();
        this.imageList = imageList;
        init(c);
    }

    public String getFirstPath() {
        return firstPath;
    }

    public void setFirstPath(String firstPath) {
        this.firstPath = firstPath;
    }

    // 初始化
    public void init(Activity c) {
        mContext = c;
        dm = new DisplayMetrics();
        mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Res.init(mContext);
    }

    @Override
    public int getCount() {
        return imageList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        // 封面
        public RoundAngleImageView imageView;
        // 文件夹名称
        public TextView folderName;
        public TextView fileNum;
    }

    ViewHolder holder = null;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(Res.getLayoutID("plugin_camera_select_folder"), null);
            holder = new ViewHolder();
//            holder.backImage = (ImageView) convertView.findViewById(Res.getWidgetID("file_back"));
            holder.imageView = (RoundAngleImageView) convertView.findViewById(Res.getWidgetID("file_image"));
            holder.imageView.setIsOld(true);
//            holder.choose_back = (ImageView) convertView.findViewById(Res.getWidgetID("choose_back"));
            holder.folderName = (TextView) convertView.findViewById(Res.getWidgetID("name"));
            holder.fileNum = (TextView) convertView.findViewById(Res.getWidgetID("filenum"));
            holder.imageView.setAdjustViewBounds(true);
            // LinearLayout.LayoutParams lp = new
            // LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,dipToPx(65));
            // lp.setMargins(50, 0, 50,0);
            // holder.imageView.setLayoutParams(lp);
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        
        if (position == 0) {
            holder.folderName.setText("相机胶卷");
            ImageUtils.loadImage(mContext, firstPath, holder.imageView, false, true);
            return convertView;
        }
        String path;
        if (imageList.get(position - 1).imageList != null && imageList.get(position - 1).imageList.size() > 0) {
            // path = photoAbsolutePathList.get(position);
            // 封面图片路径
            path = imageList.get(position - 1).imageList.get(0).imagePath;
            // 给folderName设置值为文件夹名称
            // holder.folderName.setText(fileNameList.get(position));
            holder.folderName.setText(imageList.get(position - 1).bucketName);

            // 给fileNum设置文件夹内图片数量
            // holder.fileNum.setText("" + fileNum.get(position));
           // holder.fileNum.setText("" + AlbumActivity.imageList.get(position).count);

        } else {
            path = "android_hybrid_camera_default";
            holder.folderName.setText("default");
        }
        if (path.contains("android_hybrid_camera_default"))
            holder.imageView.setImageResource(Res.getDrawableID("plugin_camera_no_pictures"));
        else {
            // holder.imageView.setImageBitmap( AlbumActivity.contentList.get(position).imageList.get(0).getBitmap());
            final ImageItem item = imageList.get(position - 1).imageList.get(0);
//            holder.imageView.setTag(item.imagePath);
//            cache.displayBmp(holder.imageView, item.thumbnailPath, item.imagePath, callback);
            ImageUtils.loadImage(mContext, item.getImagePath(), holder.imageView, false, true);
        }
        // 为封面添加监听
       // holder.imageView.setOnClickListener(new ImageViewClickListener(position, mIntent,holder.choose_back));

        return convertView;
    }

     //为每一个文件夹构建的监听器
//    private class ImageViewClickListener implements OnClickListener {
//        private int position;
//        private Intent intent;
//        private ImageView choose_back;
//
//        public ImageViewClickListener(int position, Intent intent, ImageView choose_back) {
//            this.position = position;
//            this.intent = intent;
//            this.choose_back = choose_back;
//        }
//
//        @Overrideyu
//        public void onClick(View v) {
//            // ShowAllPhotoActivity.dataList = (ArrayList<ImageItem>) AlbumActivity.imageList.get(position).imageList;
//            // Intent intent = new Intent();
//            // String folderName = AlbumActivity.imageList.get(position).bucketName;
//            // intent.putExtra("folderName", folderName);
//            // intent.setClass(mContext, ShowAllPhotoActivity.class);
//            // mContext.startActivity(intent);
//            // choose_back.setVisibility(v.VISIBLE);
//
//            String folderName = AlbumActivity.imageList.get(position).bucketName;
//            AlbumActivity.dataList = (ArrayList<ImageItem>) AlbumActivity.imageList.get(position).imageList;
//            AlbumActivity.dirName = folderName;
//            mContext.setResult(Activity.RESULT_OK);
//            mContext.finish();
//        }
//    }

    public int dipToPx(int dip) {
        return (int) (dip * dm.density + 0.5f);
    }

}
