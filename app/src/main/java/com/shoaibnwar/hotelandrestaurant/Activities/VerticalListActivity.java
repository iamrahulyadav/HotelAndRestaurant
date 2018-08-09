package com.shoaibnwar.hotelandrestaurant.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.shoaibnwar.hotelandrestaurant.Adapters.CustomeAdapterVerticallScrollItems;
import com.shoaibnwar.hotelandrestaurant.R;

import java.util.ArrayList;
import java.util.HashMap;

public class VerticalListActivity extends AppCompatActivity {

    //edited by shoaib anwar
    RecyclerView rc_list;
    CustomeAdapterVerticallScrollItems customeAdapterForImage;
    LinearLayoutManager linearLayoutManager;
    RelativeLayout rl_image_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertical_list);

        init();
        backArrowClickHandler();
    }

    private void init(){

        rl_image_back_arrow = (RelativeLayout) findViewById(R.id.rl_image_back_arrow);
        rc_list = findViewById(R.id.rc_list);
        rc_list.bringToFront();
        linearLayoutManager = new LinearLayoutManager(VerticalListActivity.this, LinearLayoutManager.VERTICAL, false);
        rc_list.setLayoutManager(linearLayoutManager);
        ArrayList<HashMap<String, String>> dataList = ( ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("mylist");

        Log.e("TAG", "the array list size is: " + dataList.size());
        //edit by shoaib anwar
        customeAdapterForImage = new CustomeAdapterVerticallScrollItems(getApplicationContext(), dataList);
        rc_list.setAdapter(customeAdapterForImage);

    }

    private void backArrowClickHandler(){
        rl_image_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
    }
}

