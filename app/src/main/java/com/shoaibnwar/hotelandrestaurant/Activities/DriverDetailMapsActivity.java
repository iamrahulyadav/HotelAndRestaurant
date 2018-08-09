package com.shoaibnwar.hotelandrestaurant.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.shoaibnwar.hotelandrestaurant.Adapters.CustomeAdapterForImage;
import com.shoaibnwar.hotelandrestaurant.Adapters.PlaceArrayAdapter;
import com.shoaibnwar.hotelandrestaurant.Fragments.PickUpAcceptanceDialogFragment;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.GPSTracker;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Services.UserService;
import com.shoaibnwar.hotelandrestaurant.Utils.Constants;
import com.shoaibnwar.hotelandrestaurant.Utils.Logger;
import com.shoaibnwar.hotelandrestaurant.Utils.ServiceProgressDialog;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;
import com.shoaibnwar.hotelandrestaurant.Utils.Utils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class DriverDetailMapsActivity extends FragmentActivity implements OnMapReadyCallback, AsyncTaskCompleteListener, View.OnClickListener,
        GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    Boolean heatMapFlag;
    HeatmapTileProvider mProvider;
    SupportMapFragment mapFragment;
    View searchBar;
    RelativeLayout centerMarker;
    Button reqBtn;
    UserService userService;
    GPSTracker gpsTracker;
    private static final int ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    String req_id;
    Handler mHandler;
    private Geocoder geocoder;
    private List<Address> addresses;
    boolean more = false;
    Double latitude, longitude;
    private PlaceAutocompleteFragment autocompleteFragment;
    SharedPreferences sharedPreferences;
    ImageView loadingImg, centerIcon;
    ImageView loadingImgCab, centerIconCab;
    ImageView loadingImgRV, centerIconRV;
    ImageView loadingImgRR, centerIconRR;
    Animation rotation;
    View layoutView, multiPinLayoutView;
    Marker marker;
    JSONArray jsonArray;
    ImageView markerImg;
    TextView cabIntensity, rvIntensity, rrIntensity;

    String notificationBody, notificationData, notificationFrom;
    String reqId, destination, lat, lng, name, mobile, finalPickUpAddress, pickUpTime;
    String detailViewId, detailType, detailTitle, detailImgURL, detailGuests, detailPrice;
    //RelativeLayout searchBarHeader;

    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(31.398160, 74.180831), new LatLng(31.430610, 74.972090));
    protected LatLng mCenterLocation = new LatLng(31.398160, 74.180831);
    boolean isServiceInProgressFlag;

    //edited by shoaib anwar
    CustomeAdapterForImage customeAdapterForImage;
    LinearLayoutManager linearLayoutManager;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<>();

    TextView tv_pick_up_address, tv_pick_city;
    TextView tv_drop_off, tv_drop_off_city;

    LinearLayout ll_bottom_views;

    ImageView iv_back_arrow;

    TextView pickup_lat, pickup_lng;

    TextView tv_cancel_ride;

    ImageView iv_driver;
    TextView tv_driver_name;
    RatingBar ratingBarSmall;
    TextView tv_rating_number;
    TextView tv_v_model;
    TextView tv_car_name;
    TextView tv_car_number;
    TextView tv_contact_driver;

    RelativeLayout rl_small_circule, rl_middle_circule, rl_outer_circule;
    RelativeLayout rl_circuls;

    TextView tv_driver_mobile;


    MyReceiver myReceiver;
    ProgressDialog pd;
    String mRequestId;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail_maps);
        sharedPreferences = getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        heatMapFlag = false;
        isServiceInProgressFlag = true;

        searchBar = (View) findViewById(R.id.place_autocomplete_fragment);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        centerMarker = (RelativeLayout) findViewById(R.id.confirm_address_map_custom_marker);
        layoutView = findViewById(R.id.center_pin_layout);
        multiPinLayoutView = findViewById(R.id.center_multi_pin_layout);

        rotation = AnimationUtils.loadAnimation(DriverDetailMapsActivity.this, R.anim.rotate);
        rotation.setFillAfter(true);
        centerIcon = (ImageView) layoutView.findViewById(R.id.center_img);
        loadingImg = (ImageView) layoutView.findViewById(R.id.loading_img);
        loadingImg.startAnimation(rotation);

