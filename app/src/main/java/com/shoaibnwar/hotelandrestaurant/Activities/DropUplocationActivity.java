package com.shoaibnwar.hotelandrestaurant.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.shoaibnwar.hotelandrestaurant.Adapters.CustomeAdapterForImage;
import com.shoaibnwar.hotelandrestaurant.Adapters.PlaceArrayAdapter;
import com.shoaibnwar.hotelandrestaurant.Fragments.PickUpAcceptanceDialogFragment;
import com.shoaibnwar.hotelandrestaurant.Model.ProductModel;
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

public class DropUplocationActivity extends FragmentActivity implements OnMapReadyCallback, AsyncTaskCompleteListener, View.OnClickListener,
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
    private AutoCompleteTextView mAutocompleteTextView;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(31.398160, 74.180831), new LatLng(31.430610, 74.972090));
    protected LatLng mCenterLocation = new LatLng(31.398160, 74.180831);
    boolean isServiceInProgressFlag;

    //edited by shoaib anwar
    CustomeAdapterForImage customeAdapterForImage;
    LinearLayoutManager linearLayoutManager;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<>();

    TextView tv_pick_up_address, tv_pick_city;
    TextView tv_drop_off, tv_drop_off_city;
    ImageView iv_search;
    RelativeLayout search_bar_header;

    TextView tv_add_detail;

    ImageView iv_back_arrow;
    Button bt_book_ride;
    TextView tv_pick_up_detail;

    TextView pickup_lat, pickup_lng;
    String mREQUEST_ID;

    TextView tv_fare_estimation;

    RelativeLayout rrRL;

    ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_uplocation);
        sharedPreferences = getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);

       /* startActivity(new Intent(DropUplocationActivity.this, AddCreditOrDebitCard.class));
        finish();*/

