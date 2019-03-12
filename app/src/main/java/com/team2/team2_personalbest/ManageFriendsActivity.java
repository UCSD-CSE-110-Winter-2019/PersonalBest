package com.team2.team2_personalbest;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ManageFriendsActivity extends AppCompatActivity {

    // TODO Declare global variable for database instance
    FirebaseUser db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        // Get db instance and display current friends
        db = new FirebaseUser(this);
        displayFriends();
    }

    public void displayFriends() {
        // TODO Get friends list from DB
        List<IUser.Friend> friends = db.getFriendlist();

        List<IUser.Friend> testFriends = new ArrayList<>();
        testFriends.add(new IUser.Friend("Daniel", "dfritsch@gmail.com", "true"));
        testFriends.add(new IUser.Friend("Panis", "aopanis@gmail.com", "true"));

        // Display all friends that both added each other
        for (IUser.Friend friend : friends) {
            if (friend.isPending.equals("false")) {
                addFriendToScrollable(friend);
            }
        }

//        // TODO Display all friends that do not have the "pending" field == true
//        String[] mockFriends = {"Daniel", "Anton", "Shady", "D", "Yosuke", "Joseph"};
//        for (String friend : mockFriends) {
//            addFriendToScrollable(friend);
//        }
    }

    public void addFriendToScrollable(IUser.Friend friend) {
        Button newFriend = new Button(this);
        newFriend.setText(friend.toString());

        LinearLayout friendsListView = findViewById(R.id.friendsListView);
        friendsListView.addView(newFriend);
    }

    public void addButtonOnClick(View view) {
        TextView emailField = findViewById(R.id.emailPromptField);
        String emailAddress = emailField.getText().toString();

        // TODO get Personal Best Friend that corresponds the that email -->
        // TODO IUser.Friend newFriend = db.getAppUser(emailAddress)
//        if (!newFriend.isPending) {
//            addFriendToScrollable(newFriend);
//        }

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