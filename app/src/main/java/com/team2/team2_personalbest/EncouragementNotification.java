package com.team2.team2_personalbest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.team2.team2_personalbest.DateHelper;
import com.team2.team2_personalbest.Day;
import com.team2.team2_personalbest.DayDatabase;
import com.team2.team2_personalbest.GoalAccomplished;
import com.team2.team2_personalbest.R;

public class EncouragementNotification {

    private static final int FIVE_HUNDRED_INCREMENT = 500;

    /**
     * Create a channel to be used for the popup
     */
    private static void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "what";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("channel", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    /**
     * Show the popup when goal is reached
     */

    public static void sendNotification(Context context){
        Intent intent = new Intent(context, GoalAccomplished.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "channel")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("Congratulations!")
                //.setContentText("Do you want to set a new step goal?")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Do you want to set a new step goal?"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(true);
        createNotificationChannel(context);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1000, mBuilder.build());
    }
    /**
     * Popup to encourage the user
     * @param increase how many steps the user improved
     */
    // Popup for encouragement
    private static void sendEncouragement(Context context, int increase){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "channel")
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                // fixme CHANGE THIS STEPs
                .setContentTitle(increase+ " steps more than yesterday! Wow!" )
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        createNotificationChannel(context);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1000, mBuilder.build());
    }

    /**
     Sends Notification when we have reached 500 above yesterdays total steps!
     */

    public static void sendSubNotification(Context context, DayDatabase dayDatabase){
        String currentDayID = DateHelper.dayDateToString(DateHelper.previousDay(0));
        Day currentDay = dayDatabase.dayDao().getDayById(currentDayID);
        String yesterdayID = DateHelper.dayDateToString(DateHelper.previousDay(1));
        Day yesterday = dayDatabase.dayDao().getDayById(yesterdayID);

        int yesterdayTotal = yesterday.getStepsTracked()+yesterday.getStepsUntracked();
        int currentStepsTotal = currentDay.getStepsTracked()+currentDay.getStepsUntracked();

        //if we have crossed yesterdays threshold
        if (currentStepsTotal > yesterdayTotal){


            //find the difference
            int difference = currentStepsTotal-yesterdayTotal;

            //if difference is 500 or 1000 or 1500 or 2000... we want to show notification
            int fiveHundredIncrements = difference/FIVE_HUNDRED_INCREMENT;
            int remainder = difference%FIVE_HUNDRED_INCREMENT;

            Log.d("YESTERDAY VS TODAY", "XXXX\nCURRENT: "+currentStepsTotal+"\nYEST: "+yesterdayTotal
                    +"\nDIFF: "+difference+"\nREM: "+remainder);
            if(remainder <= 10){

                Log.d("YESTERDAY NOTIFICATION!", "NOTIFICATION WITH INCREASE OF "
                        + FIVE_HUNDRED_INCREMENT*fiveHundredIncrements);
                sendEncouragement(context,fiveHundredIncrements*FIVE_HUNDRED_INCREMENT);

            }
        }
    }
}
