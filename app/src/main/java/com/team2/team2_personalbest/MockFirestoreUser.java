package com.team2.team2_personalbest;

import android.util.Pair;

import java.util.List;

public class MockFirestoreUser extends IUser {

    protected Friend User;
    protected MockDB db;

    public MockFirestoreUser (String name, String email, MockDB db){
        Friend me = new Friend(name, email);

        User = me;
        this.db = db;
        // Check if User needs to be added to firestore
        // (Usually if its their first time)
        if (!isUser(User.userID))
            addUser();

        if (!hasWalks(User.userID))
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
    List<Friend> getFriendList() {
        return null;
    }

    @Override
    boolean isUser(int ID) {
        return false;
    }

    @Override
    Friend getAppUser(int ID) {
        return null;
    }
}
