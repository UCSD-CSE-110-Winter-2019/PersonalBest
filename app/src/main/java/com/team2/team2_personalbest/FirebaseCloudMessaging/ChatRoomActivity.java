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
import com.team2.team2_personalbest.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {
    String TAG = ChatRoomActivity.class.getSimpleName();

    static String COLLECTION_KEY = "chats";
    //TODO change the Document Key e.g. chat between yosuke and duy -> duyyosuke(alphabetcal order)
    static String DOCUMENT_KEY = "chat7";
    String MESSAGES_KEY = "messages";
    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    CollectionReference chat;
    //TODO set it to sender user id and receiver user id passed from the original activity
    //TODO maybe using intent.putExtra?
    //TODO also u dont need to
    String from;
    String to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setUpPopup();
        setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_room);
        Intent intent = getIntent();
        intent.getStringExtra("friend's name");
        SharedPreferences sharedpreferences = getSharedPreferences("FirebaseLabApp", Context.MODE_PRIVATE);

        from = sharedpreferences.getString(FROM_KEY, null);
        //TODO this is sample
        from = "Y";
        //TODO get the name of person u r texting
        to = "Duy";
        TextView toTextView = (TextView) findViewById(R.id.user_name);
        toTextView.setText(to);

        chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(MESSAGES_KEY);

        initMessageUpdateListener();

        findViewById(R.id.btn_send).setOnClickListener(view -> sendMessage());
        subscribeToNotificationsTopic();
        //TODO edit from to userID
        sharedpreferences.edit().putString(FROM_KEY, from);
    }

    private void setUpPopup() {
        SharedPreferences sharedPreferences = getSharedPreferences("popup", MODE_PRIVATE);
        boolean openedFromGraph = sharedPreferences.getBoolean("openedFromGraph", false);
        if (openedFromGraph) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("openedFromGraph", false).apply();
            setTheme(android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        }
    }

    private void sendMessage() {

        EditText messageView = findViewById(R.id.text_message);

        Map<String, String> newMessage = new HashMap<>();
        newMessage.put(FROM_KEY, from);
        newMessage.put(TEXT_KEY, messageView.getText().toString());

        chat.add(newMessage).addOnSuccessListener(result -> {
            messageView.setText("");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
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
                            String msg = "Subscribed to channel " + DOCUMENT_KEY;
                            if (!task.isSuccessful()) {
                                msg = "Subscribe to notifications failed";
                            }
                            Log.d(TAG, msg);
                            Toast.makeText(ChatRoomActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                );
    }
}
