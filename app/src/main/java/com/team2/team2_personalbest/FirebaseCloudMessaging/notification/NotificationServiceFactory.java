package com.team2.team2_personalbest.FirebaseCloudMessaging.notification;


import com.team2.team2_personalbest.FirebaseCloudMessaging.Factory;

public class NotificationServiceFactory extends Factory<NotificationService> {
    private static NotificationServiceFactory instance;

    public static NotificationServiceFactory getInstance() {
        if (instance == null) {
            instance = new NotificationServiceFactory();
        }
        return instance;
    }
}
