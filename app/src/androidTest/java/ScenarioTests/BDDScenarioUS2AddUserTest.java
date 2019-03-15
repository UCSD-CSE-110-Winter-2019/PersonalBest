package ScenarioTests;

import com.team2.team2_personalbest.TestUser;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;

public class BDDScenarioUS2AddUserTest {

    public HashMap<Integer, TestUser> users = new HashMap<>();

    @Before
    private void setUpFriends(){
        users.put(1, new TestUser("Joey", 1));
        users.put(2, new TestUser("Chandler", 2));
    }

    @Test
    private void addFriends(){

    }
}
