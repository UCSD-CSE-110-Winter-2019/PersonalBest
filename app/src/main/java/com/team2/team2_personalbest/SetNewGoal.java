package com.team2.team2_personalbest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SetNewGoal extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newgoal_popup);
    }

    public void confirm(View view){
        EditText newGoal = (EditText) findViewById(R.id.set_goal);
        boolean digitsOnly = TextUtils.isDigitsOnly(newGoal.getText());
        //Check for valid input
        if (!digitsOnly){
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
    public void cancel(View view){
        /*
        Intent intent = new Intent(this, HomePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        */
        finish();
    }
}
