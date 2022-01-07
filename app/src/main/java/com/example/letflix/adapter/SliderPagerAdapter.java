package com.example.letflix.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.letflix.R;
import com.example.letflix.model.DATAMAIN;
import com.example.letflix.model.Slide;
import com.example.letflix.model.Trending;

import java.util.List;

public class SliderPagerAdapter extends PagerAdapter {
    private Context mContext ;
    private List<Trending> mList ;
    public SliderPagerAdapter(Context mContext, List<Trending> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout = inflater.inflate(R.layout.slide_item,null);

        ImageView slideImg = slideLayout.findViewById(R.id.slide_img);
        TextView slideText = slideLayout.findViewById(R.id.slide_title);

        Glide.with(slideText.getContext()).load(DATAMAIN.movies.get(mList.get(position).value).rawImg).into(slideImg);
        slideText.setText(DATAMAIN.movies.get(mList.get(position).value).name);

        container.addView(slideLayout);
        return slideLayout;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
