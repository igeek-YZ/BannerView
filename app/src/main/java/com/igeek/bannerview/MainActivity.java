package com.igeek.bannerview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.igeek.bannerview.widget.AspectRatioImageView;
import com.igeek.bannerviewlib.BannerViewPager;
import com.igeek.bannerviewlib.CircleIndicator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    BannerViewPager viewPager;
    CircleIndicator indicator;
    BannerAdapter adapter;
    BannerViewPager viewPager2;
    CircleIndicator indicator2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        indicator= (CircleIndicator) findViewById(R.id.indicator);
        viewPager= (BannerViewPager) findViewById(R.id.bannerViewPager);
        adapter=new BannerAdapter(buildReIds());
        viewPager.setAdapter(adapter);
        indicator.setViewPager(viewPager);
        viewPager.setOnBannerClickListener(new BannerViewPager.OnBannerClickListener() {
            @Override
            public void onBannerClick(View v, int postion) {
                Toast.makeText(MainActivity.this,"postion="+postion,Toast.LENGTH_SHORT).show();
            }
        });

        indicator2= (CircleIndicator) findViewById(R.id.indicator2);
        viewPager2= (BannerViewPager) findViewById(R.id.bannerViewPager2);
        viewPager2.setAdapter(new BannerAdapter(buildReIds()));
        indicator2.setViewPager(viewPager2);
        viewPager2.setOnBannerClickListener(new BannerViewPager.OnBannerClickListener() {
            @Override
            public void onBannerClick(View v, int postion) {
                Toast.makeText(MainActivity.this,"postion="+postion,Toast.LENGTH_SHORT).show();
            }
        });
        viewPager.startAutoPlay();
        viewPager2.startAutoPlay();
    }

    public List<Integer> buildReIds(){
        List<Integer> reIds=new ArrayList<Integer>();
        reIds.add(R.mipmap.ic_test1);
        reIds.add(R.mipmap.ic_test3);
        reIds.add(R.mipmap.ic_test2);
        return reIds;
    }

    public List<Integer> buildReIds2(){
        List<Integer> reIds=new ArrayList<Integer>();
        reIds.add(R.mipmap.all);
        reIds.add(R.mipmap.cart);
        reIds.add(R.mipmap.comments);
        reIds.add(R.mipmap.category);
        return reIds;
    }

    static class BannerAdapter extends BaseAdapter {

        List<Integer> reIds=new ArrayList<Integer>();

        public BannerAdapter(List<Integer> reIds) {
            this.reIds = reIds;
        }

        public List<Integer> getReIds() {
            return reIds;
        }

        public void setReIds(List<Integer> reIds) {
            this.reIds = reIds;
        }

        @Override
        public int getCount() {
            return reIds.size();
        }

        @Override
        public Object getItem(int i) {
            return reIds.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, final ViewGroup viewGroup) {
            if(view==null){
                view= View.inflate(viewGroup.getContext(),R.layout.layout_item,null);
            }

            AspectRatioImageView img= (AspectRatioImageView) view.findViewById(R.id.coverImg);
            img.setImageResource(reIds.get(i));

            return view;
        }
    }
}
