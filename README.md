# 图片轮询视图 BaseAdapter 创建视图

##### BannerViewPager -- 图片轮询视图
##### BannerAlphaTextView -- 文本提示视图
##### CircleIndicator -- 指示器提示视图

##### AspectRatioRelativeLayout 和 AspectRatioImageView
AspectRatio_XXX 可以依据宽度按照比列设置视图的高度

eg:

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
稍等奉上，也可以运行此工程


#### 备注：
1: 此 bannerviewlib 是参照 BGABanner-Android 简单改造而来。其中一个目的为了提示控件和主视图的分离。方便使用，扩展和维护

2: CircleIndicator 是直接从 CircleIndicator 搬过来略微修改了下直接使用的

感谢以上两位的奉献精神。如有问题请与我联系。谢谢