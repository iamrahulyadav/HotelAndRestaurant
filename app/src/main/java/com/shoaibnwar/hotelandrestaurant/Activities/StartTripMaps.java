package com.shoaibnwar.hotelandrestaurant.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.GPSTracker;
import com.shoaibnwar.hotelandrestaurant.Services.UserService;
import com.shoaibnwar.hotelandrestaurant.Utils.DirectionsJSONParser;
import com.shoaibnwar.hotelandrestaurant.Utils.Utils;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class StartTripMaps extends FragmentActivity implements OnMapReadyCallback, AsyncTaskCompleteListener,
        GoogleMap.OnMarkerClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    HeatmapTileProvider mProvider;
    SupportMapFragment mapFragment;


    UserService userService;
    GPSTracker gpsTracker;
    private static final int ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;
    String req_id;

    private Geocoder geocoder;
    private List<Address> addresses;

    Double latitude, longitude;

    SharedPreferences sharedPreferences;

    Animation rotation;

    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(new LatLng(31.398160, 74.180831), new LatLng(31.430610, 74.972090));
    protected LatLng mCenterLocation = new LatLng(31.398160, 74.180831);
    boolean isServiceInProgressFlag;

    Polyline line; //added
    LatLng latlngPickUp;
    LatLng destinationLatLng;
    static ArrayList<LatLng> finalArrayListLatLng;
    static PolylineOptions finalOptions;

    private Button bt_arrived;
    private TextView  tv_reaching_time;
    private ImageView iv_back_arrow;

    private TextView tv_address_line_1, tv_address_line_2;
    private TextView tv_delivery_Date, tv_delivery_time;

    String bookingDate = "";
    String bookingTime = "";
    String assignerID = "";
    String salemanID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_trip_maps);

        init();
        gettingIntentValues();
        btArrivedClickHandler();


    }
    private void init(){
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rotation = AnimationUtils.loadAnimation(StartTripMaps.this, R.anim.rotate);
        rotation.setFillAfter(true);

        gpsTracker = new GPSTracker(this);
        userService = new UserService(StartTripMaps.this);
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

        mGoogleApiClient = new GoogleApiClient.Builder(StartTripMaps.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();

        ArrayList<LatLng> locations = generateLocations();
        mProvider = new HeatmapTileProvider.Builder().data(locations).build();
        mProvider.setRadius(HeatmapTileProvider.DEFAULT_RADIUS);

        bt_arrived = (Button) findViewById(R.id.bt_arrived);
        tv_reaching_time = (TextView)findViewById(R.id.tv_reaching_time);

        iv_back_arrow = (ImageView) findViewById(R.id.iv_back_arrow);
        tv_address_line_1 = (TextView) findViewById(R.id.tv_address_line_1);
        tv_address_line_2 = (TextView) findViewById(R.id.tv_address_line_2);
        tv_delivery_Date = (TextView) findViewById(R.id.tv_delivery_Date);
        tv_delivery_time = (TextView) findViewById(R.id.tv_delivery_time);

    }//end of init


    private void gettingIntentValues(){

        Intent intent = getIntent();
        String address = intent.getStringExtra("address");
        bookingDate = intent.getStringExtra("bookingDate");
        bookingTime = intent.getStringExtra("bookingTime");
        assignerID = intent.getStringExtra("assignerId");
        salemanID = intent.getStringExtra("salemanid");

        tv_address_line_1.setText(address);
        if (!address.contains("Pakistan")) {
            tv_address_line_2.setText("Lahor, Pakistan");
        }else {tv_address_line_2.setText("");}
        tv_delivery_Date.setText("Booking Date " +bookingDate);
        tv_delivery_time.setText("Booking Time "+bookingTime);

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

            geocoder = new Geocoder(StartTripMaps.this, Locale.getDefault());
            double latitude = place.getLatLng().latitude;
            double longitude = place.getLatLng().longitude;
            Log.e("TAG", "the late are here: " + latitude);
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            } catch (IOException e) {
                e.printStackTrace();
            }

            String address = addresses.get(0).getAddressLine(0);
            Log.e("TAG", "the address of latlng is: " + address);
            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
//                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(address).snippet("Your pick up location"));

            Utils.hideSoftKeyboard(StartTripMaps.this);

//            mAutocompleteTextView.clearFocus();

            /*//HttpRequest
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("url", Urls.GET_ASSETS);
            map.put("Lat", String.valueOf(latitude));
            map.put("Long", String.valueOf(longitude));
            map.put("Radius", "30");
            map.put("AssetType", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.CURRENT_TAB));
            new HttpRequester(StartTripMaps.this, map, 1, StartTripMaps.this);*/
        }
    };




    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
     /*   mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                int visibility = markerLayout.getVisibility();
                if (visibility == View.VISIBLE) {
                    markerLayout.setVisibility(View.GONE);
                }
            }
        });*/

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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.latitude, gpsTracker.longitude), 11));

