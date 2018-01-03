package com.aaron.aaronlibrary.listener;

import android.view.View;
import android.widget.ImageView;

import com.aaron.aaronlibrary.photoutil.ImageItem;

import java.util.ArrayList;

public class BaseImageListener {

    public interface OnAlbumNextListener {
        public void onAlbumNext(ArrayList<ImageItem> tempSelectBitmap);
    }
    
    public interface onRightClickListener {
        public void onRightClick(View view, int position);
    }
    
    public interface onSelectClickListener {
        public void onSelectClick(ImageView imageView, int position, boolean state);
    }
}
