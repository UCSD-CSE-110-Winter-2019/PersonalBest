package com.team2.team2_personalbest;

import android.app.Activity;
import android.content.Intent;

import static org.junit.Assert.*;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import com.team2.team2_personalbest.SharedPref;
import com.team2.team2_personalbest.HomePage;

@RunWith(RobolectricTestRunner.class)
public class TestSharedPref {
    @Test
    public void testBool(){
        HomePage activity = Robolectric.setupActivity(HomePage.class);
        SharedPref goalReached = new SharedPref(activity);
        goalReached.setBool("goalReached", true);
        assertTrue(goalReached.getBool("goalReached"));
    }
    @Test
    public void testInt(){
        HomePage activity = Robolectric.setupActivity(HomePage.class);
        SharedPref goalReached = new SharedPref(activity);
        goalReached.setInt("goalReached", 69);
        assertEquals(goalReached.getInt("goalReached"),  69);
    }
    @Test
    public void testString(){
        HomePage activity = Robolectric.setupActivity(HomePage.class);
        SharedPref goalReached = new SharedPref(activity);
        goalReached.setStr("goalReached", "hello");
        assertEquals(goalReached.getStr("goalReached"),"hello");
    }
}
