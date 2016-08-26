package com.fxi.sunset.info.view;

import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;
import com.fxi.sunset.R;
import com.fxi.sunset.info.view.indicator.BannerComponent;
import com.fxi.sunset.info.view.indicator.Indicator;
import com.fxi.sunset.info.view.indicator.IndicatorViewPager;
import com.fxi.sunset.info.view.slidebar.ColorBar;
import com.fxi.sunset.info.view.slidebar.ScrollBar;

import java.util.HashMap;
import java.util.Locale;


public class BannerFragment extends Fragment {

    private BannerComponent bannerComponent;

    private  TextView newsTitle;

    private TextToSpeech t1;

    public BannerFragment() {
        // Requires empty public constructor
    }

    public static BannerFragment newInstance() {
        return new BannerFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.banner_frag, container, false);
        ViewPager viewPager = (ViewPager) root.findViewById(R.id.banner_viewPager);
        Indicator indicator = (Indicator) root.findViewById(R.id.banner_indicator);

        indicator.setScrollBar(new ColorBar(getContext(), Color.WHITE, 0, ScrollBar.Gravity.CENTENT_BACKGROUND));
        viewPager.setOffscreenPageLimit(2);

        newsTitle = (TextView)root.findViewById(R.id.news_title);

        bannerComponent = new BannerComponent(indicator, viewPager, false);
        bannerComponent.setAdapter(adapter);

        t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    // 设置朗读语言
                    int supported = t1.setLanguage(Locale.US);
                    if ((supported != TextToSpeech.LANG_AVAILABLE)
                            && (supported != TextToSpeech.LANG_COUNTRY_AVAILABLE)) {
                        Toast.makeText(getContext(), "不支持当前语言！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


//        root.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                images = new int[]{R.drawable.p2, R.drawable.p3, R.drawable.p4};
//                adapter.notifyDataSetChanged();
//            }
//        });
//        root.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                images = new int[]{R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4};
//                adapter.notifyDataSetChanged();
//            }
//        });
        //默认就是800毫秒，设置单页滑动效果的时间
        bannerComponent.setScrollDuration(1200);
        //设置播放间隔时间，默认情况是3000毫秒
        bannerComponent.setAutoPlayTime(5500);
        return root;
    }


    @Override
    public void onStart() {
        super.onStart();
        bannerComponent.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        bannerComponent.stopAutoPlay();
    }
    final HashMap<String, String> myHashAlarm = new HashMap();

    private int[] images = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4};

    private IndicatorViewPager.IndicatorViewPagerAdapter adapter = new IndicatorViewPager.IndicatorViewPagerAdapter() {

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = new View(container.getContext());
            }
            return convertView;
        }

        @Override
        public View getViewForPage(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = new ImageView(getContext());
                convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
//            Log.e("P","getviewforpage......"+position);
            ImageView imageView = (ImageView) convertView;
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(images[position]);
            newsTitle.setText("   阿萨德家水电费"+position);
//            t1.speak(newsTitle.getText().toString(), TextToSpeech.QUEUE_ADD, myHashAlarm);
            return convertView;
        }

//        @Override
//        public int getItemPosition(Object object) {
//            return RecyclingPagerAdapter.POSITION_NONE;
//        }

        @Override
        public int getCount() {
            return images.length;
        }
    };
}
