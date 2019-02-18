package com.team2.team2_personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * This activity prompt the user for the height and store it
 */
public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        final Button doneButton = (Button) findViewById(R.id.doneButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText heightField = (EditText) findViewById(R.id.enterHeightField);

                // TODO store this height value in FireBase with the user's account.
                String heightStr = heightField.getText().toString();
                if (heightStr.matches("")){
                    Toast.makeText(SetupActivity.this, "Enter a positive number", Toast.LENGTH_SHORT).show();
                    // Restart this activity
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                SharedPreferences sharedPreferences = getSharedPreferences("height", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("height", heightStr);
                editor.apply();
                finish();
            }
        });
    }
}
