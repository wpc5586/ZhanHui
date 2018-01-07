package com.aaron.aaronlibrary.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.aaron.aaronlibrary.base.activity.EditTextMultipleActivity;
import com.aaron.aaronlibrary.base.activity.EditTextSingleActivity;
import com.aaron.aaronlibrary.utils.Constants;
import com.xhy.zhanhui.R;

/**
 * 输入页面管理
 * @author wangpc
 *
 */
public class EditTextManager{
    
    public static final int TYPE_DEFAULT = 0; // 默认类型
    
    public static final int TYPE_INTEGER = 1; // 数字类型
    
    public static final int TYPE_PHONE = 2; // 电话类型
    
    public static final int TYPE_TEXT = 3; // 文本类型
    
    private TextView textView;
    
    private Context context;
    
    private OnEdittextResultListener onEdittextResultListener;

    public EditTextManager() {
        
    }
    
    /**
     * 开启单行编辑页面
     * @param context 上下文
     * @param title 标题
     * @param textView 当前页面需要改变内容的TextView
     */
    public void startSingleEdittext(Context context, String title, TextView textView) {
        this.textView = textView;
        this.context = context;
        Intent intent = new Intent(context, EditTextSingleActivity.class);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE, title);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE);
    }
    
    /**
     * 开启单行编辑页面，可以选择是否是双行
     * @param context 上下文
     * @param title 标题
     * @param textView 当前页面需要改变内容的TextView
     * @param type 类型
     */
    public void startSingleEdittext(Context context, String title, TextView textView, boolean isTwoRow,
                                    int type, OnEdittextResultListener onEdittextResultListener) {
        this.textView = textView;
        this.context = context;
        this.onEdittextResultListener = onEdittextResultListener;
        Intent intent = new Intent(context, EditTextSingleActivity.class);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE, title);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_TYPE, type);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TWO_ROW, isTwoRow);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE);
    }
    
    /**
     * 开启单行编辑页面
     * @param context 上下文
     * @param title 标题
     * @param textView 当前页面需要改变内容的TextView
     * @param onEdittextResultListener 返回结果监听
     */
    public void startSingleEdittext(Context context, String title, TextView textView,
                                    OnEdittextResultListener onEdittextResultListener) {
        this.textView = textView;
        this.context = context;
        this.onEdittextResultListener = onEdittextResultListener;
        Intent intent = new Intent(context, EditTextSingleActivity.class);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE, title);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE);
    }

    /**
     * 开启单行编辑页面
     * @param context 上下文
     * @param title 标题
     * @param textView 当前页面需要改变内容的TextView
     * @param buttonName 编辑页面右上角按钮的名字
     */
    public void startSingleEdittext(Context context, String title, TextView textView, String buttonName,
                                    OnEdittextResultListener onEdittextResultListener) {
        this.textView = textView;
        this.context = context;
        this.onEdittextResultListener = onEdittextResultListener;
        Intent intent = new Intent(context, EditTextSingleActivity.class);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE, title);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_BUTTONNAME, buttonName);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE);
    }

    /**
     * 开启单行编辑页面
     * @param context 上下文
     * @param title 标题
     * @param textView 当前页面需要改变内容的TextView
     * @param leastNum 内容最少输入的个数
     * @param maxNum 内容最多可输入的个数
     * @param type 类型
     */
    public void startSingleEdittext(Context context, String title, TextView textView, int leastNum, int maxNum, int type,
                                    OnEdittextResultListener onEdittextResultListener) {
        this.textView = textView;
        this.context = context;
        this.onEdittextResultListener = onEdittextResultListener;
        Intent intent = new Intent(context, EditTextSingleActivity.class);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE, title);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_NUM_LEAST, leastNum);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_NUM, maxNum);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_TYPE, type);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE);
    }

    /**
     * 开启多行编辑页面
     * @param context 上下文
     * @param title 标题
     * @param textView 当前页面需要改变内容的TextView
     * @param onEdittextResultListener 返回结果监听
     */
    public void startMultipleEdittext(Context context, String title, TextView textView,
                                      OnEdittextResultListener onEdittextResultListener) {
        this.textView = textView;
        this.context = context;
        this.onEdittextResultListener = onEdittextResultListener;
        Intent intent = new Intent(context, EditTextMultipleActivity.class);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE, title);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE);
    }

    /**
     * 开启多行编辑页面
     * @param context 上下文
     * @param title 标题
     * @param textView 当前页面需要改变内容的TextView
     * @param content 当前的内容
     * @param onEdittextResultListener 返回结果监听
     */
    public void startMultipleEdittext(Context context, String title, TextView textView,
                                      String content, OnEdittextResultListener onEdittextResultListener) {
        this.textView = textView;
        this.context = context;
        this.onEdittextResultListener = onEdittextResultListener;
        Intent intent = new Intent(context, EditTextMultipleActivity.class);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE, title);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_CONTENT, content);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE);
    }

    /**
     * 开启多行编辑页面
     * @param context 上下文
     * @param title 标题
     * @param textView 当前页面需要改变内容的TextView
     * @param content 当前的内容
     * @param canChangeNum 最多可以修改的字数
     * @param onEdittextResultListener 返回结果监听
     */
    public void startMultipleEdittext(Context context, String title, TextView textView,
                                      String content, int canChangeNum, OnEdittextResultListener onEdittextResultListener) {
        this.textView = textView;
        this.context = context;
        this.onEdittextResultListener = onEdittextResultListener;
        Intent intent = new Intent(context, EditTextMultipleActivity.class);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE, title);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_CONTENT, content);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_CHANGENUM, canChangeNum);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE);
    }

    /**
     * 开启多行编辑页面
     * @param context 上下文
     * @param title 标题
     * @param textView 当前页面需要改变内容的TextView
     * @param buttonName 编辑页面右上角按钮的名字
     */
    public void startMultipleEdittext(Context context, String title, TextView textView, String buttonName) {
        this.textView = textView;
        this.context = context;
        Intent intent = new Intent(context, EditTextMultipleActivity.class);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE, title);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_BUTTONNAME, buttonName);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE);
    }

    /**
     * 开启单行编辑页面
     * @param context 上下文
     * @param title 标题
     * @param textView 当前页面需要改变内容的TextView
     * @param maxNum 内容最多可输入的个数
     */
    public void startMultipleEdittext(Context context, String title, TextView textView, int maxNum) {
        this.textView = textView;
        this.context = context;
        Intent intent = new Intent(context, EditTextMultipleActivity.class);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_SINGLE_TITLE, title);
        intent.putExtra(Constants.INTENT_ALL_TO_EDITTEXT_NUM, maxNum);
        ((Activity) context).startActivityForResult(intent, Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE);
    }
    
    /**
     * 设置返回结果（已写在BaseActivity的onActivityResult内）
     * @param requestCode 请求码
     * @param resultCode 结果码
     * @param data 数据
     */
    public void setEdittextResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_ALL_TO_EDITTEXT_SINGLE) {
            if (resultCode == Constants.RESULT_ALL_TO_EDITTEXT_SINGLE) {
                if (data.hasExtra(Constants.INTENT_EDITTEXT_SINGLE_TO_ALL_CONTENT) && textView != null) {
                    String result = data.getStringExtra(Constants.INTENT_EDITTEXT_SINGLE_TO_ALL_CONTENT);
                    textView.setText(result);
                    textView.setTextColor(((Activity) context).getResources().getColor(R.color.content));
                    if (onEdittextResultListener != null) {
                        this.onEdittextResultListener.onEdittextResultListener(result);
                    }
                }
            }
        }
    }
    
    public interface OnEdittextResultListener {
        public void onEdittextResultListener(String result);
    }
}
