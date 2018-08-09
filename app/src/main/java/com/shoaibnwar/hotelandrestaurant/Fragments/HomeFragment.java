package com.shoaibnwar.hotelandrestaurant.Fragments;

/**
 * Created by gold on 7/5/2018.
 */

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.shoaibnwar.hotelandrestaurant.Activities.HomeActivity;
import com.shoaibnwar.hotelandrestaurant.Activities.MapsActivity;
import com.shoaibnwar.hotelandrestaurant.Activities.OrderFood;
import com.shoaibnwar.hotelandrestaurant.Activities.RoomBookingActivity;
import com.shoaibnwar.hotelandrestaurant.Activities.RoomServiceActivity;
import com.shoaibnwar.hotelandrestaurant.R;
import com.special.ResideMenu.ResideMenu;

/**
 * User: special
 * Date: 13-12-22
 * Mail: specialcyci@gmail.com
 */
public class HomeFragment extends Fragment {

    private View parentView;
    private ResideMenu resideMenu;
    private LinearLayout ll_room_booking, ll_order_food, ll_room_service, ll_book_cap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.home, container, false);
        setUpViews();
        ll_room_booking  = (LinearLayout) parentView.findViewById(R.id.ll_room_booking);
        ll_order_food  = (LinearLayout) parentView.findViewById(R.id.ll_order_food);
        ll_room_service  = (LinearLayout) parentView.findViewById(R.id.ll_room_service);
        ll_book_cap  = (LinearLayout) parentView.findViewById(R.id.ll_book_cap);

        //roo booking click listsner
        ll_room_booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), RoomBookingActivity.class));
            }
        });

        //order food click listener
        ll_order_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().startActivity(new Intent(getActivity(), OrderFood.class));
            }
        });

        //room service click listener
        ll_room_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().startActivity(new Intent(getActivity(), RoomServiceActivity.class));
            }
        });

        ll_book_cap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().startActivity(new Intent(getActivity(), MapsActivity.class));
            }
        });

        return parentView;
    }

    private void setUpViews() {
        HomeActivity parentActivity = (HomeActivity) getActivity();
        resideMenu = parentActivity.getResideMenu();

        /*parentView.findViewById(R.id.btn_open_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
            }
        });*/

        // add gesture operation's ignored views
        FrameLayout ignored_view = (FrameLayout) parentView.findViewById(R.id.ignored_view);
        resideMenu.addIgnoredView(ignored_view);
    }

}