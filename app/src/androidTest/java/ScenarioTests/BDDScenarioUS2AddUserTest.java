package ScenarioTests;

import com.team2.team2_personalbest.MockDB;
import com.team2.team2_personalbest.MockFirestoreUser;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class BDDScenarioUS2AddUserTest {

    public MockDB db;

    db = new MockDB(new HashMap<Integer, MockFirestoreUser>);

    @Before
    private void setUpFriends(){
        MockFirestoreUser joey = new MockFirestoreUser("Joey", "joey@gmail.com")
    }

    @Test
    private void addFriends(){

    }
}
