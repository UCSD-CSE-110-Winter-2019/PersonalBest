package com.team2.team2_personalbest;

import android.content.Context;
import android.content.Intent;
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

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    LinearLayout friendsListView = findViewById(R.id.friendsListView);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        // Get db instance and display current friends
        //TODO
        db = new FirebaseUser(this);
        displayFriends();
    }

    public void displayFriends() {
        // TODO Get friends list from DB
        List<IUser.Friend> friends = db.getFriendlist();

        List<IUser.Friend> testFriends = new ArrayList<>();
        testFriends.add(new IUser.Friend("Daniel", "dfritsch@gmail.com", "false"));
        testFriends.add(new IUser.Friend("Panis", "aopanis@gmail.com", "false"));

        // Display all friends that both added each other
        for (IUser.Friend friend : friends) {
            if (friend.isPending.equals("false")) {
                addFriendToScrollable(friend);
            }
        }
    }

    public void addFriendToScrollable(IUser.Friend friend) {
        Button newFriend = new Button(this);
        newFriend.setText(friend.toString());
        newFriend.setHeight(30);                // TODO edit height here

        // Set button margins programatically
        params.setMargins(20, 20, 20, 10);
        newFriend.setLayoutParams(params);

        // When user clicks on button w/ friend name, go to graph
        newFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GraphActivity.class);   // TODO @anton change this here to whatever Activity desired
                intent.putExtra("name", friend.name);
                startActivity(intent);
            }
        });

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