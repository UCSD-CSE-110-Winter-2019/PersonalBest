package com.team2.team2_personalbest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;

import com.google.firebase.FirebaseApp;
import com.team2.team2_personalbest.FirebaseCloudMessaging.ChatMessageService;
import com.team2.team2_personalbest.FirebaseCloudMessaging.ChatRoomActivity;
import com.team2.team2_personalbest.FirebaseCloudMessaging.notification.NotificationService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.robolectric.shadows.ShadowInstrumentation.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class UserNameTest {
    private String from;
    private EditText userNameView;
    private SharedPreferences sharedPreferences;

    @Before
    public void setUp() throws Exception {
        Context context = getInstrumentation().getTargetContext();
        FirebaseApp.initializeApp(context);
        sharedPreferences = context.getSharedPreferences("FirebaseLabApp", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("from", "test").apply();
        from = sharedPreferences.getString("from", null);

        Intent intent = TestUtils.getMainActivityIntent(mock(ChatMessageService.class), mock(NotificationService.class));
        ChatRoomActivity activity = Robolectric.buildActivity(ChatRoomActivity.class, intent).create().get();
        userNameView = activity.findViewById(R.id.user_name);
    }

    @Test
    public void readUserNameOnStart() {
        assertEquals(from, userNameView.getText().toString());
    }

    @Test
    public void saveUserNameOnEdit() {
        String userName = userNameView.getText().toString();
        assertEquals(from, userName);

        userNameView.setText(userName + " successful");

        String newFrom = sharedPreferences.getString("from", null);
        assertEquals(userName + " successful", newFrom);
    }
}