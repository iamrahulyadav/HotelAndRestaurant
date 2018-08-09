package com.shoaibnwar.hotelandrestaurant.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class FoodSelectedOrderDetailActivity extends AppCompatActivity {

    ArrayList<HashMap<String, String>> dataList;
    RecyclerView rc_order_detail;
    LinearLayoutManager linearLayoutManager;
    SelectedFoodOrderDetailAdpater mAdapter;
    RelativeLayout rl_back_arrow;
    RelativeLayout tv_confirm;
    TextView tv_total_sum;

    int retulstTotalPrrice;

    FoodOrderDb foodOrderDb;
    OrderFoodDbHelper mHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order_detail);

        init();
        onBackArrowPress();
        confirmOrderClickHandler();

    }
    private void init(){
        rc_order_detail = (RecyclerView) findViewById(R.id.rc_order_detail);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        tv_confirm = (RelativeLayout) findViewById(R.id.tv_confirm);
        linearLayoutManager = new LinearLayoutManager(FoodSelectedOrderDetailActivity.this, LinearLayoutManager.VERTICAL, false);
        rc_order_detail.setLayoutManager(linearLayoutManager);
        tv_total_sum = (TextView) findViewById(R.id.tv_total_sum);

        dataList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("mylist");
        mAdapter = new SelectedFoodOrderDetailAdpater(FoodSelectedOrderDetailActivity.this, dataList);
        rc_order_detail.setAdapter(mAdapter);
        retulstTotalPrrice = sumPrice();
        tv_total_sum.setText("Total Sum Rs."+retulstTotalPrrice);

        foodOrderDb = new FoodOrderDb(FoodSelectedOrderDetailActivity.this);
        mHelper = new OrderFoodDbHelper();
    }

    private void onBackArrowPress(){
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FoodSelectedOrderDetailActivity.this, FoodSingleItem.class);
                i.putExtra("empty", "yes");
                setResult(Activity.RESULT_OK,i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(FoodSelectedOrderDetailActivity.this, FoodSingleItem.class);
        i.putExtra("empty", "yes");
        setResult(Activity.RESULT_OK,i);
        super.onBackPressed();
        //finish();

    }

    private void confirmOrderClickHandler(){
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(FoodSelectedOrderDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.order_confirmation_dialog);
                RelativeLayout dialog_rl_ok = (RelativeLayout) dialog.findViewById(R.id.dialog_rl_ok);
                dialog_rl_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();


                        int count = foodOrderDb.getCount();
                        int generateTrackingId = count+1;
                        String trackingId = String.valueOf(generateTrackingId);
                        boolean inserted = false;
                        for (int i=0;i<dataList.size();i++){

                            String itemName = dataList.get(i).get("itemName");
                            String itemUnitPrice = dataList.get(i).get("itemUnitPrice");
                            String itemQuantity = dataList.get(i).get("itemQuantity");
                            String itemDetail = dataList.get(i).get("itemDetail");
                            String itemTotalPrice = dataList.get(i).get("itemTotalPrice");
                            String itemImagePosition = dataList.get(i).get("itemImagePosition");

                            mHelper.setTrackinId(trackingId);
                            mHelper.setItemname(itemName);
                            mHelper.setQuantities(itemQuantity);
                            mHelper.setUnitPrice(itemUnitPrice);
                            mHelper.setDetail(itemDetail);
                            mHelper.setTotalPrice(itemTotalPrice);
                            mHelper.setOrderSumPrice(String.valueOf(retulstTotalPrrice));
                            mHelper.setItemImagePosition(itemImagePosition);
                            mHelper.setRoomNumber(trackingId);
                            mHelper.setAssignStatus("0");

                            Date currentDate = Calendar.getInstance().getTime();
                            String myFormat = "dd-MMM-yyyy"; //In which you need put here
                            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                            mHelper.setCurrentDate(sdf.format(currentDate));
                            Date currentTim = Calendar.getInstance().getTime();
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                            String currentTiem  = dateFormat.format(currentTim);
                            mHelper.setCurrentTime(currentTiem);


                            //inserting data into table
                            long isInserted = foodOrderDb.insertDatatoDb(mHelper);
                            if (isInserted!=-1){
                                Log.e("TAg", "TAD inserted to table" );
                                inserted = true;
                            }
                        }

                        if (inserted) {
                        Intent intent = new Intent(FoodSelectedOrderDetailActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        }else {
                            Log.e("TAG", "Error inserting to database");
                        }
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }

    private int sumPrice(){
        int tempPrice = 0;
        for (int i=0;i<dataList.size();i++){
            String arryPrice = dataList.get(i).get("itemTotalPrice");
            String splitedPrice = arryPrice.split("\\.")[1];
            int intePrice = Integer.parseInt(splitedPrice);
            tempPrice = tempPrice+intePrice;
        }
        return tempPrice;
    }
}