/*
        reqBtn = (Button) findViewById(R.id.req_btn);
        reqBtn.setOnClickListener(this);
*/
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        heatMapFlag = false;
        isServiceInProgressFlag = true;

        searchBar = (View) findViewById(R.id.place_autocomplete_fragment);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        centerMarker = (RelativeLayout) findViewById(R.id.confirm_address_map_custom_marker);
        layoutView = findViewById(R.id.center_pin_layout);
        multiPinLayoutView = findViewById(R.id.center_multi_pin_layout);

        rotation = AnimationUtils.loadAnimation(DropUplocationActivity.this, R.anim.rotate);
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
        userService = new UserService(DropUplocationActivity.this);



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


        mGoogleApiClient = new GoogleApiClient.Builder(DropUplocationActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        mAutocompleteTextView = (AutoCompleteTextView) findViewById(R.id.search_text_view);
        mAutocompleteTextView.setThreshold(2);
        mAutocompleteTextView.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
        mAutocompleteTextView.setAdapter(mPlaceArrayAdapter);

        mAutocompleteTextView.clearFocus();
        mAutocompleteTextView.setCursorVisible(false);
        mAutocompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAutocompleteTextView.setCursorVisible(true);
            }
        });

        ArrayList<LatLng> locations = generateLocations();
        mProvider = new HeatmapTileProvider.Builder().data(locations).build();
        mProvider.setRadius(HeatmapTileProvider.DEFAULT_RADIUS);

        //edit by shoaib anwar

        tv_pick_up_address = (TextView) findViewById(R.id.tv_pick_up_address);
        tv_pick_city = (TextView) findViewById(R.id.tv_pick_city);
        tv_drop_off = (TextView) findViewById(R.id.tv_drop_off);
        tv_drop_off_city = (TextView) findViewById(R.id.tv_drop_off_city);

        pickup_lat = (TextView) findViewById(R.id.pickup_lat);
        pickup_lng = (TextView) findViewById(R.id.pickup_lng);

        SharedPreferences mySharedPRes = getSharedPreferences("tem", 0);
        /*SharedPreferences.Editor editor = mySharedPRes.edit();
        editor.putString("address", address);
        editor.putString("lat", latitude.toString());
        editor.putString("lng", longitude.toString());
        editor.commit();*/

        String address = mySharedPRes.getString("address", null);
        String city = mySharedPRes.getString("city", null);
        String pick_lat = mySharedPRes.getString("lat",null);
        String pick_lng = mySharedPRes.getString("lng", null);

       /* Intent addresCity = getIntent();
        String address = addresCity.getExtras().getString("address");
        String city = addresCity.getExtras().getString("city");
        String pick_lat = addresCity.getExtras().getString("pick_lat");
        String pick_lng = addresCity.getExtras().getString("pick_lng");*/

        tv_pick_up_address.setText(address);
        tv_pick_city.setText(city);
        pickup_lat.setText(pick_lat);
        pickup_lng.setText(pick_lng);


        iv_search = (ImageView) findViewById(R.id.iv_search);
        search_bar_header = (RelativeLayout) findViewById(R.id.search_bar_header);

        tv_add_detail = (TextView) findViewById(R.id.tv_add_detail);
        tv_pick_up_detail = (TextView) findViewById(R.id.tv_pick_up_detail);

        iv_back_arrow = (ImageView) findViewById(R.id.iv_back_arrow);
        bt_book_ride = (Button) findViewById(R.id.bt_book_ride);

        tv_fare_estimation = (TextView) findViewById(R.id.tv_fare_estimation);

        rrRL  = (RelativeLayout) findViewById(R.id.rrRL);

        pd = new ProgressDialog(DropUplocationActivity.this);


        carTapClickHandler();
        dropoffSearchHandler();
        onBackArrowPress();
        addDetailHandler();
        bookRideClickHandler();


    } // onCreate finishes

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if (ev.getAction() == MotionEvent.ACTION_DOWN && !getLocationOnScreen(mAutocompleteTextView).contains(x, y)) {
            InputMethodManager input = (InputMethodManager)
                    DropUplocationActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            input.hideSoftInputFromWindow(mAutocompleteTextView.getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

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

            geocoder = new Geocoder(DropUplocationActivity.this, Locale.getDefault());
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

            // Utils.hideSoftKeyboard(DropUplocationActivity.this);
            mAutocompleteTextView.setText("");
            mAutocompleteTextView.setHint("Search");
//            mAutocompleteTextView.clearFocus();

            //HttpRequest
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("url", Urls.GET_ASSETS);
            map.put("Lat", String.valueOf(latitude));
            map.put("Long", String.valueOf(longitude));
            map.put("Radius", "30");
            map.put("AssetType", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB));
            new HttpRequester(DropUplocationActivity.this, map, 1, DropUplocationActivity.this);

        }
    };

    AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i("Location", "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i("Location", "Fetching details for ID: " + item.placeId);
            hideKeyboard(DropUplocationActivity.this);
            search_bar_header.setVisibility(View.GONE);

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

//        Toast.makeText(DropUplocationActivity.this, "" + intent.getStringExtra("notificationInfoBody"), Toast.LENGTH_SHORT).show();
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
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Search");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.d("yayy", "Place: " + place.getName());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                geocoder = new Geocoder(DropUplocationActivity.this, Locale.getDefault());
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
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("yayy", "An error occurred: " + status);
            }
        });

/*        PickUpAcceptanceDialogFragment pickUpAcceptanceDialogFragment = PickUpAcceptanceDialogFragment.newInstance("",
                "abc", "Arfa Software Technology Park, 346-B Ferozepur Rd, Lahore",
                "Model Town, Lahore",
                "0000", "31.475768", "74.342572");
        pickUpAcceptanceDialogFragment.show(getFragmentManager(), "dialog");*/
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

