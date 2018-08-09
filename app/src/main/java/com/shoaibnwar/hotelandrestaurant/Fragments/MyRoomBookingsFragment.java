package com.shoaibnwar.hotelandrestaurant.Fragments;

/**
 * Created by gold on 8/9/2018.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoaibnwar.hotelandrestaurant.Activities.AsyncTaskCompleteListener;
import com.shoaibnwar.hotelandrestaurant.Adapters.BookingAdapter;
import com.shoaibnwar.hotelandrestaurant.Listener.RecyclerItemClickListener;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.BaseService;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Sanawal Alvi on 9/16/2017.
 */

public class MyRoomBookingsFragment extends Fragment implements AsyncTaskCompleteListener {

    BaseService baseService;
    JSONArray upComingJSONArray, newJSONArray, inProgressJSONArray;
    JSONArray jsonArray;
    RecyclerView recyclerView;
    BookingAdapter bookingAdapter;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseService = new BaseService(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_room_bookings, container, false);
        sharedPreferences = getActivity().getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);

        recyclerView = (RecyclerView) view.findViewById(R.id.rr_booking_recycler_view);
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(!SharedPrefs.getBooleanPref(sharedPreferences, SharedPrefs.BOOKING_SERVICE_STATUS)) {
            SharedPrefs.StoreBooleanPref(sharedPreferences, SharedPrefs.BOOKING_SERVICE_STATUS, true);
            //Http request
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("url", Urls.GET_ASSEST_HISTORY);
            //map.put("Cid", "1102");
            map.put("Cid", "1255");/*SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID*/
            map.put("VType", "rr");
            new HttpRequester(getActivity(), map, 1, MyRoomBookingsFragment.this);
            baseService.handleProgressBar(true);
        }
        else
        {
            populateGetAssetServiceResponse(SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.BOOKING_SERVICE_STATUS_RESPONSE));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode)
        {
            case 1:
                Log.d("xxx", response);
                SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.BOOKING_SERVICE_STATUS_RESPONSE, response);
                populateGetAssetServiceResponse(response);
                break;
        }
    }

    public void populateGetAssetServiceResponse(String response)
    {
        JSONObject obj = null;
        try {
            baseService.dismissProgressDialog();
            obj = new JSONObject(response);
            jsonArray = new JSONArray();
            jsonArray = obj.getJSONArray("Table");

            upComingJSONArray = new JSONArray();
            newJSONArray = new JSONArray();
            inProgressJSONArray = new JSONArray();

            for(int i=0; i<jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).get("Status").toString().equals("Upcoming"))
                {
                    upComingJSONArray.put(jsonArray.getJSONObject(i));
                }
                else if(jsonArray.getJSONObject(i).get("Status").toString().equals("InProgress"))
                {
                    inProgressJSONArray.put(jsonArray.getJSONObject(i));
                }
                else if(jsonArray.getJSONObject(i).get("Status").toString().equals("New"))
                {
                    newJSONArray.put(jsonArray.getJSONObject(i));
                }
            }

            //send that JSON array to bookingItemAdapter & atatch adapter with recycler view
            if (getTag().equals("Upcoming"))
            {
                bookingAdapter = new BookingAdapter(upComingJSONArray);
            }
            else if (getTag().equals("New"))
            {
                bookingAdapter = new BookingAdapter(newJSONArray);
            }
            else if (getTag().equals("In Progress"))
            {
                bookingAdapter = new BookingAdapter(inProgressJSONArray);
            }
//            bookingAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(bookingAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView,
                    new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            try
                            {
                                JSONObject obj = jsonArray.getJSONObject(position);
/*                                    Intent intent = new Intent(getActivity(), MyRVBookingDetailActivity.class);
                                    intent.putExtra("jsonObj", obj.toString());
                                    startActivity(intent);*/

//                                    Toast.makeText(getActivity(), ""+obj.get("ReqNo").toString(), Toast.LENGTH_SHORT).show();
                                if(getTag().equals("Upcoming"))
                                {
                                    CheckInDialogFragment checkInDialogFragment = CheckInDialogFragment.newInstance(obj);
                                    checkInDialogFragment.show(getActivity().getFragmentManager(), "dialog");
                                }
                                else if (getTag().equals("New"))
                                {
                                    NewBookingDialogFragment newDialogFragment = NewBookingDialogFragment.newInstance(obj);
                                    newDialogFragment.show(getActivity().getFragmentManager(), "dialog");
                                }
                                else if (getTag().equals("In Progress"))
                                {
                                    CheckOutDialogFragment checkOutDialogFragment = CheckOutDialogFragment.newInstance(obj);
                                    checkOutDialogFragment.show(getActivity().getFragmentManager(), "dialog");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {

                        }
                    }));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        Toast.makeText(getActivity(), "onStopCAlled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(getActivity(), "onDestroyCAlled", Toast.LENGTH_SHORT).show();
        SharedPrefs.StoreBooleanPref(sharedPreferences, SharedPrefs.BOOKING_SERVICE_STATUS, false);
    }
}

