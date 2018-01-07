package com.xhy.zhanhui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.BaseMap;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.vo.UpdatePwdVo;

/**
 * 修改绑定手机号页面
 * Created by Aaron on 2017/12/27.
 */

public class SettingPhoneActivity extends ZhanHuiActivity{

    private TextView tvPhone;
    private EditText etNewPhone;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setting_phone;
    }

    @Override
    protected void findView() {
        super.findView();
        setActionbarTitle("绑定手机号");
        tvPhone = findViewById(R.id.tvPhone);
        etNewPhone = findViewById(R.id.etNewPhone);
        findAndSetClickListener(R.id.btnNext);
    }

    @Override
    protected void init() {
        super.init();
        tvPhone.setText(getUserName().substring(0, 3) + "****" + getUserName().substring(7, 11));
        etNewPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    next();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 下一步
     */
    private void next() {
        final String newPhone = etNewPhone.getText().toString();
        if (TextUtils.isEmpty(newPhone)) {
            showToast("新手机号不可为空");
            return;
        }
        showAlertDialog("确认手机号码", "我们将发送短信验证码到这个号码：+86" + newPhone, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }, "好", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getSMSCode();
                finish();
                Intent intent = new Intent(mContext, ConfirmPhoneActivity.class);
                intent.putExtra("newPhone", newPhone);
                startActivity(intent);
            }
        }, true);
    }

    /**
     * 获取验证码
     */
    private void getSMSCode() {
        String newPhone = etNewPhone.getText().toString();
        PostCall.get(mContext, ServerUrl.getSmsCode(newPhone), new BaseMap(), new PostCall.PostResponse<BaseBean>() {

            @Override
            public void onSuccess(int i, byte[] bytes, BaseBean baseBean) {

            }

            @Override
            public void onFailure(int i, byte[] bytes) {}
        }, new String[]{}, false, BaseBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch(view.getId()) {
            case R.id.btnNext:
                next();
                break;
        }
    }
}
