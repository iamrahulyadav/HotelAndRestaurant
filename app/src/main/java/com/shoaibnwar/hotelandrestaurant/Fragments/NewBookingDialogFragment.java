package com.shoaibnwar.hotelandrestaurant.Fragments;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.Activities.AsyncTaskCompleteListener;
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
 * Created by gold on 8/9/2018.
 */

public class NewBookingDialogFragment extends DialogFragment implements AsyncTaskCompleteListener {

    BaseService baseService;
    SharedPreferences sharedPreferences;
    DialogDismissListener mDialogDismissListener;
    static JSONObject obj;
    JSONArray jsonArray;

    public void setDialogListener(DialogDismissListener listener){
        mDialogDismissListener = listener;
    }
    public static NewBookingDialogFragment newInstance(JSONObject object) {
        NewBookingDialogFragment frag = new NewBookingDialogFragment();
        obj = object;
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
        View v = inflater.inflate(R.layout.new_booking_dialog_fragment, null);
        builder.setView(v);

        TextView acceptText = (TextView) v.findViewById(R.id.accept_text);
        acceptText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hit checkIn service here CustomerCheckIn
                //Http request
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", Urls.INSERT_OWNER_RESPONSE);
                try {
                    map.put("ReqNo", obj.get("ReqNo").toString());
                    map.put("Status", "Accepted");
                    map.put("CMobileNo", obj.get("CMobileNo").toString());
                    map.put("VMobileNo", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_MOBILE));
                    map.put("Room", obj.get("Room").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //        map.put("Cid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                new HttpRequester(getActivity(), map, 1, NewBookingDialogFragment.this);
                baseService.handleProgressBar(true);
            }
        });
        TextView snoozeText = (TextView) v.findViewById(R.id.snooze_text);
        snoozeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hit checkIn service here CustomerCheckIn
                //Http request
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", Urls.INSERT_OWNER_RESPONSE);
                try {
                    map.put("ReqNo", obj.get("ReqNo").toString());
                    map.put("Status", "Snooze");
                    map.put("CMobileNo", obj.get("CMobileNo").toString());
                    map.put("VMobileNo", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_MOBILE));
                    map.put("Room", obj.get("Room").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //        map.put("Cid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                new HttpRequester(getActivity(), map, 1, NewBookingDialogFragment.this);
                baseService.handleProgressBar(true);
            }
        });

        TextView rejectedText = (TextView) v.findViewById(R.id.reject_text);
        rejectedText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hit checkIn service here CustomerCheckIn
                //Http request
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", Urls.INSERT_OWNER_RESPONSE);
                try {
                    map.put("ReqNo", obj.get("ReqNo").toString());
                    map.put("Status", "Rejected");
                    map.put("CMobileNo", obj.get("CMobileNo").toString());
                    map.put("VMobileNo", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_MOBILE));
                    map.put("Room", obj.get("Room").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //        map.put("Cid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                new HttpRequester(getActivity(), map, 1, NewBookingDialogFragment.this);
                baseService.handleProgressBar(true);
            }
        });

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView checkin = (TextView) v.findViewById(R.id.checkin);
        TextView checkout = (TextView) v.findViewById(R.id.checkout);
        TextView room = (TextView) v.findViewById(R.id.room);
        TextView nights = (TextView) v.findViewById(R.id.nights);
        TextView rent = (TextView) v.findViewById(R.id.rent);
        TextView person = (TextView) v.findViewById(R.id.person);

        try {
            name.setText(obj.get("CName").toString());
            checkin.setText(obj.get("CHKIN").toString());
            checkout.setText(obj.get("CHKOUT").toString());
            room.setText(obj.get("Room").toString());
            nights.setText(obj.get("Nights").toString());
            rent.setText(obj.get("Fare").toString());
            person.setText(obj.get("Guests").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                        alertBuilder.setMessage("Status changed successfully!");
                        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
//                                ((RoomBookingFragmentActivity)getActivity()).finish();
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
