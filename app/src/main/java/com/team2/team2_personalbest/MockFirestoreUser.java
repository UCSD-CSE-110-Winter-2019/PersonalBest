package com.team2.team2_personalbest;

import android.util.Pair;

import java.util.HashMap;
import java.util.List;

public class MockFirestoreUser extends IUser {

    public User user;

    private HashMap<Integer, MockFirestoreUser> friendsList;

    public MockFirestoreUser (String name, String email, MockDB db){
        User me = new User(name, email);

        user = me;
        // Check if user needs to be added to firestore
        // (Usually if its their first time)
        if (!isUser(user.userID)) {
            db.addUser(this);
            System.out.println("added.");
        }

        this.friendsList = new HashMap<Integer, MockFirestoreUser>();


//        if (!hasWalks(user.userID))
//            setWalks(getDummyWalks());
    }

    @Override
    boolean addFriend(int ID) {
        return false;
    }

    public boolean addFriend(String email, MockDB db){
        int id = UserUtilities.emailToUniqueId(email);
        if (db.getUser(id) != null) {
            friendsList.put(id, db.getUser(id));
            return true;
        }
        return false;
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

    public HashMap<Integer, MockFirestoreUser> getFriendMap() {
        return this.friendsList;
    }

    @Override
    boolean isUser(int ID) {
        return false;
    }

    @Override
    IUser.User getAppUser(int ID) {
        return null;
    }

    public User getUser() {
        return this.user;
    }

    public int getUserID() {
        return this.user.userID;
    }

}
