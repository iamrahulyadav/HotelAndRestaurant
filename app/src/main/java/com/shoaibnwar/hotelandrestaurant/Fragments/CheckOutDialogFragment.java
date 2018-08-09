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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.Activities.AsyncTaskCompleteListener;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.BaseService;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.Constants;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;
import com.shoaibnwar.hotelandrestaurant.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by gold on 8/9/2018.
 */

public class CheckOutDialogFragment extends DialogFragment implements AsyncTaskCompleteListener {

    BaseService baseService;
    SharedPreferences sharedPreferences;
    DialogDismissListener mDialogDismissListener;
    static JSONObject obj;
    JSONArray jsonArray;

    public void setDialogListener(DialogDismissListener listener){
        mDialogDismissListener = listener;
    }

    public static CheckOutDialogFragment newInstance(JSONObject object) {
        CheckOutDialogFragment frag = new CheckOutDialogFragment();
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
        View v = inflater.inflate(R.layout.check_out_dialog_fragment, null);
        builder.setView(v);

        final EditText fare_et = (EditText) v.findViewById(R.id.edit_text_fare);
        try {
            fare_et.setText(obj.get("Fare").toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RelativeLayout checkInRL = (RelativeLayout) v.findViewById(R.id.checkin_RL);
        checkInRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hit checkIn service here CustomerCheckIn
                //Http request
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", Urls.CUSTOMER_CHECK_OUT);
                try {
                    map.put("ReqNo", obj.get("ReqNo").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //        map.put("Cid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                map.put("ChkOut", Utils.getCurrentDate(Constants.SERVER_TIME_FORMAT));
                map.put("Cash", fare_et.getText().toString());
                new HttpRequester(getActivity(), map, 1, CheckOutDialogFragment.this);
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
            fare_et.setText(obj.get("Fare").toString());
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
                    BaseService.dismissProgressDialog();
                    obj = new JSONObject(response);
                    jsonArray = new JSONArray();
                    jsonArray = obj.getJSONArray("Table");

                    if(jsonArray.length() > 0) {
                        android.support.v7.app.AlertDialog.Builder alertBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Information");
                        alertBuilder.setMessage("Checked Out successfully!");
                        alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                dismiss();
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
