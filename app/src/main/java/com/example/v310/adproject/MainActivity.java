package com.example.v310.adproject;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import io.vov.vitamio.LibsChecker;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.VideoView;

public class MainActivity extends AppCompatActivity
{

    TextView AD_Text;
    private static ViewPager viewPager;
    private LinearLayout point_ll;
    //点
    ImageView point;
    MyAdapter myAdapter;
    public static MarqueeView mtextview;
    private int width,height;






    public  static Handler handler = new Handler() {
        @Override

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==1)
            {
            }
            int item = viewPager.getCurrentItem() + 1;
            viewPager.setCurrentItem(item);
            handler.sendEmptyMessageDelayed(0, 3000);


        }
    };

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        getRealMetrics();
        if(width>=height) {
            setContentView(R.layout.activity_main);
        }
        else
            setContentView(R.layout.atcvity_mian_su);

        ButterKnife.bind(this);
        Video_Thread video_thread = new Video_Thread();
        video_thread.AD_Video = (VideoView) findViewById(R.id.videoView);

        viewPager = findViewById(R.id.viewpager);
        point_ll = findViewById(R.id.point_ll);
        mtextview = findViewById(R.id.mMarqueeView);
        Location location=new Location(MainActivity.this);
        //mtextview.setText(getString(R.string.AD_Text));
        AddImageArray();
        if (!LibsChecker.checkVitamioLibs(this)) {
            return;
        }


        video_thread.start();

    }

    public void getRealMetrics()

    {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
         width = metrics.widthPixels;
         height = metrics.heightPixels;
    }




    void AddImageArray() {
        myAdapter = new MyAdapter();
        myAdapter.imageViews = new ArrayList<>();
        for (int i = 0; i < myAdapter.IMAGES.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(myAdapter.IMAGES[i]);
            myAdapter.imageViews.add(imageView);

            //添加点
            point = new ImageView(this);
            point_ll.addView(point);

        }
        viewPager.setAdapter(myAdapter);
        int item = Integer.MAX_VALUE / 2 - Integer.MAX_VALUE / 2 % myAdapter.imageViews.size();
        viewPager.setCurrentItem(item);
        handler.sendEmptyMessageDelayed(0, 3000);

    }



}
