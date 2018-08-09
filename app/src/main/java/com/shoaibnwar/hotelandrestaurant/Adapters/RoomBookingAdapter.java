package com.shoaibnwar.hotelandrestaurant.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shoaibnwar.hotelandrestaurant.Activities.CustomCalendar;
import com.shoaibnwar.hotelandrestaurant.Activities.RoomDetailActivity;
import com.shoaibnwar.hotelandrestaurant.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gold on 7/6/2018.
 */

public class RoomBookingAdapter extends RecyclerView.Adapter<RoomBookingAdapter.MyViewHolder>  {

    private ArrayList<HashMap<String, String>> dataArray;
    private Activity mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    public static final int REQUEST_PERMISSION_CODE = 300;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_room_image ;
        //public TextView custome_tv;
        public RelativeLayout rl_single_item;
        private TextView info_text, tv_date, tv_time_to_reach, tv_meeting_time;
        RelativeLayout rl_bt_book_now;
        TextView tv_v_id, tv_max_capacity, tv_room_price;


        public MyViewHolder(final View view) {
            super(view);


            iv_room_image = (ImageView) view.findViewById(R.id.iv_room_image);
            info_text = (TextView) view.findViewById(R.id.info_text);
            rl_bt_book_now = (RelativeLayout) view.findViewById(R.id.rl_bt_book_now);
            tv_v_id = (TextView) view.findViewById(R.id.tv_v_id);
            tv_max_capacity = (TextView) view.findViewById(R.id.tv_max_capacity);
            tv_room_price = (TextView) view.findViewById(R.id.tv_room_price);



        }
    }


    public RoomBookingAdapter(Activity context , ArrayList<HashMap<String, String>> appealList) {
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

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_room_booking_layout, parent, false);
        viewHolder = new MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {

           /* resltdata.put("Vid", Vid);
            resltdata.put("VType", VType);
            resltdata.put("V_ThumbImg", V_ThumbImg);
            resltdata.put("vid", Bed);
            resltdata.put("V_MaxGuest", V_MaxGuest);
            resltdata.put("V_Model", V_Model);
            resltdata.put("V_Title", V_Title);
            resltdata.put("V_City", V_City);
            resltdata.put("V_Lat", V_Lat);
            resltdata.put("V_Long", V_Long);
            resltdata.put("Count", Count);
            resltdata.put("Distance", Distance);
            resltdata.put("Price", Price);*/

            holder.tv_v_id.setText(dataArray.get(position).get("Vid"));
            holder.tv_max_capacity.setText(dataArray.get(position).get("V_MaxGuest"));
            holder.tv_room_price.setText("Rs. " + dataArray.get(position).get("Price"));
           Log.e("TAG", "the image urls are  " + dataArray.get(position).get("V_ThumbImg"));
            if (dataArray.get(position).get("V_ThumbImg").length()>4) {
                Picasso.with(mContext)
                        .load(dataArray.get(position).get("V_ThumbImg"))
                        .placeholder(R.drawable.amu_bubble_shadow)
                        //.fit()
                        .into(holder.iv_room_image);
            }
            if (dataArray.get(position).get("V_Title")!=null) {
                holder.info_text.setText(dataArray.get(position).get("V_Title"));
            }else {
                holder.info_text.setText("Room Available");
            }
            // Glide.with(mContext).load(Integer.valueOf(dataArray.get(position).get("V_ThumbImg"))).into(holder.iv_room_image);

             holder.rl_bt_book_now.setOnClickListener(new View.OnClickListener() {
                 @SuppressLint("NewApi")
                 @Override
                 public void onClick(View v) {

                    Intent intent = new Intent(mContext, CustomCalendar.class);
                    intent.putExtra("room_id", dataArray.get(position).get("Vid"));
                     ActivityOptionsCompat options = ActivityOptionsCompat.
                             makeSceneTransitionAnimation((Activity)mContext, (View)v, "appcard");
                     mContext.startActivity(intent, options.toBundle());
                 }
             });

        }

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
