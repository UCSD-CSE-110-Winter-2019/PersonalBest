package com.team2.team2_personalbest.FirebaseCloudMessaging.notification;

import com.google.android.gms.tasks.Task;

import java.util.function.Consumer;

public interface NotificationService {
    void subscribeToNotificationsTopic(String topic, Consumer<Task<Void>> callback);
}
