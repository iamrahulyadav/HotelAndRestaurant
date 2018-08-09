package com.shoaibnwar.hotelandrestaurant.Activities;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.shoaibnwar.hotelandrestaurant.Adapters.ViewPagerAdapter;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.CircleImageView;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class DetailViewCab extends Activity implements AsyncTaskCompleteListener, View.OnClickListener{

    String type, id;
    ViewPager viewPager;
    ImageView backBtn;
    CircleImageView circleImageView;
    TextView maxGuests, brand, wifi, saloon;
    TextView titleDescription, driverDescription, reviews;
    Button checkAvailabilityBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view_cab);

        Bundle bundle = getIntent().getExtras();
        id = bundle.get("Vid").toString();
        type = bundle.get("VType").toString();

        maxGuests = (TextView) findViewById(R.id.guests_no);
        brand = (TextView) findViewById(R.id.brand);
        wifi = (TextView) findViewById(R.id.wifi_status);
        saloon = (TextView) findViewById(R.id.saloon);
        titleDescription = (TextView) findViewById(R.id.description);
        driverDescription = (TextView) findViewById(R.id.driver_description);
        reviews = (TextView) findViewById(R.id.reviews_no);

        viewPager = (ViewPager) findViewById(R.id.myViewPager);
        backBtn = (ImageView) findViewById(R.id.back_arrow_Img);
        circleImageView = (CircleImageView) findViewById(R.id.driverImageView);
        backBtn.setOnClickListener(this);
        checkAvailabilityBtn = (Button) findViewById(R.id.check_availability);
        checkAvailabilityBtn.setOnClickListener(this);

        //HttpRequest
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("url", Urls.GET_ASSET_DETAIL);
        map.put("Aid", id);
        map.put("VType", type);
        new HttpRequester(DetailViewCab.this, map, 1, DetailViewCab.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        switch (serviceCode)
        {
            case 1:
//                Log.d("xxx", response);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);
                    JSONArray jsonArray = new JSONArray();
                    jsonArray = obj.getJSONArray("Table");

                    String detailTitle = jsonArray.getJSONObject(0).get("V_Title").toString();
                    String detailMaxGuests = jsonArray.getJSONObject(0).get("V_MaxGuest").toString();
                    String detailDriverImgURL = jsonArray.getJSONObject(0).get("V_DriverImage").toString();
                    String detailWifi = jsonArray.getJSONObject(0).get("Wifi").toString();
                    String detailDescription = jsonArray.getJSONObject(0).get("V_Description").toString();
/*                    if (type.equals("rr"))
                    {
                        String detailBed = jsonArray.getJSONObject(0).get("Bed").toString();
                        String detailBath = jsonArray.getJSONObject(0).get("Bath").toString();
                        String detailRooms = jsonArray.getJSONObject(0).get("Rooms").toString();
                    }*/
                    String detailBrand = jsonArray.getJSONObject(0).get("V_Brand").toString();
                    String detailReviews = jsonArray.getJSONObject(0).get("Reviews").toString();
                    String detailSliderImg1 = jsonArray.getJSONObject(0).get("V_SliderImg1").toString();
                    String detailSliderImg2 = jsonArray.getJSONObject(0).get("V_SliderImg2").toString();
                    String detailSliderImg3 = jsonArray.getJSONObject(0).get("V_SliderImg3").toString();
                    String detailPrice = jsonArray.getJSONObject(0).get("V_Price").toString();
                    String detailSaloon = jsonArray.getJSONObject(0).get("Saloon").toString();

                    ArrayList<String> urlList = new ArrayList<>();
                    urlList.add(detailSliderImg1);
                    urlList.add(detailSliderImg2);
                    urlList.add(detailSliderImg3);

                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, urlList);
                    viewPager.setAdapter(viewPagerAdapter);
                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new MyTimerTask(), 3000, 3000);

                    new MapsActivity.DownloadImageTask(circleImageView).execute(detailDriverImgURL);

                    maxGuests.setText(detailMaxGuests);
                    brand.setText(detailBrand);
                    wifi.setText(detailWifi);
                    saloon.setText(detailSaloon);
                    titleDescription.setText(detailDescription);
                    driverDescription.setText(detailDescription);
                    reviews.setText(detailReviews);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back_arrow_Img:
                onBackPressed();
                break;
            case R.id.check_availability:
                onBackPressed();
                break;
        }
    }

    public class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            DetailViewCab.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem() == 0) {
                        viewPager.setCurrentItem(1);
                    } else if (viewPager.getCurrentItem() == 1)
                        viewPager.setCurrentItem(2);
                    else if (viewPager.getCurrentItem() == 2)
                        viewPager.setCurrentItem(0);

                }
            });
        }
    }
}