/*        centerIconCab = (ImageView) multiPinLayoutView.findViewById(R.id.center_img_cab);
        centerIconRR = (ImageView) multiPinLayoutView.findViewById(R.id.center_img_rr);
        centerIconRV = (ImageView) multiPinLayoutView.findViewById(R.id.center_img_rv);*/

        cabIntensity = (TextView) findViewById(R.id.cab_intensity);
        rvIntensity = (TextView) findViewById(R.id.rv_intensity);
        rrIntensity = (TextView) findViewById(R.id.rr_intensity);

        loadingImgCab = (ImageView) multiPinLayoutView.findViewById(R.id.loading_img_cab);
        loadingImgCab.startAnimation(rotation);
        loadingImgRR = (ImageView) multiPinLayoutView.findViewById(R.id.loading_img_rr);
        loadingImgRR.startAnimation(rotation);
        loadingImgRV = (ImageView) multiPinLayoutView.findViewById(R.id.loading_img_rv);
        loadingImgRV.startAnimation(rotation);

        gpsTracker = new GPSTracker(this);
        userService = new UserService(DriverDetailMapsActivity.this);



        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 10, 280);

        View compassButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("5"));
        RelativeLayout.LayoutParams crlp = (RelativeLayout.LayoutParams) compassButton.getLayoutParams();
// position on right bottom
        crlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        crlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        crlp.setMargins(0, 200, 10, 0);


        mGoogleApiClient = new GoogleApiClient.Builder(DriverDetailMapsActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();


        ArrayList<LatLng> locations = generateLocations();
        mProvider = new HeatmapTileProvider.Builder().data(locations).build();
        mProvider.setRadius(HeatmapTileProvider.DEFAULT_RADIUS);

        //edit by shoaib anwar

        ll_bottom_views = (LinearLayout) findViewById(R.id.ll_bottom_views);

        tv_pick_up_address = (TextView) findViewById(R.id.tv_pick_up_address);
        tv_pick_city = (TextView) findViewById(R.id.tv_pick_city);
        tv_drop_off = (TextView) findViewById(R.id.tv_drop_off);
        tv_drop_off_city = (TextView) findViewById(R.id.tv_drop_off_city);

        pickup_lat = (TextView) findViewById(R.id.pickup_lat);
        pickup_lng = (TextView) findViewById(R.id.pickup_lng);

        iv_back_arrow = (ImageView) findViewById(R.id.iv_back_arrow);

        tv_cancel_ride = (TextView) findViewById(R.id.tv_cancel_ride);

        iv_driver = (ImageView) findViewById(R.id.iv_driver);
        tv_driver_name = (TextView) findViewById(R.id.tv_driver_name);
        ratingBarSmall = (RatingBar) findViewById(R.id.ratingBarSmall);
        tv_rating_number = (TextView) findViewById(R.id.tv_rating_number);
        tv_v_model = (TextView) findViewById(R.id.tv_v_model);
        tv_car_name = (TextView) findViewById(R.id.tv_car_name);
        tv_car_number = (TextView) findViewById(R.id.tv_car_number);
        tv_contact_driver = (TextView) findViewById(R.id.tv_contact_driver);
        tv_driver_mobile = (TextView) findViewById(R.id.tv_driver_mobile);

        rl_circuls = (RelativeLayout) findViewById(R.id.rl_circuls);
        rl_small_circule = (RelativeLayout) findViewById(R.id.rl_small_circule);
        rl_middle_circule = (RelativeLayout) findViewById(R.id.rl_middle_circule);
        rl_outer_circule = (RelativeLayout) findViewById(R.id.rl_outer_circule);

        pd = new ProgressDialog(DriverDetailMapsActivity.this);

        Intent intent = getIntent();
        mRequestId = intent.getExtras().getString("requestId");



        onBackArrowPress();
        carTapClickHandler();
        handlerCall();
        cancelRide();
        statSearchAnimation();

        readRideFirebaseDatabase(mRequestId);


    } // onCreate finishes


    protected Rect getLocationOnScreen(EditText mEditText) {
        Rect mRect = new Rect();
        int[] location = new int[2];

        mEditText.getLocationOnScreen(location);

        mRect.left = location[0];
        mRect.top = location[1];
        mRect.right = location[0] + mEditText.getWidth();
        mRect.bottom = location[1] + mEditText.getHeight();

        return mRect;
    }

    private ArrayList<LatLng> generateLocations() {
        ArrayList<LatLng> locations = new ArrayList<LatLng>();
        double lat;
        double lng;
        Random generator = new Random();
        for (int i = 0; i < 500; i++) {
            lat = generator.nextDouble() / 3;
            lng = generator.nextDouble() / 3;
            if (generator.nextBoolean()) {
                lat = -lat;
            }
            if (generator.nextBoolean()) {
                lng = -lng;
            }
            locations.add(new LatLng(mCenterLocation.latitude + lat, mCenterLocation.longitude + lng));
        }

        return locations;
    }

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("Location", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);

            Log.i("name", place.getName().toString());
            Log.i("coordinates", place.getLatLng().toString());

            mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

            geocoder = new Geocoder(DriverDetailMapsActivity.this, Locale.getDefault());
            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0);

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(address).snippet("Your pick up location"));

            // Utils.hideSoftKeyboard(DriverDetailMapsActivity.this);

