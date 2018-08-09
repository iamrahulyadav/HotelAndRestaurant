package com.shoaibnwar.hotelandrestaurant.Fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.Activities.AsyncTaskCompleteListener;
import com.shoaibnwar.hotelandrestaurant.Activities.TripMapsActivity;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.BaseService;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by gold on 7/11/2018.
 */

public class PickUpAcceptanceDialogFragment extends DialogFragment implements AsyncTaskCompleteListener {

    BaseService baseService;
    Context context;
    SharedPreferences sharedPreferences;
    DialogDismissListener mDialogDismissListener;
    static String pickupTime, Name, pickUp, dropOff, requestNo, Latitude, Longitude;
    JSONArray jsonArray;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    public void setDialogListener(DialogDismissListener listener){
        mDialogDismissListener = listener;
    }
    public static PickUpAcceptanceDialogFragment newInstance(String pickUp_time, String name, String pickup, String dropoff,
                                                             String reqNo, String lat, String lng) {
        PickUpAcceptanceDialogFragment frag = new PickUpAcceptanceDialogFragment();
        pickupTime = pickUp_time;
        Name = name;
        pickUp = pickup;
        dropOff = dropoff;
        requestNo = reqNo;
        Latitude = lat;
        Longitude = lng;
        return frag;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseService = new BaseService(getActivity());
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        sharedPreferences = getActivity().getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.pickup_acceptance_dialog, null);
        builder.setView(v);

        TextView acceptText = (TextView) v.findViewById(R.id.accept_txt);
        acceptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hit checkIn service here CustomerCheckIn
                //Http request
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", Urls.INSERT_DRIVER_RESPONSE);

                map.put("ReqNo", requestNo);
                map.put("VMobileNo", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_MOBILE));
                map.put("Fare", "10"); //Fare is static for now
                map.put("DLat", Latitude);
                map.put("DLong", Longitude);
                map.put("DFcm", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.ANDROID_ID));

                //        map.put("Cid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                new HttpRequester(getActivity(), map, 1, PickUpAcceptanceDialogFragment.this);
                baseService.handleProgressBar(true);
            }
        });

        TextView rejectedText = (TextView) v.findViewById(R.id.reject_txt);
        rejectedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView name = (TextView) v.findViewById(R.id.pickup);
        TextView customer = (TextView) v.findViewById(R.id.customer);
        TextView pickUp_tv = (TextView) v.findViewById(R.id.pickup);
        TextView dropOff_tv = (TextView) v.findViewById(R.id.dropoff);

        name.setText(pickupTime);
        customer.setText(Name);
        pickUp_tv.setText(pickUp);
        dropOff_tv.setText(dropOff.trim());

        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode)
        {
            case 1:
                JSONObject obj = null;
                try {
                    Log.d("xxx", response);
                    baseService.dismissProgressDialog();
                    obj = new JSONObject(response);
                    jsonArray = new JSONArray();
                    jsonArray = obj.getJSONArray("Table");

                    if(jsonArray.length() > 0) {
                        android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Information");
                        alertBuilder.setMessage("Request Accepted successfully!");
                        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                //Start TripMapsActivity...
                                Intent intent = new Intent(context, TripMapsActivity.class);
                                intent.putExtra("req_no", requestNo);
                                intent.putExtra("lat", Latitude);
                                intent.putExtra("lng", Longitude);
                                intent.putExtra("destination", dropOff);
                                context.startActivity(intent);

                            }
                        });
                        android.support.v7.app.AlertDialog alert = alertBuilder.create();
                        alert.show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    public interface DialogDismissListener{
        public void onDialogDismiss();
    }

    private void clearAddButtonSelection()
    {
        if(mDialogDismissListener!=null){
            mDialogDismissListener.onDialogDismiss();
        }
    }
}
