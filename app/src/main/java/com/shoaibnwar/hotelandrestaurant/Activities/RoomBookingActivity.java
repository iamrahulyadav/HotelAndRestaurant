package com.shoaibnwar.hotelandrestaurant.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shoaibnwar.hotelandrestaurant.Adapters.RoomBookingAdapter;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.Logger;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RoomBookingActivity extends AppCompatActivity implements AsyncTaskCompleteListener{

    RecyclerView rv_rooms;
    RoomBookingAdapter mAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<HashMap<String, String>> dataList;
    RelativeLayout rrRL;
    ImageView loading_img_rr;
    Animation rotation;
    JSONArray jsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_booking);
        init();

        callingServiceForGetingRooms();
    }
    private void init(){
        dataList = new ArrayList<>();
        rv_rooms = (RecyclerView) findViewById(R.id.rv_rooms);
        linearLayoutManager = new LinearLayoutManager(RoomBookingActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_rooms.setLayoutManager(linearLayoutManager);

        rrRL  = (RelativeLayout) findViewById(R.id.rrRL);
        loading_img_rr = (ImageView) findViewById(R.id.loading_img_rr);
        rotation = AnimationUtils.loadAnimation(RoomBookingActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);

     /*   //temp
            HashMap<String, String> itemList1 = new HashMap<>();
            int image1 = R.drawable.room_image_1;
            String img1 = String.valueOf(image1);
            String room1 = "Supperior Deluxe Room";
            itemList1.put("roomtitle", room1);
        itemList1.put("image", img1);
            dataList.add(itemList1);
            //
        HashMap<String, String> itemList2 = new HashMap<>();
        int image2 = R.drawable.room_image_2;
        String img2 = String.valueOf(image2);
        String room2 = "Deluxe Room";
        itemList2.put("roomtitle", room2);
        itemList2.put("image", img2);
        dataList.add(itemList2);//
        HashMap<String, String> itemList3 = new HashMap<>();
        int image3 = R.drawable.room_image_3;
        String img3 = String.valueOf(image3);
        String room3 = "Premier Room";
        itemList3.put("image", img3);
        itemList3.put("roomtitle", room3);
        dataList.add(itemList3);
        mAdapter = new RoomBookingAdapter(RoomBookingActivity.this, dataList);
        rv_rooms.setAdapter(mAdapter);*/


    }

    private void callingServiceForGetingRooms()
    {

        rrRL.setVisibility(View.VISIBLE);
        loading_img_rr.startAnimation(rotation);

        HashMap<String, String> map3 = new HashMap<String, String>();
        map3.put("url", Urls.GET_ASSETS);
        map3.put("Lat", "32.326544121");
        map3.put("Long", "74.2365213564");
        map3.put("Radius", "100");
        map3.put("AssetType", "rr");
        new HttpRequester(RoomBookingActivity.this, map3, 1, RoomBookingActivity.this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        if(serviceCode == 1)
        {
            rrRL.setVisibility(View.GONE);
            rotation.cancel();

            try {
                Logger.log(response);
//                BaseService.dismissProgressDialog();
                JSONObject obj = new JSONObject(response);
                jsonArray = new JSONArray();

                if (dataList.size()>0){
                    dataList.clear();
                }
                jsonArray = obj.getJSONArray("Table");
                for (int i=0; i<jsonArray.length() ; i++)
                {
                    String viewId = jsonArray.getJSONObject(i).get("Vid").toString();
                    String lat = jsonArray.getJSONObject(i).get("V_Lat").toString();
                    String lng = jsonArray.getJSONObject(i).get("V_Long").toString();
                    String objType = jsonArray.getJSONObject(i).get("VType").toString();
                    String guests = jsonArray.getJSONObject(i).get("V_MaxGuest").toString();
                    String price = jsonArray.getJSONObject(i).get("Price").toString();
                    //set marker

                    ///by shoaib anwar

                    JSONObject finalobject = jsonArray.getJSONObject(i);

                    String Vid = finalobject.getString("Vid");
                    String VType = finalobject.getString("VType");
                    String V_ThumbImg = finalobject.getString("V_ThumbImg");
                    String Bed = finalobject.getString("Bed");
                    String V_MaxGuest = finalobject.getString("V_MaxGuest");
                    String V_Model = finalobject.getString("V_Model");
                    String V_Title = finalobject.getString("V_Title");
                    String V_City = finalobject.getString("V_City");
                    String V_Lat = finalobject.getString("V_Lat");
                    String V_Long = finalobject.getString("V_Long");
                    String Count = finalobject.getString("Count");
                    String Distance = finalobject.getString("Distance");
                    String Price = finalobject.getString("Price");

                    HashMap<String, String> resltdata = new HashMap<>();
                    resltdata.put("Vid", Vid);
                    resltdata.put("VType", VType);
                    resltdata.put("V_ThumbImg", V_ThumbImg);
                    resltdata.put("vid", Bed);
                    resltdata.put("V_MaxGuest", V_MaxGuest);
                    resltdata.put("V_Model", V_Model);
                    resltdata.put("V_Title", V_Title);
                    resltdata.put("V_City", V_City);
                    resltdata.put("V_Lat", V_Lat);
                    resltdata.put("V_Long", V_Long);
                    resltdata.put("Count", Count);
                    resltdata.put("Distance", Distance);
                    resltdata.put("Price", Price);
                    dataList.add(resltdata);

               }

                mAdapter = new RoomBookingAdapter(RoomBookingActivity.this, dataList);
                rv_rooms.setAdapter(mAdapter);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

}
