package com.calibrage.payzanconsumer.fragement;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.calibrage.payzanconsumer.R;

import java.util.ArrayList;

/**
 * Created by Calibrage25 on 11/22/2017.
 */

public class SlidshowAdapter extends PagerAdapter {
    private ArrayList<Integer> IMAGES;
    private LayoutInflater inflater;
    private Context context;




    public SlidshowAdapter(Context context, ArrayList<Integer> IMAGES) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View view1 = inflater.inflate(R.layout.slidingimages_layout, view, false);

        assert view != null;
        final ImageView imageView = (ImageView) view1.findViewById(R.id.image);


        imageView.setImageResource(IMAGES.get(position));

        view.addView(view1, 0);

        return view1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
