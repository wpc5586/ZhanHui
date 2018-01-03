package com.aaron.aaronlibrary.base.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.aaron.aaronlibrary.base.utils.StrictUtils;
import com.aaron.aaronlibrary.manager.EditTextManager;
import com.aaron.aaronlibrary.utils.AppInfo;
import com.aaron.aaronlibrary.utils.Constants;
import com.aaron.aaronlibrary.utils.SystemBarTintManager;
import com.aaron.aaronlibrary.utils.ToastUtil;
import com.aaron.aaronlibrary.widget.ActionbarView;
import com.xhy.zhanhui.R;

import java.io.InputStream;
import java.util.Vector;

public abstract class BaseActivity extends AppCompatActivity implements OnClickListener, OnTouchListener{

    public static String TAG;

    /**
     * 开发阶段将DEBUGABLE设置为true，便于调试。
     */
    protected boolean debug = Constants.DEBUGABLE;

    /**
     * 是否不加载父类onCreate方法
     */
    protected boolean isNotLoadParent;

    /**
     * 新模式关闭页面的判定范围（px）
     */
    private static final int TOUCH_DECISION = 30;

    /**
     * 新模式关闭页面的手指移动判定范围（px）
     */
    private static final int MOVE_DECISION = 30;

    /**
     * 新模式关闭页面的页面缩回动画时间（ms）
     */
    private static final int MOVE_DURATION = 300;

    /**
     * 新模式关闭页面的快速滑动速度判定（px/s）
     */
    private static final int FLING_DECISION = 1500;

    /**
     * <p>变量：Activity的数组对象</p>
     */
    protected static Vector<Activity> oAct = new Vector<Activity>();

    protected boolean isMain;

    protected ProgressDialog progressDialog;
//    protected LoadingDialog progressDialog;

    protected Context mContext;

    protected EditTextManager editTextManager;

    protected BitmapFactory.Options options;

    protected RelativeLayout parent;
    protected View content;

    protected View statusView;

    protected ActionbarView actionbarView;

    /**
     * 内容布局是否在ActionBar下方
     */
    protected boolean contentIsBelow = true;

    /**
     * actionbar最右侧的按钮，默认为null，当设置过（setRightButton）后，保存为最右侧按钮对象
     */
    protected Button rightButton;

    /**
     * 是否设置新模式关闭此Activity（默认是关闭的，新模式是指手指从屏幕左侧边缘向右拖动可关闭此页面）
     */
    protected boolean isNewMode = false;

    /**
     * 判断是否可以关闭等待框（用于多次请求中间不自动关闭等待框）
     */
    protected boolean isCanDismiss = true;

    private GestureDetector gestureDetector;

    private int dx = 0;

    public static final int BACK_END_TIME = 10 * 1000 * 60; // 会话超时时间（10分钟）
    private long backEndTime = 0; // 计算进入后台时间（ms）
    protected boolean isNeedBackEnd = false; // 是否需要后台计算会话超时
    private long backEndBegin = -1; // 进入时间
    private long backEndEnd = -1; // 退出时间

    protected abstract int getContentLayoutId();

