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
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.domain.StartActivityUtils;

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
        setActionbarTitle(!TextUtils.isEmpty(nickName) ? nickName : toChatUsername);
        // 设置点击标题跳转对方个人名片
        getActionbarView().getTitleView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartActivityUtils.startVcardNoQRcode(mContext, getUserIdFromHxId(toChatUsername));
            }
        });
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
