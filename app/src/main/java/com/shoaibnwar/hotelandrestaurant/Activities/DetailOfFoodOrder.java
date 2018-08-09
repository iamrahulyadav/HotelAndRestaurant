package com.shoaibnwar.hotelandrestaurant.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.Adapters.Contacts;
import com.shoaibnwar.hotelandrestaurant.Adapters.SelectedFoodOrderDetailAdpater;
import com.shoaibnwar.hotelandrestaurant.Database.FoodOrderDb;
import com.shoaibnwar.hotelandrestaurant.DataseHelper.OrderFoodDbHelper;
import com.shoaibnwar.hotelandrestaurant.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DetailOfFoodOrder extends AppCompatActivity {

    ArrayList<HashMap<String, String>> dataList;
    RecyclerView rc_order_detail;
    LinearLayoutManager linearLayoutManager;
    SelectedFoodOrderDetailAdpater mAdapter;
    RelativeLayout rl_back_arrow;
    TextView tv_total_sum;
    ProgressBar progress_bar;

    int retulstTotalPrrice;

    FoodOrderDb foodOrderDb;
    OrderFoodDbHelper mHelper;
    String trackingId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_of_food_order);

        init();
        onBackArrowPress();

    }

    private void init(){
        rc_order_detail = (RecyclerView) findViewById(R.id.rc_order_detail);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        linearLayoutManager = new LinearLayoutManager(DetailOfFoodOrder.this, LinearLayoutManager.VERTICAL, false);
        rc_order_detail.setLayoutManager(linearLayoutManager);
        tv_total_sum = (TextView) findViewById(R.id.tv_total_sum);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);

        foodOrderDb = new FoodOrderDb(DetailOfFoodOrder.this);
        mHelper = new OrderFoodDbHelper();
        dataList = new ArrayList<>();

        Intent intent = getIntent();
        trackingId = intent.getStringExtra("trackingid");
        LoadOrderDetail loadOrderDetail = new LoadOrderDetail();
        loadOrderDetail.execute();


    }

    private void onBackArrowPress(){
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }



    private int sumPrice(){
        int tempPrice = 0;
        for (int i=0;i<dataList.size();i++){
           String sumprice = dataList.get(i).get("orderSumPrice");
            tempPrice = Integer.parseInt(sumprice);
        }
        return tempPrice;
    }

    public class LoadOrderDetail extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            gettingOrderDetailFromDB(trackingId);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress_bar.setVisibility(View.GONE);
            mAdapter = new SelectedFoodOrderDetailAdpater(DetailOfFoodOrder.this, dataList);
            rc_order_detail.setAdapter(mAdapter);
            retulstTotalPrrice = sumPrice();
            tv_total_sum.setText("Total Sum Rs."+retulstTotalPrrice);
        }
    }

    private void gettingOrderDetailFromDB(String trackingId){

        FoodOrderDb foodOrderDb = new FoodOrderDb(DetailOfFoodOrder.this);
        Log.e("TAg", "the result is " + foodOrderDb.getCount());
        ArrayList<OrderFoodDbHelper> list = foodOrderDb.getOrderByTrackingId(trackingId);
        for (OrderFoodDbHelper helper:list) {

            String id = helper.getId().toString();
            String trackinId = helper.getTrackinId().toString();
            String itemname = helper.getItemname().toString();
            String unitPrice = helper.getUnitPrice().toString();
            String quantities = helper.getQuantities().toString();
            String totalPrice = helper.getTotalPrice().toString();
            String detail = helper.getDetail().toString();
            String orderSumPrice = helper.getOrderSumPrice().toString();
            String itemImagePosition = helper.getItemImagePosition().toString();
            String roomNumber = helper.getRoomNumber().toString();
            String date = helper.getCurrentDate().toString();
            String time = helper.getCurrentTime().toString();
            String assigneStatus = helper.getAssignStatus();


            HashMap<String, String> contactDetail = new HashMap<>();
            contactDetail.put("id", id);
            contactDetail.put("trackinId", trackinId);
            contactDetail.put("itemName", itemname);
            contactDetail.put("itemUnitPrice", unitPrice);
            contactDetail.put("itemQuantity", quantities);
            contactDetail.put("itemTotalPrice", totalPrice);
            contactDetail.put("itemDetail", detail);
            contactDetail.put("orderSumPrice", orderSumPrice);
            contactDetail.put("itemImagePosition", itemImagePosition);
            contactDetail.put("roomNumber", roomNumber);
            contactDetail.put("date", date);
            contactDetail.put("time", time);
            contactDetail.put("assigneStatus", assigneStatus);

            dataList.add(contactDetail);

        }
    }
}
