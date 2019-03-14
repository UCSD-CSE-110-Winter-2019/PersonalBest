package com.team2.team2_personalbest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.team2.team2_personalbest.FirebaseCloudMessaging.ChatRoomActivity;
import com.team2.team2_personalbest.FriendGraph;
import com.team2.team2_personalbest.R;

/**
 * This activity prompt the user for the new goal if confirmed is clicked,
 *  or go back to previous activity if cancel is clicked
 */
public class ChatOrGraph extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_or_graph);
    }

    public void launchGraphActivity(View view){
        // Retrieve username from previous activity
        Bundle bundle = getIntent().getExtras();
        String userName;
        try {
            userName = bundle.getString("friend'sName");
        }
        catch(NullPointerException e){
            userName = "Shady";
        }
        Intent intent = new Intent(this, FriendGraph.class);
        // Pass username to next activity
        intent.putExtra("friend'sName", userName);
        startActivity(intent);
        finish();
    }

    public void launchChatActivity(View view){
        // Retrieve username from previous activity
        Bundle bundle = getIntent().getExtras();
        String userName;
        try {
            userName = bundle.getString("friend'sName");
        }
        catch(NullPointerException e){
            userName = "Shady";
        }
        Intent intent = new Intent(this, ChatRoomActivity.class);
        // Pass username to next activity
        intent.putExtra("friend'sName", userName);
        startActivity(intent);
        finish();
    }
}
