package com.team2.team2_personalbest;

import android.util.Pair;

import java.util.HashMap;
import java.util.List;

public class MockFirestoreUser extends IUser {

    protected User user;

    private HashMap<Integer, MockFirestoreUser> friendsList;

    public MockFirestoreUser (String name, String email, MockDB db){
        User me = new User(name, email);

        user = me;
        // Check if user needs to be added to firestore
        // (Usually if its their first time)
        if (!isUser(user.userID))
            db.addUser(this);

//        if (!hasWalks(user.userID))
//            setWalks(getDummyWalks());
    }

    @Override
    boolean addFriend(int ID) {
        return false;
    }

    boolean addFriend(int ID, MockDB db){
        if (db.getUser(ID) != null)
            friendsList.put(ID, db.getUser(ID));
        return true;
    }

    @Override
    boolean isFriend(int ID) {
        return friendsList.get(ID) != null;
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

}
