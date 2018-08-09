package com.shoaibnwar.hotelandrestaurant.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
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

import com.shoaibnwar.hotelandrestaurant.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gold on 7/10/2018.
 */

public class SelectedFoodOrderDetailAdpater extends RecyclerView.Adapter<SelectedFoodOrderDetailAdpater.MyViewHolder>  {

    private ArrayList<HashMap<String, String>> dataArray;
    private Activity mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    public static final int REQUEST_PERMISSION_CODE = 300;



    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_item_image ;
        //public TextView custome_tv;
        public RelativeLayout rl_single_item;
        private TextView tv_item_name, tv_item_unit_price, tv_item_quantities, tv_item_detail, tv_item_total_price;



        public MyViewHolder(final View view) {
            super(view);


            iv_item_image = (ImageView) view.findViewById(R.id.iv_item_image);
            tv_item_name = (TextView) view.findViewById(R.id.tv_item_name);
            tv_item_unit_price = (TextView) view.findViewById(R.id.tv_item_unit_price);
            tv_item_quantities = (TextView) view.findViewById(R.id.tv_item_quantities);
            tv_item_detail = (TextView) view.findViewById(R.id.tv_item_detail);
            tv_item_total_price = (TextView) view.findViewById(R.id.tv_item_total_price);
            rl_single_item = (RelativeLayout) view.findViewById(R.id.rl_single_item);

        }
    }


    public SelectedFoodOrderDetailAdpater(Activity context , ArrayList<HashMap<String, String>> appealList) {
        this.mContext = context;
        this.dataArray = appealList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_selected_order_item_detail, parent, false);
        viewHolder = new MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (holder instanceof MyViewHolder) {

            holder.tv_item_name.setText(dataArray.get(position).get("itemName"));
            holder.tv_item_unit_price.setText(dataArray.get(position).get("itemUnitPrice"));
            holder.tv_item_quantities.setText(dataArray.get(position).get("itemQuantity"));
            holder.tv_item_detail.setText(dataArray.get(position).get("itemDetail"));
            holder.tv_item_total_price.setText(dataArray.get(position).get("itemTotalPrice"));
            String itemImagePosition = dataArray.get(position).get("itemImagePosition");
            if (itemImagePosition.equals("1")){Glide.with(mContext).load(R.drawable.bissell_breakfast).into(holder.iv_item_image);}
            if (itemImagePosition.equals("2")){Glide.with(mContext).load(R.drawable.samosas).into(holder.iv_item_image);}
            if (itemImagePosition.equals("3")){Glide.with(mContext).load(R.drawable.coffee).into(holder.iv_item_image);}
            if (itemImagePosition.equals("4")){Glide.with(mContext).load(R.drawable.anda_paratha).into(holder.iv_item_image);}
            if (itemImagePosition.equals("5")){Glide.with(mContext).load(R.drawable.burger).into(holder.iv_item_image);}
            if (itemImagePosition.equals("6")){Glide.with(mContext).load(R.drawable.juice_coffee).into(holder.iv_item_image);}
            if (itemImagePosition.equals("7")){Glide.with(mContext).load(R.drawable.halwa_poori).into(holder.iv_item_image);}
            if (itemImagePosition.equals("8")){Glide.with(mContext).load(R.drawable.patees_burgur).into(holder.iv_item_image);}


            holder.rl_single_item.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("NewApi")
                @Override
                public void onClick(View v) {
/*
                    Intent intent = new Intent(mContext, CustomCalendar.class);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity)mContext, (View)v, "appcard");
                    mContext.startActivity(intent, options.toBundle());*/
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
