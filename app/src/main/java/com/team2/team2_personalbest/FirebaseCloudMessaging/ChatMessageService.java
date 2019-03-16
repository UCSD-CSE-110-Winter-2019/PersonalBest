package com.team2.team2_personalbest.FirebaseCloudMessaging;

import com.google.android.gms.tasks.Task;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/*
  function: service for chat message
 */
public interface ChatMessageService {
    Task<?> addMessage(Map<String, String> data);

    void addOrderedMessagesListener(Consumer<List<ChatMessage>> listener);
}
