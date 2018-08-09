package com.shoaibnwar.hotelandrestaurant.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.shoaibnwar.hotelandrestaurant.Utils.SharedPrefs;

/**
 * Created by gold on 7/12/2018.
 */


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        //Displaying token on logcat
        Log.e(TAG, "Refreshed token: " + refreshedToken);

        SharedPreferences sharedPreferences = getSharedPreferences(SharedPrefs.PREF_NAME, Context.MODE_PRIVATE);
        SharedPrefs.StoreStringPref(sharedPreferences, SharedPrefs.ANDROID_ID, refreshedToken);


    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }

}