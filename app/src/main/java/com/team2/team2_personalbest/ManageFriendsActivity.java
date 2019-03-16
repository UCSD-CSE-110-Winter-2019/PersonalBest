package com.team2.team2_personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class ManageFriendsActivity extends AppCompatActivity {

    String myName;
    String myEmail;

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
        SharedPreferences sharedPreferences = getSharedPreferences("appname_prefs", MODE_PRIVATE);
        myEmail = sharedPreferences.getString("userID", "");
        myName = sharedPreferences.getString("user name", "");

        // TODO change this to also get passed in the name

        //        Initializing Firestore user
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
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
        FirestoreUser db = new FirestoreUser(myName, myEmail);
        List<IUser.User> friends = db.getFriendList();

        // Display all friends that both added each other
        for (IUser.User friend : friends) {   // TODO change to 'friends'
            FirestoreUser friendDB = new FirestoreUser(friend.name, friend.address);
            if(friendDB.isFriend(db.user.userID)) {
                addFriendToScrollable(friend);
            }
        }
    }

    public void addFriendToScrollable(IUser.User friend) {
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
                Intent intent = new Intent(v.getContext(), ChatOrGraph.class);
                Log.d("MANAGE_FRIENDS", "\nFriend:"+friend.name+"\nID:"+friend.userID);
                intent.putExtra("friend_id", friend.userID);
                intent.putExtra("friend_name", friend.name);
                intent.putExtra("friend_email", friend.address);
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                FirestoreUser db = new FirestoreUser(myName, myEmail);
                db.addFriend(UserUtilities.emailToUniqueId(emailAddress));
            }
        }).start();
    }

//    public void removeButtonOnClick(View view) {
//        TextView emailField = findViewById(R.id.emailPromptField);
//        String emailAddress = emailField.getText().toString();
//        // TOD get name that corresponds to email
//
//        // TOD get name's email address
//        // TOD delete user object from database
//    }
}
