package com.aaron.aaronlibrary.base.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.Constants;
import com.aaron.aaronlibrary.utils.MathUtils;

import java.util.ArrayList;

/**
 * 多行输入页面页面
 * @author wangpc
 *
 */
public class EditTextMultipleActivity extends BaseActivity {

    private RelativeLayout container;
    
    private EditText editText;
    
    private TextView tv_num;
    
    private TextView tv_changenum;
    
    private Button btn_save;
    
    private int maxNum = 50;
    
    private int canChangeNum = 6;
    
    private boolean needJudgeChange;
    
    private String startContent;

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }
    
    @Override
    protected void clickRightButton() {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            showToast("输入不可为空");
            return;
        }
        if (needJudgeChange && !judgeChange()) {
            showToast("字数修改不能超过" + canChangeNum + "个字");
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(Constants.INTENT_EDITTEXT_SINGLE_TO_ALL_CONTENT, editText.getText().toString());
        setResult(Constants.RESULT_ALL_TO_EDITTEXT_SINGLE, intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * <p>方法描述: 设置监听</p>
     */
    private void setListeners() {
        editText.addTextChangedListener(new TextWatcher() {
            
            @Override
            public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {
                int surplusLength = maxNum - charSequence.length();
                if (surplusLength >= 0) {
                    tv_num.setText(surplusLength + "");
                } else {
//                    StringUtils.showToast(mContext, "");
                }
            }
            
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                
            }
            
            @Override
            public void afterTextChanged(Editable arg0) {
                
            }
        });
    }

    @Override
    public int getContentLayoutId() {
        return com.xhy.zhanhui.R.layout.activity_edittext_multiple;
    }

    @Override
    public void findView() {
        editText = (EditText) findViewById(com.xhy.zhanhui.R.id.edittext_multiple_et);
        tv_num = (TextView) findViewById(com.xhy.zhanhui.R.id.edittext_multiple_tv_num);
        tv_changenum = (TextView) findViewById(com.xhy.zhanhui.R.id.edittext_multiple_tv_changenum);
        btn_save = (Button) findViewById(com.xhy.zhanhui.R.id.head_right_button);
        container = (RelativeLayout) findViewById(com.xhy.zhanhui.R.id.container);
        setListeners();
    }

    @Override
    public void init() {
        super.init();
        setRightButton("确定");
        actionbarView.setTitleColor(getColorFromResuource(com.xhy.zhanhui.R.color.common_title));
        actionbarView.setTitleSize(MathUtils.dip2px(mContext, 18));
        container.setBackgroundResource(com.xhy.zhanhui.R.drawable.common_bg);
        // 4.4版本以上  并且不是主页  显示statusView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            parent.setBackgroundResource(com.xhy.zhanhui.R.mipmap.common_bottom_bg);
            actionbarView.setBackgroundColor(getColorFromResuource(com.xhy.zhanhui.R.color.transparent));
            statusView.getLayoutParams().height = AppInfo.getStatusBarHeight();
            statusView.setVisibility(View.VISIBLE);
        }
        if (getIntent().hasExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE)) {
            setActionbarTitle(getIntent().getStringExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE));
        }
        if (getIntent().hasExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_CONTENT)) {
            startContent = getIntent().getStringExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_CONTENT);
            editText.setText(startContent);
        }
        if (getIntent().hasExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_CHANGENUM)) {
            canChangeNum = getIntent().getIntExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_CHANGENUM, 6);
            needJudgeChange = true;
            tv_changenum.setVisibility(View.VISIBLE);
            tv_changenum.setText("最多修改" + canChangeNum + "个字");
        }
        if (getIntent().hasExtra(Constants.INTENT_ALL_TO_EDITTEXT_NUM)) {
            maxNum = getIntent().getIntExtra(Constants.INTENT_ALL_TO_EDITTEXT_NUM, 50);
            tv_num.setText(maxNum + "");
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxNum)});
        }
        if (getIntent().hasExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_BUTTONNAME)) {
            btn_save.setText(getIntent().getStringExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_BUTTONNAME));
        }
    }
    
    /**
     * 判断更改字数是否在规定范围内
     * @return boolean
     */
    private boolean judgeChange() {
        String currentContent = editText.getText().toString();
        int changeNum = 0;
        String maxContent = null;
        String minContent = null;
        if (currentContent.length() > startContent.length()) {
            maxContent = currentContent;
            minContent = startContent;
        } else {
            maxContent = startContent;
            minContent = currentContent;
        }
        ArrayList<ContentBean> beans = new ArrayList<ContentBean>();
        for (int i = 0; i < maxContent.length(); i++) {
            char temp = maxContent.charAt(i);
            boolean isHas = false;
            for (int j = 0; j < beans.size(); j++) {
                if (temp == beans.get(j).getContent().charAt(0)) {
                    beans.get(j).addNum();
                    isHas = true;
                    break;
                }
            }
            if (!isHas) {
                beans.add(new ContentBean(String.valueOf(temp)));
            }
        }
        ArrayList<ContentBean> minBeans = new ArrayList<ContentBean>();
        for (int i = 0; i < minContent.length(); i++) {
            char temp = minContent.charAt(i);
            boolean isHas = false;
            for (int j = 0; j < minBeans.size(); j++) {
                if (temp == minBeans.get(j).getContent().charAt(0)) {
                    minBeans.get(j).addNum();
                    isHas = true;
                    break;
                }
            }
            if (!isHas) {
                minBeans.add(new ContentBean(String.valueOf(temp)));
            }
        }
        for (int i = 0; i < beans.size(); i++) {
            ContentBean bean = beans.get(i);
            boolean isHas = false;
            for (int j = 0; j < minBeans.size(); j++) {
                if (bean.getContent().equals(minBeans.get(j).getContent())) {
                    int beanNum = bean.getNum();
                    int minBeanNum = minBeans.get(j).getNum();
                    if (beanNum != minBeanNum) {
                        changeNum += Math.abs(beanNum - minBeanNum);
                        isHas = true;
                        break;
                    }
                    isHas = true;
                }
            }
            if (!isHas) {
                changeNum++;
            }
        }
        return changeNum <= canChangeNum ? true : false; 
    }
    
    public class ContentBean {
        
        private String content;
        private int num = 1;
        
        public ContentBean(String content) {
            this.content = content;
        }
        
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        public int getNum() {
            return num;
        }
        public void setNum(int num) {
            this.num = num;
        }
        public void addNum() {
            this.num++;
        }
    }
}
