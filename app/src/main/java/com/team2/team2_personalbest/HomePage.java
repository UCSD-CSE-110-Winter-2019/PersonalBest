package com.team2.team2_personalbest;

import android.content.Intent;
import android.content.SharedPreferences;
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
    //TODO
    private TextView TextViewStepsLeft;
    public long goal;
    public long stepsLeft;

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
            @Override
            public void onClick(View v) {
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
        // TODO Set up the initial goal
        this.goal = 5000;
        SharedPreferences sharedPreferences = getSharedPreferences("goal", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("newgoal", "5000");
        editor.apply();

    }

    //TODO Update the goal
    @Override
    public void onResume(){
        super.onResume();
        TextViewStepsLeft = (TextView) findViewById(R.id.steps_left);
        SharedPreferences sharedPreferences = getSharedPreferences("goal", MODE_PRIVATE);
        String newGoal = sharedPreferences.getString("newgoal", "");
        this.goal = Long.parseLong(newGoal);
        this.stepsLeft = this.goal;
        TextViewStepsLeft.setText(newGoal);
    }

    protected void onClose(){
        planned_walk = false;
    }

    public void setStepCount(long stepCount){
        String stepCountDisplay = String.valueOf(stepCount) + "   " +getString(R.string.steps_taken);
        double totalDistanceInInch = stepCount * averageStrideLength;
        String milesDisplay = String.format("%.1g", convertInchToMile(totalDistanceInInch)) + "  " +getString(R.string.miles_taken);
        textViewStepCount.setText(stepCountDisplay);
        textViewDistance.setText(milesDisplay);
        //TODO Update steps left
        this.stepsLeft = this.goal - stepCount;
        //TODO When reached the goal
        if (this.stepsLeft < 0) {
            this.stepsLeft = 0;
            launchEncouragementPopup();
        }
        String stepsLeft = String.valueOf(this.stepsLeft);
        TextViewStepsLeft.setText(stepsLeft);
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
    //TODO Launch the set new goal popup
    public void set_goal(View view){
        //TODO
        Intent intent = new Intent(this, SetNewGoal.class);
        startActivity(intent);
    }

    //TODO Launch the encouragement popup
    public void launchEncouragementPopup(){
        Intent intent = new Intent(this, GoalAccomplished.class);
        startActivity(intent);
    }
}
