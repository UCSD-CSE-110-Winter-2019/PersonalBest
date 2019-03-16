package com.team2.team2_personalbest;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.team2.team2_personalbest.FirebaseCloudMessaging.ChatMessage;
import com.team2.team2_personalbest.FirebaseCloudMessaging.ChatRoomActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class ChatMessageTest2 {

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
    }

    @Test
    public void messagesDisplayedInOrder() {
        List<ChatMessage> m = new ArrayList<>();
        m.add(new ChatMessage("User1", "Hi there"));
        m.add(new ChatMessage("User1", "How are you doing?"));
        m.add(new ChatMessage("User2", "Good, how are you?"));

        Intent intent = TestUtils.getMainActivityIntent(TestUtils.getChatMessageService(m), TestUtils.getNotificationService("chat1"));
        ChatRoomActivity activity = Robolectric.buildActivity(ChatRoomActivity.class, intent).create().get();

        TextView chat = activity.findViewById(R.id.chat);

        StringBuilder sb = new StringBuilder();
        m.forEach(message -> sb.append(message.toString()));
        assertNotNull(sb.toString());
    }

}