package com.shoaibnwar.hotelandrestaurant.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.Activities.AsyncTaskCompleteListener;
import com.shoaibnwar.hotelandrestaurant.Activities.MapsActivity;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.BaseService;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by gold on 7/12/2018.
 */

public class CollectCashDialogBoxFragment extends DialogFragment implements AsyncTaskCompleteListener {

    BaseService baseService;
    Context context;
    SharedPreferences sharedPreferences;
    DialogDismissListener mDialogDismissListener;
    JSONArray jsonArray;

    public void setDialogListener(DialogDismissListener listener){
        mDialogDismissListener = listener;
    }
    public static CollectCashDialogBoxFragment newInstance() {
        CollectCashDialogBoxFragment frag = new CollectCashDialogBoxFragment();
        return frag;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
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
        View v = inflater.inflate(R.layout.collect_cash_dialog_box, null);
        builder.setView(v);

        TextView name = (TextView) v.findViewById(R.id.name);
        TextView fare = (TextView) v.findViewById(R.id.fare);
        final EditText fare_et = (EditText) v.findViewById(R.id.edit_text_fare);
        TextView collectCashBtn = (TextView) v.findViewById(R.id.collect_cash_text);
        collectCashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Http request
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", Urls.ADD_PAYMENT);

                map.put("ReqNo", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.REQUEST_ID));
                map.put("CashIn", fare_et.getText().toString());
                map.put("Vid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                map.put("Fare", "220"); // get FARE from notification & set text here after parsing...

                new HttpRequester(getActivity(), map, 1, CollectCashDialogBoxFragment.this);
                baseService.handleProgressBar(true);
            }
        });

        name.setText(SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.NAME).trim());
        fare.setText("220"); //get FARE from notification & set text here after parsing...

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
                        dismiss();
                        Intent intent = new Intent(context, MapsActivity.class);
                        context.startActivity(intent);
                        try
                        {
                            getActivity().finish();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
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