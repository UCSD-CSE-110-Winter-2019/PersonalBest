package com.team2.team2_personalbest;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    final Handler handler = new Handler();
    String fitnessServiceKey = "GOOGLE_FIT";
    private final double toGetAverageStride = 0.413;
    public double height;
    public double averageStrideLength;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //display current steps and distance
        textViewStepCount = (TextView) findViewById(R.id.step_taken);
        textViewDistance = (TextView) findViewById(R.id.miles_taken);
        averageStrideLength = calculateAveStrideLength(height);
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

    public void setStepCount(long stepCount){
        String stepCountDispaly = String.valueOf(stepCount) + "   " +getString(R.string.steps_taken);
        double totaDistanceInInch = stepCount * averageStrideLength;
        String milesDisplay = String.format("%.1g", convertInchToMile(totaDistanceInInch)) + "  " +getString(R.string.miles_taken);
        textViewStepCount.setText(stepCountDispaly);
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