//        Toast.makeText(DropUplocationActivity.this, "" + mMap.getCameraPosition().target, Toast.LENGTH_SHORT).show();

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                Log.e("TAg", "this functino is Calling...");
                // loadingImgCab.startAnimation(rotation);

                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showLocationOffDialog();
                } else {
                    if (ActivityCompat.checkSelfPermission(DropUplocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    manager.addGpsStatusListener(new GpsStatus.Listener() {
                        @Override
                        public void onGpsStatusChanged(int event) {
                            switch (event) {
                                case GpsStatus.GPS_EVENT_STARTED:
                                    Toast.makeText(DropUplocationActivity.this, "Please wait for GPS to get connected...", Toast.LENGTH_SHORT).show();
                                    break;
                                case GpsStatus.GPS_EVENT_FIRST_FIX:
                                    Toast.makeText(DropUplocationActivity.this, "GPS locked! You can now find your current location.", Toast.LENGTH_SHORT).show();
                                    manager.removeGpsStatusListener(this);
                                    break;
                            }
                        }

                    });
                }
//                Toast.makeText(DropUplocationActivity.this, ""+googleMap.getCameraPosition().target, Toast.LENGTH_SHORT).show();
                loadingImg.startAnimation(rotation);

                    /*if (SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB).equals("all")) {
                        loadingImgCab.startAnimation(rotation);
                        loadingImgRR.startAnimation(rotation);
                        loadingImgRV.startAnimation(rotation);
                    } else {
                        loadingImg.startAnimation(rotation);
                    }*/

                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(DropUplocationActivity.this, Locale.getDefault());
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
                        tv_drop_off_city.setText(knownName + ", " + city);


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
                new HttpRequester(DropUplocationActivity.this, map, 1, DropUplocationActivity.this);
//                BaseService.handleProgressBar(true);

                String picupLat = pickup_lat.getText().toString();
                String pickupLng = pickup_lng.getText().toString();
                double dPicupLat = Double.parseDouble(picupLat);
                double dPicupLng = Double.parseDouble(pickupLng);
                double distance = shortDistance(dPicupLng, dPicupLat, longitude, latitude);
                double estDistance = distance / 1000;
                double time = estDistance * 2;

                String Distance = String.valueOf(estDistance);
                String Time = String.valueOf(time);
                Log.e("TAG", "the distance between poitns is: " + Distance);
                double myTime = Double.parseDouble(Time);
                int tme = (int) myTime;
                Log.e("TAG", "the distance between poitns is time: " + tme);
                String mTime = Integer.toString(tme);

                //HttpRequest
                HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("url", Urls.FareEstimate);
                map2.put("distanceKm", Distance);
                map2.put("TravelTimeMin", mTime);
                new HttpRequester(DropUplocationActivity.this, map2, 4, DropUplocationActivity.this);


            }

        });

    }

    public void getAssetsCallBack(Object caller, Object model) {
        ProductModel.getInstance().setModel((ProductModel) model);

        if (ProductModel.getInstance().success) {
            Toast.makeText(this, "getAssetsCallBack success", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "Error while deleting, Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTaskCompleted(String response, int serviceCode) {

        Log.e("TAG","The result is here 11: " + response);

        if(serviceCode == 1)
        {
            isServiceInProgressFlag = false;

            try {
                Logger.log(response);
                mMap.clear();

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

                    /*HashMap<String, String> resltdata = new HashMap<>();
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

                    dataList.add(resltdata);*/

                    ///end of edit by shoib anwar

                    LatLng latlng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    loadingImg.clearAnimation();
                    mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.googlecab)).title(jsonArray.getJSONObject(i).get("V_Title").toString())).setTag(viewId);

                   /* if (SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB).equals("all"))
                    {
                        loadingImgCab.clearAnimation();
                        loadingImgRV.clearAnimation();
                        loadingImgRR.clearAnimation();

                        if (objType.equals("Cab"))
                            mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.googlecab)).title(jsonArray.getJSONObject(i).get("V_Title").toString())).setTag(viewId);
                        if (objType.equals("rr"))
                            mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.googlehome)).title(jsonArray.getJSONObject(i).get("V_Title").toString())).setTag(viewId);
                        if (objType.equals("rv"))
                            mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.googlerv)).title(jsonArray.getJSONObject(i).get("V_Title").toString())).setTag(viewId);
                    }
                    if (SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB).equals("cab"))
                    {
                        loadingImg.clearAnimation();
                        mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.googlecab)).title(jsonArray.getJSONObject(i).get("V_Title").toString())).setTag(viewId);
                    }
                    if (SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB).equals("rr"))
                    {
                        loadingImg.clearAnimation();
                        mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.googlehome)).title(jsonArray.getJSONObject(i).get("V_Title").toString())).setTag(viewId);
                    }
                    if (SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB).equals("rv"))
                    {
                        loadingImg.clearAnimation();
                        mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.googlerv)).title(jsonArray.getJSONObject(i).get("V_Title").toString())).setTag(viewId);
                    }*/
                }

                //end of eidt by shoaib anwar

                //HttpRequest
                HashMap<String, String> map3 = new HashMap<String, String>();
                map3.put("url", Urls.GET_REQUEST_INTENSITY);
                map3.put("Lat", String.valueOf(latitude));
                map3.put("Long", String.valueOf(longitude));
                new HttpRequester(DropUplocationActivity.this, map3, 5, DropUplocationActivity.this);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if(serviceCode == 4)
        {
            try {
                Logger.log(response);

//                BaseService.dismissProgressDialog();
                JSONObject obj = new JSONObject(response);
                JSONArray fareArray = new JSONArray();
                fareArray = obj.getJSONArray("Table");
                String fare = fareArray.getJSONObject(0).get("Fare").toString();
                Log.e("TAG", "the fare esitamtion is: " + fare);
                double fareDouble = Double.parseDouble(fare);
                int fareInt = (int)fareDouble;
                int first = fareInt;
                int second = first+35;
                tv_fare_estimation.setText("PKR " +first + "-"+second);

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if(serviceCode == 5)
        {
            JSONObject obj = null;
            JSONArray intensityJsonArray = null;
            try {
                obj = new JSONObject(response);
                intensityJsonArray = new JSONArray();
                intensityJsonArray = obj.getJSONArray("Table");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(intensityJsonArray.length() > 0)
            {
                try {
                    String cab_intensity = intensityJsonArray.getJSONObject(0).get("Cab Intensity").toString();
                    String rr_intensity = intensityJsonArray.getJSONObject(0).get("Room Intensity").toString();
                    String rv_intensity = intensityJsonArray.getJSONObject(0).get("RV Intensity").toString();

                    cabIntensity.setText(cab_intensity);
                    rrIntensity.setText(rr_intensity);
                    rvIntensity.setText(rv_intensity);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if(serviceCode == 2)
        {
            ServiceProgressDialog.dismissDialog();
            Logger.log(response);
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray jsonArray = new JSONArray();
                jsonArray = obj.getJSONArray("Table");
                req_id = jsonArray.getJSONObject(0).get("RequestId").toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            //show Dialog
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(R.string.app_name);
            alert.setMessage("Your request received successfully Please wait for response of Driver.");
            Toast.makeText(DropUplocationActivity.this, ""+req_id, Toast.LENGTH_SHORT).show();
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
        }

        if(serviceCode == 3)
        {
            //Alert Dialog for showing information...
            ServiceProgressDialog.dismissDialog();
            Logger.log(response);
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray jsonArray = new JSONArray();
                jsonArray = obj.getJSONArray("Table");
                String name = jsonArray.getJSONObject(0).get("DriverName").toString();
                String contactNo = jsonArray.getJSONObject(0).get("DriverMobile").toString();
                String V_regNo = jsonArray.getJSONObject(0).get("VRegNo").toString();
                String V_model = jsonArray.getJSONObject(0).get("V_Model").toString();
                Toast.makeText(DropUplocationActivity.this, "Found "+name, Toast.LENGTH_SHORT).show();

                final AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Driver found!");
                alert.setMessage("" + Html.fromHtml("Name: " + name + "<br>" + "Contact No: " + contactNo + "<br>" + "Vehicle Reg. No: " + V_regNo + "<br>" + "Vehicle model: " + V_model));
                //Toast.makeText(DropUplocationActivity.this, ""+req_id, Toast.LENGTH_SHORT).show();
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
    /*                                            //show Dialog
                                                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                                                alert.setTitle(R.string.app_name);
                                                alert.setMessage("Your request received successfully Please wait for response of Driver.");
                                                Toast.makeText(DropUplocationActivity.this, ""+req_id, Toast.LENGTH_SHORT).show();
                                                alert.setPositiveButton("OK", null);
                                               alert.show();*/
        }

        if(serviceCode == 7)
        {

            pd.dismiss();



            //Alert Dialog for showing information...
            ServiceProgressDialog.dismissDialog();
            Logger.log(response);
            try {
                JSONObject obj = new JSONObject(response);
                JSONArray jsonArray = new JSONArray();
                jsonArray = obj.getJSONArray("Table");
                String requestId = jsonArray.getJSONObject(0).get("RequestId").toString();
                mREQUEST_ID = requestId;
                Log.e("TAG", "the Request is is: " + requestId);
                Intent intent = new Intent(DropUplocationActivity.this, DriverDetailMapsActivity.class);
                intent.putExtra("requestId", requestId);
                startActivity(intent);
                //startActivity(new Intent(DropUplocationActivity.this, DriverDetailMapsActivity.class));

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
//                Toast.makeText(DropUplocationActivity.this, ""+position, Toast.LENGTH_SHORT).show();
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
                            new DropUplocationActivity.DownloadImageTask(markerImg).execute(detailImgURL);

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
        switch (v.getId())
        {
            /*case R.id.heat_map_tv:
                if (!isServiceInProgressFlag)
                {
                    if (heatMapFlag)
                    {
                        heatMapFlag = false;


                        mMap.clear();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.latitude, gpsTracker.longitude),15));

                        //HttpRequest
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("url", Urls.GET_ASSETS);
                        map.put("Lat", String.valueOf(latitude));
                        map.put("Long", String.valueOf(longitude));
                        map.put("Radius", "30");
                        map.put("AssetType", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB));
                        new HttpRequester(DropUplocationActivity.this, map, 1, DropUplocationActivity.this);
                        isServiceInProgressFlag = true;
                    }
                    else
                    {
                        mMap.clear();
                        heatMapFlag = true;

                        mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(31.4507, 74.3447),10));
                    }
                }
                else
                {
                    Toast.makeText(DropUplocationActivity.this, "Please wait while loading", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.add_cab_RL:
                if (SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB).equals("cab"))
                {
                    if (SharedPrefs.getBooleanPref(sharedPreferences, SharedPrefs.STATUS_ADD_CAB))
                    {
                        Intent intent = new Intent(DropUplocationActivity.this, DetailViewCab.class);
                        intent.putExtra("Vid", "1235"); // dummy id
                        intent.putExtra("VType", "cab");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intentCabAdd = new Intent(DropUplocationActivity.this, AddCabFirst.class);
                        startActivity(intentCabAdd);
                    }
                }
                else if (SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB).equals("rr"))
                {
                    if (SharedPrefs.getBooleanPref(sharedPreferences, SharedPrefs.STATUS_ADD_RR))
                    {
                        Intent intent = new Intent(DropUplocationActivity.this, DetailView.class);
                        intent.putExtra("Vid", "416"); //dummy id
                        intent.putExtra("VType", "rr");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intentCabAdd = new Intent(DropUplocationActivity.this, AddRFirst.class);
                        startActivity(intentCabAdd);
                    }
                }
                else if (SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB).equals("rv"))
                {
                    if (SharedPrefs.getBooleanPref(sharedPreferences, SharedPrefs.STATUS_ADD_RV))
                    {
                        Intent intent = new Intent(DropUplocationActivity.this, DetailView.class);
                        intent.putExtra("Vid", "450"); // dummy id
                        intent.putExtra("VType", "rv");
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intentCabAdd = new Intent(DropUplocationActivity.this, AddRVFirst.class);
                        startActivity(intentCabAdd);
                    }
                }

                break;
*/
            case R.id.markerDetailRL:

                break;
/*            case R.id.req_btn:
                //HttpRequest
                ServiceProgressDialog.showDialog(DropUplocationActivity.this);

                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", Urls.SAVE_NEW_REQUEST);
                map.put("CName", "Ali");
                map.put("Rfrom", "+323350640222");
                map.put("source", "Arfa Karim");
                map.put("custLat", "31.504163");
                map.put("custLong", "74.331526");
                map.put("asset", "-1");
                map.put("destination", "Liberty market lahore, Pakistan");
                map.put("distance", "5 km");
                map.put("dtime", "16:23");
                map.put("VType", "Cab");
                map.put("msg", "Any");
                map.put("status", "New");
                map.put("uid", "Null");

                new HttpRequester(DropUplocationActivity.this, map, 2, this);
                break;*/
        }
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
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
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
                DropUplocationActivity.this);
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
        AlertDialog.Builder internetBuilder = new AlertDialog.Builder(DropUplocationActivity.this);
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
            FragmentManager manager = DropUplocationActivity.this.getSupportFragmentManager();
            if (manager == null) {
                return;
            }
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.map, mapFragment);// 2nd parameter will be more fragment.newInstance
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction.commit();
            searchBar.setVisibility(View.VISIBLE);
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
        new HttpRequester(DropUplocationActivity.this, map2, 1, DropUplocationActivity.this);

//                        BaseService.handleProgressBar(true);
    }

    private void dropoffSearchHandler(){

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search_bar_header.setVisibility(View.VISIBLE);
                mAutocompleteTextView.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mAutocompleteTextView, InputMethodManager.SHOW_IMPLICIT);

               /* Dialog dialog = new Dialog(DropUplocationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.drop_search_dialog);
               final AutoCompleteTextView dialog_et_search_text_view = (AutoCompleteTextView) dialog.findViewById(R.id.dialog_et_search_text_view);

                dialog_et_search_text_view.setThreshold(2);
                dialog_et_search_text_view.setOnItemClickListener(mAutocompleteClickListener);
                mPlaceArrayAdapter = new PlaceArrayAdapter(DropUplocationActivity.this, android.R.layout.simple_list_item_1, BOUNDS_MOUNTAIN_VIEW, null);
                dialog_et_search_text_view.setAdapter(mPlaceArrayAdapter);

                dialog_et_search_text_view.clearFocus();
                dialog_et_search_text_view.setCursorVisible(false);
                dialog_et_search_text_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog_et_search_text_view.setCursorVisible(true);
                    }
                });

                dialog.show();
*/
            }
        });
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

    private void bookRideClickHandler(){

        bt_book_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd.setMessage("Placing Request");
                pd.setCancelable(false);
                pd.show();


                String firstName =   SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.FIRST_NAME);
                String lastName =   SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.LAST_NAME);
                String fullName = firstName + " "+ lastName;
                String C_ID =   SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID);
                String NUMBER = SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_MOBILE);
                String sourceText = tv_pick_up_address.getText().toString();
                String customerLat = pickup_lat.getText().toString();
                String customerLng = pickup_lng.getText().toString();
                Double pickLat = Double.parseDouble(customerLat);
                Double pickLng = Double.parseDouble(customerLng);
                String destinationAddress = tv_drop_off.getText().toString();
                String message = tv_pick_up_detail.getText().toString();

                Log.e("TAG", "the user credentail first name: " + firstName);
                Log.e("TAG", "the user credentail Last name: " + lastName);
                Log.e("TAG", "the user credentail fullName: " + fullName);
                Log.e("TAG", "the user credentail Customer Id: " + C_ID);
                Log.e("TAG", "the user credentail Number: " + NUMBER);
                Log.e("TAG", "the user credentail sourceText: " + sourceText);
                Log.e("TAG", "the user credentail pickLat: " + pickLat);
                Log.e("TAG", "the user credentail pickLng: " + pickLng);
                Log.e("TAG", "the user credentail destinationAddress: " + destinationAddress);
                Log.e("TAG", "the user credentail message: " + message);

                if(destinationAddress.length()==0){
                    destinationAddress = "Customer will guide.";
                }
                //HttpRequest
                HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("url", Urls.Save_Request_New);
                map2.put("CName", fullName);
                map2.put("Rfrom", NUMBER);
                map2.put("source", sourceText);
                map2.put("custLat", String.valueOf(pickLat));
                map2.put("custLong", String.valueOf(pickLng));
                map2.put("asset", "-1");
                map2.put("destination", destinationAddress);
                map2.put("distance", "5 km");
                map2.put("dtime", "16:30");
                map2.put("VType", "Cab");
                map2.put("msg", "");
                map2.put("status", "New");
                map2.put("uid", "Null");

                new HttpRequester(DropUplocationActivity.this, map2, 7, DropUplocationActivity.this);
                //mREQUEST_ID
                //


            }
        });
    }

    private void addDetailHandler(){

        tv_add_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(DropUplocationActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.pick_up_detail_dialog);

                TextView tv_pickup_text = (TextView) dialog.findViewById(R.id.tv_dialog_cancel_text);
                final EditText et_dialog_detail_text = (EditText) dialog.findViewById(R.id.et_dialog_detail_text);
                ImageView iv_dialog_crose_text = (ImageView) dialog.findViewById(R.id.iv_dialog_crose_text);
                TextView tv_dialog_save = (TextView) dialog.findViewById(R.id.tv_dialog_save);
                TextView tv_dialog_cancel_text = (TextView) dialog.findViewById(R.id.tv_dialog_cancel_text);

                String text = tv_pick_up_detail.getText().toString();
                if (text.length()>0){
                    iv_dialog_crose_text.setVisibility(View.VISIBLE);
                    et_dialog_detail_text.setText(tv_pick_up_detail.getText().toString());
                }
                iv_dialog_crose_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        et_dialog_detail_text.setText("");
                    }
                });

                tv_dialog_save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        tv_pick_up_detail.setText(et_dialog_detail_text.getText().toString());
                        dialog.dismiss();
                    }
                });

                tv_dialog_cancel_text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                dialog.show();

            }
        });
    }

    //calculation distance
    public double shortDistance(double fromLong, double fromLat, double toLong, double toLat){
        double d2r = Math.PI / 180;
        double dLong = (toLong - fromLong) * d2r;
        double dLat = (toLat - fromLat) * d2r;
        double a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.cos(fromLat * d2r)
                * Math.cos(toLat * d2r) * Math.pow(Math.sin(dLong / 2.0), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = 6367000 * c;
        return Math.round(d);
    }

}

