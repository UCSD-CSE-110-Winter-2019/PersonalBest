package com.team2.team2_personalbest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

public class ManageFriendsActivity extends AppCompatActivity {

    // TODO Declare global variable for database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        // TODO get database instance
        displayFriends();
    }

    public void displayFriends() {
        String[] friends = {"Daniel", "Anton", "Shady", "D", "Yosuke", "Joseph"};
        for (String friend : friends) {
            addFriend(friend);
        }
    }

    public void addFriend(String friend) {
        TextView newFriend = new TextView(this);
        newFriend.setText(friend);

        ScrollView friendsView = findViewById(R.id.friendsScrollView);
        friendsView.addView(newFriend);
    }





}
