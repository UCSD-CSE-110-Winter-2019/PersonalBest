package com.team2.team2_personalbest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ManageFriendsActivity extends AppCompatActivity {

    // TODO Declare global variable for database instance
    // Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        // TODO get database instance
        displayFriends();
    }

    public void displayFriends() {
        // TODO Get friends list from DB
        // TODO Display all friends that do not have the "pending" field == true
        String[] friends = {"Daniel", "Anton", "Shady", "D", "Yosuke", "Joseph"};
        for (String friend : friends) {
            addFriend(friend);
        }
    }

    public void addFriend(String friend) {
        TextView newFriend = new TextView(this);
        newFriend.setText(friend);

        LinearLayout friendsListView = findViewById(R.id.friendsListView);
        friendsListView.addView(newFriend);
    }

    public void addButtonOnClick(View view) {
        TextView emailField = findViewById(R.id.emailPromptField);
        String emailAddress = emailField.getText().toString();

        // TODO get name that corresponds to email
        addFriend(emailAddress);

        // TODO get name's email address
        // TODO add Friend object to the database
    }

    public void removeButtonOnClick(View view) {
        TextView emailField = findViewById(R.id.emailPromptField);
        String emailAddress = emailField.getText().toString();
        // TODO get name that corresponds to email

        // TODO get name's email address
        // TODO delete Friend object from database
    }
}