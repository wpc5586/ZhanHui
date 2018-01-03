package com.xhy.zhanhui.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.base.ZhanHuiApplication;
import com.xhy.zhanhui.http.domain.LoginBean;
import com.xhy.zhanhui.http.vo.UpdatePhoneVo;

/**
 * 填写验证码页面
 * Created by Aaron on 2017/12/27.
 */

public class ConfirmPhoneActivity extends ZhanHuiActivity{

    private String newPhone;
    private TextView tvPhone;
    private EditText etCode;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_confirm_phone;
    }

    @Override
    protected void findView() {
        super.findView();
        setActionbarTitle("填写验证码");
        tvPhone = findViewById(R.id.tvPhone);
        etCode = findViewById(R.id.etCode);
        findAndSetClickListener(R.id.btnComplete);
    }

    @Override
    protected void init() {
        super.init();
        newPhone = getStringExtra("newPhone");
        tvPhone.setText(newPhone.substring(0, 3) + "****" + newPhone.substring(7, 11));
        etCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    complete();
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 完成提交
     */
    private void complete() {
        String code = etCode.getText().toString();
        if (TextUtils.isEmpty(code)) {
            showToast("验证码不可为空");
            return;
        }
        PostCall.putJson(mContext, ServerUrl.updateMobile(), new UpdatePhoneVo(newPhone, code), new PostCall.PostResponse<BaseBean>() {

            @Override
            public void onSuccess(int i, byte[] bytes, BaseBean baseBean) {
                showToast("手机号修改成功");
                ZhanHuiApplication.getInstance().updateUserName(newPhone);
                finish();
            }

            @Override
            public void onFailure(int i, byte[] bytes) {}
        }, new String[]{}, false, BaseBean.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch(view.getId()) {
            case R.id.btnComplete:
                complete();
                break;
        }
    }
}
