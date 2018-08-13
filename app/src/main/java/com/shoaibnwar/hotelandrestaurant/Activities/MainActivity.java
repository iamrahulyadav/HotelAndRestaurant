package com.shoaibnwar.hotelandrestaurant.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.BaseService;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.Logger;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements AsyncTaskCompleteListener{

    EditText et_username, et_password;
    RelativeLayout rl_login_tv;
    SharedPreferences sharedPreferences;
    BaseService baseService;

    RelativeLayout rrRL;
    ImageView loading_img_rr;
    Animation rotation;
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        rl_login_tv = (RelativeLayout) findViewById(R.id.rl_login_tv);
        RelativeLayout rl_create_account = (RelativeLayout) findViewById(R.id.rl_create_account);
        rl_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
               // startActivity(new Intent(MainActivity.this, OrderFood.class));
            }
        });

        sharedPreferences = getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);
        baseService = new BaseService(MainActivity.this);

        rrRL  = (RelativeLayout) findViewById(R.id.rrRL);
        loading_img_rr = (ImageView) findViewById(R.id.loading_img_rr);
        rotation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);

        btLoginClickHandler();

    }

    private void btLoginClickHandler()
    {
        rl_login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = et_username.getText().toString();
                String password = et_password.getText().toString();
                if (userName.length()==0){
                    et_username.setError("Should not be empty");
                }
                else if (password.length()==0){
                    et_password.setError("Should not be empty");
                }else {

                    loginService(userName, password);
                }
            }
        });
    }

    private void loginService(final String userName, final String password)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", Urls.SIGN_IN);
        map.put("Email", userName);
        map.put("Password", password);
        map.put("Device", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.ANDROID_ID));
        Log.e("TAG", "the device fcm is: " + SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.ANDROID_ID));
        new HttpRequester(MainActivity.this, map, 1, MainActivity.this);
        baseService.handleProgressBar(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        Log.e("TAG", "The result is here 11: " + response);

        if (serviceCode == 1) {

            baseService.dismissProgressDialog();

            rrRL.setVisibility(View.GONE);
            rotation.cancel();
            try {
                Logger.log(response);
//                BaseService.dismissProgressDialog();
                JSONObject obj = new JSONObject(response);
                jsonArray = new JSONArray();
                jsonArray = obj.getJSONArray("Table");
                if (jsonArray != null && jsonArray.length() > 0)
                {
                    String CId = jsonArray.getJSONObject(0).get("Cid").toString();
                    String CName = jsonArray.getJSONObject(0).get("CustomerName").toString();
                    String CMobile = jsonArray.getJSONObject(0).get("CMobileNo").toString();
                    String CEmail = jsonArray.getJSONObject(0).get("EmailId").toString();
                    String CNIC = jsonArray.getJSONObject(0).get("CNIC").toString();


                    Log.e("TAg", "the user detail from server is cid: " + CId);
                    Log.e("TAg", "the user detail from server is name: " + CName);
                    Log.e("TAg", "the user detail from server is mobile: " + CMobile);


                    SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.C_ID, CId);
                    SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.C_NAME, CName);
                    SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.C_EMAIL, CEmail);
                    SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.C_MOBILE, CMobile);
                    SharedPrefs.StoreBooleanPref(sharedPreferences, SharedPrefs.ISLOGGEDIN, true);
                    SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.CNIC, CNIC);

                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    finish();

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
