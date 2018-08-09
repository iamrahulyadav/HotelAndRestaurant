package com.shoaibnwar.hotelandrestaurant.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.shoaibnwar.hotelandrestaurant.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout rl_create_account = (RelativeLayout) findViewById(R.id.rl_create_account);
        rl_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
               // startActivity(new Intent(MainActivity.this, OrderFood.class));
            }
        });
    }
}