//            mAutocompleteTextView.clearFocus();

            //HttpRequest
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("url", Urls.GET_ASSETS);
            map.put("Lat", String.valueOf(latitude));
            map.put("Long", String.valueOf(longitude));
            map.put("Radius", "30");
            map.put("AssetType", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB));
            // new HttpRequester(DriverDetailMapsActivity.this, map, 1, DriverDetailMapsActivity.this);
        }
    };


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent != null) {
            notificationBody = intent.getStringExtra("notificationInfoBody");
            notificationFrom = intent.getStringExtra("notificationInfoFrom");
            if (notificationBody != null)
                Log.d("tag", notificationBody);
        }

        try {
            reqId = intent.getStringExtra("reqId");
            destination = intent.getStringExtra("destination");
            lat = intent.getStringExtra("lat");
            lng = intent.getStringExtra("lng");
            name = intent.getStringExtra("name");
            mobile = intent.getStringExtra("mobile");
            name = intent.getStringExtra("name");

            if (intent != null && !notificationBody.equals("") && !reqId.equals("")) {
                SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.REQUEST_ID, reqId);
                SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.DESTINATION, destination);
                SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.LAT, lat);
                SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.LAT, lng);
                SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.MOBILE, mobile);
                SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.NAME, name);
            }

            String pickUpAddress = notificationBody.split(":")[1];
            finalPickUpAddress = pickUpAddress.split("Please")[0].trim();
            Log.d("xxx", finalPickUpAddress);

            pickUpTime = Utils.getCurrentDate(Constants.SERVER_TIME_FORMAT);

            PickUpAcceptanceDialogFragment pickUpAcceptanceDialogFragment = PickUpAcceptanceDialogFragment.newInstance(pickUpTime,
                    name, finalPickUpAddress, destination, reqId, lat, lng);
            pickUpAcceptanceDialogFragment.show(getFragmentManager(), "dialog");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Toast.makeText(DriverDetailMapsActivity.this, "" + intent.getStringExtra("notificationInfoBody"), Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();




        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo activeWIFIInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        if (activeWIFIInfo.isConnected() || activeNetInfo.isConnected()) {
        }
        else
        {
            showInternetDialog();
        }

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color

        if (android.os.Build.VERSION.SDK_INT >= 21){
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_color));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }
        });
        SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB, "all");
        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        LatLng latlng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_pin)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.latitude, gpsTracker.longitude), 15));

