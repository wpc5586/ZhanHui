package com.xhy.zhanhui.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aaron.aaronlibrary.bean.BaseBean;
import com.aaron.aaronlibrary.http.PostCall;
import com.aaron.aaronlibrary.http.ServerUrl;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;
import com.xhy.zhanhui.http.vo.UpdatePwdVo;

/**
 * 设置密码页面
 * Created by Aaron on 2017/12/27.
 */

public class SettingPwdActivity extends ZhanHuiActivity{

    private TextView tvPhone;
    private EditText etOldPwd, etNewPwd, etConfirmPwd;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_setting_pwd;
    }

    @Override
    protected void findView() {
        super.findView();
        setActionbarTitle("设置密码");
        tvPhone = findViewById(R.id.tvPhone);
        etOldPwd = findViewById(R.id.etOldPwd);
        etNewPwd = findViewById(R.id.etNewPwd);
        etConfirmPwd = findViewById(R.id.etConfirmPwd);
        findAndSetClickListener(R.id.btnComplete);
    }

    @Override
    protected void init() {
        super.init();
        tvPhone.setText(getUserName().substring(0, 3) + "****" + getUserName().substring(7, 11));
    }

    /**
     * 完成提交
     */
    private void complete() {
        String oldPwd = etOldPwd.getText().toString();
        String newPwd = etNewPwd.getText().toString();
        String confirmPwd = etConfirmPwd.getText().toString();
        if (TextUtils.isEmpty(oldPwd)) {
            showToast("旧密码不可为空");
            return;
        } else if (TextUtils.isEmpty(newPwd)) {
            showToast("新密码密码不可为空");
            return;
        } else if (!newPwd.equals(confirmPwd)) {
            showToast("两次密码输入不一样");
            return;
        }
        PostCall.putJson(mContext, ServerUrl.updatePassword(), new UpdatePwdVo(newPwd, oldPwd), new PostCall.PostResponse<BaseBean>() {

            @Override
            public void onSuccess(int i, byte[] bytes, BaseBean baseBean) {
                showToast("密码修改成功");
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
