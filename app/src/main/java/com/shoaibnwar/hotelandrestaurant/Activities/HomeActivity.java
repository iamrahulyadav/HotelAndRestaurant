package com.shoaibnwar.hotelandrestaurant.Activities;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.shoaibnwar.hotelandrestaurant.Fragments.FoodOrdersFragment;
import com.shoaibnwar.hotelandrestaurant.Fragments.CalendarFragment;
import com.shoaibnwar.hotelandrestaurant.Fragments.HomeFragment;
import com.shoaibnwar.hotelandrestaurant.Fragments.ProfileFragment;
import com.shoaibnwar.hotelandrestaurant.Fragments.SettingsFragment;
import com.shoaibnwar.hotelandrestaurant.R;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{


    private ResideMenu resideMenu;
    private HomeActivity mContext;
    private ResideMenuItem itemHome;
    private ResideMenuItem itemFoodHistory;
    private ResideMenuItem itemForRoomBooking;
    private ResideMenuItem itemProfile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mContext = this;
        setUpMenu();
        if( savedInstanceState == null )
            changeFragment(new HomeFragment());
    }

    private void setUpMenu() {

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        //resideMenu.setUse3D(true);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setMenuListener(menuListener);
        //valid scale factor is between 0.0f and 1.0f. leftmenu'width is 150dip.
        resideMenu.setScaleValue(0.6f);

        // create menu items;
        itemHome     = new ResideMenuItem(this, R.drawable.icon_home,     "Home");
        itemProfile  = new ResideMenuItem(this, R.drawable.icon_profile,  "Profile");
        itemFoodHistory  = new ResideMenuItem(this, R.drawable.icon_profile,  "Food Orders");
        itemForRoomBooking  = new ResideMenuItem(this, R.drawable.icon_home,  "My Room Bookings");


        itemHome.setOnClickListener(this);
        itemProfile.setOnClickListener(this);
        itemFoodHistory.setOnClickListener(this);
        itemForRoomBooking.setOnClickListener(this);

        resideMenu.addMenuItem(itemHome, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemProfile, ResideMenu.DIRECTION_LEFT);
        resideMenu.addMenuItem(itemFoodHistory, ResideMenu.DIRECTION_RIGHT);
        resideMenu.addMenuItem(itemForRoomBooking, ResideMenu.DIRECTION_RIGHT);



        // You can disable a direction by setting ->
        // resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        findViewById(R.id.title_bar_left_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });
        findViewById(R.id.title_bar_right_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public void onClick(View view) {

        if (view == itemHome){
            changeFragment(new HomeFragment());
        }else if (view == itemProfile){
            changeFragment(new ProfileFragment());
        }
        else if (view == itemFoodHistory){
            startActivity(new Intent(HomeActivity.this, ActivityFoodOrders.class));
        }
        else if (view == itemForRoomBooking){
            startActivity(new Intent(HomeActivity.this, RoomBookingFragmentActivity.class));
        }
        resideMenu.closeMenu();
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {
            //Toast.makeText(mContext, "Menu is opened!", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void closeMenu() {
           // Toast.makeText(mContext, "Menu is closed!", Toast.LENGTH_SHORT).show();
        }
    };

    private void changeFragment(Fragment targetFragment){
        resideMenu.clearIgnoredViewList();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment, targetFragment, "fragment")
                .setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // What good method is to access resideMenu？
    public ResideMenu getResideMenu(){
        return resideMenu;
    }
}