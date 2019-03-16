package com.team2.team2_personalbest.ScenarioTests;

import com.team2.team2_personalbest.IUser;
import com.team2.team2_personalbest.MockDB;
import com.team2.team2_personalbest.MockFirestoreUser;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

import org.junit.Test;
import static org.junit.Assert.*;

public class BDDScenarioUS2AddUserTest {

    public MockDB db;
    HashMap<Integer, MockFirestoreUser> temp = new HashMap();
    MockFirestoreUser joey, chandler, gunther;

    @Before
    public void setUpFriends(){
        db = new MockDB(temp);
        joey = new MockFirestoreUser("Joey", "joey@gmail.com", db);
        chandler = new MockFirestoreUser("Chandler", "chandler@gmail.com", db);
        gunther = new MockFirestoreUser("Gunther", "gunther@gmail.com", db);
        joey.addFriend("chandler@gmail.com", db);
        chandler.addFriend("joey@gmail.com", db);
    }

    @Test
    public void testFriendsExist(){
        assertEquals(joey.getFriendMap().get(chandler.getUserID()), chandler);
        assertEquals(chandler.getFriendMap().get(joey.getUserID()), joey);
    }

    @Test
    public void testNotFriends() {
        assertNull(joey.getFriendMap().get(gunther.getUserID()));
        assertNull(chandler.getFriendMap().get(gunther.getUserID()));
    }
}
