package com.shoaibnwar.hotelandrestaurant.Utils;

import android.util.Log;

/**
 * Created by gold on 7/11/2018.
 */

public class Logger {
    private static String TAG = "testtaxiapp ::: ";
    private static boolean log = true;
    public static void log(String message){
        if (log){
            Log.e(TAG,message);
        }
    }
}