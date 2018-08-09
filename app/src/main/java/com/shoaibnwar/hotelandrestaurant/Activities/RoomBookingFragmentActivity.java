package com.shoaibnwar.hotelandrestaurant.Activities;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.shoaibnwar.hotelandrestaurant.Fragments.MyRoomBookingsFragment;
import com.shoaibnwar.hotelandrestaurant.R;

public class RoomBookingFragmentActivity extends FragmentActivity {

    private FragmentTabHost mTabHost;

    public static RoomBookingFragmentActivity newInstance() {
        return new RoomBookingFragmentActivity();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_fragment_booking);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(
                mTabHost.newTabSpec("Upcoming").setIndicator("Upcoming", null), MyRoomBookingsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("New").setIndicator("New", null), MyRoomBookingsFragment.class, null);
        mTabHost.addTab(
                mTabHost.newTabSpec("In Progress").setIndicator("In Progress", null), MyRoomBookingsFragment.class, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
