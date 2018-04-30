package com.xhy.zhanhui.base;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;

import com.aaron.aaronlibrary.base.activity.BaseActivity;
import com.aaron.aaronlibrary.easeui.DemoHelper;
import com.aaron.aaronlibrary.easeui.EaseUI;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.EditVcardActivity;

import java.util.HashMap;

/**
 * Activity基类
 * Created by Aaron on 09/12/2017.
 */

public class ZhanHuiActivity extends BaseActivity {
    @Override
    protected int getContentLayoutId() {
        return 0;
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void init() {
        super.init();
        setActionbarBackground(R.color.white);
        setActionbarDividerVisibility(true);
        // 设置状态栏背景颜色
        if (Build.VERSION.SDK_INT < 21)
            initSystemBar();
        EaseUI.getInstance().pushActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(this);
    }

    /**
     * 根据size判断是否显示无数据背景
     *
     * @param size 数据数量
     */
    protected void showNoDataBg(int size) {
        ImageView imageView = findViewById(R.id.ivNoData);
        if (size == 0)
            imageView.setVisibility(View.VISIBLE);
        else
            imageView.setVisibility(View.GONE);
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    protected String getUserId() {
        return ZhanHuiApplication.getInstance().getUserId();
    }

    /**
     * 根据环信ID获取用户ID
     *
     * @return
     */
    protected String getUserIdFromHxId(String hxId) {
        return DemoHelper.getInstance().getContactList().get(hxId).getUserId();
    }

    /**
     * 获取环信用户ID
     *
     * @return
     */
    protected String getHxUserId() {
        return ZhanHuiApplication.getInstance().getHxUserId();
    }

    /**
     * 获取企业用户企业ID
     *
     * @return
     */
    protected String getCompanyId() {
        return ZhanHuiApplication.getInstance().getCompanyId();
    }

    /**
     * 根据用户ID获取环信用户ID
     *
     * @return
     */
    protected String getHxUserIdFromId(String id) {
        return DemoHelper.getInstance().getContactListNe().get(id).getUserId();
    }

    /**
     * 获取用户账号
     *
     * @return
     */
    protected String getUserName() {
        return ZhanHuiApplication.getInstance().getUserName();
    }

    /**
     * 判断当前账户的vcard_id是否为0（系统默认值），如果为0，需要先去设置个人名片，才可显示个人名片或者添加他人信任
     *
     * @return
     */
    protected boolean isVcardIdZero() {
        String vcardId = ZhanHuiApplication.getInstance().getVcardId();
        if ("0".equals(vcardId)) {
            showAlertDialog("", "个人名片信息不完善，请先完善个人名片信息。", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startMyActivity(EditVcardActivity.class);
                }
            }, "", null, false);
            return true;
        } else
            return false;
    }

    /**
     * 获取用户头像
     *
     * @return
     */
    protected String getUserAvatar() {
        return ZhanHuiApplication.getInstance().getIcon();
    }

    /**
     * 判断是否是朋友关系
     *
     * @param userId id
     * @param isHx   true：userId是环信ID false：是本系统ID
     * @return
     */
    protected boolean isFriend(String userId, boolean isHx) {
        if (isHx) {
            return DemoHelper.getInstance().getContactList().get(userId) != null;
        } else {
            return DemoHelper.getInstance().getContactListNe().get(userId) != null;
        }
    }

    /**
     * 获取视频缩略图
     *
     * @param url    地址
     * @param width  宽
     * @param height 高
     * @return
     */
    protected void createVideoThumbnail(final String url, final int width, final int height, final ImageView imageView) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            new Thread() {
//                @Override
//                public void run() {
//                    FFmpegMediaMetadataRetriever fmmr = new FFmpegMediaMetadataRetriever();
////                MediaMetadataRetrieverCompat fmmr = new MediaMetadataRetrieverCompat(MediaMetadataRetrieverCompat.RETRIEVER_ANDROID);
//                    try {
//                        fmmr.setDataSource(url);
//                        Bitmap bitmap = fmmr.getFrameAtTime();
//
//                        if (bitmap != null) {
//                            Bitmap b2 = fmmr
//                                    .getFrameAtTime(
//                                            2000000,
//                                            FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC);
//                            if (b2 != null) {
//                                bitmap = b2;
//                            }
//                            if (bitmap.getWidth() > 640) {// 如果图片宽度规格超过640px,则进行压缩
//                                bitmap = ThumbnailUtils.extractThumbnail(bitmap,
//                                        width, height,
//                                        ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
//                            }
//                            final Bitmap finalBitmap = bitmap;
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (finalBitmap != null)
//                                        imageView.setImageBitmap(finalBitmap);
//                                }
//                            });
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        ex.printStackTrace();
//                    } finally {
//                        fmmr.release();
//                    }
//                }
//            }.start();
//        } else {
            new Thread() {
                @Override
                public void run() {
                    Bitmap bitmap = null;
                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    try {
                        if (Build.VERSION.SDK_INT >= 14) {
                            retriever.setDataSource(url, new HashMap<String, String>());
                        } else {
                            retriever.setDataSource(url);
                        }
                        bitmap = retriever.getFrameAtTime(
                                1000000,
                                MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    } catch (IllegalArgumentException ex) {
                        // Assume this is a corrupt video file
                    } catch (RuntimeException ex) {
                        // Assume this is a corrupt video file.
                    } finally {
                        try {
                            retriever.release();
                        } catch (RuntimeException ex) {
                            // Ignore failures while cleaning up.
                        }
                    }
                    System.out.println("~!~ bit1 = " + bitmap.getByteCount());
                    if (bitmap != null) {
                        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                    }
                    System.out.println("~!~ bit2 = " + bitmap.getByteCount());
                    final Bitmap finalBitmap = bitmap;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("~!~ bit3 = " + finalBitmap.getByteCount());
                            if (finalBitmap != null)
                                imageView.setImageBitmap(finalBitmap);
                        }
                    });
                }
            }.start();
//        }
    }
}
