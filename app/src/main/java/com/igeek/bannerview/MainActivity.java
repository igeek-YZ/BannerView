package com.igeek.bannerview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.igeek.bannerview.widget.AspectRatioImageView;
import com.igeek.bannerviewlib.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    BannerViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager= (BannerViewPager) findViewById(R.id.bannerViewPager);
        BannerAdapter adapter=new BannerAdapter(buildReIds());
        viewPager.setAdapter(adapter);
    }

    public List<Integer> buildReIds(){
        List<Integer> reIds=new ArrayList<Integer>();
        reIds.add(R.mipmap.ic_test1);
        reIds.add(R.mipmap.ic_test2);
        reIds.add(R.mipmap.ic_test3);
        return reIds;
    }

    static class BannerAdapter extends BaseAdapter {

        List<Integer> reIds=new ArrayList<Integer>();

        public BannerAdapter(List<Integer> reIds) {
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view==null){
                view= View.inflate(viewGroup.getContext(),R.layout.layout_item,null);
            }

            AspectRatioImageView img= (AspectRatioImageView) view.findViewById(R.id.coverImg);
            img.setImageResource(reIds.get(i));

            return view;
        }
    }
}
