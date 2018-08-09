package com.shoaibnwar.hotelandrestaurant.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.shoaibnwar.hotelandrestaurant.Activities.DriverDetailMapsActivity;
import com.shoaibnwar.hotelandrestaurant.R;

/**
 * Created by gold on 7/12/2018.
 */

public class FireBaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "xxx";
    String reqId, destination, lat, lng, name, mobile, finalPickUpAddress, pickUpTime;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        Log.e("TAg", "the message from fire base is: " + remoteMessage.toString());
        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

//        Log.d("xxx", "reqId: "+ reqId);

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try
            {

                String driverArrived = remoteMessage.getData().get("DriverArrived");
                Log.e("TAG", "the driver arrived status is: " + driverArrived);
                if (driverArrived!=null){

                    Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.cab_colored)
                            .setContentTitle(driverArrived)
                            .setContentText(remoteMessage.getNotification().getBody())
                            .setSound(notificationSound)
                            .setAutoCancel(true);
                    //.setContentIntent(pendingIntent);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(0, notificationBuilder.build());

                }else {

                    String VModel = remoteMessage.getData().get("VModel").toString();
                    String VRegNo = remoteMessage.getData().get("VRegNo").toString();
                    String DriverName = remoteMessage.getData().get("DriverName").toString();
                    String VMobileNo = remoteMessage.getData().get("VMobileNo").toString();
                    String DriverImage = remoteMessage.getData().get("DriverImage").toString();
                    String AssetImage = remoteMessage.getData().get("AssetImage").toString();
                    String DFcm = remoteMessage.getData().get("DFcm").toString();
                    String DLat = remoteMessage.getData().get("DLat").toString();
                    String DLong = remoteMessage.getData().get("DLong").toString();
                    String DLicense = remoteMessage.getData().get("DLicense").toString();
                    String CustomerRating = remoteMessage.getData().get("CustomerRating").toString();


                    Log.e("TAG", "the driver response through fcm is Vmodel :" + VModel);
                    Log.e("TAG", "the driver response through fcm is VRegNo :" + VRegNo);
                    Log.e("TAG", "the driver response through fcm is DriverName :" + DriverName);
                    Log.e("TAG", "the driver response through fcm is VMobileNo :" + VMobileNo);
                    Log.e("TAG", "the driver response through fcm is DriverImage :" + DriverImage);
                    Log.e("TAG", "the driver response through fcm is AssetImage :" + AssetImage);
                    Log.e("TAG", "the driver response through fcm is DFcm :" + DFcm);
                    Log.e("TAG", "the driver response through fcm is DLat :" + DLat);
                    Log.e("TAG", "the driver response through fcm is DLong :" + DLong);
                    Log.e("TAG", "the driver response through fcm is DLicense :" + DLicense);
                    Log.e("TAG", "the driver response through fcm is CustomerRating :" + CustomerRating);

                    Intent intent = new Intent();
                    intent.setAction("driver_response");
                    intent.putExtra("VModel", VModel);
                    intent.putExtra("VRegNo", VRegNo);
                    intent.putExtra("DriverName", DriverName);
                    intent.putExtra("VMobileNo", VMobileNo);
                    intent.putExtra("DriverImage", DriverImage);
                    intent.putExtra("AssetImage", AssetImage);
                    intent.putExtra("DFcm", DFcm);
                    intent.putExtra("DLat", DLat);
                    intent.putExtra("DLong", DLong);
                    intent.putExtra("DLicense", DLicense);
                    intent.putExtra("CustomerRating", CustomerRating);
                    sendBroadcast(intent);

                    SharedPreferences preDriverInformation = getSharedPreferences("driver_info", 0);
                    SharedPreferences.Editor editor = preDriverInformation.edit();

                    editor.putString("VModel", VModel);
                    editor.putString("VRegNo", VRegNo);
                    editor.putString("DriverName", DriverName);
                    editor.putString("VMobileNo", VMobileNo);
                    editor.putString("DriverImage", DriverImage);
                    editor.putString("AssetImage", AssetImage);
                    editor.putString("DFcm", DFcm);
                    editor.putString("DLat", DLat);
                    editor.putString("DLong", DLong);
                    editor.putString("DLicense", DLicense);
                    editor.putString("CustomerRating", CustomerRating);
                    //editor.commit();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        if(remoteMessage.getNotification() != null)
        {

            Log.e("TAg", "the notification is: " + remoteMessage.getNotification());

           /* Intent intent = new Intent(this, MapsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("notificationInfoBody", remoteMessage.getNotification().getBody());
            intent.putExtra("reqId", reqId);
            intent.putExtra("destination", destination);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("name", name);
            intent.putExtra("mobile", mobile);
            intent.putExtra("notificationInfoFrom", remoteMessage.getFrom());

            Log.e("TAG", "the reqId is: " + reqId);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.cab_colored)
                    .setContentTitle("Request")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setSound(notificationSound)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());*/


            ////////////////////////

/*            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }*/

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        String title = intent.getExtras().getString("gcm.notification.title");
        String body = intent.getExtras().getString("gcm.notification.body");
        String data = intent.getExtras().getString("gcm.notification");

        Log.e("TAG: ", "Key is haree: " + title);
        Log.e("TAG: ", "Key is body: " + body);
        Log.e("TAG: ", "Key is data: " + data);


        if (body.equals("Driver Arrived")){

            notificationForDriverArried(title, body);

        }

        //sendNotification(body);

     /*   for (String key : intent.getExtras().keySet()) {
            Object value = intent.getExtras().get(key);

            Log.e("TAG: ", " test test testr: " + key);
            Log.e("TAG: ", " text text text text: " + value);

            if (key.equals("title")){

                Log.e("TAG: ", " shoaib shoaib shoaib shoaib: " + value);

                String valueString = value.toString();
                Log.e("TAg", "The message from server " + valueString);
                String finishedText[] = valueString.split("\\.");
                String firstPart = finishedText[0];
                String secondPart = finishedText[1];
                //Calling method to generate notification
                NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancelAll();
                sendNotification(firstPart, secondPart);

            }

        }*/

    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void notificationForDriverArried(String title, String body){

        Intent intent = new Intent(this, DriverDetailMapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.cab_colored)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(notificationSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }
}