    protected abstract void findView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (false) {
            StrictUtils.startThread();
            StrictUtils.startVm();
        }
        super.onCreate(savedInstanceState);
        // 禁止截屏（安全保护）
//        if (!Constants.DEBUGABLE)
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        baseInit();
        if (isNotLoadParent)
            return;
        setContentView(R.layout.activity_base);
        baseFindView();
        setContentLayout(getContentLayoutId());
        findView();
        init();
        setNewModeFinish();
    }

    protected void setContentLayout(int layoutId) {
        if (layoutId == 0)
            return;
        content = View.inflate(mContext, layoutId, null);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        if (contentIsBelow)
            params.addRule(RelativeLayout.BELOW, actionbarView.getId());
        parent.addView(content, params);
        if (!contentIsBelow) {
            LayoutParams paramsStatus = (LayoutParams) statusView.getLayoutParams();
            LayoutParams paramsActionbar = (LayoutParams) actionbarView.getLayoutParams();
            parent.removeView(statusView);
            parent.removeView(actionbarView);
            parent.addView(statusView, paramsStatus);
            parent.addView(actionbarView, paramsActionbar);
            if (isBigerVersion(21))
                statusView.setVisibility(View.VISIBLE);
        }
    }

    protected void setActionbarOnContent(){
        LayoutParams paramsActionbar = (LayoutParams) actionbarView.getLayoutParams();
        parent.removeView(actionbarView);
        parent.addView(actionbarView, paramsActionbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        editTextManager.setEdittextResult(requestCode, resultCode, data);
    }

    protected void baseFindView() {
        parent = findViewById(R.id.parent);
        actionbarView = findViewById(R.id.actionbar);
        statusView = findViewById(R.id.statusbar);
        if (isNewMode)
            actionbarView.getBackButton().setOnTouchListener(this);
    }

    /**
     * 设置新模式（如果不需要新模式，在init中设置isNewMode = false即可）
     */
    protected void setNewModeFinish() {
        if (isNewMode && !isMain)
            parent.setOnTouchListener(this);
    }

    protected void setActionbarBackground(int id) {
        actionbarView.setBackgroundColor(getColorFromResuource(id));
    }

    protected void setActionbarDividerVisibility(boolean visibility) {
        actionbarView.getDividerView().setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    protected void setStatusBarVisibility(boolean visibility) {
        statusView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    protected void setStatusBackground(int id) {
        statusView.setBackgroundColor(getColorFromResuource(id));
    }

    /**
     * 设置actionbar标题
     * @param title 标题内容
     */
    protected void setActionbarTitle(String title) {
        actionbarView.setTitle(title);
    }

    /**
     * 设置actionbar标题颜色
     * @param color 标题颜色
     */
    protected void setActionbarTitleColor(int color) {
        actionbarView.setTitleColor(getColorFromResuource(color));
    }

    public ActionbarView getActionbarView() {
        return actionbarView;
    }

    /**
     * 设置actionbar是否显示
     * @param visibility true:显示 false:隐藏
     */
    protected void setActionbarVisibility(boolean visibility) {
        actionbarView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    protected void baseInit() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        TAG = getClass().getSimpleName();
        mContext = this;
        editTextManager = new EditTextManager();
        if (!isMain) {
            oAct.add(this);
        }
        if (isNewMode)
            gestureDetector = new GestureDetector(mContext, new ActivityFinishListener());
    }

    protected void init() {
        if (getIntent().hasExtra("title"))
            setActionbarTitle(getIntent().getStringExtra("title"));
    }
    @SuppressWarnings("TypeParameterUnusedInFormals")
    protected <T extends View> T findAndSetClickListener(@IdRes int id) {
        View view = findViewById(id);
        view.setOnClickListener(this);
        return (T) view;
    }

    @Override
    protected void onDestroy() {
        oAct.remove(this);
        editTextManager = null;
        System.gc();
        super.onDestroy();
    }

    /**
     * <p>方法描述: 关闭除了MainActivity的所有Activity对象 </p>
     */
    public static void popAllActivityExceptMain() {
        while (oAct.size() > 1) {// 存在Activity对象
            oAct.elementAt(0).finish();// 销毁Activity对象
            oAct.remove(oAct.elementAt(0));// 删除Activity对象
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (backEndBegin == -1 || !isNeedBackEnd)
            return;
        backEndEnd = System.currentTimeMillis();
        backEndTime = backEndEnd - backEndBegin;
        if (backEndTime > BACK_END_TIME && !Constants.DEBUGABLE) {
            showAlertDialog("", "会话超时，请重新登录", "确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    popAllActivityExceptMain();
//                    MainActivity.getInstance().finish();
//                    MainActivity.instance = null;
//                    UserSharedPreferences.getInstance().clean();
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//                    startActivity(intent);
                }
            }, "", null, false);
        }
        backEndBegin = -1;
        backEndEnd = -1;
        backEndTime = 0;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!AppInfo.isAppOnForeground(mContext))
            backEndBegin = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        if (rightButton != null && view.getId() == rightButton.getId()) {
            clickRightButton();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dx = (int) event.getX();
                if (dx <= TOUCH_DECISION)
                    return true;
                break;
            case MotionEvent.ACTION_MOVE:
                int dis = (int) (event.getRawX() - dx);
                if (dx <= TOUCH_DECISION && dis >= MOVE_DECISION) {
                    smoothParent(dis);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                int centerX = parent.getMeasuredWidth() >> 1;
                int nowLeft = parent.getLeft();
                if (nowLeft <= centerX)
                    smoothLeft(nowLeft, MOVE_DURATION);
                else
                    smoothRight(nowLeft, MOVE_DURATION);
                if (nowLeft > 0)
                    return true;
                break;

            default:
                break;
        }
        return view.onTouchEvent(event);
    }

    /**
     * 页面跟随手指移动
     * @param left 页面left
     */
    private void smoothParent(int left) {
        int top = parent.getTop();
        int right = parent.getMeasuredWidth() + left;
        int bottom = parent.getMeasuredHeight();
        parent.layout(left, top, right, bottom);
    }

    /**
     * 页面移回左侧，取消关闭操作
     * @param left 页面left
     * @param duration 动画时间
     */
    private void smoothLeft(int left, int duration) {
        ValueAnimator animator = ObjectAnimator.ofFloat(left, 0);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float left = (float) valueAnimator.getAnimatedValue();
                smoothParent((int) left);
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    /**
     * 页面移到右侧，关闭页面
     * @param nowLeft 页面左边
     * @param duration 动画时间
     */
    private void smoothRight(int nowLeft, int duration) {
        final int end = parent.getMeasuredWidth();
        ValueAnimator animator = ObjectAnimator.ofFloat(nowLeft, end);
        animator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float left = (float) valueAnimator.getAnimatedValue();
                smoothParent((int) left);
                if (left == end) {
                    parent.setVisibility(View.GONE);
                    finish();
                }
            }
        });
        animator.setDuration(duration);
        animator.start();
    }

    /**
     * 设置Actionbar最右侧文字按钮（点击事件需重写clickRightButton方法）
     * @param name 按钮名
     * @return Button 右侧按钮对象
     */
    protected Button setRightButton(String name) {
        rightButton = actionbarView.addRightTextButton(name, this);
        return rightButton;
    }

    /**
     * 最右侧按钮点击事件
     */
    protected void clickRightButton() {

    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public void showProgressDialog(String content) {
        if (progressDialog == null) {
//            progressDialog = LoadingDialog.createDialog(mContext);
            progressDialog = new ProgressDialog(mContext);
//            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(content);
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing() && isCanDismiss) {
            progressDialog.dismiss();
        }
    }

    protected void showLog(String title, String content) {
        if (Constants.DEBUGABLE)
            System.out.println("~!~ " + title + " = " + content);
    }

    public void showToast(final String content){
        runOnUiThread(new Runnable() {
            public void run() {
                ToastUtil.show(mContext, content);
            }
        });
    }

    public void showLongToast(final String content){
        runOnUiThread(new Runnable() {
            public void run() {
                ToastUtil.showLong(mContext, content);
            }
        });
    }

    public void startMyActivity(Class<?> cls) {
        startActivity(new Intent(mContext, cls));
    }

    public void startMyActivity(String title, Class<?> cls) {
        Intent intent = new Intent(mContext, cls);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    protected void startMyActivity(Class<?> cls, int requestCode) {
        startActivityForResult(new Intent(mContext, cls), requestCode);
    }

    /**
     * 方法描述: 获取dimen值
     *
     * @param id 资源文件
     */
    protected int getDimen(int id) {
        return getResources().getDimensionPixelOffset(id);
    }

    /**
     * 方法描述: 获取color值
     *
     * @param id 资源文件
     */
    protected int getColorFromResuource(int id) {
        return getResources().getColor(id);
    }

    /**
     * 方法描述: 从drawable中获取Bitmap
     *
     * @param id 资源文件
     */
    @SuppressWarnings("deprecation")
    protected Bitmap getBitmapFromDrawable(int id) {
        InputStream is = this.getResources().openRawResource(id);
        if (options == null) {
            options = new BitmapFactory.Options();
            options.inTempStorage = new byte[100 * 1024];
            options.inPurgeable = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 2;
            options.inInputShareable = true;
        }
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 方法描述: 从drawable中获取Bitmap
     *
     * @param id 资源文件
     * @param inSampleSize 缩放大小
     */
    @SuppressWarnings("deprecation")
    protected Bitmap getBitmapFromDrawable(int id, int inSampleSize) {
        InputStream is = this.getResources().openRawResource(id);
        if (options == null) {
            options = new BitmapFactory.Options();
            options.inTempStorage = new byte[100 * 1024];
            options.inPurgeable = true;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = inSampleSize;
            options.inInputShareable = true;
        }
        return BitmapFactory.decodeStream(is, null, options);
    }

    private class ActivityFinishListener implements OnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent arg0) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent arg0) {}

        @Override
        public boolean onScroll(MotionEvent ev1, MotionEvent ev2, float distanceX,
                                float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent arg0) {}

        @Override
        public boolean onFling(MotionEvent ev1, MotionEvent ev2, float velocityX,
                               float velocityY) {
            int left = parent.getLeft();
            if (left != 0 && Math.abs(velocityX) > FLING_DECISION) {
                if (velocityX > 0) {
                    int duration = (int) ((parent.getMeasuredWidth() - left) / (velocityX / 1000.0f));
                    smoothRight(left, duration);
                } else {
                    int duration = (int) (left / (-velocityX / 1000.0f));
                    smoothLeft(left, duration);
                }
                return true;
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent arg0) {
            return false;
        }
    }

    /**
     * 设置view的背景
     * @param view
     * @param resourceId 资源id
     */
    protected void setViewBackground(View view, int resourceId) {
        int bottom = view.getPaddingBottom();
        int top = view.getPaddingTop();
        int right = view.getPaddingRight();
        int left = view.getPaddingLeft();
        view.setBackgroundResource(resourceId);
        view.setPadding(left, top, right, bottom);
    }

    public View getStatusView() {
        return statusView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
                return false;
            } else if (isMain) {
                // 进入后台
                moveTaskToBack(false);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 当前系统版本号是否大于等于该版本号（安卓系统版本）
     * @param version 版本号
     * @return true 大于等于  false 小于
     */
    protected boolean isBigerVersion(int version) {
        return Build.VERSION.SDK_INT >= version;
    }

    /**
     * 检查权限
     * @param permission 权限字段
     * @return true 有权限  false 无权限
     */
    protected boolean checkSelfPermissionGranted(String permission) {
        if (isBigerVersion(Build.VERSION_CODES.M)) {
            return mContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else
            return PermissionChecker.checkSelfPermission(mContext, permission) == PermissionChecker.PERMISSION_GRANTED;
    }

    protected void setActionbarMode(int mode) {
        actionbarView.setMode(mode);
    }

    protected void showAlertDialog(String title, String message, String button1, DialogInterface.OnClickListener listener1, String button2, DialogInterface.OnClickListener listener2, boolean cancelable) {
        if (Build.VERSION.SDK_INT >= 21) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setCancelable(cancelable);
            if (!TextUtils.isEmpty(title))
                builder.setTitle(title);
            if (!TextUtils.isEmpty(message))
                builder.setMessage(message);
            if (!TextUtils.isEmpty(button2))
                builder.setPositiveButton(button2, listener2);
            builder.setNegativeButton(button1, listener1).create().show();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext).setCancelable(cancelable);
            if (!TextUtils.isEmpty(title))
                builder.setTitle(title);
            if (!TextUtils.isEmpty(message))
                builder.setMessage(message);
            if (!TextUtils.isEmpty(button2))
                builder.setPositiveButton(button2, listener2);
            builder.setNegativeButton(button1, listener1).create().show();
        }
    }

    protected void showAlertListDialog(String[] items, DialogInterface.OnClickListener listener, boolean cancelable) {
        if (Build.VERSION.SDK_INT >= 21) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext).setCancelable(cancelable);
            builder.setItems(items, listener).create().show();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext).setCancelable(cancelable);
            builder.setItems(items, listener).create().show();
        }
    }

    /**
     * 状态栏颜色调整
     */
    protected void initSystemBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            //设置透明状态栏,这样才能让 ContentView 向上
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);

        // 使用颜色资源
        tintManager.setStatusBarTintResource(getStatusBarColor());//buttom_background
    }

    protected int getStatusBarColor() {
        return R.color.white;
    }

    protected String getStringExtra(String key) {
        if (getIntent().hasExtra(key))
            return getIntent().getStringExtra(key);
        return "";
    }
}
