package com.team2.team2_personalbest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * This activity prompt the user for the new goal if confirmed is clicked,
 *  or go back to previous activity if cancel is clicked
 */
public class SetNewGoal extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgoal_popup);
    }

    /**
     * Confirm button
     * @param view
     */
    public void confirm(View view){
        EditText newGoal = (EditText) findViewById(R.id.set_goal);
        //TODO Check for valid input
        boolean digitsOnly = TextUtils.isDigitsOnly(newGoal.getText());
        // Making sure it's a number and editText is not empty
        if (!digitsOnly || newGoal.getText().toString().matches("")){
            Toast.makeText(SetNewGoal.this, "Enter a positive number", Toast.LENGTH_SHORT).show();
            // Restart this activity
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("goal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("newgoal", newGoal.getText().toString());
        editor.apply();
        finish();
    }

    /**
     * Cancel button
     * @param view
     */
    public void cancel(View view){
        finish();
    }
}
