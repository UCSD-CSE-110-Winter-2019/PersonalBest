package com.team2.team2_personalbest;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.team2.team2_personalbest.fitness.FitnessService;
import com.team2.team2_personalbest.fitness.FitnessServiceFactory;
import com.team2.team2_personalbest.fitness.GoogleFitAdapter;

import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;



public class HomePage extends AppCompatActivity {
    //keep track of the current steps take
    public static final String FITNESS_SERVICE_KEY = "FITNESS_SERVICE_KEY";
    String fitnessServiceKey = "GOOGLE_FIT";

    private final int UPDATE_LENGTH = 5000; //update step count every 5 seconds
    private final double TO_GET_AVERAGE_STRIDE = 0.413;
    private static final String TAG = "HomePage";

    private TextView textViewStepCount;
    private TextView textViewDistance;
    private TextView textViewPlannedSteps, textViewPlannedDistance;
    private Button toggle_walk;

    private FitnessService fitnessService;
    private boolean planned_walk = false;
    final Handler handler = new Handler();
    public double height;
    public double averageStrideLength;

    /* Vars for planned walk data storage */
    private long psBaseline = 0; //daily steps at time planned steps turned on
    private long psDailyTotal = 0; //total planned steps before current planned walk
    private long psStepsThisWalk = 0; //holder for planned steps during current walk

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //Getting XML elements
        textViewStepCount = findViewById(R.id.step_taken); //daily step counter
        textViewDistance = findViewById(R.id.miles_taken); //daily mile counter
        textViewPlannedSteps = findViewById(R.id.planned_steps); //planned step counter
        textViewPlannedDistance = findViewById(R.id.planned_distance); //planned mile counter
        toggle_walk = findViewById(R.id.toggle_walk); //planned walk button

        averageStrideLength = calculateAveStrideLength(height);

        //set button color to green by default
        toggle_walk.setBackgroundColor(Color.GREEN);


        FitnessServiceFactory.put(fitnessServiceKey, new FitnessServiceFactory.BluePrint() {
            @Override
            public FitnessService create(HomePage homePage) {
                return new GoogleFitAdapter(homePage);
            }
        });

        fitnessService = FitnessServiceFactory.create(fitnessServiceKey, this);
        fitnessService.setup();

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
                    psDailyTotal += psStepsThisWalk;
                    psStepsThisWalk = 0;
                    planned_walk = false;
                    textViewPlannedSteps.setVisibility(View.INVISIBLE);
                    textViewPlannedDistance.setVisibility(View.INVISIBLE);
                    toggle_walk.setText("Start Planned Walk/Run");
                    toggle_walk.setBackgroundColor(Color.GREEN);
                } else { //Turn on planned walk
                    planned_walk = true;
                    fitnessService.updateStepCount();
                    textViewPlannedSteps.setVisibility(View.VISIBLE);
                    textViewPlannedDistance.setVisibility(View.VISIBLE);
                    toggle_walk.setText("End Planned Walk/Run");
                    toggle_walk.setBackgroundColor(Color.RED);
                }
            }
        });

        //update step every 5 seconds
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        fitnessService.updateStepCount();
                        Toast.makeText(HomePage.this, "updating!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0,UPDATE_LENGTH);
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
        String stepCountDisplay = String.format(Locale.US, "%d %s", stepCount, getString(R.string.steps_taken));
        double totalDistanceInInch = stepCount * averageStrideLength;
        String milesDisplay = String.format(Locale.US, "%.1g %s", convertInchToMile(totalDistanceInInch),
                                            getString(R.string.miles_taken));

        textViewStepCount.setText(stepCountDisplay);
        textViewDistance.setText(milesDisplay);

        //total daily steps should always be >= to planned
        if (stepCount < psDailyTotal){
            psDailyTotal = 0;
        }

        psStepsThisWalk = stepCount - psBaseline; //Current walk steps
        long plannedSteps = psStepsThisWalk + psDailyTotal; //Add current walk steps to total daily steps

        String plannedStepCountDisplay = String.format(Locale.US, "%d %s", plannedSteps,
                getString(R.string.planned_steps));
        double totalPlannedDistanceInInch = plannedSteps * averageStrideLength;
        String plannedMilesDisplay = String.format(Locale.US, "%.1g %s", convertInchToMile(totalPlannedDistanceInInch),
                getString(R.string.planned_distance));

        textViewPlannedSteps.setText(plannedStepCountDisplay);
        textViewPlannedDistance.setText(plannedMilesDisplay);
    }
    public void setPsBaseline(long stepCount){
        psBaseline = stepCount;
    }

    public double calculateAveStrideLength(double height) {
        return height * TO_GET_AVERAGE_STRIDE;
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
