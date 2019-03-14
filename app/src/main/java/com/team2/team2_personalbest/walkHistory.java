package com.team2.team2_personalbest;

import android.arch.persistence.room.Room;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class walkHistory extends AppCompatActivity {

    private final double TO_GET_AVERAGE_STRIDE = 0.413;
    private int userHeight = 60;


    private WalkDatabase walkDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk_history);

        final String DATABASE_NAME1 = "walk_db";
        walkDatabase = Room.databaseBuilder(getApplicationContext(),
                WalkDatabase.class, DATABASE_NAME1)
                .build();


        setup();


    }

    private void setup() {

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        new Thread(new Runnable() {
            @Override
            public void run() {
                List<PlannedWalk> walks = walkDatabase.walkDao().getAllWalks();
                for (PlannedWalk i : walks) {
                    Log.d("PLANNED WALK", String.format("%d\n", i.getSteps()));
                }


                setContentView(linearLayout);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                for (int i = walks.size() - 1; i >= 0; i--) {
                    TextView textView = new TextView(getBaseContext());
                    PlannedWalk walk = walks.get(i);

                    String walkData = getStringForWalk(walk);
                    textView.setText(walkData);
                    textView.setHeight(300);
                    textView.setGravity(Gravity.CENTER);


                    TextView line = new TextView(getBaseContext());
                    line.setHeight(5);

                    int blackColor = ContextCompat.getColor(getApplicationContext(), android.R.color.black);
                    line.setBackgroundColor(blackColor);
                    linearLayout.addView(line);


                    linearLayout.addView(textView);
                }
                TextView line = new TextView(getBaseContext());
                line.setHeight(5);
                line.setBackgroundColor(getResources().getColor(android.R.color.black));
                linearLayout.addView(line);
            }
        }).start();

    }

    private String getStringForWalk(PlannedWalk walk){
        double time = walk.getTime();
        double steps = walk.getSteps();

        String timeStr = String.format("%.10f", walk.getTime());


        //WE HAVE TIME, STEPS, and HEIGHT

        //calculate distance
        double distance = convertInchToMile(steps * calculateAveStrideLength(userHeight));

        double speed = distance/time;

        String ret = ("Steps Walked: " + steps + "\n" +
                      "Time: " + timeStr+"\n"+
                      "Distance: "+distance+"\n"+
                      "Average Speed: "+speed+"\n");

        return ret;
    }

    public double calculateAveStrideLength(double height) {
        return height * TO_GET_AVERAGE_STRIDE;
    }

    public double convertInchToMile(double inch) {
        return inch * 1.57828e-5;
    }


}
