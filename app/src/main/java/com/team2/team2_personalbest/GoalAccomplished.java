package com.team2.team2_personalbest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


public class GoalAccomplished extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goalaccomplished);
    }

    public void yes(View view){
        Intent intent = new Intent(this, SetNewGoal.class);
        startActivity(intent);
        finish();
    }

    public void no(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("goal", MODE_PRIVATE);
        String newGoal = sharedPreferences.getString("newgoal", "");
        // New suggested goal
        long goal = Long.parseLong(newGoal) + 500;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("newgoal", Long.toString(goal));
        editor.apply();
        finish();
    }

}
