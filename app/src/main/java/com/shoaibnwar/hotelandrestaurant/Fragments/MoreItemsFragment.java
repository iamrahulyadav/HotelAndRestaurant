package com.shoaibnwar.hotelandrestaurant.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shoaibnwar.hotelandrestaurant.Adapters.MoreItemsAdapter;
import com.shoaibnwar.hotelandrestaurant.Listener.RecyclerItemClickListener;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.BaseService;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;

/**
 * Created by gold on 7/11/2018.
 */

public class MoreItemsFragment extends android.support.v4.app.Fragment{

    private String INBOX = "Inbox";
    private String TRIPS = "My Trips";
    private String ROOM_BOOKINGS = "My Room Bookings";
    private String RV_BOKINGS = "My RV Bookings";
    private String PROFILE = "Settings";
    private String LOGOUT = "Logout";
    private String LANGUAGE = "Select Language";
    private String FAVOURITES = "Favourites";
    private String SEMD_INVITE = "Send Invites";
    private String WALLET = "Wallet";
    private String FAQ = "FAQ";
    private String SUPPORT = "Support";
    SharedPreferences sharedPreferences;
    BaseService baseService;

    RecyclerView recyclerView;
    MoreItemsAdapter moreItemsAdapter;

    //private String[] menuTitle = {INBOX, TRIPS, ROOM_BOOKINGS, RV_BOKINGS, PROFILE, LOGOUT, LANGUAGE};
    private String[] menuTitle = {TRIPS, ROOM_BOOKINGS, RV_BOKINGS, PROFILE, LANGUAGE, FAVOURITES, SEMD_INVITE, LOGOUT, WALLET, FAQ, SUPPORT};
    private int[] menuImages = {R.drawable.cabs_txt, R.drawable.rooms, R.drawable.rv, R.drawable.profile,
            R.drawable.logout, R.drawable.profile, R.drawable.cabs, R.drawable.history, R.drawable.inbox, R.drawable.inbox, R.drawable.inbox};

    public MoreItemsFragment()
    {

    }

    public static MoreItemsFragment newInstance() {
        return new MoreItemsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_items_layout, container, false);

        sharedPreferences = getActivity().getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);
        baseService = new BaseService(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.more_recycler_view);
        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        moreItemsAdapter = new MoreItemsAdapter(menuTitle, menuImages);

        recyclerView.setAdapter(moreItemsAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
               /* if (position == 0){

                    Intent intent = new Intent(getActivity(), MyBookings.class);
                    startActivity(intent);
                }
                if (position == 1) {
                    Intent intent = new Intent(getActivity(), RoomBookingFragmentActivity.class);
                    startActivity(intent);
                }
                if (position == 2) {
                    Intent intent = new Intent(getActivity(), RVBookingFragmentActivity.class);
                    startActivity(intent);
                }
                if (position == 3) {
                    Intent intent = new Intent(getActivity(), Settings.class);
                    startActivity(intent);
                }
                if (position == 4) {

                }
                if (position == 5) {

                }
                if (position == 6){

                }
                if (position == 7){

                    final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                    alert.setTitle("Alert!");
                    alert.setMessage("Do you want to logout");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                            dialogInterface.dismiss();

                            //http logout service
                            HashMap<String, String> map = new HashMap<String, String>();
                            map.put("url", Urls.LOG_OUT);
                            map.put("Cid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                            new HttpRequester(getActivity(), map, 1, new AsyncTaskCompleteListener() {
                                @Override
                                public void onTaskCompleted(String response, int serviceCode) {
                                    baseService.dismissProgressDialog();
                                    try {
                                        Log.d("xxx", response.toString());
                                        JSONObject obj = new JSONObject(response);
                                        JSONArray jsonArray = new JSONArray();
                                        jsonArray = obj.getJSONArray("Table");
                                        if (jsonArray != null && jsonArray.length() > 0) {
                                            SharedPrefs.StoreBooleanPref(sharedPreferences, SharedPrefs.ISLOGGEDIN, false);
                                            Intent intent = new Intent(getActivity(), SignInActivity.class);
                                            startActivity(intent);
                                            getActivity().finish();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            baseService.handleProgressBar(true);

                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alert.show();
                }*/
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}

