package com.igeek.bannerviewlib;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class BannerViewAdapter extends PagerAdapter {

    private BaseAdapter viewAdapter;
    private boolean mAutoPlayAble;

    public BannerViewAdapter(boolean mAutoPlayAble) {
        this.mAutoPlayAble = mAutoPlayAble;
    }

    public void setViewAdapter(BaseAdapter viewAdapter) {
        this.viewAdapter = viewAdapter;
    }

    @Override
    public int getCount() {
        return viewAdapter==null?0:(mAutoPlayAble ? Integer.MAX_VALUE : viewAdapter.getCount());
    }


    public int getViewCount(){
        return viewAdapter==null?0:viewAdapter.getCount();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int finalPosition = position % viewAdapter.getCount();
        View view=viewAdapter.getView(finalPosition,null,container);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
