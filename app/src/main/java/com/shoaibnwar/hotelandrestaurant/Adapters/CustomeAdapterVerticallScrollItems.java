package com.shoaibnwar.hotelandrestaurant.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.shoaibnwar.hotelandrestaurant.Activities.SingleItemDetailActivityForCabs;
import com.shoaibnwar.hotelandrestaurant.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by gold on 7/12/2018.
 */

public class CustomeAdapterVerticallScrollItems extends RecyclerView.Adapter<CustomeAdapterVerticallScrollItems.MyViewHolder>  {

    private ArrayList<HashMap<String, String>> dataArray;
    private Context mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    public static final int REQUEST_PERMISSION_CODE = 300;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image ;
        //public TextView custome_tv;
        public RelativeLayout rl_single_item;

        public MyViewHolder(final View view) {
            super(view);


            iv_image = (ImageView) view.findViewById(R.id.iv_image);
            rl_single_item = (RelativeLayout) view.findViewById(R.id.rl_single_item);
            // custome_tv = (TextView) view.findViewById(R.id.custome_tv);

        }
    }


    public CustomeAdapterVerticallScrollItems(Context context , ArrayList<HashMap<String, String>> appealList) {
        this.mContext = context;
        this.dataArray = appealList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View itemView = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.pharmacy_custom_row, parent, false);
//        return new MyViewHolder(itemView);

        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_layout_vertical_items, parent, false);
        viewHolder = new MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {

            //holder.custome_tv.setText(bloodAppealList.get(position).get("V_Title"));
            Picasso.with(mContext)
                    .load(dataArray.get(position).get("V_ThumbImg"))
                    .placeholder(R.drawable.amu_bubble_shadow)
                    //.fit()
                    .into(holder.iv_image);
            // Glide.with(mContext).load(bloodAppealList.get(position).get("V_ThumbImg")).into(holder.custome_image);

        }

        holder.rl_single_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String assetId = dataArray.get(position).get("Vid");
                String assetType = dataArray.get(position).get("VType");

                Intent detailActivity = new Intent(mContext, SingleItemDetailActivityForCabs.class);
                detailActivity.putExtra("Vid", assetId);
                detailActivity.putExtra("VType", assetType);
                detailActivity.addFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(detailActivity);

            }
        });

    }


    @Override
    public int getItemCount() {
        return dataArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == dataArray.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

}

