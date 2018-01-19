package com.aaron.aaronlibrary.easeui.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.aaron.aaronlibrary.easeui.DemoHelper;
import com.aaron.aaronlibrary.easeui.base.EaseBaseActivity;
import com.aaron.aaronlibrary.easeui.base.EaseChatFragment;
import com.aaron.aaronlibrary.easeui.db.DemoDBManager;
import com.aaron.aaronlibrary.easeui.domain.EaseUser;
import com.aaron.aaronlibrary.easeui.runtimepermissions.PermissionsManager;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.activity.MainActivity;
import com.xhy.zhanhui.domain.StartActivityUtils;
import com.xhy.zhanhui.http.domain.HxFriendBean;

import java.util.HashMap;
import java.util.Map;

/**
 * chat activity，EaseChatFragment was used {@link ChatFragment}
 *
 */
public class ChatActivity extends EaseBaseActivity {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;
    String nickName;

    @Override
    protected int getContentLayoutId() {
        return R.layout.em_activity_chat;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
    }

    @Override
    protected void init() {
        super.init();
        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userId");
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

        nickName = getStringExtra("nickName");
        if (DemoHelper.getInstance().getContactList().get(toChatUsername) != null && !TextUtils.isEmpty(DemoHelper.getInstance().getContactList().get(toChatUsername).getNickname()))
            nickName = DemoHelper.getInstance().getContactList().get(toChatUsername).getNickname();
        else if (DemoHelper.getInstance().getTempContactList().get(toChatUsername) != null && !TextUtils.isEmpty(DemoHelper.getInstance().getTempContactList().get(toChatUsername).getNickname()))
            nickName = DemoHelper.getInstance().getTempContactList().get(toChatUsername).getNickname();
        if (!TextUtils.isEmpty(nickName)) {
            setActionbarTitle(nickName);
        } else {
            setActionbarTitle("获取昵称中...");
            // 通过服务器获取该环信ID的用户信息
            PostCall.get(mContext, ServerUrl.hxidFriend(toChatUsername), new BaseMap(), new PostCall.PostResponse<HxFriendBean>() {
                @Override
                public void onSuccess(int i, byte[] bytes, HxFriendBean bean) {
                    HxFriendBean.Obj data = bean.getData();
                    setActionbarTitle(data.getNickname());
                    EaseUser user = new EaseUser(toChatUsername);
                    user.setAvatar(data.getIcon());
                    user.setNickname(data.getNickname());
                    user.setUserId(data.getUser_id());
                    user.setV_title(data.getV_title());
                    if (!DemoHelper.getInstance().getTempContactList().containsKey(toChatUsername))
                        DemoHelper.getInstance().getTempContactList().put(toChatUsername, user);
                }

                @Override
                public void onFailure(int i, byte[] bytes) {

                }
            }, new String[]{"", ""}, false, HxFriendBean.class);
        }
        // 设置点击标题跳转对方个人名片
        getActionbarView().getTitleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                StartActivityUtils.startVcardNoQRcode(mContext, getUserIdFromHxId(toChatUsername));
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	// make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        chatFragment.onBackPressed();
//        if (EasyUtils.isSingleActivity(this)) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//        }
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
