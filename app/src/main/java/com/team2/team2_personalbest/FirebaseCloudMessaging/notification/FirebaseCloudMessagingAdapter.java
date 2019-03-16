package com.team2.team2_personalbest.FirebaseCloudMessaging.notification;

import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.team2.team2_personalbest.FirebaseCloudMessaging.FirebaseFirestoreAdapter;

import java.util.function.Consumer;


public class FirebaseCloudMessagingAdapter implements NotificationService {
    private final String TAG = FirebaseFirestoreAdapter.class.getSimpleName();
    private static NotificationService instance;

    public static NotificationService getInstance() {
        if (instance == null) {
            instance = new FirebaseCloudMessagingAdapter();
        }
        return instance;
    }

    @Override
    public void subscribeToNotificationsTopic(String topic, Consumer<Task<Void>> callback) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(callback::accept);
    }
}
