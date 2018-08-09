package com.shoaibnwar.hotelandrestaurant.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.R;

import java.util.List;

/**
 * Created by gold on 7/11/2018.
 */

public class MoreItemsAdapter extends RecyclerView.Adapter<MoreItemsAdapter.MyViewHolder> {

    String[] titles;
    int[] images;

    public MoreItemsAdapter(String[] title, int[] image)
    {
        titles = title;
        images = image;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private ImageView img;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_adapter);
            img = (ImageView) view.findViewById(R.id.img_adapter);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.more_items_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(titles[position]);
        holder.img.setImageResource(images[position]);
     /*   if (position == 5)
        {
            holder.img.setImageResource(images[position]);
//            holder.img.setColorFilter(R.color.grey);
        }
        else
        {
            holder.img.setImageResource(images[position]);
        }*/
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }
}

