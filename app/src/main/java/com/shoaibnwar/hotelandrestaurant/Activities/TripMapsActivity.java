package com.shoaibnwar.hotelandrestaurant.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.shoaibnwar.hotelandrestaurant.Fragments.CollectCashDialogBoxFragment;
import com.shoaibnwar.hotelandrestaurant.R;
import com.shoaibnwar.hotelandrestaurant.Services.BaseService;
import com.shoaibnwar.hotelandrestaurant.Services.GPSTracker;
import com.shoaibnwar.hotelandrestaurant.Services.HttpRequester;
import com.shoaibnwar.hotelandrestaurant.Utils.Constants;
import com.shoaibnwar.hotelandrestaurant.Utils.DirectionsJSONParser;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;
import com.shoaibnwar.hotelandrestaurant.Utils.Urls;
import com.shoaibnwar.hotelandrestaurant.Utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class TripMapsActivity extends FragmentActivity implements OnMapReadyCallback, AsyncTaskCompleteListener,
        View.OnClickListener{

    BaseService baseService;
    SupportMapFragment mapFragment;
    GoogleMap mMap;
    GPSTracker gpsTracker;
    SharedPreferences sharedPreferences;
    RelativeLayout arrivedBtn;
    LinearLayout startBotomBar, endBottomBar;
    RelativeLayout startBtn, alertBtn, endRL;
    JSONArray jsonArray;
    String reqId, destination;
    String latitude, longitude;
    TextView address;
    private Geocoder geocoder;
    Boolean tripStarted;
    private ArrayList<LatLng> points; //added
    Polyline line; //added
    static ArrayList<LatLng> finalArrayListLatLng;
    LatLng latlngPickUp;
    LatLng destinationLatLng;
    static PolylineOptions finalOptions;
    ImageView navigatorBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip_activity_maps);
        sharedPreferences = getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);

        tripStarted = false;

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.trip_map);
        mapFragment.getMapAsync(this);

        address = (TextView) findViewById(R.id.address);

        navigatorBtn = (ImageView) findViewById(R.id.navigation_img);
        navigatorBtn.setOnClickListener(this);

        arrivedBtn = (RelativeLayout) findViewById(R.id.arrived_btn);
        arrivedBtn.setOnClickListener(this);

        startBotomBar = (LinearLayout) findViewById(R.id.start_bottom_bar);
        endBottomBar = (LinearLayout) findViewById(R.id.end_bottom_bar);
        startBtn = (RelativeLayout) findViewById(R.id.startRL);
        startBtn.setOnClickListener(this);
        alertBtn = (RelativeLayout) findViewById(R.id.alertRL);
        alertBtn.setOnClickListener(this);
        endRL = (RelativeLayout) findViewById(R.id.endRL);
        endRL.setOnClickListener(this);

        baseService = new BaseService(TripMapsActivity.this);
        points = new ArrayList<LatLng>();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gpsTracker = new GPSTracker(TripMapsActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
// position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0,0,10,180);

        //Add marker to destination point
        String reqNo = getIntent().getStringExtra("req_no");
        latitude = getIntent().getStringExtra("lat");
        longitude = getIntent().getStringExtra("lng");
        reqId = getIntent().getStringExtra("lng");
        destination = getIntent().getStringExtra("destination");

        latlngPickUp = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        destinationLatLng = null;

        String pickUpAddress = getAddressFromLatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        address.setText(pickUpAddress);
        Log.d("xxx:PickUpLatLng ", latlngPickUp.toString());
        Log.d("xxx:PicUpAddress ", pickUpAddress);

        mMap.addMarker(new MarkerOptions().position(latlngPickUp).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));

        //move camera to current location point
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gpsTracker.latitude, gpsTracker.longitude),13));

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng = new LatLng(latitude, longitude);
                points.add(latLng);
                reDrawLine(mMap);
            }
        });

        if (destination.contains("Customer will guide.") || destination.contains("customer will guide.")
                || destination.contains("Customer will guide") || destination.contains("customer will guide"))
        {
            Log.d("xxx:DestLatLng ", "Customer will guide.");
        }
        else
        {
            destinationLatLng = getLocationFromAddress(TripMapsActivity.this, destination);
            Log.d("xxx:DestLatLng ", destinationLatLng.toString());
            mMap.addMarker(new MarkerOptions().position(destinationLatLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
//            ArrayList<LatLng> list = getDirections(latlngPickUp.latitude, latlngPickUp.longitude, destinationLatLng.latitude, destinationLatLng.longitude);
            /*DownloadDirections task = new DownloadDirections();
            task.execute(String.valueOf(latlngPickUp.latitude), String.valueOf(latlngPickUp.longitude),
                    String.valueOf(destinationLatLng.latitude), String.valueOf(destinationLatLng.longitude));*/

            String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latlngPickUp.latitude + "," + latlngPickUp.longitude
                    + "&destination=" + destinationLatLng.latitude + "," + destinationLatLng.longitude + "&sensor=false";

            DownloadTask downloadTask = new DownloadTask();
            // Start downloading json data from Google Directions API
            downloadTask.execute(url);

        }
    }

    private static class MyTaskParams
    {
        ArrayList<LatLng> arr;
        GoogleMap googleMap;
        MyTaskParams(GoogleMap map, ArrayList<LatLng> arrList)
        {
            googleMap = map;
            arr = arrList;
        }
    }


    private String downloadUrl(String strUrl) throws IOException {
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
    private class DownloadTask extends AsyncTask<String, Void, String> {

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
                lineOptions.width(10);
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
                Toast.makeText(TripMapsActivity.this, "Unable to draw your trip, try Google navigation", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class DownloadDirections extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... params) {
            final double latitude = Double.parseDouble(params[0]);
            final double longitude = Double.parseDouble(params[1]);
            final double latitude2 = Double.parseDouble(params[2]);
            final double longitude2 = Double.parseDouble(params[3]);
            try {
                String tag[] = { "lat", "lng" };
                ArrayList<LatLng> list_of_geopoints = new ArrayList<LatLng>();
                HttpResponse response = null;

                String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +latitude + "," + longitude
                        + "&destination=" + latitude2 + "," + longitude2 + "&sensor=false";

                Log.d("xxx:url", url.toString());
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpPost httpPost = new HttpPost(url);
                response = httpClient.execute(httpPost, localContext);
                InputStream in = response.getEntity().getContent();
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document doc = builder.parse(in);
                if (doc != null) {
                    NodeList nl1, nl2;
                    nl1 = doc.getElementsByTagName(tag[0]);
                    nl2 = doc.getElementsByTagName(tag[1]);
                    if (nl1.getLength() > 0) {
                        for (int i = 0; i < nl1.getLength(); i++) {
                            Node node1 = nl1.item(i);
                            Node node2 = nl2.item(i);
                            double lat = Double.parseDouble(node1.getTextContent());
                            double lng = Double.parseDouble(node2.getTextContent());
                            int latitudeVal = (int) (lat * 1E6);
                            int longitudeVal = (int) (lng * 1E6);
                            list_of_geopoints.add(new LatLng(Double.parseDouble(String.valueOf(latitudeVal)), Double.parseDouble(String.valueOf(longitudeVal))));
                        }
                    }
                    else
                    {
                        // No points found
                    }
                }
                finalArrayListLatLng = list_of_geopoints;
                return response.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Toast.makeText(TripMapsActivity.this, "error", Toast.LENGTH_SHORT).show();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            drawDirections(mMap, finalArrayListLatLng);
            Log.d("xxxx", finalArrayListLatLng.toString());
        }
    }

    private class DrawDirectionsInBackground extends AsyncTask<MyTaskParams, Void, Void>
    {
        GoogleMap my_map;
        @Override
        protected Void doInBackground(MyTaskParams... params) {
            my_map = params[0].googleMap;
            PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
            for (int i = 0; i < params[0].arr.size(); i++) {
                LatLng point = params[0].arr.get(i);
                options.add(point);
            }
            finalOptions = options;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(TripMapsActivity.this, "trying to draw trip", Toast.LENGTH_SHORT).show();
            line = my_map.addPolyline(finalOptions); //add Polyline
        }
    }

    public static ArrayList<LatLng> getDirections(double lat1, double lon1, double lat2, double lon2) {
        return null;
    }

    private void reDrawLine(GoogleMap googleMap)
    {
        PolylineOptions options = new PolylineOptions().width(10).color(Color.RED).geodesic(true);
        for (int i = 0; i < points.size(); i++) {
            LatLng point = points.get(i);
            options.add(point);
        }
//        addMarker(); //add Marker in current position
        line = googleMap.addPolyline(options); //add Polyline
    }

    private void drawDirections(final GoogleMap googleMap, final ArrayList<LatLng> pointsArrayList)
    {
        PolylineOptions options = new PolylineOptions().width(10).color(Color.BLUE).geodesic(true);
        for (int i = 0; i < pointsArrayList.size(); i++) {
            LatLng point = pointsArrayList.get(i);
            options.add(point);
        }
        line = googleMap.addPolyline(options); //add Polyline

/*        MyTaskParams params = new MyTaskParams(googleMap, pointsArrayList);
        DrawDirectionsInBackground task = new DrawDirectionsInBackground();
        task.execute(params);*/
    }

    public String getAddressFromLatLng(double lat, double lng)
    {
        List<Address> addresses = null;
        geocoder = new Geocoder(TripMapsActivity.this, Locale.getDefault());
        double latitude = lat;
        double longitude = lng;
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);

        return address;
    }

    public LatLng getLocationFromAddress(Context context,String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return p1;
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

                    if (jsonArray.length() > 0) {
                        arrivedBtn.setVisibility(View.GONE);
                        startBotomBar.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case 2:
                JSONObject obj2 = null;
                try {
                    Log.d("xxx", response);
                    baseService.dismissProgressDialog();
                    obj2 = new JSONObject(response);
                    jsonArray = new JSONArray();
                    jsonArray = obj2.getJSONArray("Table");

                    if (jsonArray.length() > 0)
                    {
                        startBotomBar.setVisibility(View.GONE);
                        endBottomBar.setVisibility(View.VISIBLE);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

            case 3:
                JSONObject obj4 = null;
                try {
                    Log.d("xxx", response);
                    baseService.dismissProgressDialog();
                    obj4 = new JSONObject(response);
                    jsonArray = new JSONArray();
                    jsonArray = obj4.getJSONArray("Table");

                    if (jsonArray.length() > 0)
                    {
                        endBottomBar.setVisibility(View.GONE);
                        CollectCashDialogBoxFragment collectCashDialogBoxFragment = CollectCashDialogBoxFragment.newInstance();
                        collectCashDialogBoxFragment.show(getFragmentManager(), "dialog");
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.navigation_img:
                Uri gmmIntentUri = Uri.parse("https://www.google.com/maps?daddr=" + destinationLatLng.latitude + "," + destinationLatLng.longitude
                        +"&saddr=" + latlngPickUp.latitude + "," + latlngPickUp.longitude);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
            case R.id.arrived_btn:
                //Http request
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("url", Urls.DRIVER_ARRIVED);
                map.put("Vid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                map.put("Reqno", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.REQUEST_ID)); //reqId
                map.put("ArrivalTime", Utils.getCurrentDate(Constants.SERVER_TIME_FORMAT));

                new HttpRequester(TripMapsActivity.this, map, 1, TripMapsActivity.this);
                baseService.handleProgressBar(true);
                break;
            case R.id.startRL:
                tripStarted = true;
                //Http request
                HashMap<String, String> map3 = new HashMap<String, String>();
                map3.put("url", Urls.START_RIDE);
                map3.put("Vid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                map3.put("Reqno", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.REQUEST_ID)); //reqId
                map3.put("StartTime", Utils.getCurrentDate(Constants.SERVER_TIME_FORMAT));

                new HttpRequester(TripMapsActivity.this, map3, 2, TripMapsActivity.this);
                baseService.handleProgressBar(true);
                break;
            case R.id.alertRL:
                //Http request
                HashMap<String, String> map2 = new HashMap<String, String>();
                map2.put("url", Urls.DRIVER_ARRIVED);
                map2.put("Vid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                map2.put("Reqno", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.REQUEST_ID)); //reqId
                map2.put("ArrivalTime", Utils.getCurrentDate(Constants.SERVER_TIME_FORMAT));

                new HttpRequester(TripMapsActivity.this, map2, 1, TripMapsActivity.this);
                baseService.handleProgressBar(true);
                break;
            case R.id.endRL:
                //Http request
                HashMap<String, String> map4 = new HashMap<String, String>();
                map4.put("url", Urls.END_RIDE);
                map4.put("Vid", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_ID));
                map4.put("Reqno", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.REQUEST_ID)); //reqId
                map4.put("EndTime", Utils.getCurrentDate(Constants.SERVER_TIME_FORMAT));
                map4.put("DropLocation", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.DESTINATION));
                map4.put("VMobileNo", SharedPrefs.getStringPref(sharedPreferences, SharedPrefs.C_MOBILE));
                map4.put("SLat", latitude);
                map4.put("SLong", longitude);
                map4.put("DLat", String.valueOf(gpsTracker.getLatitude()));
                map4.put("DLong", String.valueOf(gpsTracker.getLongitude()));

                new HttpRequester(TripMapsActivity.this, map4, 3, TripMapsActivity.this);
                baseService.handleProgressBar(true);
                break;
        }
    }

}
