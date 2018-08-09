package com.shoaibnwar.hotelandrestaurant.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.shoaibnwar.hotelandrestaurant.Adapters.Contacts;
import com.shoaibnwar.hotelandrestaurant.Database.FoodOrderDb;
import com.shoaibnwar.hotelandrestaurant.DataseHelper.OrderFoodDbHelper;
import com.shoaibnwar.hotelandrestaurant.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by gold on 7/13/2018.
 */

public class FragmentsAssigned extends Fragment {

    RecyclerView rc_list;
    LinearLayoutManager linearLayoutManager;
    ArrayList<HashMap<String, String>> contactsList;
    Contacts contactsAdapter;
    ProgressBar progress_bar;
    RelativeLayout iv_back_arrow;

    String trackingCode ="0";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_assigned_layout, container, false);
        init(v);
        return v;
    }

    private void init(View v){
        rc_list = (RecyclerView) v.findViewById(R.id.rc_list);
        rc_list.bringToFront();
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rc_list.setLayoutManager(linearLayoutManager);
        contactsList = new ArrayList<>();
        progress_bar = (ProgressBar) v.findViewById(R.id.progress_bar);

        //reading contacts
        LoadContacts looadContacts = new LoadContacts();
        looadContacts.execute();
    }

    public class LoadContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress_bar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            gettingAllDataFromDb();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progress_bar.setVisibility(View.GONE);
            Collections.reverse(contactsList);
            contactsAdapter = new Contacts(getActivity(), contactsList);
            rc_list.setAdapter(contactsAdapter);
        }
    }
    private void gettingAllDataFromDb(){
        FoodOrderDb foodOrderDb = new FoodOrderDb(getActivity());
        Log.e("TAg", "the result is " + foodOrderDb.getCount());
        ArrayList<OrderFoodDbHelper> list = foodOrderDb.getAllAsignments();
        for (OrderFoodDbHelper helper:list){

            String id =  helper.getId().toString();
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
            contactDetail.put("itemname", itemname);
            contactDetail.put("unitPrice", unitPrice);
            contactDetail.put("quantities", quantities);
            contactDetail.put("totalPrice", totalPrice);
            contactDetail.put("detail", detail);
            contactDetail.put("orderSumPrice", orderSumPrice);
            contactDetail.put("itemImagePosition", itemImagePosition);
            contactDetail.put("roomNumber", roomNumber);
            contactDetail.put("date", date);
            contactDetail.put("time", time);
            contactDetail.put("assigneStatus", assigneStatus);


                if (!trackingCode.equals(trackinId)) {
                    if (!assigneStatus.equals("0")) {
                    contactsList.add(contactDetail);
                    trackingCode = trackinId;
                }
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAg", "the view is created call");
    }
}
