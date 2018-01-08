package com.xhy.zhanhui.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aaron.aaronlibrary.utils.Constants;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;

/**
 * 欢迎页面（初次打开app显示）
 * Created by Aaron on 2018/1/7.
 */

public class GuideActivity extends ZhanHuiActivity  implements ViewPager.OnPageChangeListener {

    private ViewPager viewPager;

    private View[] views = new View[3];

    private Integer[] mDrawableIds = {Constants.VERSION_IS_USER ? R.mipmap.guide_user : R.mipmap.guide_company,
            Constants.VERSION_IS_USER ? R.mipmap.guide_user : R.mipmap.guide_company,
            Constants.VERSION_IS_USER ? R.mipmap.guide_user : R.mipmap.guide_company};

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    protected void findView() {
        viewPager = findViewById(R.id.viewPager);
    }

    @Override
    protected void init() {
        super.init();
        setActionbarVisibility(false);
        initViewPager();
    }

    /**
     * 设置页面
     */
    private void initViewPager() {
        for (int i = 0; i < mDrawableIds.length; i++) {
            ImageView imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(mDrawableIds[i]);
//            if (i != 2) {
                views[i] = imageView;
            if (i == mDrawableIds.length - 1)
                imageView.setOnClickListener(this);
//            } else {
//                RelativeLayout layout = new RelativeLayout(mContext);
//                layout.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                layout.addView(imageView, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//                Button button = new Button(mContext);
//                button.setGravity(Gravity.CENTER);
//                button.setText("进入经分");
//                button.getPaint().setFakeBoldText(true);
//                button.setTextColor(getColorFromResuource(R.color.white));
//                button.setBackgroundResource(R.drawable.guide_btn);
//                button.setOnClickListener(this);
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MathUtils.dip2px(mContext, 293), MathUtils.dip2px(mContext, 43));
//                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//                params.bottomMargin = MathUtils.dip2px(mContext, 35);
//                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
//                layout.addView(button, params);
//                views[i] = layout;
//            }
        }
        viewPager.setAdapter(new GuideAdapter());
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
//        if (position == 3) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                    InputUtils.toggleSoftInput(mContext);
//                }
//            }, 500);
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views[position]);
        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views[position]);
            return views[position];
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            popAllActivityExceptMain();
            finish();
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }
}
