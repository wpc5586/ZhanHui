package com.xhy.zhanhui.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.ToastUtil;
import com.aaron.aaronlibrary.utils.VerifyUtils;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.domain.LoginBean;
import com.xhy.zhanhui.http.vo.RegistVo;

/**
 * 注册页面
 * Created by Aaron on 09/12/2017.
 */

public class RegistActivity extends ZhanHuiActivity {

    private RelativeLayout rlUp;
    private TextView btnCode;
    private EditText etUser, etCode, etPwd, etConfirm;
    private ImageView ivLogo;

    private int countDown = 60;
    /**
     * 是否正在倒计时
     */
    private boolean isCountDowning;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_regist;
    }

    @Override
    protected void findView() {
        super.findView();
        rlUp = findViewById(R.id.rlUp);
        ivLogo = findViewById(R.id.ivLogo);
        findAndSetClickListener(R.id.regist_Btn);
        findAndSetClickListener(R.id.tvRegist);
        btnCode = findAndSetClickListener(R.id.tvCode);
        etUser = findViewById(R.id.login_user_name);
        etCode = findViewById(R.id.login_edit_code);
        etPwd = findViewById(R.id.login_edit_pwd);
        etConfirm = findViewById(R.id.login_edit_confirm);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarVisibility(false);
        initWidget();
        etConfirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    regist();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 初始化布局大小
     */
    private void initWidget() {
        int screenW = AppInfo.getScreenWidthOrHeight(mContext, true);
        int screenH = AppInfo.getScreenWidthOrHeight(mContext, false);
        int upH = (int) (screenH * 0.3516f);
        rlUp.getLayoutParams().height = upH;
        ivLogo.getLayoutParams().width = (int) (screenW * 0.42f);
    }

    /**
     * 注册
     */
    private void regist() {
        String phone = etUser.getText().toString();
        String code = etCode.getText().toString();
        String pwd = etPwd.getText().toString();
        String confirm = etConfirm.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            showToast("手机号不可为空");
            return;
        } else if (!VerifyUtils.isPhone(phone)) {
            showToast("请输入正确的手机号码");
            return;
        } else if (TextUtils.isEmpty(confirm)) {
            showToast("验证码不可为空");
            return;
        } else if (TextUtils.isEmpty(pwd)) {
            showToast("密码不可为空");
            return;
        } else if (!pwd.equals(confirm)) {
            showToast("两次密码输入不一样");
            return;
        }
        PostCall.postJson(mContext, ServerUrl.regist(), new RegistVo(phone, pwd, code), new PostCall.PostResponse<BaseBean>() {

            @Override
            public void onSuccess(int i, byte[] bytes, BaseBean baseBean) {
                showToast("用户注册成功");
                finish();
            }

            @Override
            public void onFailure(int i, byte[] bytes) {}
        }, new String[]{}, false, BaseBean.class);
    }

    /**
     * 获取验证码
     */
    private void obtainCode() {
        isCountDowning = true;
        String phone = etUser.getText().toString();
        if (!VerifyUtils.isPhone(phone)) {
            showToast("请输入正确的手机号码");
            return;
        }
        PostCall.get(mContext, ServerUrl.getSmsCode(phone), new BaseMap(), new PostCall.PostResponse<BaseBean>() {
            @Override
            public void onSuccess(int i, byte[] bytes, BaseBean smsBean) {
                ToastUtil.show(mContext, "验证码已发送");
                setObtainCode();
            }

            @Override
            public void onFailure(int i, byte[] bytes) {

            }
        }, new String[]{}, true, BaseBean.class);
    }

    /**
     * 设置获取验证码按钮状态
     */
    private void setObtainCode() {
        final String codeText = "重新获取(";
        if (!isCountDowning)
            return;
        btnCode.setEnabled(false);
        btnCode.setText(codeText + countDown + ")");
        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < 60; i++) {
                    if (!isCountDowning)
                        break;
                    try {
                        sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                btnCode.setText(codeText + --countDown + ")");
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        countDown = 60;
                        btnCode.setEnabled(true);
                        if (isCountDowning)
                            btnCode.setText("重新获取");
                        else
                            btnCode.setText("点击获取验证码");
                    }
                });
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tvCode:
                obtainCode();
                break;
            case R.id.regist_Btn:
                regist();
                break;
            case R.id.tvRegist:
                // TODO 跳转条款页面
                break;
        }
    }
}
