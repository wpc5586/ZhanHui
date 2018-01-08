package com.aaron.aaronlibrary.easeui.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.aaron.aaronlibrary.easeui.DemoHelper;
import com.aaron.aaronlibrary.easeui.EaseUI;
import com.aaron.aaronlibrary.easeui.domain.EaseUser;
import com.aaron.aaronlibrary.utils.ImageUtils;
import com.bumptech.glide.Glide;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiApplication;

import java.util.Map;

public class EaseUserUtils {

    static EaseUI.EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        Map<String, EaseUser> contactList = DemoHelper.getInstance().getContactList();
        Map<String, EaseUser> tempContactList = DemoHelper.getInstance().getTempContactList();
        if (imageView != null) {
            if (username.equals(ZhanHuiApplication.getInstance().getHxUserId())) {
                ImageUtils.loadImageCircle(context, ZhanHuiApplication.getInstance().getIcon(), imageView);
            } else if (contactList.get(username) != null && !TextUtils.isEmpty(contactList.get(username).getAvatar())) {
                ImageUtils.loadImageCircle(context, contactList.get(username).getAvatar(), imageView);
            } else if (tempContactList.get(username) != null && !TextUtils.isEmpty(tempContactList.get(username).getNickname())){
                ImageUtils.loadImageCircle(context, tempContactList.get(username).getAvatar(), imageView);
            } else{
                Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
            }
        }
//        EaseUser user = getUserInfo(username);
//        if (user != null && user.getAvatar() != null) {
//            try {
//                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(context).load(avatarResId).into(imageView);
//            } catch (Exception e) {
//                //use default avatar
//                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
//            }
//        } else {
//            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
//        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        Map<String, EaseUser> contactList = DemoHelper.getInstance().getContactList();
        Map<String, EaseUser> tempContactList = DemoHelper.getInstance().getTempContactList();
        if (textView != null) {
            System.out.println("~!~  = " + username + ", = " + contactList.get(username));
            if (contactList.get(username) != null && !TextUtils.isEmpty(contactList.get(username).getNickname())) {
                textView.setText(contactList.get(username).getNickname());
            } else if (tempContactList.get(username) != null && !TextUtils.isEmpty(tempContactList.get(username).getNickname())){
                textView.setText(tempContactList.get(username).getNickname());
            } else{
                textView.setText("获取昵称中...");
            }
        }
    }

}
