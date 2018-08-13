package com.shoaibnwar.hotelandrestaurant.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.Logger;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity implements AsyncTaskCompleteListener{

    EditText et_username, et_number, et_email, et_password, et_confirm_password;
    RelativeLayout bt_singup;
    RelativeLayout rrRL;
    ImageView loading_img_rr;
    Animation rotation;
    JSONArray jsonArray;
    String mPhoneNumber = "";
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();
        startMobileWithOnlyNumber03();
        singupButtonHandler();
    }

    private void init()
    {
        et_username = (EditText) findViewById(R.id.et_username);
        et_number = (EditText) findViewById(R.id.et_number);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirm_password = (EditText) findViewById(R.id.et_confirm_password);
        bt_singup = (RelativeLayout) findViewById(R.id.bt_singup);

        rrRL  = (RelativeLayout) findViewById(R.id.rrRL);
        loading_img_rr = (ImageView) findViewById(R.id.loading_img_rr);
        rotation = AnimationUtils.loadAnimation(RegistrationActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);

        sharedPreferences = getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);
    }

    private void singupButtonHandler()
    {
        bt_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString();
                String phone = et_number.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String confirmPassword = et_confirm_password.getText().toString();

                if (username.length()==0){
                    et_username.setError("Should not be empty");
                }
                else if (phone.length()==0){
                    et_number.setError("Should not be empty");
                }
                else if (phone.length()!=11){
                    et_number.setError("Invalid Phone Number");
                }
                else if (email.length()==0){
                    et_email.setError("Should not be empty");
                }
                else if (!emailValidator(email)){
                    et_email.setError("Invalid Email");
                }
                else if (password.length()==0){
                    et_password.setError("Should not be empty");
                }
                else if (confirmPassword.length()==0){
                    et_confirm_password.setError("Should not be empty");
                }
                else if (!password.equals(confirmPassword)){
                    Toast.makeText(RegistrationActivity.this, "Password not match", Toast.LENGTH_SHORT).show();
                }
                else {
                    //calling API

                    phone = phone.substring(1);
                    phone = "+92"+phone;
                    mPhoneNumber = phone;

                    rrRL.setVisibility(View.VISIBLE);
                    loading_img_rr.startAnimation(rotation);

                   /* //HttpRequest
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("url", Urls.SIGN_UP);
                    map.put("UserName",  username);
                    map.put("number",  phone);
                    map.put("Emailid",  email);
                    map.put("Password",  password);
                    map.put("ustatus",  "");
                    map.put("referralCode",  "");
                    new HttpRequester(RegistrationActivity.this, map, 1, RegistrationActivity.this);
*/

                    //HttpRequest
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("url", Urls.VERIFY_USER);
                    map.put("email", et_email.getText().toString());
                    map.put("VerificationCode", "vzfm8526VZ");
                    new HttpRequester(RegistrationActivity.this, map, 2, RegistrationActivity.this);
                    rrRL.setVisibility(View.VISIBLE);
                    rotation.start();

                }
            }
        });
    }

    public void startMobileWithOnlyNumber03()
    {

        et_number.addTextChangedListener(new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                String x = s.toString();


                if (x.startsWith("1")){

                    Toast.makeText(RegistrationActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_number.setText("");
                }
                if (x.startsWith("2")){

                    Toast.makeText(RegistrationActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_number.setText("");
                }
                if (x.startsWith("3")){

                    Toast.makeText(RegistrationActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_number.setText("");
                }

                if (x.startsWith("4")){

                    Toast.makeText(RegistrationActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_number.setText("");
                }
                if (x.startsWith("5")){

                    Toast.makeText(RegistrationActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_number.setText("");
                }
                if (x.startsWith("6")){

                    Toast.makeText(RegistrationActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_number.setText("");
                }
                if (x.startsWith("7")){

                    Toast.makeText(RegistrationActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_number.setText("");
                }
                if (x.startsWith("8")){

                    Toast.makeText(RegistrationActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_number.setText("");
                }
                if (x.startsWith("9")){

                    Toast.makeText(RegistrationActivity.this, "Pleae enter number starting with 03", Toast.LENGTH_SHORT).show();
                    et_number.setText("");
                }

                if (x.startsWith("0")){
                    if (x.length()==11){
                        //doctorSignInEmail.setText(x);
                        et_number.setFilters(new InputFilter[] {new InputFilter.LengthFilter(11)});

                    }
                }
                else {
                    et_number.setFilters(new InputFilter[] {new InputFilter.LengthFilter(120)});
                }



            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }
        });//end for login editText

    }
    public static boolean emailValidator(final String mailAddress) {

        Pattern pattern;
        Matcher matcher;

        final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(mailAddress);
        return matcher.matches();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        Log.e("TAG", "The result is here 11: " + response);

        if (serviceCode == 1) {
            rrRL.setVisibility(View.GONE);
            rotation.cancel();

            try {
                Logger.log(response);
//                BaseService.dismissProgressDialog();
                JSONObject obj = new JSONObject(response);
                jsonArray = new JSONArray();
                jsonArray = obj.getJSONArray("Table");
                String isRegistered = jsonArray.getJSONObject(0).get("isRegistered").toString();
                if (isRegistered.equals("true")) {

                    showVerificationDialog();

                }else {
                    String errro = obj.getString("Error").toString();
                    Toast.makeText(this, errro, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (serviceCode == 2) {
            rrRL.setVisibility(View.GONE);
            rotation.cancel();

            try {
                Logger.log(response);
//                BaseService.dismissProgressDialog();
                JSONObject obj = new JSONObject(response);
                jsonArray = new JSONArray();
                boolean isVerified = obj.getBoolean("isVerified");
                if (isVerified) {
                    jsonArray = obj.getJSONArray("Table");
                    String Cid = jsonArray.getJSONObject(0).get("Cid").toString();
                    String CustomerName = jsonArray.getJSONObject(0).get("CustomerName").toString();
                    String CMobileNo = jsonArray.getJSONObject(0).get("CMobileNo").toString();
                    String EmailId = jsonArray.getJSONObject(0).get("EmailId").toString();
                    String CNIC = jsonArray.getJSONObject(0).get("CNIC").toString();
                /*    String Password = jsonArray.getJSONObject(0).get("Password").toString();
                    String URights = jsonArray.getJSONObject(0).get("URights").toString();
                    String Status = jsonArray.getJSONObject(0).get("Status").toString();
                    String CiLogInStatusd = jsonArray.getJSONObject(0).get("CiLogInStatusd").toString();
                    String IsDemo = jsonArray.getJSONObject(0).get("IsDemo").toString();
                    String VerificationCode = jsonArray.getJSONObject(0).get("VerificationCode").toString();
                    String IsTrackable = jsonArray.getJSONObject(0).get("IsTrackable").toString();
                    String Network = jsonArray.getJSONObject(0).get("Network").toString();
                    String IsOwner = jsonArray.getJSONObject(0).get("IsOwner").toString();
                    String IsDriver = jsonArray.getJSONObject(0).get("IsDriver").toString();
                    String ProfileImageUrl = jsonArray.getJSONObject(0).get("ProfileImageUrl").toString();
                    String ReferralCode = jsonArray.getJSONObject(0).get("ReferralCode").toString();
                    String AppName = jsonArray.getJSONObject(0).get("AppName").toString();
                    String Address = jsonArray.getJSONObject(0).get("Address").toString();
                    String Lat = jsonArray.getJSONObject(0).get("Lat").toString();
                    String Long = jsonArray.getJSONObject(0).get("Long").toString();
                    String CreatedSince = jsonArray.getJSONObject(0).get("CreatedSince").toString();
                    String CompName = jsonArray.getJSONObject(0).get("CompName").toString();*/

                    Log.e("TAG", "the user info " + Cid);
                    Log.e("TAG", "the user info " + CustomerName);
                    Log.e("TAG", "the user info " + CMobileNo);
                    Log.e("TAG", "the user info " + EmailId);

                    SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.C_ID, Cid);
                    SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.C_NAME, CustomerName);
                    SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.C_EMAIL, EmailId);
                    SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.C_MOBILE, CMobileNo);
                    SharedPrefs.StoreBooleanPref(sharedPreferences, SharedPrefs.ISLOGGEDIN, true);

                    startActivity(new Intent(RegistrationActivity.this, HomeActivity.class));
                    finish();

                } else {
                    Toast.makeText(this, "Incorrect Verification Code", Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (serviceCode == 3) {
            rrRL.setVisibility(View.GONE);
            rotation.cancel();

            try {
                Logger.log(response);
//                BaseService.dismissProgressDialog();
                JSONObject obj = new JSONObject(response);
                jsonArray = new JSONArray();
                jsonArray = obj.getJSONArray("Table");
                String reSendMobile = jsonArray.getJSONObject(0).get("reSendMobile").toString();
                if (reSendMobile.equals("true")){
                    Toast.makeText(this, "Code has been sent to " + et_number.getText().toString(), Toast.LENGTH_SHORT).show();
                    showVerificationDialog();
                }else {
                    String errro = obj.getString("Error").toString();
                    Toast.makeText(this, errro, Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showVerificationDialog()
    {
        final Dialog verificationDialog = new Dialog(RegistrationActivity.this);
        verificationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        verificationDialog.setContentView(R.layout.verification_code_dialog);
        final EditText et_verification_cdoe = (EditText) verificationDialog.findViewById(R.id.et_verification_cdoe);
        TextView tv_description = (TextView) verificationDialog.findViewById(R.id.tv_description);
        Button bt_resend_code = (Button) verificationDialog.findViewById(R.id.bt_resend_code);
        Button bt_submit = (Button) verificationDialog.findViewById(R.id.bt_submit);
        tv_description.setText("A Verification Code has be sent to " + et_number.getText().toString() + ". Please Enter code");
        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verficationCode = et_verification_cdoe.getText().toString();
                if (verficationCode.length() == 0) {
                    et_verification_cdoe.setError("Should not be emoty");
                } else {

                    verificationDialog.dismiss();

                    //HttpRequest
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("url", Urls.VERIFY_USER);
                    map.put("email", et_email.getText().toString());
                    map.put("VerificationCode", verficationCode);
                    new HttpRequester(RegistrationActivity.this, map, 2, RegistrationActivity.this);
                    rrRL.setVisibility(View.VISIBLE);
                    rotation.start();

                }
            }
        });
        bt_resend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verificationDialog.dismiss();
                //HttpRequest
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", Urls.RESEND_VERIFICATION_CODE);
                map.put("strMobileNo", mPhoneNumber);
                new HttpRequester(RegistrationActivity.this, map, 3, RegistrationActivity.this);
                rrRL.setVisibility(View.VISIBLE);
                rotation.start();
            }
        });
        verificationDialog.show();
    }
}
