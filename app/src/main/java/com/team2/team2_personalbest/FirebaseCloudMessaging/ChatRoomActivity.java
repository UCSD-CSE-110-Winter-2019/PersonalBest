package com.team2.team2_personalbest.FirebaseCloudMessaging;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.team2.team2_personalbest.IUser;
import com.team2.team2_personalbest.R;
import com.team2.team2_personalbest.UserUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {
    String TAG = ChatRoomActivity.class.getSimpleName();

    static String COLLECTION_KEY = "chats";
    //TODO change the Document Key e.g. chat between yosuke and duy -> duyyosuke(alphabetcal order)
//    static String DOCUMENT_KEY = "frinedGraphTest";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "fromUserName";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";
    String DOCUMENT_KEY;

    CollectionReference chat;

    String fromUserName;
    String toUserName;
    String fromUserEmail;
    String toUserEmail;
    int fromUserId;
    int toUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpPopup();

        // Get Friend Data
        Bundle bundle = getIntent().getExtras();
        toUserName = bundle.getString("friend_name");
        toUserId = bundle.getInt("friend_id");
        toUserEmail = bundle.getString("friend_email");
        //setTheme(android.R.style.Theme_DeviceDefault);
        setContentView(R.layout.chat_room);
        super.onCreate(savedInstanceState);
        SharedPreferences sharedpreferences = getSharedPreferences("FirebaseLabApp", Context.MODE_PRIVATE);

        // Get my Data
        SharedPreferences sharedPreferences = getSharedPreferences("appname_prefs", MODE_PRIVATE);
        fromUserEmail = sharedPreferences.getString("userID", "");
        fromUserName = sharedPreferences.getString("user name", "");
        fromUserId = UserUtilities.emailToUniqueId(fromUserEmail);

        TextView toTextView = (TextView) findViewById(R.id.user_name);
        Log.d("CHAT_ROOM", "User Name: "+toUserName);
        toTextView.setText(toUserName);

        DOCUMENT_KEY = Integer.toString(getChatID(toUserId, fromUserId));
        chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY);

        initMessageUpdateListener();

        findViewById(R.id.btn_send).setOnClickListener(view -> sendMessage());
        subscribeToNotificationsTopic();
        //TODO edit fromUserName toUserName userID
        sharedpreferences.edit().putString(FROM_KEY, fromUserName);


    }

    private void setUpPopup() {
        SharedPreferences sharedPreferences = getSharedPreferences("popup", MODE_PRIVATE);
        boolean openedFromGraph = sharedPreferences.getBoolean("openedFromGraph", false);
        if (openedFromGraph) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("openedFromGraph", false).apply();
            setTheme(android.R.style.Theme_Material_Dialog_NoActionBar);
        }
    }

    private void sendMessage() {

        EditText messageView = findViewById(R.id.text_message);

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, fromUserName);
        newMessage.put(TEXT_KEY, messageView.getText().toString());

        chat.add(newMessage).addOnSuccessListener(result -> {
            messageView.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }


    /*
        Takes two UserID's and gives their absolute difference,
        Used to get ChatID for two users
        returns ChatId as integer
    */
    private int getChatID(int user1, int user2){
        if(user1 >= user2)
            return user1-user2;
        else
            return user2-user1;
    }


    private void initMessageUpdateListener() {
        chat.orderBy(TIMESTAMP_KEY, Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();
                        documentChanges.forEach(change -> {
                            QueryDocumentSnapshot document = change.getDocument();
                            sb.append(document.get(FROM_KEY));
                            sb.append(":\n");
                            sb.append(document.get(TEXT_KEY));
                            sb.append("\n");
                            sb.append("---\n");
                        });


                        TextView chatView = findViewById(R.id.chat);
                        chatView.append(sb.toString());
                    }
                });
    }

    private void subscribeToNotificationsTopic() {
        FirebaseMessaging.getInstance().subscribeToTopic(DOCUMENT_KEY)
                .addOnCompleteListener(task -> {
                            String msg = "Subscribed toUserName channel " + DOCUMENT_KEY;
                            if (!task.isSuccessful()) {
                                msg = "Subscribe toUserName notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(ChatRoomActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }
}