//        Toast.makeText(StartTripMaps.this, "" + mMap.getCameraPosition().target, Toast.LENGTH_SHORT).show();

        googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {


                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                }
                else {
                    if (ActivityCompat.checkSelfPermission(StartTripMaps.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    manager.addGpsStatusListener(new GpsStatus.Listener() {
                        @Override
                        public void onGpsStatusChanged(int event) {
                            switch(event) {
                                case GpsStatus.GPS_EVENT_STARTED:
                                    Toast.makeText(StartTripMaps.this, "Please wait for GPS to get connected...", Toast.LENGTH_SHORT).show();
                                    break;
                                case GpsStatus.GPS_EVENT_FIRST_FIX:
                                    Toast.makeText(StartTripMaps.this, "GPS locked! You can now find your current location.", Toast.LENGTH_SHORT).show();
                                    manager.removeGpsStatusListener(this);
                                    break;
                            }
                        }

                    });
                }
//                Toast.makeText(StartTripMaps.this, ""+googleMap.getCameraPosition().target, Toast.LENGTH_SHORT).show();


                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(StartTripMaps.this, Locale.getDefault());
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



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }


        });

        LatLng destinationLatLng = new LatLng(31.571359, 74.310355);
        mMap.addMarker(new MarkerOptions().position(destinationLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).title("Destination!"));

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitude + "," + longitude
                + "&destination=" + 31.571359 + "," + 74.310355 + "&sensor=false";

        DownloadTask downloadTask = new DownloadTask();
        LatLng sourLatLng = new LatLng(latitude, longitude);

        double distance = shortDistance(74.310355, 31.571359, longitude, latitude);
        distance = distance/1000;
        int intDistance = (int)distance;
        double time = distance*2.5;
        int TIME = (int)time;
        String timeIs = (timeConvert((int)time));
        Log.e("TAg", "the distance is: " + intDistance);
        Log.e("TAg", "the distance is: " + timeIs);

        tv_reaching_time.setText("You are "+TIME + " min away from the destination");


        // Start downloading json data from Google Directions API
        downloadTask.execute(url);


    } // onMapReady finished



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTaskCompleted(String response, int serviceCode) {
        if(serviceCode == 1)
        {
            isServiceInProgressFlag = false;

        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int position = Integer.parseInt(marker.getTag().toString());
//                Toast.makeText(StartTripMaps.this, ""+position, Toast.LENGTH_SHORT).show();
                /*for (int i=0; i<jsonArray.length() ; i++) {
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
                            new DownloadImageTask(markerImg).execute(detailImgURL);
                            markerTitle.setText(detailTitle);
                            markerGuests.setText(detailGuests);
                            markerPrice.setText(detailPrice);
                            markerLayout.setVisibility(View.VISIBLE);
                        }
                        else
                        {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }*/
                return false;
            }
        });
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

    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String>{

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service

            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }


    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(R.color.redish);
            }

            // Drawing polyline in the Google Map for the i-th route
            try
            {
                mMap.addPolyline(lineOptions);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(StartTripMaps.this, "Unable to draw your trip, try Google navigation", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void btArrivedClickHandler() {

        bt_arrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

           /*     Intent i = new Intent(StartTripMaps.this, OrderTaking.class);
                i.putExtra("bookingDate", bookingDate);
                i.putExtra("bookingTime", bookingTime);
                i.putExtra("assignerID", assignerID);
                i.putExtra("salemanID", salemanID);
                startActivity(i);*/

                Intent i = new Intent(StartTripMaps.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
    }

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


    //converting time into hrs and day
    public String timeConvert(int time) {
        return time/24/60 + ":" + time/60%24 + ':' + time%60;
    }
}

