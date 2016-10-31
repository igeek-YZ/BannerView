package com.igeek.bannerviewlib;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class BannerViewAdapter extends PagerAdapter {

    private BaseAdapter viewAdapter;
    private boolean mAutoPlayAble;
    private View.OnClickListener clickListener;
    private List<View> views=new ArrayList<>();

    public BannerViewAdapter(boolean mAutoPlayAble, View.OnClickListener clickListener) {
        this.mAutoPlayAble = mAutoPlayAble;
        this.clickListener = clickListener;
    }

    public void setViewAdapter(BaseAdapter viewAdapter) {
        this.viewAdapter = viewAdapter;
    }

    public BaseAdapter getViewAdapter() {
        return viewAdapter;
    }

    @Override
    public int getCount() {
        return viewAdapter==null||viewAdapter.getCount()==0?0:(mAutoPlayAble ? Integer.MAX_VALUE : viewAdapter.getCount());
    }


    public int getViewCount(){
        return viewAdapter==null?0:viewAdapter.getCount();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final int viewCount=viewAdapter.getCount();
        final int finalPosition = viewCount==0?0:position % viewCount;
        View view=null;
        if(position<viewCount||finalPosition>=views.size()){
            view=viewAdapter.getView(finalPosition,null,container);
            views.add(view);
        }else{
            view=views.get(finalPosition);
        }

        if(view.getParent()!=null)
            ((ViewGroup)view.getParent()).removeView(view);

        if(clickListener!=null)
            view.setOnClickListener(clickListener);

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
