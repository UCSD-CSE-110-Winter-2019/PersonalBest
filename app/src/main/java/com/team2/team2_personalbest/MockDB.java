package com.team2.team2_personalbest;

import java.util.HashMap;

public class MockDB {
    private HashMap<Integer, MockFirestoreUser> db;

    public MockDB(HashMap<Integer, MockFirestoreUser> db) {
        this.db = db;
    }

    public MockFirestoreUser getUser(int id) {
        return db.get(id);
    }

    public void addUser(MockFirestoreUser newUser) {
        db.put(newUser.user.userID, newUser);
    }
}
