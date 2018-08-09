package com.shoaibnwar.hotelandrestaurant.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.shoaibnwar.hotelandrestaurant.Activities.MapsActivity;
import com.shoaibnwar.hotelandrestaurant.R;

import java.util.List;

/**
 * Created by gold on 7/12/2018.
 */

public class ViewPagerAdapter extends PagerAdapter
{
    private Context context;
    private LayoutInflater layoutInflater;
    private List<String> urlList;
//    private Integer [] images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4};

    public ViewPagerAdapter(Context context, List<String> urls) {
        this.context = context;
        this.urlList = urls;
    }

    @Override
    public int getCount() {
        return urlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.detail_custom_layout, null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        try {
            final String profile_image_urlVal =  urlList.get(position);

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new MapsActivity.DownloadImageTask(imageView).execute(profile_image_urlVal);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }
}
