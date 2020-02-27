package com.example.v310.adproject;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

class MyAdapter extends PagerAdapter {

    public ArrayList<ImageView> imageViews;
    public final int IMAGES[] = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,R.drawable.e };


    //点
    ImageView point;


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int relPosition = position%imageViews.size();
        ImageView imageView = imageViews.get(relPosition);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}