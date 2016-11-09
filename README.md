# 图片轮询视图 BaseAdapter 创建视图

##### BannerViewPager -- 图片轮询视图
##### BannerAlphaTextView -- 文本提示视图
##### CircleIndicator -- 指示器提示视图

##### AspectRatioRelativeLayout 和 AspectRatioImageView
AspectRatio_XXX 可以依据宽度按照比列设置视图的高度

activity:

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        indicator= (CircleIndicator) findViewById(R.id.indicator);
        viewPager= (BannerViewPager) findViewById(R.id.bannerViewPager);
        viewPager.setAdapter(new BannerAdapter(buildReIds()));
        indicator.setViewPager(viewPager);
        viewPager.setOnBannerClickListener(new BannerViewPager.OnBannerClickListener() {
            @Override
            public void onBannerClick(View v, int postion) {
                Toast.makeText(MainActivity.this,"postion="+postion,Toast.LENGTH_SHORT).show();
            }
        });
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
                public View getView(final int i, View view, final ViewGroup viewGroup) {
                    if(view==null){
                        view= View.inflate(viewGroup.getContext(),R.layout.layout_item,null);
                    }

                    AspectRatioImageView img= (AspectRatioImageView) view.findViewById(R.id.coverImg);
                    img.setImageResource(reIds.get(i));

                    return view;
                }
    }

xml:

    <com.igeek.bannerview.widget.AspectRatioRelativeLayout
        app:aspectRatio="0.5872"
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <com.igeek.bannerviewlib.BannerViewPager
            android:id="@+id/bannerViewPager"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            android:background="@android:color/white"
            app:transitionAnim="defaultAnim"
            app:allowTouchScrollable="true"
            app:autoPlayAble="true"
            app:autoPlayInterval="3000" />

        <com.igeek.bannerviewlib.CircleIndicator
            android:background="@android:color/holo_red_dark"
            android:id="@+id/indicator"
            android:layout_width="match_parent"
            android:layout_height="40dp"
            android:layout_alignParentBottom="true"
            app:ci_animator="@animator/anim_indicator_scale"
            app:ci_drawable="@mipmap/dot_selected"
            app:ci_drawable_unselected="@mipmap/dot_normal"
            app:ci_gravity="center"
            app:ci_height="8dp"
            app:ci_margin="4dp"
            app:ci_orientation="horizontal"
            app:ci_width="8dp" />

    </com.igeek.bannerview.widget.AspectRatioRelativeLayout>


#### 效果图:

<img src="https://github.com/igeek-YZ/BannerView/blob/master/pic/bannerview.gif" width = "335" height = "559" alt="335" align=center />

#### 属性说明:
    <declare-styleable name="BannerViewPager">
            <!-- 页面停留的时间 -->
            <attr name="autoPlayInterval" format="integer|reference"/>
            <!-- 开启自动播放 -->
            <attr name="autoPlayAble" format="boolean|reference"/>
            <!-- 允许触摸手动滚动 -->
            <attr name="allowTouchScrollable" format="boolean|reference"/>
            <!-- 页面切换动画的时间 -->
            <attr name="transitionDuration" format="integer|reference"/>
            <!-- 页面切换的动画效果 -->
            <attr name="transitionAnim" format="enum">
                <enum name="defaultAnim" value="0" />
                <enum name="alpha" value="1" />
                <enum name="rotate" value="2" />
                <enum name="cube" value="3" />
                <enum name="flip" value="4" />
                <enum name="accordion" value="5" />
                <enum name="zoomFade" value="6" />
                <enum name="fade" value="7" />
                <enum name="zoomCenter" value="8" />
                <enum name="zoomStack" value="9" />
                <enum name="stack" value="10" />
                <enum name="depth" value="11" />
                <enum name="zoom" value="12" />
            </attr>
    </declare-styleable>
#### 备注：
1: 此 bannerviewlib 是参照 BGABanner-Android 简单改造而来。其中一个目的为了提示控件和主视图的分离。方便使用，扩展和维护

2: CircleIndicator 是直接从 CircleIndicator 搬过来略微修改了下直接使用的

感谢以上两位的奉献精神。如有问题请与我联系。谢谢