//        Toast.makeText(DriverDetailMapsActivity.this, "" + mMap.getCameraPosition().target, Toast.LENGTH_SHORT).show();

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                Log.e("TAg", "this functino is Calling...");
                // loadingImgCab.startAnimation(rotation);

                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showLocationOffDialog();
                }
                else {
                    if (ActivityCompat.checkSelfPermission(DriverDetailMapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    manager.addGpsStatusListener(new GpsStatus.Listener() {
                        @Override
                        public void onGpsStatusChanged(int event) {
                            switch(event) {
                                case GpsStatus.GPS_EVENT_STARTED:
                                    Toast.makeText(DriverDetailMapsActivity.this, "Please wait for GPS to get connected...", Toast.LENGTH_SHORT).show();
                                    break;
                                case GpsStatus.GPS_EVENT_FIRST_FIX:
                                    Toast.makeText(DriverDetailMapsActivity.this, "GPS locked! You can now find your current location.", Toast.LENGTH_SHORT).show();
                                    manager.removeGpsStatusListener(this);
                                    break;
                            }
                        }

                    });
                }
//                Toast.makeText(DriverDetailMapsActivity.this, ""+googleMap.getCameraPosition().target, Toast.LENGTH_SHORT).show();
                //loadingImg.startAnimation(rotation);

                    /*if (SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB).equals("all")) {
                        loadingImgCab.startAnimation(rotation);
                        loadingImgRR.startAnimation(rotation);
                        loadingImgRV.startAnimation(rotation);
                    } else {
                        loadingImg.startAnimation(rotation);
                    }*/

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(DriverDetailMapsActivity.this, Locale.getDefault());
                try {
                    latitude = googleMap.getCameraPosition().target.latitude;
                    longitude = googleMap.getCameraPosition().target.longitude;
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (addresses != null) {
                    try {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();
                        Log.d("address is::", address);

                        Log.e("TAG", "Here is getting result of Address: " + address);
                        Log.e("TAG", "Here is getting result of Address: " + city);
                        Log.e("TAG", "Here is getting result of Address: " + state);
                        Log.e("TAG", "Here is getting result of Address: " + country);
                        Log.e("TAG", "Here is getting result of Address: " + postalCode);
                        Log.e("TAG", "Here is getting result of Address: " + knownName);

                        tv_drop_off.setText(address);
                        tv_drop_off_city.setText(knownName  + ", "+city);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //HttpRequest
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", Urls.GET_ASSETS);
                map.put("Lat", String.valueOf(latitude));
                map.put("Long", String.valueOf(longitude));
                map.put("Radius", "30");
                map.put("AssetType", "Cab");
                //  new HttpRequester(DriverDetailMapsActivity.this, map, 1, DriverDetailMapsActivity.this);
//                BaseService.handleProgressBar(true);

            }

        });

    } // onMapReady finished


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        Log.e("TAG","The result is here 11: " + response);

        if(serviceCode == 7)
        {
            //Alert Dialog for showing information...
            ServiceProgressDialog.dismissDialog();
            Logger.log(response);
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray jsonArray = new JSONArray();
                jsonArray = obj.getJSONArray("Table");
                String requestId = jsonArray.getJSONObject(0).get("RequestId").toString();
                Log.e("TAG", "the Request is is: " + requestId);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        if(serviceCode == 8)
        {
            pd.dismiss();

            //Alert Dialog for showing information...
            ServiceProgressDialog.dismissDialog();
            Logger.log(response);
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray jsonArray = new JSONArray();
                jsonArray = obj.getJSONArray("Table");
                String cancelResponse = jsonArray.getJSONObject(0).get("IsSuccess").toString();
                Log.e("TAG", "the cacel result is: " + cancelResponse);
                if (cancelResponse.equals("True")){
                    Toast.makeText(DriverDetailMapsActivity.this, "Your Request Has been Cancelled", Toast.LENGTH_SHORT).show();
                    Intent mapsActivity = new Intent(DriverDetailMapsActivity.this, MapsActivity.class);
                    mapsActivity.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(mapsActivity);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if(serviceCode == 9)
        {


            layoutView.setVisibility(View.VISIBLE);
            ll_bottom_views.setVisibility(View.VISIBLE);


            //Alert Dialog for showing information...
            ServiceProgressDialog.dismissDialog();
            Logger.log(response);
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray jsonArray = new JSONArray();
                jsonArray = obj.getJSONArray("Table");
                String DriverMobile = jsonArray.getJSONObject(0).get("DriverMobile").toString();
                String DriverName = jsonArray.getJSONObject(0).get("DriverName").toString();
                String VRegNo = jsonArray.getJSONObject(0).get("VRegNo").toString();
                String CustomerRating = jsonArray.getJSONObject(0).get("CustomerRating").toString();
                String DriverImage = jsonArray.getJSONObject(0).get("DriverImage").toString();
                String AssetImage = jsonArray.getJSONObject(0).get("AssetImage").toString();
                String LicenseNo = jsonArray.getJSONObject(0).get("LicenseNo").toString();
                String V_Model = jsonArray.getJSONObject(0).get("V_Model").toString();

                Log.e("TAG", "the result for connected driver is DriverMobile: " + DriverMobile);
                Log.e("TAG", "the result for connected driver is DriverName: " + DriverName);
                Log.e("TAG", "the result for connected driver is VRegNo: " + VRegNo);
                Log.e("TAG", "the result for connected driver is CustomerRating: " + CustomerRating);
                Log.e("TAG", "the result for connected driver is DriverImage: " + DriverImage);
                Log.e("TAG", "the result for connected driver is AssetImage: " + AssetImage);
                Log.e("TAG", "the result for connected driver is LicenseNo: " + LicenseNo);
                Log.e("TAG", "the result for connected driver is V_Model: " + V_Model);


                tv_driver_name.setText(DriverName);
                ratingBarSmall.setRating(Float.parseFloat(CustomerRating));
                tv_rating_number.setText(CustomerRating);
                tv_v_model.setText(VRegNo);


                Picasso.with(DriverDetailMapsActivity.this)
                        .load(DriverImage)
                        .placeholder(R.drawable.amu_bubble_shadow)
                        //.fit()
                        .into(iv_driver);



            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = Integer.parseInt(marker.getTag().toString());
//                Toast.makeText(DriverDetailMapsActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                for (int i=0; i<jsonArray.length() ; i++) {
                    try {
                        detailViewId = jsonArray.getJSONObject(i).get("Vid").toString();
                        if(detailViewId.equals(String.valueOf(position)))
                        {
                            detailType = jsonArray.getJSONObject(i).get("VType").toString();
                            detailTitle = jsonArray.getJSONObject(i).get("V_Title").toString();
                            detailImgURL = jsonArray.getJSONObject(i).get("V_ThumbImg").toString();
                            detailGuests = jsonArray.getJSONObject(i).get("V_MaxGuest").toString();
                            detailPrice = jsonArray.getJSONObject(i).get("Price").toString();

                            //setmarker image through sownloading URL
                            new DriverDetailMapsActivity.DownloadImageTask(markerImg).execute(detailImgURL);

                        }
                        else
                        {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            try {
                bmImage.setImageBitmap(result);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void showLocationOffDialog() {

        AlertDialog locationAlertDialog;
        AlertDialog.Builder gpsBuilder = new AlertDialog.Builder(
                DriverDetailMapsActivity.this);
        gpsBuilder.setCancelable(false);
        gpsBuilder
                .setTitle("No GPS")
                .setMessage("Please turn GPS services ON")
                .setPositiveButton("Enable",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete
                                dialog.dismiss();
                                Intent viewIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(viewIntent);

                            }
                        })

                .setNegativeButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                                dialog.dismiss();
                                finish();
                            }
                        });
        locationAlertDialog = gpsBuilder.create();
        locationAlertDialog.show();
    }

    private void showInternetDialog() {
        AlertDialog internetDialog;
        AlertDialog.Builder internetBuilder = new AlertDialog.Builder(DriverDetailMapsActivity.this);
        internetBuilder.setCancelable(false);
        internetBuilder
                .setTitle("No Internet")
                .setMessage("Please turn Internet services ON")
                .setPositiveButton("Enable 3G/4G",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // continue with delete
                                Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("Enable Wifi",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // User pressed Cancel button. Write
                                // Logic Here
                                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
                            }
                        })
                .setNeutralButton("Exit",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // do nothing
                                finish();
                            }
                        });
        internetDialog = internetBuilder.create();
        internetDialog.show();
    }



    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    private void onBackArrowPress(){
        iv_back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    private void carTapClickHandler() {
        isServiceInProgressFlag = true;
        SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB, "cab");
        heatMapFlag = false;
        //searchBarHeader.setVisibility(View.VISIBLE);
        layoutView.setVisibility(View.VISIBLE);
        multiPinLayoutView.setVisibility(View.GONE);

        loadingImg.setImageResource(R.drawable.loadingred);
        loadingImg.startAnimation(rotation);
        centerIcon.setImageResource(R.drawable.googlecab);
//                        Toast.makeText(MapsActivity.this, "cabs_plain clicked", Toast.LENGTH_SHORT).show();
        if (more) {
            FragmentManager manager = DriverDetailMapsActivity.this.getSupportFragmentManager();
            if (manager == null) {
                return;
            }
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.map, mapFragment);// 2nd parameter will be more fragment.newInstance
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
            centerMarker.setVisibility(View.VISIBLE);

            more = false;
        }

        //HttpRequest
        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("url", Urls.GET_ASSETS);
        map2.put("Lat", String.valueOf(latitude));
        map2.put("Long", String.valueOf(longitude));
        map2.put("Radius", "30");
        map2.put("AssetType", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB));
        //new HttpRequester(DriverDetailMapsActivity.this, map2, 1, DriverDetailMapsActivity.this);

