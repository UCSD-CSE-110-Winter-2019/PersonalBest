package com.team2.team2_personalbest;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MockFirestoreUser extends IUser {

    protected User user;
    protected MockDB db;

    private

    public MockFirestoreUser (String name, String email, MockDB db){
        IUser.User me = new User(name, email);

        user = me;
        this.db = db;
        // Check if user needs to be added to firestore
        // (Usually if its their first time)
        if (!isUser(user.userID))
            addUser();

        if (!hasWalks(user.userID))
            setWalks(getDummyWalks());
    }

    @Override
    boolean addFriend(int ID) {
        return false;
    }

    @Override
    boolean isFriend(int ID) {
        return false;
    }

    @Override
    boolean removeFriend(int ID) {
        return false;
    }

    @Override
    List<Pair<Integer, Integer>> getWalks(int ID) {
        return null;
    }

    @Override
    boolean hasWalks(int ID) {
        return false;
    }

    @Override
    void setWalks(List<Pair<Integer, Integer>> walks) {

    }

    @Override
    List<IUser.User> getFriendList() {
        return null;
    }

    @Override
    boolean isUser(int ID) {
        return false;
    }

    @Override
    IUser.User getAppUser(int ID) {
        return null;
    }

    private void addUser(){
        // Create a new user To add to Firestore userList
        Map<Integer, User> user = new HashMap<>();
        user.put(this.user.getUserID(), this.user);

        // Add a new document with a generated ID
        db.collection("Users").document(Integer.toString(this.user.userID))
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}
