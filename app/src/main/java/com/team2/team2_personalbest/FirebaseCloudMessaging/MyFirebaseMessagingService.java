package com.team2.team2_personalbest.FirebaseCloudMessaging;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        SharedPreferences sharedPreferences = getSharedPreferences("popup", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("openedFromFCMNotification", true).apply();
        Log.d("buhbye", "gg");
//        need toUserName implement this if you want toUserName do something when you receive a notification while app is in the foreground.
    }
}
