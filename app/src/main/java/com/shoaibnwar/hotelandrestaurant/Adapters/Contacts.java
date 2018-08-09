package com.shoaibnwar.hotelandrestaurant.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.Activities.ActivityFoodOrders;
import com.shoaibnwar.hotelandrestaurant.Activities.DetailOfFoodOrder;
import com.shoaibnwar.hotelandrestaurant.Activities.SingleItemDetailActivity;
import com.shoaibnwar.hotelandrestaurant.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by gold on 7/13/2018.
 */

public class Contacts extends RecyclerView.Adapter<Contacts.MyViewHolder>  {

    private ArrayList<HashMap<String, String>> dataArray;
    private Activity mContext;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    public static final int REQUEST_PERMISSION_CODE = 300;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_image ;
        //public TextView custome_tv;
        public RelativeLayout rl_single_item;
        public TextView tv_id;
        public TextView tv_detail, tv_tracking_id, tv_total_price, tv_room_no, tv_item_qunatities, tv_item_code, tv_item_name, tv_table_id;
        public TextView tv_date, tv_time;
        public TextView tv_assign;
        public LinearLayout rl_assign_now;

        public MyViewHolder(final View view) {
            super(view);


            iv_image = (ImageView) view.findViewById(R.id.iv_image);

            tv_detail = (TextView) view.findViewById(R.id.tv_detail);
            tv_tracking_id = (TextView) view.findViewById(R.id.tv_tracking_id);
            tv_total_price = (TextView) view.findViewById(R.id.tv_total_price);
            tv_room_no = (TextView) view.findViewById(R.id.tv_room_no);
            tv_item_qunatities = (TextView) view.findViewById(R.id.tv_item_qunatities);
            tv_item_code = (TextView) view.findViewById(R.id.tv_item_code);
            tv_item_name = (TextView) view.findViewById(R.id.tv_item_name);
            tv_table_id = (TextView) view.findViewById(R.id.tv_table_id);
            tv_date = (TextView) view.findViewById(R.id.tv_date);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_assign = (TextView) view.findViewById(R.id.tv_assign);
            rl_single_item = (RelativeLayout) view.findViewById(R.id.rl_single_item);
            rl_assign_now = (LinearLayout) view.findViewById(R.id.rl_assign_now);

        }
    }


    public Contacts(Activity context , ArrayList<HashMap<String, String>> appealList) {
        this.mContext = context;
        this.dataArray = appealList;
    }

    @Override
    public Contacts.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Contacts.MyViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.e("TAg", "the view type : " + viewType);

        //View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_layout_vertical_items, parent, false);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custome_contact_list, parent, false);
        viewHolder = new Contacts.MyViewHolder(itemView);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final Contacts.MyViewHolder holder, final int position) {
        if (holder instanceof Contacts.MyViewHolder) {


            //
            String id = dataArray.get(position).get("id");
            String trackinId = dataArray.get(position).get("trackinId");
            String itemname = dataArray.get(position).get("itemname");
            String unitPrice = dataArray.get(position).get("unitPrice");
            String quantities = dataArray.get(position).get("quantities");
            String totalPrice = dataArray.get(position).get("totalPrice");
            String detail = dataArray.get(position).get("detail");
            String orderSumPrice = dataArray.get(position).get("orderSumPrice");
            String itemImagePosition = dataArray.get(position).get("itemImagePosition");
            String roomNumber = dataArray.get(position).get("roomNumber");
            String date = dataArray.get(position).get("date");
            String time = dataArray.get(position).get("time");
            String assignStatus = dataArray.get(position).get("assigneStatus");

            holder.tv_tracking_id.setText(trackinId);
            holder.tv_item_name.setText(itemname);
            holder.tv_total_price.setText("Rs."+orderSumPrice);
            holder.tv_room_no.setText(roomNumber);
            holder.tv_item_qunatities.setText(quantities);
            holder.tv_item_code.setText(trackinId);
            holder.tv_detail.setText(detail);
            holder.tv_table_id.setText(id);
            holder.tv_date.setText(date);
            holder.tv_time.setText(time);
            if (!assignStatus.equals("0")) {
                holder.tv_assign.setText("Assigned");
                holder.tv_assign.setTextColor(mContext.getResources().getColor(R.color.blue));
            }

        }

        holder.rl_single_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String orderTracking = holder.tv_tracking_id.getText().toString();
                Intent intent = new Intent(mContext, DetailOfFoodOrder.class);
                intent.putExtra("trackingid", orderTracking);
                mContext.startActivity(intent);


            }
        });

        holder.rl_assign_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textFromTv = holder.tv_assign.getText().toString();
                if (textFromTv.equals("Assigned")){
                    Intent intent = new Intent(mContext, SingleItemDetailActivity.class);
                    intent.putExtra("status", "1");
                    intent.putExtra("tableId", holder.tv_table_id.getText().toString());
                    mContext.startActivity(intent);
                }
                else if (textFromTv.equals("Assign Now")){

                    Intent intent = new Intent(mContext, SingleItemDetailActivity.class);
                    intent.putExtra("status", "0");
                    intent.putExtra("tableId", holder.tv_table_id.getText().toString());
                    mContext.startActivity(intent);
                }
            }
        });
    }

  /*  @Override
    public int getItemCount() {
        return dataArray.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == dataArray.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }*/

    @Override
    public int getItemCount() {
        return dataArray.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}


