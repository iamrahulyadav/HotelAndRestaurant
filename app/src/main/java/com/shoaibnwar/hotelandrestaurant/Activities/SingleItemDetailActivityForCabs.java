package com.shoaibnwar.hotelandrestaurant.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.Logger;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class SingleItemDetailActivityForCabs extends AppCompatActivity implements AsyncTaskCompleteListener {

    ImageView iv_back_arrow;
    TextView tv_title, tv_reviews, tv_max_guest, tv_v_brand, tv_wifi, tv_model;
    TextView tv_driver_detail;
    TextView tv_mileage_tv, tv_ride_confort_tv, tv_on_time_tv, tv_driter_rating_tv;
    ImageView iv_person;

    ImageView slide_image_1, slide_image_2, slide_image_3;

    Button bt_book_now;
    Button bt_ride_now;


    JSONArray jsonArray;
    RelativeLayout rrRL;
    ImageView loading_img_rr;
    Animation rotation;

    String Aid;
    String VType;


    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item_detail_for_cabs);
        System.gc();

        init();
        onBackArrowPress();
        overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

        Intent intentData = getIntent();
        Aid = intentData.getStringExtra("Vid");
        VType = intentData.getStringExtra("VType");


        Log.e("TAG", "the clicked Vid is: " + Aid);
        Log.e("TAG", "the clicked Vid is: " + VType);

        //HttpRequest
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", Urls.GET_ASSET_DETAIL);
        map.put("Aid",  Aid);
        map.put("VType",  VType);
        new HttpRequester(SingleItemDetailActivityForCabs.this, map, 6, SingleItemDetailActivityForCabs.this);


        Log.e("TAG", "the aid is: " + VType);

        if (VType.equals("rr") || VType.equals("rv")) {
            bt_book_now.setVisibility(View.VISIBLE);
            bt_ride_now.setVisibility(View.GONE);
            String firstName =   SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.FIRST_NAME);
            String lastName =   SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.LAST_NAME);
            final String fullName = firstName + " "+ lastName;
            final String C_ID =   SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID);
            final String NUMBER = SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.NUMBER);

            bookRR(C_ID, Aid, VType, "200", "4", "4", "8-july-2018", "10-july-2018", fullName, NUMBER);

        }
        else if (VType.equals("Cab")){
            bt_book_now.setVisibility(View.GONE);
            bt_ride_now.setVisibility(View.VISIBLE);
            bookCab();
        }


    }//end of onCreate method

    private void init(){

        sharedPreferences = getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);

        iv_back_arrow = (ImageView) findViewById(R.id.iv_back_arrow);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_reviews = (TextView) findViewById(R.id.tv_reviews);
        tv_max_guest = (TextView) findViewById(R.id.tv_max_guest);
        tv_v_brand = (TextView) findViewById(R.id.tv_v_brand);
        tv_wifi = (TextView) findViewById(R.id.tv_wifi);
        tv_model = (TextView) findViewById(R.id.tv_model);
        tv_mileage_tv = (TextView) findViewById(R.id.tv_mileage_tv);
        tv_ride_confort_tv = (TextView) findViewById(R.id.tv_ride_confort_tv);
        tv_on_time_tv = (TextView) findViewById(R.id.tv_on_time_tv);
        tv_driter_rating_tv = (TextView) findViewById(R.id.tv_driter_rating_tv);
        tv_driver_detail = (TextView) findViewById(R.id.tv_driver_detail);

        iv_person = (ImageView) findViewById(R.id.iv_person);
        rrRL  = (RelativeLayout) findViewById(R.id.rrRL);
        loading_img_rr = (ImageView) findViewById(R.id.loading_img_rr);

        slide_image_1 = (ImageView) findViewById(R.id.slide_image_1);
        slide_image_2 = (ImageView) findViewById(R.id.slide_image_2);
        slide_image_3 = (ImageView) findViewById(R.id.slide_image_3);

        bt_book_now = (Button) findViewById(R.id.bt_book_now);
        bt_ride_now = (Button) findViewById(R.id.bt_ride_now);

        rotation = AnimationUtils.loadAnimation(SingleItemDetailActivityForCabs.this, R.anim.rotate);
        rotation.setFillAfter(true);

        loading_img_rr.startAnimation(rotation);


    }
    private void onBackArrowPress(){
        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        rrRL.setVisibility(View.GONE);
        rotation.cancel();

        //  rrRL.setVisibility(View.GONE);
        Log.e("TAG","The result is here: " + response);
        if(serviceCode == 6)
        {
            //isServiceInProgressFlag = false;

            try {
                Logger.log(response);
//                BaseService.dismissProgressDialog();
                JSONObject obj = new JSONObject(response);
                jsonArray = new JSONArray();

               /* if (dataList.size()>0){
                    dataList.clear();
                }*/
                jsonArray = obj.getJSONArray("Table");
                for (int i=0; i<jsonArray.length() ; i++)
                {
                    String V_Title = jsonArray.getJSONObject(i).get("V_Title").toString();
                    String V_MaxGuest = jsonArray.getJSONObject(i).get("V_MaxGuest").toString();
                    String DriverName = jsonArray.getJSONObject(i).get("DriverName").toString();
                    String VMobileno = jsonArray.getJSONObject(i).get("VMobileno").toString();
                    String V_Description = jsonArray.getJSONObject(i).get("V_Description").toString();
                    String V_DriverImage = jsonArray.getJSONObject(i).get("V_DriverImage").toString();
                    String Wifi = jsonArray.getJSONObject(i).get("Wifi").toString();
                    String TV = jsonArray.getJSONObject(i).get("TV").toString();
                    String Fridge = jsonArray.getJSONObject(i).get("Fridge").toString();
                    String Rooms = jsonArray.getJSONObject(i).get("Rooms").toString();
                    String Bed = jsonArray.getJSONObject(i).get("Bed").toString();
                    String Bath = jsonArray.getJSONObject(i).get("Bath").toString();
                    String AC = jsonArray.getJSONObject(i).get("AC").toString();
                    String Lunch = jsonArray.getJSONObject(i).get("Lunch").toString();
                    String Laundry = jsonArray.getJSONObject(i).get("Laundry").toString();
                    String V_Price = jsonArray.getJSONObject(i).get("V_Price").toString();
                    String CustomerName = jsonArray.getJSONObject(i).get("CustomerName").toString();
                    String Cid = jsonArray.getJSONObject(i).get("Cid").toString();
                    String V_SliderImg1 = jsonArray.getJSONObject(i).get("V_SliderImg1").toString();
                    String V_SliderImg2 = jsonArray.getJSONObject(i).get("V_SliderImg2").toString();
                    String V_SliderImg3 = jsonArray.getJSONObject(i).get("V_SliderImg3").toString();
                    String Reviews = jsonArray.getJSONObject(i).get("Reviews").toString();
                    String V_Chkin = jsonArray.getJSONObject(i).get("V_Chkin").toString();
                    String V_Chkout = jsonArray.getJSONObject(i).get("V_Chkout").toString();
                    String V_Brand = jsonArray.getJSONObject(i).get("V_Brand").toString();
                    String V_Model = jsonArray.getJSONObject(i).get("V_Model").toString();

                    Log.e("TAG", "the result string for mytrip  V_Title: " + V_Title);
                    Log.e("TAG", "the result string for mytrip  V_MaxGuest: " + V_MaxGuest);

                    tv_title.setText(V_Title);
                    tv_reviews.setText(Reviews + " Reviews");
                    tv_max_guest.setText(V_MaxGuest);
                    tv_v_brand.setText(V_Brand);
                    tv_wifi.setText(Wifi);
                    tv_model.setText(V_Model);
                    tv_driter_rating_tv.setText(Reviews);
                    tv_driver_detail.setText(V_Description);


                    Picasso.with(this)
                            .load(V_DriverImage)
                            .placeholder(R.drawable.amu_bubble_shadow)
                            //.fit()
                            .into(iv_person);

                    //image for scroll
                    Picasso.with(this)
                            .load(V_SliderImg1)
                            //.placeholder(R.drawable.amu_bubble_shadow)
                            //.fit()
                            .into(slide_image_1);


                    Picasso.with(this)
                            .load(V_SliderImg2)
                            //.placeholder(R.drawable.amu_bubble_shadow)
                            //.fit()
                            .into(slide_image_2);


                    Picasso.with(this)
                            .load(V_SliderImg3)
                            //.placeholder(R.drawable.amu_bubble_shadow)
                            //.fit()
                            .into(slide_image_3);

                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        if(serviceCode == 12)
        {
            //isServiceInProgressFlag = false;

            try {
                Logger.log(response);
//                BaseService.dismissProgressDialog();
                JSONObject obj = new JSONObject(response);
                jsonArray = new JSONArray();
                String error = obj.getString("Error");
                if (error.equals("You have already booked it."))
                {
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                }
                else {
                    jsonArray = obj.getJSONArray("Table");
                    String IsCusscess = jsonArray.getJSONObject(0).get("IsSuccess").toString();

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

    }

    private void bookRR(final String Cid, final String Vid,
                        final String VType, final String Req_Price,
                        final String Guests, final String Req_Nights,
                        final String Chkin, final String Chkout,
                        final String CName, final String CMobileNo){

        bt_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                Intent ii = new Intent(SingleItemDetailActivityForCabs.this, CustomCalendar.class);

                Log.e("TAg", "the vid is here: " + Aid);

                ii.putExtra("vid", Aid.toString());
                ii.putExtra("VType", VType);
                startActivity(ii);



            /*    AlertDialog.Builder alert = new AlertDialog.Builder(SingleItemDetailActivityForCabs.this);
                alert.setTitle("Alert!");
                alert.setMessage("Do you want to confirm your booking?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        //HttpRequest
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("url", Urls.BookRRandRV);
                        map.put("Cid",  Cid);
                        map.put("Vid",  Vid);
                        map.put("Vtype",  VType);
                        map.put("Req_Price",  Req_Price);
                        map.put("Guests",  Guests);
                        map.put("Req_Nights",  Req_Nights);
                        map.put("Chkin",  Chkin);
                        map.put("Chkout",  Chkout);
                        map.put("CName",  CName);
                        map.put("CMobileNo",  CMobileNo);

                        new HttpRequester(SingleItemDetailActivityForCabs.this, map, 12, SingleItemDetailActivityForCabs.this);
                    }
                });
                alert.show();*/

            }
        });
    }

    private void bookCab(){
        bt_ride_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent rideNowActivity = new Intent(SingleItemDetailActivityForCabs.this, DropUplocationActivity.class);
                startActivity(rideNowActivity);
            }
        });
    }
}
