package com.shoaibnwar.hotelandrestaurant.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.R;

public class RoomServiceActivity extends AppCompatActivity {

    EditText ed_text_room_service;
    RelativeLayout rl_back_arrow;
    RelativeLayout rl_bt_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custome_room_service_layout);
        init();
        submitClickHandler();
        onBackArrowPressListener();

    }
    private void init(){
        ed_text_room_service = (EditText) findViewById(R.id.ed_text_room_service);
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
        rl_bt_submit = (RelativeLayout) findViewById(R.id.rl_bt_submit);

    }

    private void submitClickHandler(){
        rl_bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(RoomServiceActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.order_confirmation_dialog);
                RelativeLayout dialog_rl_ok = (RelativeLayout) dialog.findViewById(R.id.dialog_rl_ok);
                TextView dialog_tv_title  = (TextView) dialog.findViewById(R.id.dialog_tv_title);
                TextView dialog_tv_detail  = (TextView) dialog.findViewById(R.id.dialog_tv_detail);
                dialog_tv_title.setText("Submitted Successfully!");
                dialog_tv_detail.setText("Your request for room service have been submitted, our staf will reach you short.");

                dialog_rl_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                        finish();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
            }
        });
    }

    private void onBackArrowPressListener()
    {
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

