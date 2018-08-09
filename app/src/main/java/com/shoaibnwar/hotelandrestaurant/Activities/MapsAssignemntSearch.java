package com.shoaibnwar.hotelandrestaurant.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.shoaibnwar.hotelandrestaurant.Adapters.PlaceArrayAdapter;

import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.GPSTracker;
import com.shoaibnwar.hotelandrestaurant.Services.UserService;
import com.shoaibnwar.hotelandrestaurant.Utils.Utils;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MapsAssignemntSearch extends FragmentActivity implements OnMapReadyCallback, AsyncTaskCompleteListener, View.OnClickListener,
        GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    Boolean heatMapFlag;
    HeatmapTileProvider mProvider;
    SupportMapFragment mapFragment;
    View searchBar;
    RelativeLayout centerMarker;

    UserService userService;
    GPSTracker gpsTracker;
    private static final int ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;

    Handler mHandler;
    private Geocoder geocoder;
    private List<Address> addresses;

    Double latitude, longitude;
    private PlaceAutocompleteFragment autocompleteFragment;
    Animation rotation;
    Marker marker;
    JSONArray jsonArray;
    String ADRESS = "-1";


    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private AutoCompleteTextView mAutocompleteTextView;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(31.398160, 74.180831), new LatLng(31.430610, 74.972090));
    protected LatLng mCenterLocation = new LatLng(31.398160, 74.180831);
    boolean isServiceInProgressFlag;

    RelativeLayout ll_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_assignemnt_search);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        heatMapFlag = false;
        isServiceInProgressFlag = true;

        searchBar = (View) findViewById(R.id.place_autocomplete_fragment);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        rotation = AnimationUtils.loadAnimation(MapsAssignemntSearch.this, R.anim.rotate);
        rotation.setFillAfter(true);

        gpsTracker = new GPSTracker(this);
        userService = new UserService(MapsAssignemntSearch.this);


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

        mGoogleApiClient = new GoogleApiClient.Builder(MapsAssignemntSearch.this)
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


        RelativeLayout iv_bottom_pointing = (RelativeLayout) findViewById(R.id.iv_bottom_pointing);
        iv_bottom_pointing.bringToFront();

        ll_confirm = (RelativeLayout) findViewById(R.id.ll_confirm);

        confirmClickHandler();

    } // onCreate finishes

    @SuppressLint("NewApi")
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        if (ev.getAction() == MotionEvent.ACTION_DOWN && !getLocationOnScreen(mAutocompleteTextView).contains(x, y)) {
            InputMethodManager input = (InputMethodManager)
                    MapsAssignemntSearch.this.getSystemService(Context.INPUT_METHOD_SERVICE);
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

            geocoder = new Geocoder(MapsAssignemntSearch.this, Locale.getDefault());
            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;
            Log.e("TAG", "the late are here: " + latitude);
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0);
            ADRESS = address;
            Log.e("TAG", "the address of latlng is: " + address);
            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(address).snippet("Your pick up location"));

            Utils.hideSoftKeyboard(MapsAssignemntSearch.this);
            mAutocompleteTextView.setText("");
            mAutocompleteTextView.setHint("Search");
//            mAutocompleteTextView.clearFocus();

            //HttpRequest
           /* HashMap<String, String> map = new HashMap<String, String>();
            map.put("url", Urls.GET_ASSETS);
            map.put("Lat", String.valueOf(latitude));
            map.put("Long", String.valueOf(longitude));
            map.put("Radius", "30");
            map.put("AssetType", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB));
            new HttpRequester(MapsAssignemntSearch.this, map, 1, MapsAssignemntSearch.this);*/
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
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onResume() {
        super.onResume();


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo activeWIFIInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_WIFI);
        if (activeWIFIInfo.isConnected() || activeNetInfo.isConnected()) {
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
//        window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_color));

        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        autocompleteFragment.setHint("Search");
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.d("TAG", "Place String is: " + place);
                Log.d("TAG", "Place: " + place.getName());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

                geocoder = new Geocoder(MapsAssignemntSearch.this, Locale.getDefault());
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

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);

        final LatLng latlng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(latlng).icon(BitmapDescriptorFactory.fromResource(R.drawable.user_pin)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.latitude, gpsTracker.longitude), 15));

//        Toast.makeText(MapsActivity.this, "" + mMap.getCameraPosition().target, Toast.LENGTH_SHORT).show();

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                if (heatMapFlag) {

                } else {
                    final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    }
                    else {
                        if (ActivityCompat.checkSelfPermission(MapsAssignemntSearch.this, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            return;
                        }
                        manager.addGpsStatusListener(new GpsStatus.Listener() {
                            @Override
                            public void onGpsStatusChanged(int event) {

                            }

                        });
                    }
//                Toast.makeText(MapsActivity.this, ""+googleMap.getCameraPosition().target, Toast.LENGTH_SHORT).show();

                    LatLng latlng = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();
                    Log.e("TAg", "the camera change postion lat lng " + latlng);


                    Geocoder geocoder;
                    List<Address> addresses = null;
                    geocoder = new Geocoder(MapsAssignemntSearch.this, Locale.getDefault());
                    try {
                        latitude = latlng.latitude;
                        longitude = latlng.longitude;
                        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        ADRESS = addresses.get(0).getAddressLine(0);
                        Log.e("TAg", "the camera change postion lat lng " + ADRESS);
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



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }



            }


        });




    } // onMapReady finished



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
        LatLng latlng = mMap.getProjection().getVisibleRegion().latLngBounds.getCenter();

    }

    @Override
    public void onConnectionSuspended(int i) {
        mPlaceArrayAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onTaskCompleted(String response, int serviceCode) {

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


    private void confirmClickHandler()
    {

        ll_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!ADRESS.equals("-1")){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",ADRESS);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }else {
                    Toast.makeText(gpsTracker, "Please Set Location First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
