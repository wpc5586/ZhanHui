package com.xhy.zhanhui.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.easeui.DemoHelper;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.Constants;
import com.aaron.aaronlibrary.utils.VerifyUtils;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.base.ZhanHuiApplication;
import com.xhy.zhanhui.http.domain.FriendBean;
import com.xhy.zhanhui.http.domain.LoginBean;
import com.xhy.zhanhui.http.vo.LoginVo;

/**
 * 登录页面
 * Created by Aaron on 09/12/2017.
 */

public class LoginActivity extends ZhanHuiActivity {

    private RelativeLayout rlUp, rlPhone, rlUser;
    private Button btnLogin;
    private TextView tvCode, tvRegist;
    private EditText etUser, etPwdOrCode;
    private ImageView ivLogo;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void findView() {
        super.findView();
        rlUp = findViewById(R.id.rlUp);
        ivLogo = findViewById(R.id.ivLogo);
        btnLogin = findAndSetClickListener(R.id.login_Btn);
        rlPhone = findAndSetClickListener(R.id.rlPhone);
        rlUser = findAndSetClickListener(R.id.rlUser);
        tvCode = findAndSetClickListener(R.id.tvCode);
        tvRegist = findAndSetClickListener(R.id.tvRegist);
        etUser = findViewById(R.id.login_user_name);
        etPwdOrCode = findViewById(R.id.login_edit_code);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarVisibility(false);
        initWidget();
        etPwdOrCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    login();
                    return true;
                }
                return false;
            }
        });
//        switchLogin(0);
        if (Constants.DEBUGABLE) {
//            etUser.setText("18609817413");
//            etPwdOrCode.setText("123456");
            if (Constants.VERSION_IS_USER) {
                etUser.setText("15600615052"); // 观众
                etPwdOrCode.setText("888888");
            } else {
                etUser.setText("15600615060"); // 企业
                etPwdOrCode.setText("888888");
            }
//            login();
        }
    }

    /**
     * 初始化布局大小
     */
    private void initWidget() {
        int screenW = AppInfo.getScreenWidthOrHeight(mContext, true);
        int screenH = AppInfo.getScreenWidthOrHeight(mContext, false);
        int upH = (int) (screenH / 1.609f);
        rlUp.getLayoutParams().height = upH;
        ivLogo.getLayoutParams().width = (int) (screenW * 0.35f);
    }

    /**
     * 切换登录方式
     * @param index 0：手机号登录 1：账号密码登录
     */
    private void switchLogin(int index) {
        switch (index) {
            case 0:
                rlPhone.findViewById(R.id.bottom1).setVisibility(View.VISIBLE);
                rlUser.findViewById(R.id.bottom2).setVisibility(View.INVISIBLE);
                tvCode.setVisibility(View.VISIBLE);
                etUser.setHint("请输入手机号");
                etUser.setText("");
                etUser.requestFocus();
                etUser.requestFocusFromTouch();
//                etUser.setInputType(InputType.TYPE_CLASS_PHONE);
                etPwdOrCode.setHint("请输入验证码");
                break;
            case 1:
                rlPhone.findViewById(R.id.bottom1).setVisibility(View.INVISIBLE);
                rlUser.findViewById(R.id.bottom2).setVisibility(View.VISIBLE);
                tvCode.setVisibility(View.GONE);
                etUser.setText("");
                etUser.requestFocus();
                etUser.requestFocusFromTouch();
//                etUser.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etUser.setHint("请输入账号");
                etPwdOrCode.setHint("请输入密码");
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        final String phone = etUser.getText().toString();
        String pwd = etPwdOrCode.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不可为空");
            return;
        } else if (!VerifyUtils.isPhone(phone)) {
            showToast("请输入正确的手机号码");
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            showToast("密码不可为空");
            return;
        }
        showProgressDialog("登录中");
        PostCall.postJson(mContext, ServerUrl.login(), new LoginVo(phone, pwd), new PostCall.PostResponse<LoginBean>() {

            @Override
            public void onSuccess(int i, byte[] bytes, LoginBean bean) {
                bean.getObj().setUserName(phone);
                ZhanHuiApplication.getInstance().login(bean);
                getFriends();
                loginEmchat(bean.getObj().getHx_username(), bean.getObj().getHx_password());
//                showToast("登录成功");
//                startMyActivity(MainActivity.class);
//                finish();
            }

            @Override
            public void onFailure(int i, byte[] bytes) {
                if (Constants.DEBUGABLE) {
                    String login = "{\"code\":0,\"data\":{\"hx_password\":\"36664824\",\"hx_username\":\"nebintel1513235133133\",\"icon\":\"http://nebintel02.oss-cn-beijing.aliyuncs.com/timg2.jpg\",\"user_id\":28},\"msg\":\"用户登录成功\"}";
                    LoginBean bean = new Gson().fromJson(login, LoginBean.class);
                    ZhanHuiApplication.getInstance().login(bean);
                    showToast("登录成功");
                    startMyActivity(MainActivity.class);
                    finish();
                }
            }
        }, new String[]{}, false, LoginBean.class);
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
                dismissProgressDialog();
                startMyActivity(MainActivity.class);
                finish();
                showToast("登录成功");
                showLog("登录聊天服务器成功!", "");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                dismissProgressDialog();
                showToast("登录失败，请稍后重试");
                ZhanHuiApplication.getInstance().logout();
                showLog("登录聊天服务器失败!", "");
            }
        });
    }

    /**
     * 获取朋友列表
     */
    private void getFriends() {
        DemoHelper.getInstance().setContactsSyncedWithServer(false);
        DemoHelper.getInstance().setSyncingContactsWithServer(true);
        PostCall.get(mContext, ServerUrl.trustFriends(), new BaseMap(), new PostCall.PostResponse<FriendBean>() {
            @Override
            public void onSuccess(int i, byte[] bytes, FriendBean bean) {
                ZhanHuiApplication.getInstance().setFriendBean(bean);
            }

            @Override
            public void onFailure(int i, byte[] bytes) {
                startMyActivity(MainActivity.class);
            }
        }, new String[]{"", ""}, false, FriendBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rlPhone:
                switchLogin(0);
                break;
            case R.id.rlUser:
                switchLogin(1);
                break;
            case R.id.tvCode:
                showToast("获取验证码");
                break;
            case R.id.tvRegist:
                startMyActivity(RegistActivity.class);
                break;
            case R.id.login_Btn:
                login();
                break;
        }
    }
}
