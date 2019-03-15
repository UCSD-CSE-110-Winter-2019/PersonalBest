package ScenarioTests;

import com.team2.team2_personalbest.MockDB;
import com.team2.team2_personalbest.MockFirestoreUser;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class BDDScenarioUS2AddUserTest {

    public MockDB db;
    HashMap<Integer, MockFirestoreUser> temp = new HashMap();
    MockFirestoreUser joey, chandler;

    @Before
    private void setUpFriends(){
        db = new MockDB(temp);
        joey = new MockFirestoreUser("Joey", "joey@gmail.com", db);
        chandler = new MockFirestoreUser("Chandler", "chandler@bing.com", db);
    }

    @Test
    private void addFriends(){
        joey.addFriend("chandler@gmail.com");
    }
}
