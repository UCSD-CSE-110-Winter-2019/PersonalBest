package com.team2.team2_personalbest;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.team2.team2_personalbest.fitness.FitnessService;
import com.team2.team2_personalbest.fitness.FitnessServiceFactory;
import com.team2.team2_personalbest.fitness.GoogleFitAdapter;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class HomePage extends AppCompatActivity {
    //keep track of the current steps take
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    private static final String TAG = "HomePage";
    private TextView textViewStepCount;
    private TextView textViewDistance;
    private FitnessService fitnessService;
    private boolean planned_walk = false;
    final Handler handler = new Handler();
    String fitnessServiceKey = "GOOGLE_FIT";
    private final double toGetAverageStride = 0.413;
    public double height;
    public double averageStrideLength;
    private Button toggle_walk;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //display current steps and distance
        textViewStepCount = (TextView) findViewById(R.id.step_taken);
        textViewDistance = (TextView) findViewById(R.id.miles_taken);
        averageStrideLength = calculateAveStrideLength(height);
        toggle_walk = findViewById(R.id.toggle_walk);
        toggle_walk.setBackgroundColor(Color.GREEN);


        toggle_walk.setOnClickListener(new View.OnClickListener() {
            /**
             * author josephl310
             *
             * Implements toggle functionality of button: switches between planned and unplanned
             * walks
             */
            @Override
            public void onClick(View v) {
                //TODO: Update with styling
                if (planned_walk){ //User was on planned walk, wants to end it
                    planned_walk = false;
                    toggle_walk.setText("Start Planned Walk/Run");
                    toggle_walk.setBackgroundColor(Color.GREEN);
                } else {
                    planned_walk = true;
                    toggle_walk.setText("End Planned Walk/Run");
                    toggle_walk.setBackgroundColor(Color.RED);
                }
            }
        });
        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomePage homePage) {
                return new GoogleFitAdapter(homePage);
            }
        });

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();

        //update step every 15 seconds
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        fitnessService.updateStepCount();
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0,5* 1000);
        fitnessService.setup();

    }

    /**
     * Author: josephl310
     *
     * This should end planned walks when the app is quit
     */
    @Override
    protected void onDestroy(){
        planned_walk = false;
        super.onDestroy();
    }
    public void setStepCount(long stepCount){
        String stepCountDisplay = String.valueOf(stepCount) + "   " +getString(R.string.steps_taken);
        double totalDistanceInInch = stepCount * averageStrideLength;
        String milesDisplay = String.format("%.1g", convertInchToMile(totalDistanceInInch)) + "  " +getString(R.string.miles_taken);
        textViewStepCount.setText(stepCountDisplay);
        textViewDistance.setText(milesDisplay);
    }

    public double calculateAveStrideLength(double height) {
        return height * toGetAverageStride;
    }

    public double convertInchToMile(double inch){
        return inch * 1.57828e-5;
    }

    private boolean checkIfDayHasChanged(){
        Calendar c = Calendar.getInstance();
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);
        int seconds = c.get(Calendar.SECOND);
        if(hours == 0 && minutes == 0 && seconds < 30){
            return true;
        }
        else return false;
    }
}
