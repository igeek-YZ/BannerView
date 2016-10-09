package com.igeek.bannerviewlib;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

import java.util.List;

public class BannerAlphaTextView extends TextView implements ViewPager.OnPageChangeListener,BannerViewPager.onViewsChangedListener{

    private BannerViewPager viewPager;
    private List<String> tipTexts;

    public BannerAlphaTextView(Context context) {
        this(context,null);
    }

    public BannerAlphaTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerAlphaTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setViewPager(BannerViewPager viewPager) {
        if(viewPager!=null){
            this.viewPager = viewPager;
            this.viewPager.addOnPageChangeListener(this);
            this.viewPager.addViewsChangedListener(this);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0.5) {
            setText(tipTexts.get((position + 1) % tipTexts.size()));
            ViewHelper.setAlpha(this, positionOffset);
        } else {
            ViewHelper.setAlpha(this, 1 - positionOffset);
            setText(tipTexts.get(position % tipTexts.size()));
        }
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onViewsChanged(BannerViewPager viewPager, int NewCount) {
        if(tipTexts!=null&&NewCount>0){
            int postion=viewPager.getCurrentItem()/NewCount;
            setText(tipTexts.get(postion));
        }
    }

    public void setTipTexts(List<String> tipTexts) {
        this.tipTexts = tipTexts;
    }
}
