package com.aaron.aaronlibrary.base.activity;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.aaron.aaronlibrary.base.utils.PublicMethod;
import com.xhy.zhanhui.R;
import com.xhy.zhanhui.base.ZhanHuiActivity;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 列表基类
 * T 数据Bean
 * V holder
 * Created by wpc on 2017/8/1.
 */
public abstract class BaseListActivity<T, V> extends ZhanHuiActivity {

    protected List<T> datas = new ArrayList<>();

    protected PtrClassicFrameLayout ptrFrameLayout;

    protected ListView listView;

    protected BaseListAdapter adapter;

    protected ImageView tvNotData;

    protected int count = 0;

    protected int page = 1;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_base_list;
    }

    @Override
    protected void findView() {
        listView = (ListView) findViewById(R.id.listView);
        ptrFrameLayout = (PtrClassicFrameLayout) findViewById(R.id.ptrFrameLayout);
        tvNotData = (ImageView) findViewById(R.id.tv_not_data);
    }

    @Override
    protected void init() {
        super.init();
        getData();
    }

    /**
     * 设置分页形式的数据
     */
    protected void setPagingDatas(List<T> data, int totalCount) {
        if (page == 1) {
            datas = data;
            if (datas == null)
                datas = new ArrayList<>();
            this.count = totalCount;
            if (this.count == 0)
                tvNotData.setVisibility(View.VISIBLE);
            else
                tvNotData.setVisibility(View.GONE);
            setListView();
            ptrFrameLayout.setVisibility(View.VISIBLE);
            initPull();
        } else {
            if (ptrFrameLayout != null && ptrFrameLayout.isRefreshing())
                ptrFrameLayout.refreshComplete();
            datas.addAll(data);
            adapter.notifyDataSetChanged();
        }
        isNotMore();
    }

    /**
     * 设置数据
     */
    protected void setDatas(List<T> data) {
        datas = data;
        if (datas == null)
            datas = new ArrayList<>();
        count = data.size();
        if (count == 0)
            tvNotData.setVisibility(View.VISIBLE);
        else
            tvNotData.setVisibility(View.GONE);
        setListView();
        ptrFrameLayout.setVisibility(View.VISIBLE);
        ptrFrameLayout.setMode(PtrFrameLayout.Mode.NONE);
    }

    private void setListView() {
        adapter = new BaseListAdapter(mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                T data = datas.get(position);
                itemClick(data);
            }
        });
    }

    /**
     * 设置上拉下拉
     */
    private void initPull() {
        PublicMethod.setPullView(mContext, ptrFrameLayout, PtrFrameLayout.Mode.LOAD_MORE, new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(final PtrFrameLayout frame) {
                page++;
                getData();
            }

            @Override
            public void onRefreshBegin(final PtrFrameLayout frame) {}
        });
    }

    /**
     * 判断是否没有更多了
     * @return true：没有更多  false：还有下一页
     */
    private boolean isNotMore() {
        boolean isNotMore = datas.size() == count;
        if (isNotMore)
            ptrFrameLayout.setMode(PtrFrameLayout.Mode.NONE);
        return isNotMore;
    }

    protected abstract BaseObject adapterFindViews(ViewGroup parent);
    protected abstract void adapterInit(V holder, T item);
    protected abstract void itemClick(T item);
    protected abstract void getData();

    class BaseListAdapter extends BaseAdapter {

        private Context mContext;

        public BaseListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public T getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            V holder = null;
            if (convertView == null) {
                BaseObject object = adapterFindViews(parent);
                holder = object.holder;
                convertView = object.view;
                convertView.setTag(holder);
            } else {
                holder = (V) convertView.getTag();
            }
            T item = getItem(position);
            adapterInit(holder, item);
            return convertView;
        }
    }

    public class BaseObject {
        public V holder;
        public View view;
    }
}
