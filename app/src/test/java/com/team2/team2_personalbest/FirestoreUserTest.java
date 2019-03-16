package com.team2.team2_personalbest;

import android.util.Pair;

import com.google.firebase.FirebaseApp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import static org.junit.Assert.*;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
public class FirestoreUserTest {

    @Test
    public void getDataTest() {
        FirebaseApp.initializeApp(RuntimeEnvironment.application);
        new Thread(new Runnable() {
            @Override
            public void run() {
                FirestoreUser user = new FirestoreUser("DNF", "DNF");
                List<Pair<Integer, Integer>> walks = user.getWalks(user.user.userID);
                assertEquals(walks.get(0), new Pair<Integer, Integer>(0,5007));
                List<IUser.User> users = user.getFriendList();
                assertEquals(users.get(0).userID, -1507416782);
            }
        }).start();


    }

}
