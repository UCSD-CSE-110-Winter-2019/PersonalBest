package com.team2.team2_personalbest;

import android.app.Activity;
import android.content.Intent;

import static org.junit.Assert.*;


import org.junit.Test;

import com.team2.team2_personalbest.SharedPref;
import com.team2.team2_personalbest.HomePage;

public class TestSharedPref {
    @Test
    public void testBool(){
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
        SharedPref goalReached = new SharedPref(HomePage);
        goalReached.setBool("goalReached", true);
        assertTrue(goalReached.getBool("goalReached"));
    }
}
