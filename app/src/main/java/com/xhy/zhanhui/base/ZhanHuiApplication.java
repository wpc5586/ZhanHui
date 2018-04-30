package com.xhy.zhanhui.base;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.aaron.aaronlibrary.base.app.CrashApplication;
import com.aaron.aaronlibrary.easeui.DemoHelper;
import com.aaron.aaronlibrary.easeui.db.DemoDBManager;
import com.aaron.aaronlibrary.easeui.domain.EaseUser;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.xhy.zhanhui.activity.MainActivity;
import com.xhy.zhanhui.http.domain.FriendBean;
import com.xhy.zhanhui.http.domain.LoginBean;
import com.xhy.zhanhui.http.domain.VersionBean;
import com.xhy.zhanhui.preferences.TrustSharedPreferences;
import com.xhy.zhanhui.preferences.UserSharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by Aaron on 09/12/2017.
 */

public class ZhanHuiApplication extends CrashApplication {

    public static Context applicationContext;
    private static ZhanHuiApplication instance;
    private LoginBean loginBean;
    private VersionBean versionBean;
    private FriendBean friendBean;
    private DemoHelper.DataSyncListener listener; // 联系人获取成功监听
    // login user name
    public final String PREF_USERNAME = "username";

    /**
     * nickname for current user, the nickname instead of ID be shown when user receive notification from APNs
     */
    public static String currentUserNick = "";

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
// ============== fabric start
//		Fabric.with(this, new Crashlytics());
// ============== fabric end
        applicationContext = this;
        instance = this;
//
//        //init demo helper
        DemoHelper.getInstance().init(applicationContext);
//        //red packet code : 初始化红包SDK，开启日志输出开关
//        RedPacket.getInstance().initRedPacket(applicationContext, RPConstant.AUTH_METHOD_EASEMOB, new RPInitRedPacketCallback() {
//
//            @Override
//            public void initTokenData(RPValueCallback<TokenData> callback) {
//                TokenData tokenData = new TokenData();
//                tokenData.imUserId = EMClient.getInstance().getCurrentUser();
//                //此处使用环信id代替了appUserId 开发者可传入App的appUserId
//                tokenData.appUserId = EMClient.getInstance().getCurrentUser();
//                tokenData.imToken = EMClient.getInstance().getAccessToken();
//                //同步或异步获取TokenData 获取成功后回调onSuccess()方法
//                callback.onSuccess(tokenData);
//            }
//
//            @Override
//            public RedPacketInfo initCurrentUserSync() {
//                //这里需要同步设置当前用户id、昵称和头像url
//                String fromAvatarUrl = "";
//                String fromNickname = EMClient.getInstance().getCurrentUser();
//                EaseUser easeUser = EaseUserUtils.getUserInfo(fromNickname);
//                if (easeUser != null) {
//                    fromAvatarUrl = TextUtils.isEmpty(easeUser.getAvatar()) ? "none" : easeUser.getAvatar();
//                    fromNickname = TextUtils.isEmpty(easeUser.getNick()) ? easeUser.getUsername() : easeUser.getNick();
//                }
//                RedPacketInfo redPacketInfo = new RedPacketInfo();
//                redPacketInfo.fromUserId = EMClient.getInstance().getCurrentUser();
//                redPacketInfo.fromAvatarUrl = fromAvatarUrl;
//                redPacketInfo.fromNickName = fromNickname;
//                return redPacketInfo;
//            }
//        });
//        RedPacket.getInstance().setDebugMode(true);
        //end of red packet code
        login(UserSharedPreferences.getInstance().getLoginData());
    }

    public static ZhanHuiApplication getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public String getCompanyId() {
        return loginBean == null ? "" : loginBean.getObj().getCompany_id();
    }

    public String getUserId() {
        return loginBean == null ? "" : loginBean.getObj().getUser_id();
    }

    public String getHxUserId() {
        return loginBean == null ? "" : loginBean.getObj().getHx_username();
    }

    public String getUserName() {
        return loginBean == null ? "" : loginBean.getObj().getUserName();
    }

    public String getVcardId() {
        return loginBean == null ? "0" : loginBean.getObj().getVcard_id();
    }

    public String getIcon() {
        return loginBean == null ? "" : loginBean.getObj().getIcon();
    }
    public String getNickname() {
        return loginBean == null ? "" : loginBean.getObj().getNickname();
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void login(LoginBean loginBean) {
        this.loginBean = loginBean;
        saveLoginInfo();
    }

    /**
     * 更新缓存VcardId
     * @param vcardId VcardId
     */
    public void updateVcardId(String vcardId) {
        this.loginBean.getObj().setVcard_id(vcardId);
        saveLoginInfo();
    }

    /**
     * 更新缓存用户头像
     * @param icon
     */
    public void updateAvatar(String icon) {
        this.loginBean.getObj().setIcon(icon);
        saveLoginInfo();
        MainActivity.getInstance().refreshAvatar();
    }

    /**
     * 更新缓存用户名（登录用户名，也是电话号）
     * @param userName
     */
    public void updateUserName(String userName) {
        this.loginBean.getObj().setUserName(userName);
        saveLoginInfo();
        MainActivity.getInstance().refreshName();
    }

    /**
     * 更新缓存用户昵称
     * @param nickName
     */
    public void updateNickName(String nickName) {
        this.loginBean.getObj().setNickname(nickName);
        saveLoginInfo();
        MainActivity.getInstance().refreshName();
    }

    public VersionBean getVersionBean() {
        return versionBean;
    }

    public void setVersionBean(VersionBean versionBean) {
        this.versionBean = versionBean;
    }

    public void saveLoginInfo() {
        new Thread() {
            @Override
            public void run() {
                UserSharedPreferences.getInstance().setLoginData(loginBean);
            }
        }.start();
    }

    public void cleanLoginInfo() {
        new Thread() {
            @Override
            public void run() {
                UserSharedPreferences.getInstance().clean();
                TrustSharedPreferences.getInstance().clean();
            }
        }.start();
    }

    public void logout() {
        loginBean = null;
        new Thread(){
            @Override
            public void run() {
                cleanLoginInfo();
                DemoHelper.getInstance().logout(true, null);
                listener = null;
            }
        }.start();
    }

    /**
     * 登录环信
     */
    public void loginEmchat() {
        loginEmchat(loginBean.getObj().getHx_username(), loginBean.getObj().getHx_password());
    }

    /**
     * 登录环信
     *
     * @param userName 用户名
     * @param password 密码
     */
    private void loginEmchat(String userName, String password) {
        EMClient.getInstance().login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
            }

            @Override
            public void onProgress(int progress, String status) {}

            @Override
            public void onError(int code, String message) {}
        });
    }

    public FriendBean getFriendBean() {
        return friendBean;
    }

    public void setFriendBean(FriendBean friendBean) {
        this.friendBean = friendBean;
        List<EaseUser> users = new ArrayList<>();
        for (int i = 0; i < friendBean.getData().size(); i++) {
            FriendBean.Obj data = friendBean.getData().get(i);
            EaseUser user = new EaseUser(data.getHx_username());
            user.setAvatar(data.getIcon());
            user.setUserId(data.getUser_id());
            user.setV_title(data.getV_title());
            user.setNickname(data.getNickname());
            users.add(user);
        }
        DemoDBManager.getInstance().saveContactList(users);
        DemoHelper.getInstance().setContactsSyncedWithServer(true);
        DemoHelper.getInstance().setSyncingContactsWithServer(false);
        if (listener != null)
            listener.onSyncComplete(true);
    }

    public void setSyncContactListener(DemoHelper.DataSyncListener listener) {
        this.listener = listener;
    }
}
