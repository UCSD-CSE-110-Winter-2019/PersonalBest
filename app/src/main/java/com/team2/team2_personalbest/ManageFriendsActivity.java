package com.team2.team2_personalbest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

    FirestoreUser db;

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
    );

    LinearLayout friendsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_friends);

        friendsListView = findViewById(R.id.friendsListView);

        // TODO get name from sharedpreferences as well and pass that into the db constructor
        SharedPreferences sharedPreferences = getSharedPreferences("userID", MODE_PRIVATE);
        String email = sharedPreferences.getString("userID", "");

        // TODO change this to also get passed in the name

        //        Initializing Firestore User
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                db = new FirestoreUser("Shardul", "sssaiya@ucsd.edu");
                displayFriends();
            }
        });
        thread.start();
//        try {
//            thread.wait();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void displayFriends() {
        List<IUser.Friend> friends = db.getFriendList();

        /*List<IUser.Friend> testFriends = new ArrayList<>();
        testFriends.add(new IUser.Friend("Daniel", "dfritsch@gmail.com"));
        testFriends.add(new IUser.Friend("Panis", "aopanis@gmail.com"));
        testFriends.add(new IUser.Friend("Shady", "shady@gmail.com"));
        testFriends.add(new IUser.Friend("Yosuke", "yosuke@gmail.com"));
        testFriends.add(new IUser.Friend("D", "D@gmail.com"));
*/
        // Display all friends that both added each other
        for (IUser.Friend friend : friends) {   // TODO change to 'friends'
            addFriendToScrollable(friend);
        }
    }

    public void addFriendToScrollable(IUser.Friend friend) {
        Button newFriend = new Button(this);
        newFriend.setText(friend.toString());
        newFriend.setHeight(30);

        // Set button margins programatically
        params.setMargins(20, 20, 20, 10);
        newFriend.setLayoutParams(params);

        // When user clicks on button w/ friend name, go to graph
        newFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FriendGraph.class);
                intent.putExtra("name", friend.name);
                startActivity(intent);
            }
        });

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                friendsListView.addView(newFriend);
            }
        });
    }

    public void addButtonOnClick(View view) {
        TextView emailField = findViewById(R.id.emailPromptField);
        String emailAddress = emailField.getText().toString();

        // Add friend to curr user's friend list by the email's unique hashed ID
        db.addFriend(UserUtilities.emailToUniqueId(emailAddress));
    }

//    public void removeButtonOnClick(View view) {
//        TextView emailField = findViewById(R.id.emailPromptField);
//        String emailAddress = emailField.getText().toString();
//        // TOD get name that corresponds to email
//
//        // TOD get name's email address
//        // TOD delete Friend object from database
//    }
}