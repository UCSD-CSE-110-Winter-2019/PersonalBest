package com.team2.team2_personalbest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        final Button doneButton = (Button) findViewById(R.id.doneButton);

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO switch Activity to MainActivity (homepage)
                launchActivity();
            }
        });
    }

    public void launchActivity() {
        // TODO this "MainActivity.class" will need to be updated to whatever Activity the homepage is.
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
