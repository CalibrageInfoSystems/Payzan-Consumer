package com.calibrage.payzanconsumer.notifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.activity.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Calibrage11 on 1/3/2018.
 */

public class MyFireBaseMessagingService extends FirebaseMessagingService{

    private static final String TAG = "MyFirebaseMsgService";
     private String subTitle,message;
    private String imageUri;
    private String FilterCriteria;
    private String Action;
    private  Bitmap bitmap;
    private Bundle notificatinData;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //Displaying data in log
        //It is optional
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage
        );
        String s = remoteMessage.getData().get("image");

        //Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getBody(),s);


    }

    @Override
    public   void  handleIntent(Intent intent) {
        notificatinData = intent.getExtras();
        if (notificatinData != null) {
            //   CommonUtils.logPrint(TAG, "Notification Message Body: " + intent.getExtras().toString());
            message = intent.getStringExtra("gcm.notification.body");
            subTitle = intent.getStringExtra("gcm.notification.title");
            imageUri = intent.getStringExtra("mediaUrl");
            FilterCriteria = intent.getStringExtra("FilterCriteria");
            Action = intent.getStringExtra("Action");
            bitmap = getBitmapFromURL(imageUri);

        }
    }





    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody,String imageurl) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        Bitmap bitmap = getBitmapFromURL("https://graph.facebook.com/1297352706984524/picture?type=large");
        // Bitmap bitmap = getBitmapFromURL("https://media.licdn.com/media/AAEAAQAAAAAAAAbqAAAAJDFkNWZiNjkxLTcyZjItNDJmYi1iMWMzLWNkODA4MWQyYTI3Mw.png");
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.home_logo_demo);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.home_logo_demo)
                .setContentTitle("WhatsPOPN Notification")
                .setContentText("aaaaaaaa"+messageBody)
                .setLargeIcon(getBitmapFromURL(imageurl))
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(getBitmapFromURL(imageurl)))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 , notificationBuilder.build());

    }
    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.home_logo_demo : R.drawable.home_logo_demo;
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    }

