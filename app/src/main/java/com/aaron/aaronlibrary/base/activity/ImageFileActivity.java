package com.aaron.aaronlibrary.base.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.adapter.FolderAdapter;
import com.aaron.aaronlibrary.photoutil.AlbumHelper;
import com.aaron.aaronlibrary.photoutil.Bimp;
import com.aaron.aaronlibrary.photoutil.ImageBucket;
import com.aaron.aaronlibrary.photoutil.ImageItem;
import com.aaron.aaronlibrary.photoutil.Res;
import com.aaron.aaronlibrary.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个类主要是用来进行显示包含图片的文件夹
 *
 * @author king
 * @QQ:595163260
 * @version 2014年10月18日 下午11:48:06
 */
public class ImageFileActivity extends Activity {

    private FolderAdapter folderAdapter;
    private Button bt_cancel;
    public ArrayList<ImageItem> dataList;
    public List<ImageBucket> imageList;
    public String dirName;

    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(Res.getLayoutID("plugin_camera_image_file"));
//        PublicWay.activityList.add(this);

        if (getIntent().hasExtra(Constants.INTENT_ALBUM_TO_FILE_DATA1)) {
//            dataList = new ArrayList<>();
            imageList = (List<ImageBucket>) getIntent().getSerializableExtra(Constants.INTENT_ALBUM_TO_FILE_DATA1);
//            for (int i = 0; i < imageList.size(); i++) {
//                dataList.addAll(imageList.get(i).imageList);
//            }

            dataList = (ArrayList<ImageItem>) getIntent().getSerializableExtra(Constants.INTENT_ALBUM_TO_FILE_DATA2);
            dirName = getIntent().getStringExtra(Constants.INTENT_ALBUM_TO_FILE_NAME);
        }

        bt_cancel = (Button) findViewById(Res.getWidgetID("cancel"));
        ListView listView = (ListView) findViewById(Res.getWidgetID("fileListView"));
        TextView textView = (TextView) findViewById(Res.getWidgetID("headerTitle"));

        bt_cancel.setOnClickListener(new CancelListener());

        textView.setText(Res.getString("photo"));

        folderAdapter = new FolderAdapter(this, imageList);
        
        if (dataList != null && dataList.size() > 0)
            folderAdapter.setFirstPath(dataList.get(0).getImagePath());

        listView.setAdapter(folderAdapter);
        
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                if (position == 0) {
//                    int allSize = 0;
//                    for (int i = 0; i < imageList.size(); i++) {
//                        allSize += imageList.get(i).imageList.size();
//                    }
//                    if (dataList.size() != allSize) {
//                        dataList.clear();
//                        for (int i = 0; i < imageList.size(); i++) {
//                            dataList.addAll(imageList.get(i).imageList);
//                        }
//                    }
                    AlbumHelper.onListChanged(null, imageList, "相机胶卷");
                    setResult(Activity.RESULT_OK);
                    finish();
                    return;
                }
                String folderName = imageList.get(position - 1).bucketName;
                dataList.clear();
                dataList.addAll(imageList.get(position - 1).imageList);
                dirName = folderName;
                AlbumHelper.onListChanged(dataList, imageList, folderName);
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }
    
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(com.xhy.zhanhui.R.anim.activity_anim_default, com.xhy.zhanhui.R.anim.slide_out_to_bottom);
    }

    private class CancelListener implements OnClickListener {// 取消按钮的监听
        public void onClick(View v) {
            // 清空选择的图片
            Bimp.tempSelectBitmap.clear();
            finish();
        }
    }
}