//                        BaseService.handleProgressBar(true);
    }

    private void handlerCall(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                loadingImg.clearAnimation();
                //calling check service reposne for drivers

               /* HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("url", Urls.checkServiceResponse);
                map2.put("Reqid", "267673");
                new HttpRequester(DriverDetailMapsActivity.this, map2, 9, DriverDetailMapsActivity.this);*/


            }
        },3500);
    }

    private void cancelRide(){
        tv_cancel_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(DriverDetailMapsActivity.this);
                alert.setTitle("Alert!");
                alert.setIcon(R.drawable.alert_dot);
                alert.setMessage("Do want to cancel ride");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                        cancelingRideApiCall();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alert.show();
            }
        });
    }

    private void cancelingRideApiCall(){

        pd.setMessage("Canceling Request");
        pd.setCancelable(false);
        pd.show();

        HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("url", Urls.CancelRequest);
        map2.put("ReqNo", "267673");
        map2.put("Reason", "Other");
        new HttpRequester(DriverDetailMapsActivity.this, map2, 8, DriverDetailMapsActivity.this);

    }

    private void statSearchAnimation(){

        rl_small_circule.setVisibility(View.GONE);
        rl_middle_circule.setVisibility(View.GONE);
        rl_outer_circule.setVisibility(View.GONE);
        layoutView.setVisibility(View.GONE);
        handlerCall1();

    }

    private void handlerCall1(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rl_small_circule.setVisibility(View.VISIBLE);
                rl_middle_circule.setVisibility(View.GONE);
                rl_outer_circule.setVisibility(View.GONE);

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.scale_up_animation);

                rl_small_circule.setAnimation(animation);

                handlerCall2();

            }
        },1000);
    }
    private void handlerCall2(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                rl_small_circule.setVisibility(View.VISIBLE);
                rl_middle_circule.setVisibility(View.VISIBLE);
                rl_outer_circule.setVisibility(View.GONE);

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.scale_up_animation);
                rl_middle_circule.setAnimation(animation);



                handlerCall3();

            }
        },1000);
    }

    private void handlerCall3(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                rl_small_circule.setVisibility(View.VISIBLE);
                rl_middle_circule.setVisibility(View.VISIBLE);
                rl_outer_circule.setVisibility(View.VISIBLE);

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.scale_up_animation);
                rl_outer_circule.setAnimation(animation);

                handlerCall1();

            }
        },1000);
    }

    @Override
    public void onStart() {

        //Register BroadcastReceiver
        //to receive event from our service
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("driver_response");
        registerReceiver(myReceiver, intentFilter);

        super.onStart();
    }

    @Override
    public void onStop() {
        unregisterReceiver(myReceiver);

        super.onStop();
    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub

            rl_circuls.setVisibility(View.GONE);
            ll_bottom_views.setVisibility(View.VISIBLE);

            String VModel  = arg1.getExtras().getString("VModel");
            String VRegNo  = arg1.getExtras().getString("VRegNo");
            String DriverName  = arg1.getExtras().getString("DriverName");
            String VMobileNo  = arg1.getExtras().getString("VMobileNo");
            String DriverImage  = arg1.getExtras().getString("DriverImage");
            String AssetImage  = arg1.getExtras().getString("AssetImage");
            String DFcm  = arg1.getExtras().getString("VModel");
            String DLat  = arg1.getExtras().getString("DLat");
            String DLong  = arg1.getExtras().getString("DLong");
            String DLicense  = arg1.getExtras().getString("DLicense");
            String CustomerRating  = arg1.getExtras().getString("CustomerRating");

            double driverLat = Double.parseDouble(DLat);
            double driverLng = Double.parseDouble(DLong);


            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(driverLat, driverLng)).title(DriverName).icon(BitmapDescriptorFactory.fromResource(R.drawable.googlecab));
            Marker driverMarker =  mMap.addMarker(markerOptions);

            //temHandlerMarkerPostion(driverMarker, driverLat, driverLng);


            tv_driver_name.setText(DriverName);
            if (CustomerRating.length()>1) {
                ratingBarSmall.setRating(Float.parseFloat(CustomerRating));
            }
            tv_rating_number.setText(CustomerRating);
            tv_v_model.setText(VModel);
            tv_car_number.setText(VRegNo);
            tv_driver_mobile.setText(VMobileNo);



            Picasso.with(DriverDetailMapsActivity.this)
                    .load(DriverImage)
                    .placeholder(R.drawable.amu_bubble_shadow)
                    //.fit()
                    .into(iv_driver);


        }

    }

    private void temHandlerMarkerPostion(final Marker driverMarker, final double mLat, final double mLong){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                double lati = mLat+0.0000223;
                double lngi = mLong+0.0000224;

                driverMarker.setPosition(new LatLng(lati, lngi));

                temHandlerMarkerPostion(driverMarker, lati, lngi);

            }
        },1+00);
    }

    private void readRideFirebaseDatabase(String reqId){
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("BroadCastingDb/"+reqId);
        Log.e("TAG", "the value from data is: " + mFirebaseInstance.getReference());
        Log.e("TAG", "the value from data is: " + mFirebaseDatabase.getKey());
        Log.e("TAG", "the value from data is: " + mFirebaseDatabase);

        //

        mFirebaseDatabase =  FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mFirebaseDatabase.child("BroadCastingDb");

        Query phoneQuery = ref.child(reqId);
        phoneQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object  ob = dataSnapshot.getValue();
                Log.e("TAg", "the data change is: " + ob);
               /* Object  ob = dataSnapshot.getValue();
                String jstring = ob.toString();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(jstring);
                    double lat = jsonObject.getDouble("Latitude");
                    double lng = jsonObject.getDouble("Longitude");
                    Log.e("TAG", "12345 lat " +  lat);
                    Log.e("TAG", "12345 lng " +  lng);
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("TAG", "onCancelled", databaseError.toException());
            }
        });

    }

}

