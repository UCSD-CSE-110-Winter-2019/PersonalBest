package com.team2.team2_personalbest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * This activity is called when user completed the goal
 */
public class GoalAccomplished extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goalaccomplished);
    }

    /**
     * Yes button
     * start setNewGoal Activity
     * @param view
     */
    public void yes(View view){
        Intent intent = new Intent(this, SetNewGoal.class);
        startActivity(intent);
        finish();
    }

    /**
     * No button
     * Increment the goal by 500 and return to previous activity
     * @param view
     */
    public void no(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("goal", MODE_PRIVATE);
        String newGoal = sharedPreferences.getString("newgoal", "0");
        // New suggested goal
        long goal = Long.parseLong(newGoal)+500;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("newgoal", Long.toString(goal));
        editor.apply();
        finish();
    }

}
