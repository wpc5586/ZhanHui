package com.xhy.zhanhui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * FragmentAdapter
 * Created by Aaron on 09/12/2017.
 */

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> datas = new ArrayList<>();
    private String[] titles;
    private FragmentManager fm;

    public FragmentAdapter(FragmentManager fm, ArrayList<Fragment> datas, String[] titles) {
        super(fm);
        this.datas = datas;
        this.fm = fm;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int arg0) {
        arg0 = arg0 % this.datas.size();
        Fragment fragmentA = datas.get(arg0);
        return fragmentA;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}