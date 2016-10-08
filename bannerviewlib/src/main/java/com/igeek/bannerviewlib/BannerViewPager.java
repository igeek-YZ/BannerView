package com.igeek.bannerviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.igeek.bannerviewlib.transformer.TransitionEffect;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class BannerViewPager extends ViewPager implements View.OnClickListener{

    private static final int VEL_THRESHOLD = 400;

    private boolean mAllowTouchScrollable;
    private boolean mAutoPlayAble;
    private int mAutoPlayInterval;
    private int mTransitionDuration;
    private int mPageScrollPosition;
    private float mPageScrollPositionOffset;

    private AutoPlayTask mAutoPlayTask;
    private BannerViewAdapter pagerAdapter;

    private List<onViewsChangedListener> viewsListeners;
    private OnBannerClickListener bannerClickListener;

    public BannerViewPager(Context context) {
        this(context,null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.BannerViewPager);
        mAutoPlayAble=ta.getBoolean(R.styleable.BannerViewPager_autoPlayAble,false);
        mAllowTouchScrollable=ta.getBoolean(R.styleable.BannerViewPager_allowTouchScrollable,true);
        mAutoPlayInterval=ta.getInteger(R.styleable.BannerViewPager_autoPlayInterval,3000);
        mTransitionDuration=ta.getInteger(R.styleable.BannerViewPager_transitionDuration,1000);
        int ordinal = ta.getInt(R.styleable.BannerViewPager_transitionAnim,TransitionEffect.Default.ordinal());
        TransitionEffect effect = TransitionEffect.values()[ordinal];
        ta.recycle();

        mAutoPlayTask = new AutoPlayTask(this);
        setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);
        setOffscreenPageLimit(1);
        setPageChangeDuration(mTransitionDuration);
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mPageScrollPosition = position;
                mPageScrollPositionOffset = positionOffset;
            }
            @Override
            public void onPageSelected(int position) {
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        setTransitionEffect(effect);
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            boolean canPlay=pagerAdapter!=null&&pagerAdapter.getViewCount()>1;
            setAutoPlayAble(canPlay);
            setAllowTouchScrollable(canPlay);
            startAutoPlay();
        } else if (visibility == INVISIBLE) {
            stopAutoPlay();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mAllowTouchScrollable) {
            return super.onInterceptTouchEvent(ev);
        } else {
            return false;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mAutoPlayAble) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    startAutoPlay();
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mAllowTouchScrollable) {
            if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_UP) {
                checkTouchToUpdateScrollerPostion(getXVelocity());
                return false;
            } else {
                return super.onTouchEvent(ev);
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoPlay();
    }

    /** 开始轮播 */
    public void startAutoPlay() {
        stopAutoPlay();
        if (mAutoPlayAble) {
            postDelayed(mAutoPlayTask, mAutoPlayInterval);
        }
    }

    /** 停止轮播 */
    public void stopAutoPlay() {
        if (mAutoPlayAble) {
            removeCallbacks(mAutoPlayTask);
        }
    }

    /** 设置View转场动画 */
    public void setTransitionEffect(TransitionEffect effect) {
        setPageTransformer(false, com.igeek.bannerviewlib.transformer.PageTransformer.getPageTransformer(effect));
    }

    @Override
    public void setPageTransformer(boolean reverseDrawingOrder, PageTransformer transformer) {
        Class viewpagerClass = ViewPager.class;
        try {
            boolean hasTransformer = transformer != null;
            Field pageTransformerField = viewpagerClass.getDeclaredField("mPageTransformer");
            pageTransformerField.setAccessible(true);
            PageTransformer mPageTransformer = (PageTransformer) pageTransformerField.get(this);

            boolean needsPopulate = hasTransformer != (mPageTransformer != null);
            pageTransformerField.set(this, transformer);

            Method setChildrenDrawingOrderEnabledCompatMethod = viewpagerClass.getDeclaredMethod("setChildrenDrawingOrderEnabledCompat", boolean.class);
            setChildrenDrawingOrderEnabledCompatMethod.setAccessible(true);
            setChildrenDrawingOrderEnabledCompatMethod.invoke(this, hasTransformer);

            Field drawingOrderField = viewpagerClass.getDeclaredField("mDrawingOrder");
            drawingOrderField.setAccessible(true);
            if (hasTransformer) {
                drawingOrderField.setInt(this, reverseDrawingOrder ? 2 : 1);
            } else {
                drawingOrderField.setInt(this, 0);
            }

            if (needsPopulate) {
                Method populateMethod = viewpagerClass.getDeclaredMethod("populate");
                populateMethod.setAccessible(true);
                populateMethod.invoke(this);
            }
        } catch (Exception e) {
        }
    }

    /** 切换到指定索引的页面，主要用于自动轮播 */
    public void setBannerCurrentItemInternal(int position) {
        Class viewpagerClass = ViewPager.class;
        try {
            Method setCurrentItemInternalMethod = viewpagerClass.getDeclaredMethod("setCurrentItemInternal", int.class, boolean.class, boolean.class);
            setCurrentItemInternalMethod.setAccessible(true);
            setCurrentItemInternalMethod.invoke(this, position, true, true);
            ViewCompat.postInvalidateOnAnimation(this);
        } catch (Exception e) {
        }
    }

    /**
     * 设置是否允许用户手指滑动
     * @param allowTouchScrollable true表示允许跟随用户触摸滑动，false反之
     */
    public void setAllowTouchScrollable(boolean allowTouchScrollable) {
        mAllowTouchScrollable = allowTouchScrollable;
    }

    /**
     * 设置调用setCurrentItem(int item, boolean smoothScroll)方法时，page切换的时间长度
     * @param duration page切换的时间长度
     */
    public void setPageChangeDuration(int duration) {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            scrollerField.set(this, new BannerScroller(getContext(), duration));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 检查用户当前滑动的状态更新视图 */
    public void checkTouchToUpdateScrollerPostion(float xVelocity) {
        if (mPageScrollPosition < getCurrentItem()) {
            // 往右滑
            if (xVelocity > VEL_THRESHOLD || (mPageScrollPositionOffset < 0.7f && xVelocity > -VEL_THRESHOLD)) {
                setBannerCurrentItemInternal(mPageScrollPosition);
            } else {
                setBannerCurrentItemInternal(mPageScrollPosition + 1);
            }
        } else {
            // 往左滑
            if (xVelocity < -VEL_THRESHOLD || (mPageScrollPositionOffset > 0.3f && xVelocity < VEL_THRESHOLD)) {
                setBannerCurrentItemInternal(mPageScrollPosition + 1);
            } else {
                setBannerCurrentItemInternal(mPageScrollPosition);
            }
        }
    }

    /** 获取当前X轴的速率 */
    private float getXVelocity() {
        float xVelocity = 0;
        Class viewpagerClass = ViewPager.class;
        try {
            Field velocityTrackerField = viewpagerClass.getDeclaredField("mVelocityTracker");
            velocityTrackerField.setAccessible(true);
            VelocityTracker velocityTracker = (VelocityTracker) velocityTrackerField.get(this);

            Field activePointerIdField = viewpagerClass.getDeclaredField("mActivePointerId");
            activePointerIdField.setAccessible(true);

            Field maximumVelocityField = viewpagerClass.getDeclaredField("mMaximumVelocity");
            maximumVelocityField.setAccessible(true);
            int maximumVelocity = maximumVelocityField.getInt(this);

            velocityTracker.computeCurrentVelocity(1000, maximumVelocity);
            xVelocity = VelocityTrackerCompat.getXVelocity(velocityTracker, activePointerIdField.getInt(this));
        } catch (Exception e) {
        }
        return xVelocity;
    }

    /** 设置是否开启自动轮播 */
    public void setAutoPlayAble(boolean autoPlayAble) {
        mAutoPlayAble = autoPlayAble;
    }

    /** 设置自动轮播的时间间隔 */
    public void setAutoPlayInterval(int autoPlayInterval) {
        mAutoPlayInterval = autoPlayInterval;
    }

    /** 切换到下一页 */
    private void switchToNextPage() {
        setCurrentItem(getCurrentItem() + 1);
    }

    /** 这里设置适配器 */
    public void setAdapter(BaseAdapter adapter) {
        if(pagerAdapter==null){
            pagerAdapter=new BannerViewAdapter(mAutoPlayAble,this);
        }
        adapter.registerDataSetObserver(dataSetObserver);
        pagerAdapter.setViewAdapter(adapter);
        super.setAdapter(pagerAdapter);
    }

    @Override
    public void onClick(View v) {
        if(bannerClickListener!=null){
            if(pagerAdapter!=null&&pagerAdapter.getViewCount()>0){
                bannerClickListener.onBannerClick(this,getCurrentItem()%pagerAdapter.getViewCount());
            }
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        new IllegalAccessError("please call setAdapter(BaseAdapter) method");
    }

    public void setOnBannerClickListener(OnBannerClickListener clickListener) {
        this.bannerClickListener = clickListener;
    }

    public void addViewsChangedListener(onViewsChangedListener listener) {
        if(listener==null) return ;
        if(viewsListeners==null){
            viewsListeners=new ArrayList<>();
        }
        this.viewsListeners.add(listener);
    }

    static class AutoPlayTask implements Runnable {

        private final WeakReference<BannerViewPager> mBanner;

        private AutoPlayTask(BannerViewPager banner) {
            mBanner = new WeakReference<>(banner);
        }

        @Override
        public void run() {
            BannerViewPager banner = mBanner.get();
            if (banner != null) {
                banner.switchToNextPage();
                banner.startAutoPlay();
            }
        }
    }

    static class BannerScroller extends Scroller {
        private int mDuration = 1000;

        public BannerScroller(Context context, int duration) {
            super(context);
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

    static interface onViewsChangedListener{
        void onViewsChanged(BannerViewPager viewPager,int NewCount);
    }

    public static interface OnBannerClickListener{
        void onBannerClick(View v,int postion);
    }

    final DataSetObserver dataSetObserver=new DataSetObserver() {
        @Override
        public void onChanged() {
            if(pagerAdapter!=null){
                pagerAdapter.notifyDataSetChanged();
                for(onViewsChangedListener listener:viewsListeners){
                    if(listener!=null){
                        listener.onViewsChanged(BannerViewPager.this,pagerAdapter.getViewCount());
                    }
                }
            }
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };
}