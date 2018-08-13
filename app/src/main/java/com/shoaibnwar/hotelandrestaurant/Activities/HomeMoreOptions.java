package com.shoaibnwar.hotelandrestaurant.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.shoaibnwar.hotelandrestaurant.R;

import static java.sql.Types.NULL;

public class HomeMoreOptions extends AppCompatActivity {

    RelativeLayout rl_back_arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circule_buttons);
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

        init();
        onBackArrowClickHandler();
    }

    private void init()
    {
        rl_back_arrow = (RelativeLayout) findViewById(R.id.rl_back_arrow);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition( R.anim.slide_out_down, R.anim.slide_in_down);
    }

    private void onBackArrowClickHandler()
    {
        rl_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition( R.anim.slide_out_down, R.anim.slide_in_down);
            }
        });
    }
}
