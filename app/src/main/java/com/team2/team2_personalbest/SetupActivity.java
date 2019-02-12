package com.team2.team2_personalbest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                String height = heightField.getText().toString();

                // Go back to homepage
                launchActivity();
            }
        });
    }

    public void launchActivity() {
        // TODO change if homepage is not MainActivity. Change to whatever homepage is.
        Intent intent = new Intent(this, HomePage.class);
        startActivity(intent);
    }
}
